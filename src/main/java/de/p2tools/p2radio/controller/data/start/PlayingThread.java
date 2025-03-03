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
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.controller.pevent.PEvents;
import javafx.application.Platform;

public class PlayingThread extends Thread {

    private final int stat_start = 0;
    private final int stat_running = 1;
    private final int stat_restart = 3;
    private final int stat_end = 99;

    private final StartDto startDto;
    private int runTime = 0;
    private int startCounter = 0; // Anzahl der Startversuche
    private static final int START_COUNTER_MAX = 3;

    public PlayingThread(ProgData progData, StartDto startDto) {
        super();
        this.startDto = startDto;

        setName("PlayingThread: " + this.startDto.getStationData().getStationName());
        setDaemon(true);
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_TIMER_SECOND) {
            public void ping(P2Event event) {
                // wenn der Sender mind. 60s läuft, wird der Start-Counter hochgesetzt
                ++runTime;
                if (runTime == ProgConst.START_COUNTER_MIN_TIME && startDto.getStationData() != null) {
                    StartProgramFactory.setStartCounter(startDto.getStationData());
                }
            }
        });
    }

    @Override
    public synchronized void run() {
        try {
            int stat = stat_start;
            while (stat < stat_end) {
                stat = switch (stat) {
                    case stat_start -> startProgram(); // starten
                    case stat_running -> runProgram(); // läuft bis zum Abbruch
                    case stat_restart -> restartProgram();
                    default -> stat;
                };
            }

        } catch (final Exception ex) {
            P2Log.errorLog(987989569, ex);
            startDto.setStateError();
        }

        if (!startDto.isStopFromButton()) {
            // nur dann muss es gemacht werden, sonst machts START selbst
            StartFactory.stopStation(false); // Prozess ist dann ja schon beendet
        }
    }

    private int startProgram() {
        // versuch das Programm zu Starten
        ++startCounter;
        final StartRuntimeExec runtimeExec = new StartRuntimeExec(startDto);
        final Process process = runtimeExec.exec();
        startDto.setProcess(process);

        if (process != null) {
            startDto.setStateStartedRun();
            startDto.setStarting(false);
            return stat_running;

        } else {
            return stat_restart;
        }
    }

    private int runProgram() {
        // hier läufts bis zum Abbruch
        int retStatus = stat_running;
        try {
            final int exitV = startDto.getProcess().exitValue(); //liefert ex wenn noch nicht fertig
            if (exitV != 0) {
                // <> 0 -> Fehler
                retStatus = stat_restart;

            } else {
                // 0 -> beendet
                retStatus = stat_end;
            }
        } catch (final Exception ex) {
            P2ToolsFactory.pause(1_000);
        }
        return retStatus;
    }

    private int restartProgram() {
        // counter prüfen und starten bis zu einem Maxwert
        if (startCounter < START_COUNTER_MAX) {
            // dann nochmal von vorne
            return stat_start;

        } else {
            // dann wars das
            startDto.setStateError();
            startDto.setStarting(false);

            if (SetFactory.checkProgram(startDto.getSetData().getProgPath())) {
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

            return stat_end;
        }
    }
}