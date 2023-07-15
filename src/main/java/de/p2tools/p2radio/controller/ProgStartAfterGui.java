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

package de.p2tools.p2radio.controller;

import de.p2tools.p2lib.guitools.P2WindowIcon;
import de.p2tools.p2lib.tools.ProgramToolsFactory;
import de.p2tools.p2lib.tools.date.DateFactory;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.LogMessage;
import de.p2tools.p2lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.controller.radiosreadwritefile.StationLoadFactory;
import de.p2tools.p2radio.tools.update.SearchProgramUpdate;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgStartAfterGui {

    private ProgStartAfterGui() {
    }

    /**
     * alles was nach der GUI gemacht werden soll z.B.
     * Senderliste beim Programmstart!! laden
     */
    public static void workAfterGui(ProgData progData) {
        P2WindowIcon.addWindowP2Icon(progData.primaryStage);
        startMsg();
        setTitle(progData.primaryStage);

        progData.initProgData();
        checkProgUpdate(progData);
        loadStationProgStart();
    }

    private static void startMsg() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfos.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectoryString());
        list.add(PLog.LILNE2);
        list.add("");
        list.add("Programmsets:");
        list.addAll(ProgData.getInstance().setDataList.getStringListSetData());
        ProgConfig.getConfigLog(list);
        LogMessage.startMsg(ProgConst.PROGRAM_NAME, list);
    }

    public static void setTitle(Stage stage) {
        if (ProgData.debug) {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + ProgramToolsFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + ProgramToolsFactory.getProgVersion());
        }
    }

    private static void checkProgUpdate(ProgData progData) {
        // Prüfen obs ein Programmupdate gibt
        PDuration.onlyPing("checkProgUpdate");
        if (ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get() &&
                !updateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck(progData, false);

        } else {
            // will der User nicht --oder-- wurde heute schon gemacht
            List list = new ArrayList(5);
            list.add("Kein Update-Check:");
            if (!ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get()) {
                list.add("  der User will nicht");
            }
            if (updateCheckTodayDone()) {
                list.add("  heute schon gemacht");
            }
            PLog.sysLog(list);
        }
    }

    private static boolean updateCheckTodayDone() {
        return ProgConfig.SYSTEM_UPDATE_DATE.get().equals(DateFactory.F_FORMAT_yyyy_MM_dd.format(new Date()));
    }

    private static void runUpdateCheck(ProgData progData, boolean showAlways) {
        //prüft auf neue Version, ProgVersion und auch (wenn gewünscht) BETA-Version, ..
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(DateFactory.F_FORMAT_yyyy_MM_dd.format(new Date()));
        new SearchProgramUpdate(progData).searchNewProgramVersion(showAlways);
    }

    /**
     * Senderliste beim Programmstart laden
     */
    private static void loadStationProgStart() {
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

        if (ProgData.firstProgramStart) {
            //dann wird immer geladen
            logList.add("erster Programmstart: Neue Senderliste laden");
            progData.loadNewStationList.loadNewStationFromServer();

        } else {
            // gespeicherte Senderliste laden, gibt keine Fortschrittsanzeige und kein Abbrechen
            logList.add("Programmstart, gespeicherte Senderliste laden");
            boolean loadOk = StationLoadFactory.readList();
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
                Platform.runLater(() -> progData.stationGuiPack.getStationGuiController().selUrl());
                break;
            case 1:
                Platform.runLater(() -> progData.favouriteGuiPack.getFavouriteGuiController().selUrl());
                break;
            case 2:
            default:
                Platform.runLater(() -> progData.historyGuiPack.getHistoryGuiController().selUrl());
                break;
        }

        logList.add(PLog.LILNE1);
        logList.add("");
        PLog.sysLog(logList);
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
        StationListFactory.addStationInList(progData.favouriteList);
        StationListFactory.addStationInList(progData.historyList);
    }
}
