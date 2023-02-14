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

import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadConfigFile;
import de.p2tools.p2Lib.tools.ProgramToolsFactory;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.controller.data.ImportSetDataFactory;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.gui.startDialog.StartDialogController;
import de.p2tools.p2radio.tools.storedFilter.InitStoredFilter;
import javafx.application.Platform;

import java.nio.file.Path;

public class ProgStartBeforeGui {
    public static boolean firstProgramStart = false;

    private ProgStartBeforeGui() {
    }

    public static boolean workBeforeGui(ProgData progData) {
        boolean loadOk = loadProgConfigData();
        if (ProgConfig.SYSTEM_LOG_ON.get()) {
            PLogger.setFileHandler(ProgInfos.getLogDirectoryString());
        }

        if (!loadOk) {
            PDuration.onlyPing("Erster Start");
            firstProgramStart = true;
            UpdateConfig.setUpdateDone(); //dann ists ja kein Programmupdate
            ProgConfig.SYSTEM_SHOW_MSG_SETDATA_CHANGED.setValue(true);//den Dialog brauchts dann auch nicht
            firstStartDialog(progData);

        } else {
            //dann hat das Laden geklappt :)
            ProgData.getInstance().blackDataList.sortIncCounter(false);
        }
        resetConfigs();
        return firstProgramStart;
    }

    private static void resetConfigs() {
        if (!ProgConfig.SYSTEM_PROG_VERSION.getValueSafe().equals(ProgramToolsFactory.getProgVersion()) ||
                !ProgConfig.SYSTEM_PROG_BUILD_NO.getValueSafe().equals(ProgramToolsFactory.getBuild()) ||
                !ProgConfig.SYSTEM_PROG_BUILD_DATE.getValueSafe().equals(ProgramToolsFactory.getCompileDate())) {

            //dann ist eine neue Version/Build
            PLog.sysLog("===============================");
            PLog.sysLog(" eine neue Version/Build");
            PLog.sysLog(" Einstellung zurücksetzen");

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

        PDate pDate = new PDate(ProgConfig.SYSTEM_PROG_BUILD_DATE.getValueSafe());
        if (pDate.before(new PDate("20.12.2022"))) {
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
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        if (!loadProgConfig()) {
            PLog.sysLog("-> konnte nicht geladen werden!");
            clearConfig();
            return false;

        } else {
            UpdateConfig.update(); //falls es ein Programmupdate gab, Configs anpassen
            PLog.sysLog("-> wurde gelesen!");
            return true;
        }
    }

    private static void clearConfig() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.favouriteList.clear();
        progData.historyList.clear();
        progData.blackDataList.clear();
    }

    private static boolean loadProgConfig() {
        final Path path = ProgInfos.getSettingsFile();
        PLog.sysLog("Programmstart und ProgConfig laden von: " + path);

        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, path);
        ProgConfig.addConfigData(configFile);
        ReadConfigFile readConfigFile = new ReadConfigFile();
        readConfigFile.addConfigFile(configFile);

        return readConfigFile.readConfigFile();
    }
}
