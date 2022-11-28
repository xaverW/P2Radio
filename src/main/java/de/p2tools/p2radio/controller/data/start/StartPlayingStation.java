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


import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.playable.Playable;
import javafx.application.Platform;

import java.awt.*;
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
    private final Playable playable;
    private String exMessage = "";
    private boolean stop = false;
    private int runTime = 0;

    public StartPlayingStation(ProgData progData, Start start) {
        super();
        this.progData = progData;
        this.start = start;
        playable = start.getPlayable();

        setName("START-STATION-THREAD: " + this.start.getStationName());
        setDaemon(true);
        progData.pEventHandler.addListener(new PListener(Events.TIMER) {
            public void ping(PEvent event) {
                ++runTime;
                if (runTime == ProgConst.START_COUNTER_MIN_TIME && playable != null) {
                    StartProgramFactory.setClickCount(playable);
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
            PLog.errorLog(987989569, ex);
            if (start.getStarter().getRestartCounter() == 0) {
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
        start.getStarter().incStartCounter();
        startMsg(start);
        final StartRuntimeExec runtimeExec = new StartRuntimeExec(start);
        final Process process = runtimeExec.exec();
        start.getStarter().setProcess(process);

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
                if (start.getStarter().getProcess() != null) {
                    start.getStarter().getProcess().destroy();
                }

            } else {
                final int exitV = start.getStarter().getProcess().exitValue(); //liefert ex wenn noch nicht fertig
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
        if (start.getStarter().getStartCounter() < StartStatus.START_COUNTER_MAX) {
            // dann nochmal von vorne
            retStatus = stat_start;
        } else {
            // dann wars das
            retStatus = stat_finished_error;
        }
        return retStatus;
    }

    private void startMsg(Start starter) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(PLog.LILNE3);
        list.add("Sender abspielen");

        list.add("URL: " + starter.getUrl());
        list.add("Startzeit: " + PDateFactory.F_FORMAT_HH_mm_ss.format(starter.getStarter().getStartTime()));
        list.add("Programmaufruf: " + starter.getProgramCall());
        list.add("Programmaufruf[]: " + starter.getProgramCallArray());

        list.add(PLog.LILNE_EMPTY);
        PLog.sysLog(list.toArray(new String[list.size()]));
    }

    private void finalizePlaying(Start start) {
        //Aufräumen
        finishedMsg(start);

        if (start.getStartStatus().isStateError()) {
        } else if (!start.getStartStatus().isStateInit()) {
            //dann ist er gelaufen
        }

        refreshTable();

        start.getStarter().setProcess(null);
        start.getStarter().setStartTime(null);

        if (playable != null) {
            playable.setStart(null);
        }
    }

    private void finishedMsg(final Start start) {
        if (ProgConfig.DOWNLOAD_BEEP.get()) {
            try {
                Toolkit.getDefaultToolkit().beep();
            } catch (final Exception ignored) {
            }
        }

        final ArrayList<String> list = new ArrayList<>();
        list.add(PLog.LILNE3);
        list.add("Sender abspielen beendet");
        list.add("Startzeit: " + PDateFactory.F_FORMAT_HH_mm_ss.format(start.getStarter().getStartTime()));
        list.add("Endzeit: " + PDateFactory.F_FORMAT_HH_mm_ss.format(new PDate().getTime()));

        if (start.getStarter().getRestartCounter() > 0) {
            list.add("Restarts: " + start.getStarter().getRestartCounter());
        }

        final long dauer = start.getStarter().getStartTime().diffInMinutes();
        if (dauer == 0) {
            list.add("Dauer: " + start.getStarter().getStartTime().diffInSeconds() + " s");
            //list.add("Dauer: <1 Min.");
        } else {
            list.add("Dauer: " + start.getStarter().getStartTime().diffInMinutes() + " Min");
        }

        list.add("URL: " + start.getUrl());
        list.add("Programmaufruf: " + start.getProgramCall());
        list.add("Programmaufruf[]: " + start.getProgramCallArray());

        list.add(PLog.LILNE_EMPTY);
        PLog.sysLog(list);
    }

    private void refreshTable() {
        if (playable != null && playable.isStation()) {
            ProgData.getInstance().stationGuiController.tableRefresh();

        } else if (playable != null && playable.isFavourite()) {
            ProgData.getInstance().favouriteGuiController.tableRefresh();
            if (progData.smallRadioGuiController != null) {
                progData.smallRadioGuiController.tableRefresh();
            }

        } else if (playable != null && playable.isLastPlayed()) {
            ProgData.getInstance().lastPlayedGuiController.tableRefresh();
        }
    }
}