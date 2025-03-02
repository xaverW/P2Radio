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


import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.pevent.PEvents;

public class PlayingThread extends Thread {

    private final int stat_start = 0;
    private final int stat_running = 1;
    private final int stat_restart = 3;
    private final int stat_end = 99;

    private Start start;
    private int runTime = 0;
    private int startCounter = 0; // Anzahl der Startversuche
    private static final int START_COUNTER_MAX = 3;


    public PlayingThread(ProgData progData, Start start) {
        super();
        this.start = start;

        setName("START-STATION-THREAD: " + this.start.getStationData().getStationName());
        setDaemon(true);
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_TIMER_SECOND) {
            public void ping(P2Event event) {
                // wenn der Sender mind. 60s läuft, wird der Start-Counter hochgesetzt
                ++runTime;
                if (runTime == ProgConst.START_COUNTER_MIN_TIME && start.getStationData() != null) {
                    StartProgramFactory.setStartCounter(start.getStationData());
                }
            }
        });
    }

    @Override
    public synchronized void run() {
        int stat = stat_start;

        try {
            while (stat < stat_end) {
                stat = runningLoop(stat);
            }

        } catch (final Exception ex) {
            P2Log.errorLog(987989569, ex);
            start.setStateError();
        }

        if (!start.isStopFromButton()) {
            // nur dann muss es gemacht werden, sonst machts START selbst
            StartFactory.stopRunningStation(false); // Prozess ist dann ja schon beendet
        }
        start = null;
    }

    private int runningLoop(int stat) {
        // läuft, solange der Sender läuft
        switch (stat) {
            case stat_start:
                // Versuch, das Programm zu Starten
                stat = startProgram();
                break;

            case stat_running:
                // hier läuft es bis zum Abbruch
                stat = runProgram();
                break;

            case stat_restart:
                stat = restartProgram();
                break;
        }

        return stat;
    }

    private int startProgram() {
        //versuch das Programm zu Starten
        //die Reihenfolge: startCounter - startmeldung ist wichtig!
        ++startCounter;
        final StartRuntimeExec runtimeExec = new StartRuntimeExec(start);
        final Process process = runtimeExec.exec();
        start.setProcess(process);

        if (process != null) {
            start.setStateStartedRun();
            start.setStarting(false);
            return stat_running;

        } else {
            return stat_restart;
        }
    }

    private int runProgram() {
        //hier läufts bis zum Abbruch oder Ende
        int retStatus = stat_running;
        try {
            final int exitV = start.getProcess().exitValue(); //liefert ex wenn noch nicht fertig
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
        // counter prüfen und starten bis zu einem Maxwert, sonst endlos
        if (startCounter < START_COUNTER_MAX) {
            // dann nochmal von vorne
            return stat_start;

        } else {
            // dann wars das
            start.setStateError();
            return stat_end;
        }
    }
}