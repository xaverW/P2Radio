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
        StartFactory.stopStation(false);

        if (ProgData.getInstance().primaryStage.isShowing()) {
            P2GuiSize.getSize(ProgConfig.SYSTEM_SIZE_GUI, ProgData.getInstance().primaryStage);
        }

        if (ProgData.getInstance().primaryStageSmall != null &&
                ProgData.getInstance().primaryStageSmall.isShowing()) {
            P2GuiSize.getSize(ProgConfig.SMALL_RADIO_SIZE, ProgData.getInstance().primaryStageSmall);
        }

        saveProgConfig();
        P2LogMessage.endMsg();

        // und dann Programm beenden
        Platform.runLater(() -> {
            // dann jetzt beenden -> Tschüss
            Platform.exit();
            System.exit(0);
        });
    }

    public static void saveProgConfig() {
        // Tabellen holen
        writeTableWindowSettings();

        // sind die Programmeinstellungen
        P2Log.sysLog("Alle Programmeinstellungen sichern");
        final Path xmlFilePath = ProgInfos.getSettingsFile();
        ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true);
        ProgConfig.addConfigData(configFile);
        ConfigWriteFile.writeConfigFile(configFile);
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
}
