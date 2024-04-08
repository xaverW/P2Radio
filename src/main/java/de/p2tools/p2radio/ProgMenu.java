package de.p2tools.p2radio;/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


import de.p2tools.p2lib.guitools.P2Open;
import de.p2tools.p2lib.tools.log.P2Logger;
import de.p2tools.p2lib.tools.shortcut.P2ShortcutWorker;
import de.p2tools.p2radio.controller.ProgQuit;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.P2RadioShortCuts;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.gui.configdialog.ConfigDialogController;
import de.p2tools.p2radio.gui.dialog.AboutDialogController;
import de.p2tools.p2radio.gui.dialog.ResetDialogController;
import de.p2tools.p2radio.tools.update.SearchProgramUpdate;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

public class ProgMenu extends MenuButton {
    public ProgMenu() {
        makeMenu();
        setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.SYSTEM_DARK_THEME.setValue(!ProgConfig.SYSTEM_DARK_THEME.getValue());

            }
        });
    }

    private void makeMenu() {
        ProgData progData = ProgData.getInstance();
        // Menü
        final MenuItem miConfig = new MenuItem("Einstellungen des Programms");
        miConfig.setOnAction(e -> new ConfigDialogController(progData));

        final MenuItem miLoadStationList = new MenuItem("Neue Senderliste laden");
        miLoadStationList.setOnAction(e -> progData.loadNewStationList.loadNewStationFromServer());

        final CheckMenuItem miDarkMode = new CheckMenuItem("Dark Mode");
        miDarkMode.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DARK_THEME);

        final MenuItem miQuit = new MenuItem("Beenden");
        miQuit.setOnAction(e -> ProgQuit.quit(progData.primaryStage, true));
        P2ShortcutWorker.addShortCut(miQuit, P2RadioShortCuts.SHORTCUT_QUIT_PROGRAM);

        final MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(ProgData.getInstance()).showDialog());

        final MenuItem miLog = new MenuItem("Logdatei öffnen");
        miLog.setOnAction(event -> {
            P2Logger.openLogFile();
        });

        final MenuItem miUrlHelp = new MenuItem("Anleitung im Web");
        miUrlHelp.setOnAction(event -> {
            P2Open.openURL(ProgConst.URL_WEBSITE_HELP,
                    ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());
        });

        final MenuItem miReset = new MenuItem("Einstellungen zurücksetzen");
        miReset.setOnAction(event -> new ResetDialogController(progData));

        final MenuItem miSearchUpdate = new MenuItem("Gibt’s ein Update?");
        miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));

        final Menu mHelp = new Menu("Hilfe");
        mHelp.getItems().addAll(miUrlHelp, miLog, miReset, miSearchUpdate, new SeparatorMenuItem(), miAbout);

        setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        getStyleClass().addAll("btnFunction", "btnFunc-1");
        setGraphic(ProgIconsP2Radio.ICON_TOOLBAR_MENU_TOP.getImageView());
        getItems().addAll(miConfig, miLoadStationList, miDarkMode, mHelp,
                new SeparatorMenuItem(), miQuit);
    }
}
