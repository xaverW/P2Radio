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
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.PShortCut;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.picon.PIconFactory;
import de.p2tools.p2radio.controller.picon.PIconShow;
import de.p2tools.p2radio.controller.update.SearchProgramUpdate;
import de.p2tools.p2radio.gui.configdialog.ConfigDialogController;
import de.p2tools.p2radio.gui.dialog.AboutDialogController;
import de.p2tools.p2radio.gui.dialog.ResetDialogController;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import org.kordamp.ikonli.javafx.FontIcon;

public class ProgMenu extends MenuButton {
    public ProgMenu() {
        makeMenu();
        setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() > 1) {
                    ProgConfig.SYSTEM_GUI_THEME_1.set(!ProgConfig.SYSTEM_GUI_THEME_1.get());
                }
            }

            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.SYSTEM_DARK_THEME.set(!ProgConfig.SYSTEM_DARK_THEME.get());
            }
        });
    }

    private void makeMenu() {
        ProgData progData = ProgData.getInstance();
        // Menü
        final MenuItem miConfig = new MenuItem("Einstellungen des Programms");
        miConfig.setOnAction(e -> new ConfigDialogController(progData));

        final MenuItem miLoadStationList = new MenuItem("Neue Senderliste laden");
        miLoadStationList.setOnAction(e -> progData.worker.loadFromWeb());

        final CheckMenuItem miDarkMode = new CheckMenuItem("Dunkle Oberfläche");
        miDarkMode.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DARK_THEME);

        final CheckMenuItem miColorMode = new CheckMenuItem("Farb-Modus-1");
        miColorMode.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_GUI_THEME_1);

        final MenuItem miQuit = new MenuItem("Beenden");
        miQuit.setOnAction(e -> ProgQuitFactory.quit());
        P2ShortcutWorker.addShortCut(miQuit, PShortCut.SHORTCUT_QUIT_PROGRAM);

        final MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(ProgData.getInstance()).showDialog());

        final MenuItem miLog = new MenuItem("Logdatei öffnen");
        miLog.setOnAction(event -> {
            P2Logger.openLogFile();
        });

        final MenuItem miUrlHelp = new MenuItem("Anleitung im Web");
        miUrlHelp.setOnAction(event -> {
            P2Open.openURL(ProgConst.URL_WEBSITE_HELP,
                    ProgConfig.SYSTEM_PROG_OPEN_URL, PIconFactory.PICON.BTN_DIR_OPEN.getFontIcon());
        });

        final MenuItem miReset = new MenuItem("Einstellungen zurücksetzen");
        miReset.setOnAction(event -> new ResetDialogController(progData));

        final MenuItem miSearchUpdate = new MenuItem("Gibt’s ein Update?");
        miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData).searchNewProgramVersion(true));

        final Menu mHelp = new Menu("Hilfe");
        mHelp.getItems().addAll(miUrlHelp, miLog, miReset, miSearchUpdate, new SeparatorMenuItem(), miAbout);

        setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        getStyleClass().addAll("pFuncBtn", "btnProgMenu");
        FontIcon node = PIconFactory.PICON.PROG_MENU.getFontIcon();
        node.setScaleX(1.5);
        setGraphic(node);
        getItems().addAll(miConfig, new SeparatorMenuItem(),
                miLoadStationList, miDarkMode, miColorMode, mHelp,
                new SeparatorMenuItem(), miQuit);

        if (ProgData.debug) {
            final MenuItem miSearchAllUpdate = new MenuItem("Alle Programm-Downloads anzeigen");
            miSearchAllUpdate.setOnAction(a -> new SearchProgramUpdate(progData)
                    .searchNewProgramVersion(true, true));

            final MenuItem miIcon = new MenuItem("Icons");
            miIcon.setOnAction(a -> new PIconShow());

            final MenuItem miResetAll = new MenuItem("DATUM und GEMACHT zurücksetzen");
            miResetAll.setOnAction(a -> {
                ProgConfig.SYSTEM_SEARCH_UPDATE_TODAY_DONE.set("2020.01.01"); // heute noch nicht gemacht
                ProgConfig.SYSTEM_SEARCH_UPDATE_LAST_DATE.set("2020.01.01"); // letztes Datum, bis zu dem geprüft wurde, wenn leer wird das buildDate genommen
            });
            final MenuItem miResetTodayDone = new MenuItem("Datum \"heute schon gemacht\" zurücksetzen");
            miResetTodayDone.setOnAction(a -> {
                ProgConfig.SYSTEM_SEARCH_UPDATE_TODAY_DONE.set("2020.01.01"); // heute noch nicht gemacht
            });
            final MenuItem miResetLastSearch = new MenuItem("Datum \"letzte Suche\" zurücksetzen");
            miResetLastSearch.setOnAction(a -> {
                ProgConfig.SYSTEM_SEARCH_UPDATE_LAST_DATE.set("2020.01.01"); // letztes Datum, bis zu dem geprüft wurde, wenn leer wird das buildDate genommen
            });

            mHelp.getItems().addAll(new SeparatorMenuItem(), miSearchAllUpdate, miIcon,
                    miResetAll, miResetTodayDone, miResetLastSearch);
        }
    }
}
