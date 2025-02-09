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


import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Start Station via an external program.
 */
public class StartPlayingStation extends Thread {

    private final ProgData progData;
    private final int stat_start = 0;
    private final int stat_running = 1;
    private final int stat_restart = 3;
    private final int stat_finished_ok = 10; //ab hier ist schluss
    private final int stat_finished_error = 11;
    private final int stat_end = 99;
    private final Start start;
    private final StationData stationData;
    private String exMessage = "";
    private boolean stop = false;
    private int runTime = 0;

    public StartPlayingStation(ProgData progData, Start start) {
        super();
        this.progData = progData;
        this.start = start;
        stationData = start.getStationData();

        setName("START-STATION-THREAD: " + this.start.getStationName());
        setDaemon(true);
        progData.pEventHandler.addListener(new P2Listener(Events.TIMER) {
            public void ping(P2Event event) {
                ++runTime;
                if (runTime == ProgConst.START_COUNTER_MIN_TIME && stationData != null) {
                    StartProgramFactory.setStartCounter(stationData);
                }
            }
        });
    }

    void stopThread() {
        stop = true;
    }

    @Override
    public synchronized void run() {
        int stat = stat_start;
        refreshTable();

        try {
            while (stat < stat_end) {
                if (stop) {
                    start.getStartStatus().setState(StartStatus.STATE_INIT);
                }
                stat = runningLoop(stat);
            }
        } catch (final Exception ex) {
            exMessage = ex.getLocalizedMessage();
            P2Log.errorLog(987989569, ex);
            if (start.getRestartCounter() == 0) {
                // nur beim ersten Mal melden -> nervt sonst
                Platform.runLater(() -> new StartStationErrorDialogController(start, exMessage));
            }
            start.getStartStatus().setState(StartStatus.STATE_ERROR);
            start.getStartStatus().setErrorMessage(exMessage);
        }

        finalizePlaying(start);
    }

    private int runningLoop(int stat) {
        switch (stat) {
            case stat_start:
                // versuch das Programm zu Starten
                stat = startProgram();
                break;

            case stat_running:
                // hier läuft der Download bis zum Abbruch oder Ende
                stat = runProgram();
                break;

            case stat_restart:
                stat = restartProgram();
                break;

            case stat_finished_error:
                start.getStartStatus().setStateError();
                stat = stat_end;
                break;

            case stat_finished_ok:
                start.getStartStatus().setStateFinished();
                stat = stat_end;
                break;
        }

        return stat;
    }

    private int startProgram() {
        //versuch das Programm zu Starten
        //die Reihenfolge: startCounter - startmeldung ist wichtig!
        int retStat;
        start.incStartCounter();
        startMsg(start);
        final StartRuntimeExec runtimeExec = new StartRuntimeExec(start);
        final Process process = runtimeExec.exec();
        start.setProcess(process);

        if (process != null) {
            start.getStartStatus().setStateStartedRun();
            retStat = stat_running;
        } else {
            retStat = stat_restart;
        }
        return retStat;
    }

    private int runProgram() {
        //hier läufts bis zum Abbruch oder Ende
        int retStatus = stat_running;
        try {
            if (start.getStartStatus().isStateStopped()) {
                //soll abgebrochen werden
                retStatus = stat_finished_ok;
                if (start.getProcess() != null) {
                    start.getProcess().destroy();
                }

            } else {
                final int exitV = start.getProcess().exitValue(); //liefert ex wenn noch nicht fertig
                if (exitV != 0) {
                    retStatus = stat_restart;
                } else {
                    retStatus = stat_finished_ok;
                }
            }
        } catch (final Exception ex) {
            try {
                this.wait(2000);
            } catch (final InterruptedException ignored) {
            }
        }
        return retStatus;
    }

    private int restartProgram() {
        int retStatus;
        // counter prüfen und starten bis zu einem Maxwert, sonst endlos
        if (start.getStartCounter() < StartStatus.START_COUNTER_MAX) {
            // dann nochmal von vorne
            retStatus = stat_start;
        } else {
            // dann wars das
            retStatus = stat_finished_error;
        }
        return retStatus;
    }

    private void finalizePlaying(Start start) {
        //Aufräumen
        PlayingTitle.stopNowPlaying();
        finishedMsg(start);

        if (start.getStartStatus().isStateError()) {

        } else {
            //dann ist er gelaufen und wurde beendet (Programm hat sich beendet: durch fehlerhafte URL oder user)
            start.setProcess(null);
            start.setStartTime(null);
            if (stationData != null) {
                stationData.setStart(null);
            }
        }

//        StartFactory.stopRunningStation();
//        System.out.println("refreshTable");
        refreshTable();
    }

    private void startMsg(Start starter) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Sender abspielen");

        list.add("URL: " + starter.getUrl());
        list.add("Startzeit: " + P2DateConst.F_FORMAT_dd_MM_yyyy___HH__mm__ss.format(starter.getStartTime()));
        list.add("Programmaufruf: " + starter.getProgramCall());
        list.add("Programmaufruf[]: " + starter.getProgramCallArray());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list.toArray(new String[list.size()]));
    }

    private void finishedMsg(final Start start) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Sender abspielen beendet");
        list.add("Startzeit: " + P2DateConst.F_FORMAT_dd_MM_yyyy___HH__mm__ss.format(start.getStartTime()));
        list.add("Endzeit: " + P2DateConst.DT_FORMATTER_dd_MM_yyyy___HH__mm__ss.format(LocalDateTime.now()));

        if (start.getRestartCounter() > 0) {
            list.add("Restarts: " + start.getRestartCounter());
        }

        final long dauer = start.getStartTime().diffInMinutes();
        if (dauer == 0) {
            list.add("Dauer: " + start.getStartTime().diffInSeconds() + " s");
            //list.add("Dauer: <1 Min.");
        } else {
            list.add("Dauer: " + start.getStartTime().diffInMinutes() + " Min");
        }

        list.add("URL: " + start.getUrl());
        list.add("Programmaufruf: " + start.getProgramCall());
        list.add("Programmaufruf[]: " + start.getProgramCallArray());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list);
    }

    private void refreshTable() {
        //nicht optimal??
        progData.pEventHandler.notifyListener(new P2Event(Events.REFRESH_TABLE));
    }
}