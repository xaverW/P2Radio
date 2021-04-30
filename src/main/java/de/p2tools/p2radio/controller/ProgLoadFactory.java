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

import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadConfigFile;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventNotifyLoadRadioList;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import javafx.application.Platform;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProgLoadFactory {

    private ProgLoadFactory() {
    }

    public static boolean loadProgConfigData() {
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        boolean found;
        if ((found = loadProgConfig()) == false) {
            //todo? teils geladene Reste entfernen
            PLog.sysLog("-> konnte nicht geladen werden!");
            clearConfig();
        } else {
            initAfterLoad();
            UpdateConfig.update(); // falls es ein Programmupdate gab, Configs anpassen
            PLog.sysLog("-> wurde gelesen!");
        }

        initP2Lib();
        return found;
    }

    /**
     * Senderliste beim Programmstart laden
     */
    public static void loadStationProgStart(boolean firstProgramStart) {
        // Gui startet ein wenig flüssiger
        Thread th = new Thread(() -> {
            final ProgData progData = ProgData.getInstance();
            PDuration.onlyPing("Programmstart Senderliste laden: start");

            progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.START,
                    new EventLoadRadioList("", "gespeicherte Senderliste laden",
                            EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false));

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

                    progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.PROGRESS,
                            new EventLoadRadioList("", "Senderliste zu alt, neue Senderliste laden",
                                    EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

                    logList.add("Senderliste zu alt, neue Senderliste laden");
                    logList.add(PLog.LILNE3);
                    progData.loadNewStationList.loadNewStationFromServer();

                } else {
                    progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.LOADED,
                            new EventLoadRadioList("", "Senderliste verarbeiten",
                                    EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

                    afterLoadingStationList(logList);
                    logList.add("Liste der Radios geladen");

                    progData.eventNotifyLoadRadioList.notifyFinishedOk();
                }
            }

            boolean panelStation = ProgConfig.SYSTEM_LAST_TAB_STATION.get();
            if (panelStation) {
                Platform.runLater(() -> progData.stationGuiController.selUrl());
            } else {
                Platform.runLater(() -> progData.favouriteGuiController.selUrl());
            }

            logList.add(PLog.LILNE1);
            logList.add("");
            PLog.addSysLog(logList);
        });

        th.setName("loadStationProgStart");
        th.start();
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

        progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.LOADED,
                new EventLoadRadioList("", "Sender markieren",
                        EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

        logList.add("Sender markieren");
        final int count = progData.stationList.markStations();
        logList.add("Anzahl doppelte Sender: " + count);
        logList.add("Tags suchen");
        progData.stationList.loadFilterLists();

        progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.LOADED,
                new EventLoadRadioList("", "Blacklist filtern",
                        EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

        logList.add(PLog.LILNE3);
        logList.add("Favoriten markieren");
        StationListFactory.findAndMarkFavouriteStations(progData);

        logList.add(PLog.LILNE3);
        logList.add("Blacklist filtern");
        progData.stationList.filterListWithBlacklist(true);

        progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.LOADED,
                new EventLoadRadioList("", "Sender in Favoriten eingetragen",
                        EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

        logList.add("Sender in Favoriten eingetragen");
        progData.favouriteList.addStationInList();
    }

    private static void clearConfig() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.favouriteList.clear();
        progData.blackDataList.clear();
    }

    private static boolean loadProgConfig() {
        final Path path = ProgInfos.getSettingsFile();
        PLog.sysLog("Programmstart und ProgConfig laden von: " + path.toString());
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, path);

        ProgConfig.addConfigData(configFile);

        ReadConfigFile readConfigFile = new ReadConfigFile();
        readConfigFile.addConfigFile(configFile);

        boolean ret = readConfigFile.readConfigFile();
        return ret;
    }

    private static void initP2Lib() {
        P2LibInit.initLib(ProgData.getInstance().primaryStage, ProgConst.PROGRAM_NAME, ProgInfos.getUserAgent(),
                ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private static void initAfterLoad() {
        ProgData.getInstance().blackDataList.sortIncCounter(false);
    }
}


