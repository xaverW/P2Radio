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

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.events.Event;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2Lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.*;
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
import de.p2tools.p2radio.tools.update.SearchProgramUpdate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class P2RadioController extends StackPane {

    private final ProgData progData;
    private final PMaskerPane maskerPane = new PMaskerPane();
    Button btnSmallRadio = new Button("");
    Button btnStation = new Button("Sender");
    Button btnFavourite = new Button("Favoriten");
    Button btnLastPlayed = new Button("History");
    MenuButton menuButton = new MenuButton("");
    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();
    StationGuiPack stationGuiPack = new StationGuiPack();
    FavouriteGuiPack favouriteGuiPack = new FavouriteGuiPack();
    LastPlayedGuiPack lastPlayedGuiPack = new LastPlayedGuiPack();
    private StatusBarController statusBarController;
    private Pane paneStation;
    private Pane paneFavourite;
    private Pane paneLastPlayed;

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
            tilePaneStationFavourite.getChildren().addAll(btnStation, btnFavourite, btnLastPlayed);
            HBox.setHgrow(tilePaneStationFavourite, Priority.ALWAYS);
            hBoxTop.getChildren().addAll(btnSmallRadio, tilePaneStationFavourite, menuButton);

            // Center
            paneStation = stationGuiPack.pack();
            paneFavourite = favouriteGuiPack.pack();
            paneLastPlayed = lastPlayedGuiPack.pack();
            stackPaneCont.getChildren().addAll(paneStation, paneFavourite, paneLastPlayed);

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
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction(a -> progData.loadNewStationList.setStop(true));
    }

    private void initButton() {
        btnSmallRadio.setTooltip(new Tooltip("kleine Senderübersicht anzeigen"));
        btnSmallRadio.setOnAction(e -> selPanelSmallRadio());
        btnSmallRadio.setMaxWidth(Double.MAX_VALUE);
        btnSmallRadio.getStyleClass().add("btnTab");
        btnSmallRadio.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_SMALL_RADIO_24.getImageView());

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
        miConfig.setOnAction(e -> ConfigDialogController.getInstanceAndShow());

        final MenuItem miLoadStationList = new MenuItem("Neue Senderliste laden");
        miLoadStationList.setOnAction(e -> progData.loadNewStationList.loadNewStationFromServer());

        final MenuItem miQuit = new MenuItem("Beenden");
        miQuit.setOnAction(e -> ProgQuitFactory.quit(progData.primaryStage, true));
        PShortcutWorker.addShortCut(miQuit, P2RadioShortCuts.SHORTCUT_QUIT_PROGRAM);

        final MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> AboutDialogController.getInstanceAndShow());

        final MenuItem miLog = new MenuItem("Logdatei öffnen");
        miLog.setOnAction(event -> {
            PLogger.openLogFile();
        });

        final MenuItem miUrlHelp = new MenuItem("Anleitung im Web");
        miUrlHelp.setOnAction(event -> {
            POpen.openURL(ProgConst.URL_WEBSITE_HELP,
                    ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        });

        final MenuItem miReset = new MenuItem("Einstellungen zurücksetzen");
        miReset.setOnAction(event -> new ResetDialogController(progData));

        final MenuItem miSearchUpdate = new MenuItem("Gibts ein Update?");
        miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));

        final Menu mHelp = new Menu("Hilfe");
        mHelp.getItems().addAll(miUrlHelp, miLog, miReset, miSearchUpdate, new SeparatorMenuItem(), miAbout);

        menuButton.setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        menuButton.getStyleClass().add("btnFunctionWide");
        menuButton.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_MENU_TOP.getImageView());
        menuButton.getItems().addAll(miConfig, miLoadStationList, mHelp,
                new SeparatorMenuItem(), miQuit);

        progData.pEventHandler.addListener(new PListener(Events.LOAD_RADIO_LIST) {
            public <T extends Event> void pingGui(T event) {
                if (event.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) event;
                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        if (stackPaneCont.getChildren().size() == 0) {
                            return;
                        }

                        Node node = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
                        if (node != null && node == paneStation) {
                            progData.stationGuiController.isShown();
                        }
                        if (node != null && node == paneFavourite) {
                            progData.favouriteGuiController.isShown();
                        }
                        if (node != null && node == paneLastPlayed) {
                            progData.lastPlayedGuiController.isShown();
                        }
                    }
                }
            }
        });

        progData.pEventHandler.addListener(new PListener(Events.LOAD_RADIO_LIST) {
            public <T extends Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) runEvent;

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        if (stackPaneCont.getChildren().size() == 0) {
                            return;
                        }

                        Node node = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
                        if (node != null && node == paneStation) {
                            progData.stationGuiController.isShown();
                        }
                        if (node != null && node == paneFavourite) {
                            progData.favouriteGuiController.isShown();
                        }
                        if (node != null && node == paneLastPlayed) {
                            progData.lastPlayedGuiController.isShown();
                        }
                    }
                }
            }
        });

