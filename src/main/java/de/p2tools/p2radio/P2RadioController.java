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

package de.p2tools.p2radio;

import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2Lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.ProgSaveFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.P2RadioShortCuts;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.FavouriteGuiPack;
import de.p2tools.p2radio.gui.StationGuiPack;
import de.p2tools.p2radio.gui.StatusBarController;
import de.p2tools.p2radio.gui.configDialog.ConfigDialogController;
import de.p2tools.p2radio.gui.dialog.AboutDialogController;
import de.p2tools.p2radio.gui.dialog.ResetDialogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class P2RadioController extends StackPane {

    Button btnStation = new Button("Sender");
    Button btnFavourite = new Button("Favoriten");

    MenuButton menuButton = new MenuButton("");
    MenuButton menuButton2 = new MenuButton("");

    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();

    private PMaskerPane maskerPane = new PMaskerPane();
    private StatusBarController statusBarController;

    private SplitPane splitPaneStation;
    private SplitPane splitPaneFavourite;

    private final ProgData progData;

    StationGuiPack stationGuiPack = new StationGuiPack();
    FavouriteGuiPack favouriteGuiPack = new FavouriteGuiPack();

    public P2RadioController() {
        progData = ProgData.getInstance();
        init();
    }

    private void init() {
        try {
            // Toolbar
            HBox hBoxTop = new HBox();
            hBoxTop.setPadding(new Insets(10));
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);

            TilePane tilePaneStationFavourite = new TilePane();
            tilePaneStationFavourite.setHgap(20);
            tilePaneStationFavourite.setAlignment(Pos.CENTER);
            tilePaneStationFavourite.getChildren().addAll(btnStation, btnFavourite);
            HBox.setHgrow(tilePaneStationFavourite, Priority.ALWAYS);
            hBoxTop.getChildren().addAll(menuButton2, /*btnLoadStation,*/ tilePaneStationFavourite, menuButton);

            // Center
            splitPaneStation = stationGuiPack.pack();
            splitPaneFavourite = favouriteGuiPack.pack();
            stackPaneCont.getChildren().addAll(splitPaneStation, splitPaneFavourite);

            // Statusbar
            statusBarController = new StatusBarController(progData);

            // Gui zusammenbauen
            borderPane.setTop(hBoxTop);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);
            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane, maskerPane);

            initMaskerPane();
            initButton();
            selPanelStation();
        } catch (Exception ex) {
            PLog.errorLog(597841023, ex);
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        progData.maskerPane = maskerPane;
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();
        Button btnStop = maskerPane.getButton();
        maskerPane.setButtonText("");
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP);
        btnStop.setOnAction(a -> progData.loadNewStationList.setStop(true));

    }

    private void initButton() {
        btnStation.setTooltip(new Tooltip("Sender anzeigen"));
        btnStation.setOnAction(e -> selPanelStation());
        btnStation.setMaxWidth(Double.MAX_VALUE);

        btnFavourite.setTooltip(new Tooltip("Favoriten anzeigen"));
        btnFavourite.setOnAction(e -> selPanelFavourite());
        btnFavourite.setMaxWidth(Double.MAX_VALUE);

        infoPane();

        // Menü
        final MenuItem miConfig = new MenuItem("Einstellungen des Programms");
        miConfig.setOnAction(e -> new ConfigDialogController());

        final MenuItem miLoadStationList = new MenuItem("neue Senderliste laden");
        miLoadStationList.setOnAction(e -> progData.loadNewStationList.loadNewStationFromServer());

        final MenuItem miQuit = new MenuItem("Beenden");
        miQuit.setOnAction(e -> ProgQuitFactory.quit(true));
        PShortcutWorker.addShortCut(miQuit, P2RadioShortCuts.SHORTCUT_QUIT_PROGRAM);

        final MenuItem miAbout = new MenuItem("über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(progData));

        final MenuItem miLog = new MenuItem("Logdatei öffnen");
        miLog.setOnAction(event -> {
            PLogger.openLogFile();
        });

        final MenuItem miUrlHelp = new MenuItem("Anleitung im Web");
        miUrlHelp.setOnAction(event -> {
            POpen.openURL(ProgConst.ADRESSE_WEBSITE_HELP,
                    ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
        });

        final MenuItem miReset = new MenuItem("Einstellungen zurücksetzen");
        miReset.setOnAction(event -> new ResetDialogController(progData));

        final Menu mHelp = new Menu("Hilfe");
        mHelp.getItems().addAll(miUrlHelp, miLog, miReset, new SeparatorMenuItem(), miAbout);

        // ProgInfoDialog
        if (ProgData.debug) {
            final MenuItem miDebug = new MenuItem("Debugtools");
            miDebug.setOnAction(event -> {
                MTPTester mtpTester = new MTPTester(progData);
                mtpTester.showDialog();
            });
            final MenuItem miSave = new MenuItem("alles Speichern");
            miSave.setOnAction(a -> ProgSaveFactory.saveAll());

            mHelp.getItems().addAll(new SeparatorMenuItem(), miDebug, miSave);
        }

        menuButton.setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        menuButton.getStyleClass().add("btnFunctionWide");
        menuButton.setGraphic(new ProgIcons().FX_ICON_TOOLBAR_MENU_TOP);
        menuButton.getItems().addAll(miConfig, miLoadStationList, mHelp,
                new SeparatorMenuItem(), miQuit);

        menuButton2.getStyleClass().add("btnFunctionWide");
        menuButton2.setGraphic(new ProgIcons().FX_ICON_TOOLBAR_MENU_TOP);
        menuButton2.setVisible(false);
    }

    private void selPanelStation() {
        if (maskerPane.isVisible()) {
            return;
        }

        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneStation)) {
            // dann ist der 2. Klick
            stationGuiPack.closeSplit();
            return;
        }

        setButtonStyle(btnStation);

        splitPaneStation.toFront();
        progData.stationGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.STATION);
    }

    private void selPanelFavourite() {
        if (maskerPane.isVisible()) {
            return;
        }

        setButtonStyle(btnFavourite);
        splitPaneFavourite.toFront();
        progData.favouriteGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.FAVOURITE);
    }

    private void infoPane() {
        btnStation.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneStation)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.STATION_GUI_DIVIDER_ON.setValue(!ProgConfig.STATION_GUI_DIVIDER_ON.get());
            }
        });
        btnFavourite.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneFavourite)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.FAVOURITE_GUI_DIVIDER_ON.setValue(!ProgConfig.FAVOURITE_GUI_DIVIDER_ON.get());
            }
        });
    }

    private void setButtonStyle(Button btnSel) {
        btnStation.getStyleClass().clear();
        btnFavourite.getStyleClass().clear();

        if (btnSel.equals(btnStation)) {
            btnStation.getStyleClass().add("btnTab-sel");
        } else {
            btnStation.getStyleClass().add("btnTab");
        }
        if (btnSel.equals(btnFavourite)) {
            btnFavourite.getStyleClass().add("btnTab-sel");
        } else {
            btnFavourite.getStyleClass().add("btnTab");
        }
    }
}
