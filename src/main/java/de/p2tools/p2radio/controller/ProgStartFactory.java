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

import de.p2tools.p2Lib.icon.GetIcon;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.date.PDateFactory;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.controller.data.PsetVorlagen;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.gui.startDialog.StartDialogController;
import de.p2tools.p2radio.tools.storedFilter.InitStoredFilter;
import de.p2tools.p2radio.tools.update.SearchProgramUpdate;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgStartFactory {

    private ProgStartFactory() {
    }

    public static boolean workBeforeGui(ProgData progData) {
        boolean firstProgramStart = false;
        boolean loadOk = ProgLoadFactory.loadProgConfigData();
        if (ProgConfig.SYSTEM_LOG_ON.get()) {
            PLogger.setFileHandler(ProgInfos.getLogDirectoryString());
        }

        if (!loadOk) {
            PDuration.onlyPing("Erster Start");
            firstProgramStart = true;
            UpdateConfig.setUpdateDone(); //dann ists ja kein Programmupdate

            StartDialogController startDialogController = new StartDialogController();
            if (!startDialogController.isOk()) {
                // dann jetzt beenden -> Tschüss
                Platform.exit();
                System.exit(0);
            }

            Platform.runLater(() -> {
                PDuration.onlyPing("Erster Start: PSet");
                // kann ein Dialog aufgehen
                final SetDataList pSet = new PsetVorlagen().getStandarset(true /*replaceMuster*/);
                if (pSet != null) {
                    progData.setDataList.addSetData(pSet);
                    ProgConfig.SYSTEM_UPDATE_PROGSET_VERSION.setValue(pSet.version);
                }
                PDuration.onlyPing("Erster Start: PSet geladen");
            });

            InitStoredFilter.initFilter();
        }
        return firstProgramStart;
    }

    /**
     * alles was nach der GUI gemacht werden soll z.B.
     * Senderliste beim Programmstart!! laden
     *
     * @param firstProgramStart
     */
    public static void workAfterGui(ProgData progData, boolean firstProgramStart) {
        GetIcon.addWindowP2Icon(progData.primaryStage);
        startMsg();
        setTitle(progData.primaryStage);

        progData.startTimer();
        checkProgUpdate(progData);
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
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + ProgramTools.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + ProgramTools.getProgVersion());
        }
    }

    private static void checkProgUpdate(ProgData progData) {
        // Prüfen obs ein Programmupdate gibt
        PDuration.onlyPing("checkProgUpdate");


        if (ProgData.debug) {
            // damits bei jedem Start gemacht wird
            PLog.sysLog("DEBUG: Update-Check");
            runUpdateCheck(progData, true);

        } else if (ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get() &&
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
        return ProgConfig.SYSTEM_UPDATE_DATE.get().equals(PDateFactory.F_FORMAT_yyyy_MM_dd.format(new Date()));
    }

    private static void runUpdateCheck(ProgData progData, boolean showAlways) {
        //prüft auf neue Version, ProgVersion und auch (wenn gewünscht) BETA-Version, ..
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(PDateFactory.F_FORMAT_yyyy_MM_dd.format(new Date()));
        new SearchProgramUpdate(progData).searchNewProgramVersion(showAlways);
    }
}
