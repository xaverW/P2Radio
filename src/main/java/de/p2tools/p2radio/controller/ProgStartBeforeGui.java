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

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.tools.ProgramToolsFactory;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2Logger;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.config.UpdateConfig;
import de.p2tools.p2radio.controller.data.ImportSetDataFactory;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.gui.startdialog.StartDialogController;
import de.p2tools.p2radio.tools.storedfilter.InitStoredFilter;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProgStartBeforeGui {

    private ProgStartBeforeGui() {
    }

    public static void workBeforeGui(ProgData progData) {
        if (!loadProgConfigData()) {
            PDuration.onlyPing("Erster Start");
            ProgData.firstProgramStart = true;
            UpdateConfig.setUpdateDone(); //dann ists ja kein Programmupdate
            ProgConfig.SYSTEM_SHOW_MSG_SETDATA_CHANGED.setValue(true);//den Dialog brauchts dann auch nicht
            firstStartDialog(progData);

        } else {
            //dann hat das Laden geklappt :)
            ProgData.getInstance().blackDataList.sortIncCounter(false);
        }
        resetConfigs();
    }

    private static void resetConfigs() {
        if (!ProgConfig.SYSTEM_PROG_VERSION.getValueSafe().equals(ProgramToolsFactory.getProgVersion()) ||
                !ProgConfig.SYSTEM_PROG_BUILD_NO.getValueSafe().equals(ProgramToolsFactory.getBuild()) ||
                !ProgConfig.SYSTEM_PROG_BUILD_DATE.getValueSafe().equals(ProgramToolsFactory.getCompileDate())) {

            //dann ist eine neue Version/Build
            P2Log.sysLog("===============================");
            P2Log.sysLog(" eine neue Version/Build");
            P2Log.sysLog(" Einstellung zurücksetzen");

            ProgConfig.STATION_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.STATION_GUI_TABLE_SORT.setValue("");
            ProgConfig.STATION_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.STATION_GUI_TABLE_VIS.setValue("");
            ProgConfig.STATION_GUI_TABLE_ORDER.setValue("");

            ProgConfig.SMALL_RADIO_TABLE_STATION_WIDTH.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_STATION_SORT.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_STATION_UP_DOWN.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_STATION_VIS.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_STATION_ORDER.setValue("");

            ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_WIDTH.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_SORT.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_UP_DOWN.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_VIS.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_ORDER.setValue("");

            ProgConfig.SMALL_RADIO_TABLE_HISTORY_WIDTH.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_HISTORY_SORT.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_HISTORY_UP_DOWN.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_HISTORY_VIS.setValue("");
            ProgConfig.SMALL_RADIO_TABLE_HISTORY_ORDER.setValue("");

            ProgConfig.FAVOURITE_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.FAVOURITE_GUI_TABLE_SORT.setValue("");
            ProgConfig.FAVOURITE_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.FAVOURITE_GUI_TABLE_VIS.setValue("");
            ProgConfig.FAVOURITE_GUI_TABLE_ORDER.setValue("");

            ProgConfig.HISTORY_GUI_TABLE_WIDTH.setValue("");
            ProgConfig.HISTORY_GUI_TABLE_SORT.setValue("");
            ProgConfig.HISTORY_GUI_TABLE_UP_DOWN.setValue("");
            ProgConfig.HISTORY_GUI_TABLE_VIS.setValue("");
            ProgConfig.HISTORY_GUI_TABLE_ORDER.setValue("");
        }

        P2Date pDate = new P2Date(ProgConfig.SYSTEM_PROG_BUILD_DATE.getValueSafe());
        if (pDate.before(new P2Date("20.12.2022"))) {
            //Die Sets haben sich geändert
            if (ProgData.getInstance().setDataList.isEmpty()) {
                final SetDataList pSet = ImportSetDataFactory.getStandarset();
                if (pSet != null) {
                    ProgData.getInstance().setDataList.addSetData(pSet);
                }
            }

            //früher wurden die starts im Klickfeld gespeichert
            ProgData.getInstance().favouriteList.stream().forEach(stationData -> {
                stationData.setStarts(stationData.getClickCount());
            });
            ProgData.getInstance().historyList.stream().forEach(stationData -> {
                stationData.setStarts(stationData.getClickCount());
            });
        }
    }

    private static void firstStartDialog(ProgData progData) {
        StartDialogController startDialogController = new StartDialogController();
        if (!startDialogController.isOk()) {
            // dann jetzt beenden -> Tschüss
            Platform.exit();
            System.exit(0);
        }

        PDuration.onlyPing("Erster Start: PSet");
        final SetDataList pSet = ImportSetDataFactory.getStandarset();
        if (pSet != null) {
            progData.setDataList.addSetData(pSet);
        }
        PDuration.onlyPing("Erster Start: PSet geladen");

        InitStoredFilter.initFilter();
    }

    private static boolean loadProgConfigData() {
        if (ProgConfig.SYSTEM_LOG_ON.get()) {
            P2Logger.setFileHandler(ProgInfos.getLogDirectoryString());
        }

        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        if (!loadProgConfig()) {
            P2Log.sysLog("-> konnte nicht geladen werden!");
            clearTheConfigs();
            return false;

        } else {
            UpdateConfig.update(); //falls es ein Programmupdate gab, Configs anpassen
            P2Log.sysLog("-> wurde gelesen!");
            return true;
        }
    }

    private static boolean loadProgConfig() {
        final Path xmlFilePath = ProgInfos.getSettingsFile();
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        try {
            if (!Files.exists(xmlFilePath)) {
                //dann gibts das Konfig-File gar nicht
                P2Log.sysLog("Konfig existiert nicht!");
                return false;
            }

            P2Log.sysLog("Programmstart und ProgConfig laden von: " + xmlFilePath);
            ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true) {
                @Override
                public void clearConfigFile() {
                    clearTheConfigs();
                }
            };
            ProgConfig.addConfigData(configFile);
            if (ConfigReadFile.readConfig(configFile)) {
                P2Log.sysLog("Konfig wurde geladen!");
                return true;

            } else {
                // dann hat das Laden nicht geklappt
                P2Log.sysLog("Konfig konnte nicht geladen werden!");
                return false;
            }
        } catch (final Exception ex) {
            P2Log.errorLog(915470101, ex);
        }
        return false;
    }

    private static void clearTheConfigs() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.favouriteList.clear();
        progData.historyList.clear();
        progData.blackDataList.clear();
    }
}
