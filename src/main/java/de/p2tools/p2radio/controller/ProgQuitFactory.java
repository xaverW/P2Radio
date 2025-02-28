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
import de.p2tools.p2lib.configfile.ConfigWriteFile;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import javafx.application.Platform;

import java.nio.file.Path;

public class ProgQuitFactory {

    private ProgQuitFactory() {
    }

    /**
     * Quit the application
     */
    public static void quit() {
        //dann jetzt beenden, aus Button
        StartFactory.stopRunningStation(false);
        writeTableWindowSettings();

        if (ProgData.getInstance().primaryStageBig.isShowing()) {
            P2GuiSize.getSize(ProgConfig.SYSTEM_SIZE_GUI, ProgData.getInstance().primaryStageBig);
        }

        if (ProgData.getInstance().primaryStageSmall != null && ProgData.getInstance().primaryStageSmall.isShowing()) {
            P2GuiSize.getSize(ProgConfig.SMALL_RADIO_SIZE, ProgData.getInstance().primaryStageSmall);
        }

        saveProgConfig();
        P2LogMessage.endMsg();

        // ============
        if (ProgConfig.SYSTEM_SIZE_GUI.getValue().equals(ProgData.gui)) {
            P2Log.sysLog("GUI ist gleich");
            P2Log.sysLog("GUI: " + ProgConfig.SYSTEM_SIZE_GUI.getValue());
        } else {
            P2Log.sysLog("GUI: " + ProgData.gui);
            P2Log.sysLog("GUI: " + ProgConfig.SYSTEM_SIZE_GUI.getValue());
        }

        P2Log.sysLog("");
        if (ProgConfig.SMALL_RADIO_SIZE.getValue().equals(ProgData.small)) {
            P2Log.sysLog("SMALL ist gleich");
            P2Log.sysLog("SMALL: " + ProgConfig.SMALL_RADIO_SIZE.getValue());
        } else {
            P2Log.sysLog("SMALL: " + ProgData.small);
            P2Log.sysLog("SMALL: " + ProgConfig.SMALL_RADIO_SIZE.getValue());
        }

        P2Log.sysLog("");
        if (ProgConfig.STATION__FILTER_DIALOG_SIZE.getValue().equals(ProgData.dialog)) {
            P2Log.sysLog("DIALOG ist gleich");
            P2Log.sysLog("DIALOG: " + ProgConfig.STATION__FILTER_DIALOG_SIZE.getValue());
        } else {
            P2Log.sysLog("DIALOG: " + ProgData.dialog);
            P2Log.sysLog("DIALOG: " + ProgConfig.STATION__FILTER_DIALOG_SIZE.getValue());
        }
        // ============

        // und dann Programm beenden
        Platform.runLater(() -> {
            // dann jetzt beenden -> Tschüss
            Platform.exit();
            System.exit(0);
        });
    }

    private static void writeTableWindowSettings() {
        // Tabelleneinstellungen merken
        if (ProgData.getInstance().smallRadioGuiController != null) {
            ProgData.getInstance().smallRadioGuiController.saveTable();
        }
        if (ProgData.getInstance().stationGuiPack != null) {
            ProgData.getInstance().stationGuiPack.getStationGuiController().saveTable();
        }
        if (ProgData.getInstance().favouriteGuiPack != null) {
            ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().saveTable();
        }
        if (ProgData.getInstance().historyGuiPack != null) {
            ProgData.getInstance().historyGuiPack.getHistoryGuiController().saveTable();
        }
    }

    public static void saveProgConfig() {
        //sind die Programmeinstellungen
        P2Log.sysLog("Alle Programmeinstellungen sichern");
        final Path xmlFilePath = ProgInfos.getSettingsFile();
        ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true);
        ProgConfig.addConfigData(configFile);
        ConfigWriteFile.writeConfigFile(configFile);
    }
}