//        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
//            @Override
//            public void finished(EventLoadRadioList event) {
//                if (stackPaneCont.getChildren().size() == 0) {
//                    return;
//                }
//
//                Node node = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
//                if (node != null && node == paneStation) {
//                    progData.stationGuiController.isShown();
//                }
//                if (node != null && node == paneFavourite) {
//                    progData.favouriteGuiController.isShown();
//                }
//                if (node != null && node == paneLastPlayed) {
//                    progData.lastPlayedGuiController.isShown();
//                }
//            }
//        });
    }

    private void selPanelSmallRadio() {
        if (maskerPane.isVisible()) {
            return;
        }

        if (progData.favouriteList.isEmpty()) {
            PAlert.showErrorAlert("keine Favoriten",
                    "Es sind noch keine Favoriten angelegt. Diese müssen " +
                            "zuerst angelegt werden!");
            return;
        }

//        PGuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, ProgData.getInstance().primaryStage);
        progData.primaryStage.close();
        new SmallRadioGuiPack(progData);
    }

    private void selPanelStation() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(0);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneStation)) {
            // dann ist der 2. Klick
            stationGuiPack.closeSplit();
            return;
        }
        initPanelStation();
    }

    private void initPanelStation() {
        setButtonStyle(btnStation);
        paneStation.toFront();
        progData.stationGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.STATION);
    }

    private void selPanelFavourite() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(1);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneFavourite)) {
            // dann ist der 2. Klick
            favouriteGuiPack.closeSplit();
            return;
        }
        initPanelFavourite();
    }

    private void initPanelFavourite() {
        setButtonStyle(btnFavourite);
        paneFavourite.toFront();
        progData.favouriteGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.FAVOURITE);
    }

    private void selPanelLastPlayed() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(2);
        if (maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneLastPlayed)) {
            // dann ist der 2. Klick
            lastPlayedGuiPack.closeSplit();
            return;
        }
        initPanelLastPlayed();
    }

    private void initPanelLastPlayed() {
        setButtonStyle(btnLastPlayed);
        paneLastPlayed.toFront();
        progData.lastPlayedGuiController.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.LAST_PLAYED);
    }

    private void infoPane() {
        btnStation.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneStation)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.STATION_GUI_DIVIDER_ON.setValue(!ProgConfig.STATION_GUI_DIVIDER_ON.get());
            }
        });
        btnFavourite.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneFavourite)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.FAVOURITE_GUI_DIVIDER_ON.setValue(!ProgConfig.FAVOURITE_GUI_DIVIDER_ON.get());
            }
        });
        btnLastPlayed.setOnMouseClicked(mouseEvent -> {
            if (maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneLastPlayed)) {
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
            btnStation.getStyleClass().add("btnTabEasy-sel");
        } else {
            btnStation.getStyleClass().add("btnTabEasy");
        }
        if (btnSel.equals(btnFavourite)) {
            btnFavourite.getStyleClass().add("btnTabEasy-sel");
        } else {
            btnFavourite.getStyleClass().add("btnTabEasy");
        }
        if (btnSel.equals(btnLastPlayed)) {
            btnLastPlayed.getStyleClass().add("btnTabEasy-sel");
        } else {
            btnLastPlayed.getStyleClass().add("btnTabEasy");
        }
    }
}
