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

import de.p2tools.p2Lib.guiTools.PGuiSize;
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
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import de.p2tools.p2radio.controller.data.P2RadioShortCuts;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.FavouriteGuiPack;
import de.p2tools.p2radio.gui.LastPlayedGuiPack;
import de.p2tools.p2radio.gui.StationGuiPack;
import de.p2tools.p2radio.gui.StatusBarController;
import de.p2tools.p2radio.gui.configDialog.ConfigDialogController;
import de.p2tools.p2radio.gui.dialog.AboutDialogController;
import de.p2tools.p2radio.gui.dialog.ResetDialogController;
import de.p2tools.p2radio.gui.smallRadio.SmallRadioGuiPack;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class P2RadioController extends StackPane {

    Button btnSmallRadio = new Button("");
    Button btnStation = new Button("Sender");
    Button btnFavourite = new Button("Favoriten");
    Button btnLastPlayed = new Button("History");

    MenuButton menuButton = new MenuButton("");
//    MenuButton menuButton2 = new MenuButton("");

    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();

    private PMaskerPane maskerPane = new PMaskerPane();
    private StatusBarController statusBarController;

    private SplitPane splitPaneStation;
    private SplitPane splitPaneFavourite;
    private SplitPane splitPaneLastPlayed;

    private final ProgData progData;

    StationGuiPack stationGuiPack = new StationGuiPack();
    FavouriteGuiPack favouriteGuiPack = new FavouriteGuiPack();
    LastPlayedGuiPack lastPlayedGuiPack = new LastPlayedGuiPack();

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
//            HBox hBox = new HBox();
//            hBox.setAlignment(Pos.CENTER_LEFT);
//            hBox.getChildren().add(btnSmallRadio);
//            HBox.setHgrow(hBox, Priority.ALWAYS);
            tilePaneStationFavourite.getChildren().addAll(btnStation, btnFavourite, btnLastPlayed);
            HBox.setHgrow(tilePaneStationFavourite, Priority.ALWAYS);
            hBoxTop.getChildren().addAll(btnSmallRadio, /*btnLoadStation,*/ tilePaneStationFavourite, menuButton);

            // Center
            splitPaneStation = stationGuiPack.pack();
            splitPaneFavourite = favouriteGuiPack.pack();
            splitPaneLastPlayed = lastPlayedGuiPack.pack();
            stackPaneCont.getChildren().addAll(splitPaneStation, splitPaneFavourite, splitPaneLastPlayed);

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
            switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                case 0:
                default:
                    initPanelStation();
                    progData.stationGuiController.selUrl();
                    break;
                case 1:
                    initPanelFavourite();
                    progData.favouriteGuiController.selUrl();
                    break;
                case 2:
                    initPanelLastPlayed();
                    progData.lastPlayedGuiController.selUrl();
            }
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
        btnSmallRadio.setTooltip(new Tooltip("kleine Senderübersicht anzeigen"));
        btnSmallRadio.setOnAction(e -> selPanelSmallRadio());
        btnSmallRadio.setMaxWidth(Double.MAX_VALUE);
        btnSmallRadio.getStyleClass().add("btnTab");
        btnSmallRadio.setGraphic(new ProgIcons().ICON_TOOLBAR_SMALL_RADIO);

        btnStation.setTooltip(new Tooltip("Sender anzeigen"));
        btnStation.setOnAction(e -> selPanelStation());
        btnStation.setMaxWidth(Double.MAX_VALUE);

        btnFavourite.setTooltip(new Tooltip("Favoriten anzeigen"));
        btnFavourite.setOnAction(e -> selPanelFavourite());
        btnFavourite.setMaxWidth(Double.MAX_VALUE);

        btnLastPlayed.setTooltip(new Tooltip("zuletzt gespielte Sender anzeigen"));
        btnLastPlayed.setOnAction(e -> selPanelLastPlayed());
        btnLastPlayed.setMaxWidth(Double.MAX_VALUE);

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
            final MenuItem miDebug = new MenuItem("Debug: Debugtools");
            miDebug.setOnAction(event -> {
                MTPTester mtpTester = new MTPTester(progData);
                mtpTester.showDialog();
            });
            final MenuItem miSave = new MenuItem("Debug: Alles Speichern");
            miSave.setOnAction(a -> ProgSaveFactory.saveAll());

            mHelp.getItems().addAll(new SeparatorMenuItem(), miDebug, miSave);
        }

        menuButton.setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        menuButton.getStyleClass().add("btnFunctionWide");
        menuButton.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU_TOP);
        menuButton.getItems().addAll(miConfig, miLoadStationList, mHelp,
                new SeparatorMenuItem(), miQuit);

//        menuButton2.getStyleClass().add("btnFunctionWide");
//        menuButton2.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU_TOP);
//        menuButton2.setVisible(false);

        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
            @Override
            public void finished(EventLoadRadioList event) {
                if (stackPaneCont.getChildren().size() == 0) {
                    return;
                }

                Node node = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
                if (node != null && node == splitPaneStation) {
                    progData.stationGuiController.isShown();
                }
                if (node != null && node == splitPaneFavourite) {
                    progData.favouriteGuiController.isShown();
                }
                if (node != null && node == splitPaneLastPlayed) {
                    progData.lastPlayedGuiController.isShown();
                }
            }
        });
    }

    private void selPanelSmallRadio() {
        if (maskerPane.isVisible()) {
            return;
        }

        PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, ProgData.getInstance().primaryStage);
        progData.primaryStage.close();
        new SmallRadioGuiPack();
    }
    
    private void selPanelStation() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(0);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneStation)) {
            // dann ist der 2. Klick
            stationGuiPack.closeSplit();
            return;
        }
        initPanelStation();
    }

    private void initPanelStation() {
        setButtonStyle(btnStation);
        splitPaneStation.toFront();
        progData.stationGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.STATION);
    }

    private void selPanelFavourite() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(1);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneFavourite)) {
            // dann ist der 2. Klick
            favouriteGuiPack.closeSplit();
            return;
        }
        initPanelFavourite();
    }

    private void initPanelFavourite() {
        setButtonStyle(btnFavourite);
        splitPaneFavourite.toFront();
        progData.favouriteGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.FAVOURITE);
    }

    private void selPanelLastPlayed() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(2);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneLastPlayed)) {
            // dann ist der 2. Klick
            lastPlayedGuiPack.closeSplit();
            return;
        }
        initPanelLastPlayed();
    }

    private void initPanelLastPlayed() {
        setButtonStyle(btnLastPlayed);
        splitPaneLastPlayed.toFront();
        progData.lastPlayedGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.LAST_PLAYED);
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
        btnLastPlayed.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(splitPaneLastPlayed)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.LAST_PLAYED_GUI_DIVIDER_ON.setValue(!ProgConfig.LAST_PLAYED_GUI_DIVIDER_ON.get());
            }
        });
    }

    private void setButtonStyle(Button btnSel) {
        btnStation.getStyleClass().clear();
        btnFavourite.getStyleClass().clear();
        btnLastPlayed.getStyleClass().clear();

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
        if (btnSel.equals(btnLastPlayed)) {
            btnLastPlayed.getStyleClass().add("btnTab-sel");
        } else {
            btnLastPlayed.getStyleClass().add("btnTab");
        }
    }
}
