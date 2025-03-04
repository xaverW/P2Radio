/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2radio.controller.data.start;


import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import javafx.application.Platform;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class PlayingThread extends Thread {
    public static final int STATE_INIT = 0; // nicht gestartet
    public static final int STATE_RESTART = 1; // startet
    public static final int STATE_STARTED_RUN = 2; // läuft
    public static final int STATE_KILL = 3; // beendet, OK
    public static final int STATE_END = 4; // beendet, OK
    public static final int STATE_ERROR = 5; // beendet, fehlerhaft

    private final StationData stationData;
    private final P2Date startTime;
    private final SetData setData;

    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

    private final IntegerProperty runningState = new SimpleIntegerProperty(STATE_INIT);
    private int runTime = 0;
    private int startCounter = 0; // Anzahl der Startversuche
    private static final int START_COUNTER_MAX = 3;

    private Process process = null; //Prozess des Programms (VLC)
    private final BooleanProperty isStarting = new SimpleBooleanProperty(true); // PlayingThread meldet, wenn Start fertig
    private final BooleanProperty isRunning = new SimpleBooleanProperty(true); // meldet, solange er läuft

    public PlayingThread(SetData setData, StationData stationData) {
        super();

        this.stationData = stationData;
        this.startTime = new P2Date();
        this.setData = setData;
        StartProgramFactory.makeProgParameter(this);

        setName("PlayingThread: " + getStationData().getStationName());
        setDaemon(true);
        ProgData.getInstance().pEventHandler.addListener(new P2Listener(PEvents.EVENT_TIMER_SECOND) {
            public void ping(P2Event event) {
                // wenn der Sender mind. 60s läuft, wird der Start-Counter hochgesetzt
                ++runTime;
                if (runTime == ProgConst.START_COUNTER_MIN_TIME && getStationData() != null) {
                    StartProgramFactory.setStartCounter(getStationData());
                }
            }
        });
    }

    @Override
    public synchronized void run() {
        try {
            stationData.setError(false); // falls sie ERROR schon hatte
            stationData.setNowPlaying(true);
            stationData.setStationDateLastStart(LocalDateTime.now());

            while (runningState.get() < STATE_END) {
                switch (runningState.get()) {
                    case STATE_INIT -> startProgram(); // starten
                    case STATE_STARTED_RUN -> runProgram(); // läuft bis zum Abbruch
                    case STATE_RESTART -> restartProgram();
                    case STATE_KILL -> killProgram();
                    default -> runningState.set(STATE_INIT);
                }
            }

        } catch (final Exception ex) {
            P2Log.errorLog(987989569, ex);
            runningState.set(STATE_ERROR);
        }

        quitt();
    }

    public void killProcess() {
        runningState.set(STATE_KILL);
        this.interrupt();
    }

    private void quitt() {
        System.out.println("PlayingThread - quitt");

        stationData.setError(isStateError());
        stationData.setNowPlaying(false);

        isStarting.set(false); // falls der Start schiefging
        isRunning.set(false);

        PlayingTitle.stopNowPlaying();
        StartFactory.finishedMsg(this);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
    }

    // *************************************
    private void startProgram() {
        // versuch das Programm zu Starten
        ++startCounter;
        final StartRuntimeExec startRuntimeExec = new StartRuntimeExec(this);
        process = startRuntimeExec.exec();

        if (process != null) {
            runningState.set(STATE_STARTED_RUN);
            StartFactory.startMsg(this);
            isStarting.set(false);

        } else {
            runningState.set(STATE_RESTART);
        }
    }

    // *************************************
    private void runProgram() {
        // hier läufts bis zum Abbruch
        try {
            final int exitV = process.exitValue(); //liefert ex wenn noch nicht fertig
            if (exitV != 0) {
                // <> 0 -> Fehler
                runningState.set(STATE_RESTART);

            } else {
                // 0 -> beendet
                runningState.set(STATE_END);
            }
        } catch (final Exception ex) {
            runningState.set(STATE_STARTED_RUN);
            P2ToolsFactory.pause(1_000);
        }
    }

    // *************************************
    private void restartProgram() {
        // counter prüfen und starten bis zu einem Maxwert
        if (startCounter < START_COUNTER_MAX) {
            // dann nochmal von vorne
            runningState.set(STATE_INIT);

        } else {
            // dann wars das
            runningState.set(STATE_ERROR);

            if (SetFactory.checkProgram(getSetData().getProgPath())) {
                // Programm passt
                Platform.runLater(() -> {
                    P2Alert.showErrorAlert("Sender starten", "Der Sender konnte nicht gestartet werden.");
                });

            } else {
                // dann kann das Programm nicht gestartet werden
                Platform.runLater(() -> {
                    P2Alert.showErrorAlert("Sender starten", "Das Programm konnte nicht gestartet werden.");
                });
            }
        }
    }

    private void killProgram() {
        System.out.println("PlayingThread - killProgram");
        if (process != null) {
            process.destroy();
        }

        // damit das Programm auch wirklich geschlossen ist
        P2ToolsFactory.waitWhile(3_000, (a) ->
                (process != null && process.isAlive()));
        runningState.set(STATE_END);
    }

    //=======================================
    public StationData getStationData() {
        return stationData;
    }

    public SetData getSetData() {
        return setData;
    }

    public P2Date getStartTime() {
        return startTime;
    }

    //=======================================
    public String getProgramCall() {
        return programCall.get();
    }

    public void setProgramCall(String programCall) {
        this.programCall.set(programCall);
    }

    public String getProgramCallArray() {
        return programCallArray.get();
    }

    public void setProgramCallArray(String programCallArray) {
        this.programCallArray.set(programCallArray);
    }

    //=======================================
    public boolean isStarting() {
        return isStarting.get();
    }

    public BooleanProperty isStartingProperty() {
        return isStarting;
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public boolean isStateError() {
        return runningState.get() == STATE_ERROR;
    }

    public boolean isStateStartedRun() {
        return runningState.get() == STATE_STARTED_RUN;
    }
}