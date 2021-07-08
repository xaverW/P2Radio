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

import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.dialog.QuitDialogController;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ProgQuitFactory {

    private ProgQuitFactory() {
    }

    private static void stopAllStations() {
        //todo->station auch stoppen
        ProgData.getInstance().startFactory.stopAll();
    }

    private static void writeWindowSizes() {
        // Hauptfenster
        PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, ProgData.getInstance().primaryStage);
    }

    private static void writeTableSettings() {
        // Tabelleneinstellungen merken
        ProgData.getInstance().stationGuiController.saveTable();
        ProgData.getInstance().favouriteGuiController.saveTable();
        ProgData.getInstance().lastPlayedGuiController.saveTable();
    }

    /**
     * Quit the application
     *
     * @param showOptionTerminate show options dialog when stations are running
     */
    public static boolean quit(Stage stage, boolean showOptionTerminate) {
        if (ProgData.getInstance().favouriteList.countStartedAndRunningFavourites() > 0 ||
                ProgData.getInstance().stationList.countStartedAndRunningFavourites() > 0 ||
                ProgData.getInstance().lastPlayedList.countStartedAndRunningFavourites() > 0) {
            if (showOptionTerminate) {
                //dann erst mal fragen
                QuitDialogController quitDialogController;
                quitDialogController = new QuitDialogController(stage);
                if (!quitDialogController.canTerminate()) {
                    //und nicht beenden
                    return false;
                }
            }
        }

        //dann jetzt beenden
        writeTableSettings();
        stopAllStations();
        writeWindowSizes();

        ProgSaveFactory.saveProgConfig();
        LogMessage.endMsg();

        // und dann Programm beenden
        Platform.runLater(() -> {
            // dann jetzt beenden -> Thüss
            Platform.exit();
            System.exit(0);
        });
        return true;
    }

//    private static boolean quit_(Stage stage, boolean showOptionTerminate) {
//        // erst mal prüfen ob noch Sender laufen
//        //todo auch Sender aus Tab Sender
//        if (ProgData.getInstance().favouriteList.countStartedAndRunningFavourites() > 0 ||
//                ProgData.getInstance().stationList.countStartedAndRunningFavourites() > 0 ||
//                ProgData.getInstance().lastPlayedList.countStartedAndRunningFavourites() > 0) {
//
//            // und ob der Dialog angezeigt werden soll
//            if (showOptionTerminate) {
//                QuitDialogController quitDialogController;
//                quitDialogController = new QuitDialogController(stage);
//                if (!quitDialogController.canTerminate()) {
//                    return false;
//                }
//            }
//        }
//
//        // und dann Programm beenden
//        writeTableSettings();
//        stopAllStations();
//        writeWindowSizes();
//
//        ProgSaveFactory.saveProgConfig();
//        LogMessage.endMsg();
//        return true;
//    }
}
