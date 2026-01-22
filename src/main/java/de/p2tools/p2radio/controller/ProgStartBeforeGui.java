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

import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.tools.P2ToolsRaspberry;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2Logger;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.ImportSetDataFactory;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.gui.startdialog.StartDialogController;
import de.p2tools.p2radio.tools.storedfilter.InitStoredFilter;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ProgStartBeforeGui {

    private ProgStartBeforeGui() {
    }

    public static void workBeforeGui(ProgData progData) {
        ProgData.raspberry = P2ToolsRaspberry.isRaspberry();
        boolean load = loadAll();
        initP2lib();

        if (!load) {
            // dann ist der erste Start
            P2Duration.onlyPing("Erster Start");
            ProgData.firstProgramStart = true;

            StartDialogController startDialogController = new StartDialogController();
            if (!startDialogController.isOk()) {
                // dann jetzt beenden -> Tschüs
                Platform.exit();
                System.exit(0);
            }

            P2Duration.onlyPing("Erster Start: PSet");
            final SetDataList pSet = ImportSetDataFactory.getStandarset();
            if (pSet != null) {
                progData.setDataList.addSetData(pSet);
            }
            P2Duration.onlyPing("Erster Start: PSet geladen");

            InitStoredFilter.initFilter();
            ProgConfigUpdate.setUpdateDone(); // brauchts dann ja nicht
        }

        ProgData.getInstance().blackDataList.sortIncCounter(false);
    }

    private static void initP2lib() {
        P2LibInit.initLib(ProgData.getInstance().primaryStage, ProgConst.PROGRAM_NAME, "",
                ProgConfig.SYSTEM_THEME_CHANGED,
                ProgConfig.SYSTEM_DARK_THEME,
                ProgConfig.SYSTEM_BLACK_WHITE_ICON,
                ProgConfig.SYSTEM_ICON_COLOR,
                ProgConfig.SYSTEM_CSS_ADDER,

                new String[]{
                        "de/p2tools/p2radio/css/mtfx.css",
                        "de/p2tools/p2radio/css/pFuncBtn.css",
                        "de/p2tools/p2radio/css/pFuncMenu.css",
                        "de/p2tools/p2radio/css/pFuncTitleBar.css",
                        "de/p2tools/p2radio/css/pFuncTable.css",
                        "de/p2tools/p2radio/css/pFuncToolBar.css",
                        "de/p2tools/p2radio/css/pFuncTips.css",
                        "de/p2tools/p2radio/css/pFuncStartDialog.css",
                        "de/p2tools/p2radio/css/smallGui.css"
                },

                new String[]{
                        "de/p2tools/p2radio/css/mtfx-dark.css"
                },

                ProgData.getInstance().cssProp,
                ProgConfig.SYSTEM_FONT_SIZE,

                null,
                ProgConst.PROGRAM_ICON, P2RadioFactory.getOwnIconPath(),
                ProgData.debug, ProgData.duration);

        P2RadioFactory.setProgramIcon();
    }

    private static boolean loadAll() {
        ArrayList<String> logList = new ArrayList<>();
        boolean ret = load(logList);

        if (ProgConfig.SYSTEM_LOG_ON.getValue()) {
            // dann für den evtl. geänderten LogPfad
            P2Logger.setFileHandler(ProgInfos.getLogDirectory_String());
        }
        P2Log.sysLog(logList);

        if (!ret) {
            P2Log.sysLog("Weder Konfig noch Backup konnte geladen werden!");
            // teils geladene Reste entfernen
            clearTheConfigs();
        }

        return ret;
    }

    private static boolean load(ArrayList<String> logList) {
        final Path xmlFilePath = ProgInfos.getSettingsFile();
        P2Duration.onlyPing("ProgStartFactory.loadProgConfigData");
        try {
            if (!Files.exists(xmlFilePath)) {
                //dann gibts das Konfig-File gar nicht
                logList.add("Konfig existiert nicht!");
                return false;
            }

            logList.add("Programmstart und ProgConfig laden von: " + xmlFilePath);
            ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true) {
                @Override
                public void clearConfigFile() {
                    clearTheConfigs();
                }
            };
            ProgConfig.addConfigData(configFile);
            if (ConfigReadFile.readConfig(configFile)) {
                initAfterLoad();
                logList.add("Konfig wurde geladen!");
                return true;

            } else {
                // dann hat das Laden nicht geklappt
                logList.add("Konfig konnte nicht geladen werden!");
                return false;
            }
        } catch (final Exception ex) {
            logList.add(ex.getLocalizedMessage());
        }
        return false;
    }

    private static void initAfterLoad() {
        //dann hat das Laden geklappt :)
        ProgData.getInstance().blackDataList.sortIncCounter(false);
        ProgConfigUpdate.update(); // falls es ein Programmupdate gab, Configs anpassen
    }

    private static void clearTheConfigs() {
        ProgData progData = ProgData.getInstance();
        progData.setDataList.clear();
        progData.favouriteList.clear();
        progData.historyList.clear();
        progData.blackDataList.clear();
    }
}
