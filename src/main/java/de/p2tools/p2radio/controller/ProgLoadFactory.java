/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

package de.p2tools.p2radio.controller;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class ProgLoadFactory {

    private ProgLoadFactory() {
    }

    /**
     * Senderliste beim Programmstart laden
     */
    public static void loadStationProgStart(boolean firstProgramStart) {
        PLog.sysLog("START: loadStationProgStart");
        final ProgData progData = ProgData.getInstance();
        PDuration.onlyPing("Programmstart Senderliste laden: start");

        progData.pEventHandler.notifyListener(
                new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.START,
                        "", "gespeicherte Senderliste laden",
                        RunEventRadio.PROGRESS_INDETERMINATE, false));

        final List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(PLog.LILNE1);

        if (firstProgramStart) {
            //dann wird immer geladen
            logList.add("erster Programmstart: Neue Senderliste laden");
            progData.loadNewStationList.loadNewStationFromServer();

        } else {
            // gespeicherte Senderliste laden, gibt keine Fortschrittsanzeige und kein Abbrechen
            logList.add("Programmstart, gespeicherte Senderliste laden");
            boolean loadOk = SenderLoadFactory.readList();
            if (!loadOk || progData.stationList.isTooOld() && ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS.get()) {
                //wenn die gespeicherte zu alt ist
                progData.pEventHandler.notifyListener(
                        new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.PROGRESS,
                                "", "Senderliste zu alt, neue Senderliste laden",
                                RunEventRadio.PROGRESS_INDETERMINATE, false/* Fehler */));

                logList.add("Senderliste zu alt, neue Senderliste laden");
                logList.add(PLog.LILNE3);
                progData.loadNewStationList.loadNewStationFromServer();

            } else {
                progData.pEventHandler.notifyListener(
                        new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.LOADED,
                                "", "Senderliste verarbeiten",
                                RunEventRadio.PROGRESS_INDETERMINATE, false/* Fehler */));

                afterLoadingStationList(logList);
                logList.add("Liste der Radios geladen");

                PLog.sysLog("START: loadStationProgStart FINISHED");
                progData.pEventHandler.notifyListener(
                        new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.FINISHED,
                                "", "", 0, false));
            }
        }

        switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
            case 0:
                Platform.runLater(() -> progData.stationGuiController.selUrl());
                break;
            case 1:
                Platform.runLater(() -> progData.favouriteGuiController.selUrl());
                break;
            case 2:
            default:
                Platform.runLater(() -> progData.lastPlayedGuiController.selUrl());
                break;
        }

        logList.add(PLog.LILNE1);
        logList.add("");
        PLog.addSysLog(logList);
    }

    /**
     * alles was nach einem Neuladen oder Einlesen einer gespeicherten Senderliste ansteht
     */
    public static void afterLoadingStationList(List<String> logList) {
        final ProgData progData = ProgData.getInstance();

        logList.add("");
        logList.add("Jetzige Liste erstellt am: " + progData.stationList.getGenDate());
        logList.add("  Anzahl Sender: " + progData.stationList.size());
        logList.add("  Anzahl Neue:  " + progData.stationList.countNewStations());
        logList.add("");
        logList.add(PLog.LILNE2);
        logList.add("");

        progData.pEventHandler.notifyListener(
                new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.LOADED,
                        "", "Sender markieren",
                        RunEventRadio.PROGRESS_INDETERMINATE, false/* Fehler */));

        logList.add("Sender markieren");
        final int count = progData.stationList.markStations();
        logList.add("Anzahl doppelte Sender: " + count);
        logList.add("Tags suchen");
        progData.stationList.loadFilterLists();

        progData.pEventHandler.notifyListener(
                new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.LOADED,
                        "", "Blacklist filtern",
                        RunEventRadio.PROGRESS_INDETERMINATE, false/* Fehler */));

        logList.add(PLog.LILNE3);
        logList.add("Favoriten markieren");
        StationListFactory.findAndMarkFavouriteStations(progData);

        logList.add(PLog.LILNE3);
        logList.add("Blacklist filtern");
        progData.stationList.filterListWithBlacklist(true);

        progData.pEventHandler.notifyListener(
                new RunEventRadio(Events.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.LOADED,
                        "", "Sender in Favoriten eingetragen",
                        RunEventRadio.PROGRESS_INDETERMINATE, false/* Fehler */));

        logList.add("Sender in Favoriten eingetragen");
        progData.favouriteList.addStationInList();
        progData.lastPlayedList.addStationInList();
    }
}
