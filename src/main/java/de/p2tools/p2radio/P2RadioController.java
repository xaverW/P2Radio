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

import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.FavouriteGuiPack;
import de.p2tools.p2radio.gui.HistoryGuiPack;
import de.p2tools.p2radio.gui.StationGuiPack;
import de.p2tools.p2radio.gui.StatusBarController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class P2RadioController extends StackPane {

    private final ProgData progData;
    Button btnSmallRadio = new Button("");
    Button btnStation = new Button("Sender");
    Button btnFavourite = new Button("Favoriten");
    Button btnHistory = new Button("History");
    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();
    StationGuiPack stationGuiPack = new StationGuiPack();
    FavouriteGuiPack favouriteGuiPack = new FavouriteGuiPack();
    HistoryGuiPack historyGuiPack = new HistoryGuiPack();
    private StatusBarController statusBarController;
    private Pane paneStation;
    private Pane paneFavourite;
    private Pane paneHistory;

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
            tilePaneStationFavourite.getChildren().addAll(btnStation, btnFavourite, btnHistory);
            HBox.setHgrow(tilePaneStationFavourite, Priority.ALWAYS);
            hBoxTop.getChildren().addAll(btnSmallRadio, tilePaneStationFavourite, new ProgMenu());

            // Center
            paneStation = stationGuiPack.pack();
            paneFavourite = favouriteGuiPack.pack();
            paneHistory = historyGuiPack.pack();
            stackPaneCont.getChildren().addAll(paneStation, paneFavourite, paneHistory);

            // Statusbar
            statusBarController = new StatusBarController(progData);

            // Gui zusammenbauen
            borderPane.setTop(hBoxTop);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);
            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane, progData.maskerPane);

            initMaskerPane();
            initButton();
            initPanel();
        } catch (Exception ex) {
            P2Log.errorLog(597841023, ex);
        }
    }

    public void initPanel() {
        switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
            case 0:
            default:
                initPanelStation();
                break;
            case 1:
                initPanelFavourite();
                break;
            case 2:
                initPanelHistory();
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(progData.maskerPane, Pos.CENTER);
        progData.maskerPane.setPadding(new Insets(4, 1, 1, 1));
        progData.maskerPane.toFront();
        Button btnStop = progData.maskerPane.getButton();
        progData.maskerPane.setButtonText("");
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction(a -> progData.loadNewStationList.setStop(true));
    }

    private void initButton() {
        btnSmallRadio.setTooltip(new Tooltip("Kleine Senderübersicht anzeigen"));
        btnSmallRadio.setOnAction(e -> selPanelSmallRadio());
        btnSmallRadio.setMaxWidth(Double.MAX_VALUE);
        btnSmallRadio.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnSmallRadio.setGraphic(ProgIcons.ICON_TOOLBAR_SMALL_RADIO_24.getImageView());

        btnStation.setTooltip(new Tooltip("Sender anzeigen"));
        btnStation.setOnAction(e -> selPanelStation());
        btnStation.setMaxWidth(Double.MAX_VALUE);

        btnFavourite.setTooltip(new Tooltip("Favoriten anzeigen"));
        btnFavourite.setOnAction(e -> selPanelFavourite());
        btnFavourite.setMaxWidth(Double.MAX_VALUE);

        btnHistory.setTooltip(new Tooltip("Zuletzt gespielte Sender anzeigen"));
        btnHistory.setOnAction(e -> selPanelHistory());
        btnHistory.setMaxWidth(Double.MAX_VALUE);

        infoPane();

        progData.pEventHandler.addListener(new P2Listener(Events.LOAD_RADIO_LIST) {
            public <T extends P2Event> void pingGui(T event) {
                if (event.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) event;
                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        if (stackPaneCont.getChildren().isEmpty()) {
                            return;
                        }

                        Node node = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
                        if (node != null && node == paneStation) {
                            progData.stationGuiPack.getStationGuiController().isShown();
                        }
                        if (node != null && node == paneFavourite) {
                            progData.favouriteGuiPack.getFavouriteGuiController().isShown();
                        }
                        if (node != null && node == paneHistory) {
                            progData.historyGuiPack.getHistoryGuiController().isShown();
                        }
                    }
                }
            }
        });
    }

    public void selPanelSmallRadio() {
        if (progData.maskerPane.isVisible()) {
            return;
        }

        ProgData.STATION_TAB_ON.setValue(Boolean.FALSE);
        ProgData.FAVOURITE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.HISTORY_TAB_ON.setValue(Boolean.FALSE);

        ProgConfig.SYSTEM_SMALL_RADIO.set(true);
    }

    private void selPanelStation() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(0);
        if (progData.maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneStation)) {
            // dann ist der 2. Klick
            if (!ProgConfig.STATION__FILTER_IS_RIP.get()) {
                ProgConfig.STATION__FILTER_IS_SHOWING.setValue(!ProgConfig.STATION__FILTER_IS_SHOWING.get());
            }
            return;
        }
        initPanelStation();
    }

    private void initPanelStation() {
        ProgData.STATION_TAB_ON.setValue(Boolean.TRUE);
        ProgData.FAVOURITE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.HISTORY_TAB_ON.setValue(Boolean.FALSE);
        setButtonStyle(btnStation);
        paneStation.toFront();
        progData.stationGuiPack.getStationGuiController().isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.STATION);
    }

    private void selPanelFavourite() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(1);
        if (progData.maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneFavourite)) {
            // dann ist der 2. Klick
            if (!ProgConfig.FAVOURITE__FILTER_IS_RIP.get()) {
                ProgConfig.FAVOURITE__FILTER_IS_SHOWING.setValue(!ProgConfig.FAVOURITE__FILTER_IS_SHOWING.get());
            }
            return;
        }
        initPanelFavourite();
    }

    private void initPanelFavourite() {
        ProgData.STATION_TAB_ON.setValue(Boolean.FALSE);
        ProgData.FAVOURITE_TAB_ON.setValue(Boolean.TRUE);
        ProgData.HISTORY_TAB_ON.setValue(Boolean.FALSE);
        setButtonStyle(btnFavourite);
        paneFavourite.toFront();
        progData.favouriteGuiPack.getFavouriteGuiController().isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.FAVOURITE);
    }

    private void selPanelHistory() {
        ProgConfig.SYSTEM_LAST_TAB_STATION.set(2);
        if (progData.maskerPane.isVisible()) {
            return;
        }
        if (stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneHistory)) {
            // dann ist der 2. Klick
            if (!ProgConfig.HISTORY__FILTER_IS_RIP.get()) {
                ProgConfig.HISTORY__FILTER_IS_SHOWING.setValue(!ProgConfig.HISTORY__FILTER_IS_SHOWING.get());
            }
            return;
        }
        initPanelHistory();
    }

    private void initPanelHistory() {
        ProgData.STATION_TAB_ON.setValue(Boolean.FALSE);
        ProgData.FAVOURITE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.HISTORY_TAB_ON.setValue(Boolean.TRUE);
        setButtonStyle(btnHistory);
        paneHistory.toFront();
        progData.historyGuiPack.getHistoryGuiController().isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.HISTORY);
    }

    private void infoPane() {
        btnStation.setOnMouseClicked(mouseEvent -> {
            if (progData.maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneStation)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.STATION__INFO_IS_SHOWING.setValue(!ProgConfig.STATION__INFO_IS_SHOWING.get());
            }
        });
        btnFavourite.setOnMouseClicked(mouseEvent -> {
            if (progData.maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneFavourite)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.FAVOURITE__INFO_IS_SHOWING.setValue(!ProgConfig.FAVOURITE__INFO_IS_SHOWING.get());
            }
        });
        btnHistory.setOnMouseClicked(mouseEvent -> {
            if (progData.maskerPane.isVisible() ||
                    !stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1).equals(paneHistory)) {
                return;
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.HISTORY__INFO_IS_SHOWING.setValue(!ProgConfig.HISTORY__INFO_IS_SHOWING.get());
            }
        });
    }

    private void setButtonStyle(Button btnSel) {
        btnStation.getStyleClass().clear();
        btnFavourite.getStyleClass().clear();
        btnHistory.getStyleClass().clear();

        if (btnSel.equals(btnStation)) {
            btnStation.getStyleClass().add("btnTabTop-sel");
        } else {
            btnStation.getStyleClass().add("btnTabTop");
        }
        if (btnSel.equals(btnFavourite)) {
            btnFavourite.getStyleClass().add("btnTabTop-sel");
        } else {
            btnFavourite.getStyleClass().add("btnTabTop");
        }
        if (btnSel.equals(btnHistory)) {
            btnHistory.getStyleClass().add("btnTabTop-sel");
        } else {
            btnHistory.getStyleClass().add("btnTabTop");
        }
    }
}
