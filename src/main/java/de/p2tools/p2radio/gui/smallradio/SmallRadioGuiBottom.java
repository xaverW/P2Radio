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

package de.p2tools.p2radio.gui.smallradio;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SmallRadioGuiBottom extends HBox {

    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final Button btnClearFilter = new Button("");
    private final Button btnRandom = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final RadioButton rbSender = new RadioButton("Sender");
    private final RadioButton rbFavourite = new RadioButton("Favoriten");
    private final RadioButton rbHistory = new RadioButton("History");
    private final SmallRadioGuiController smallRadioGuiController;
    private final HBox hBoxSearch = new HBox(P2LibConst.DIST_BUTTON);
    private final ProgData progData;

    public SmallRadioGuiBottom(SmallRadioGuiController smallRadioGuiController) {
        progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        initBottom();
        initStartButton();
    }

    private void initBottom() {
        setPadding(new Insets(0, 10, 10, 10));

        //Collection
        ToggleGroup tg = new ToggleGroup();
        rbSender.setToggleGroup(tg);
        rbFavourite.setToggleGroup(tg);
        rbHistory.setToggleGroup(tg);
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_STATION)) {
            rbSender.setSelected(true);
        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_FAVOURITE)) {
            rbFavourite.setSelected(true);
        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_HISTORY)) {
            rbHistory.setSelected(true);
        }
        rbSender.setOnAction(a -> {
            setHbCollection();
            setList();
        });
        rbFavourite.setOnAction(a -> {
            setHbCollection();
            setList();
        });
        rbHistory.setOnAction(a -> {
            setHbCollection();
            setList();
        });

        HBox hBoxRb = new HBox(P2LibConst.DIST_BUTTON);
        hBoxRb.setAlignment(Pos.CENTER);
        hBoxRb.getChildren().addAll(rbSender, rbFavourite, rbHistory);
        VBox vbColl = new VBox(P2LibConst.DIST_BUTTON);

        initCbo();
        hBoxSearch.setAlignment(Pos.CENTER);

        vbColl.getChildren().addAll(hBoxRb, hBoxSearch);

        HBox hBoxButton = new HBox(P2LibConst.DIST_BUTTON);
        hBoxButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxButton.getChildren().addAll(btnStart, btnStop, P2GuiTools.getVDistance(P2LibConst.DIST_BUTTON_BLOCK), btnRandom);

        setAlignment(Pos.BOTTOM_CENTER);
        getChildren().addAll(P2GuiTools.getHBoxGrower(), vbColl, P2GuiTools.getHBoxGrower(), hBoxButton);
    }

    private void initCbo() {
        cboCollections.setMaxWidth(Double.MAX_VALUE);
        cboCollections.setMinWidth(100);
        cboCollections.setItems(progData.collectionList);
        cboCollections.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.setValue(newValue.getName());
        });

        cboGenre.setMaxWidth(Double.MAX_VALUE);
        cboGenre.setMinWidth(100);
        cboGenre.setVisibleRowCount(25);
        cboGenre.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                if (rbSender.isSelected()) {
                    ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.setValue(newValue);
                } else if (rbFavourite.isSelected()) {
                    ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.setValue(newValue);
                } else {
                    ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.setValue(newValue);
                }
            }
        });
        cboGenre.setItems(progData.filterWorker.getAllGenreList());
        setHbCollection();
    }

    private void setHbCollection() {
        hBoxSearch.getChildren().clear();
        if (rbSender.isSelected()) {
            hBoxSearch.getChildren().addAll(new Label("Genre:"), cboGenre, btnClearFilter);

        } else if (rbFavourite.isSelected()) {
            hBoxSearch.getChildren().addAll(new Label("Genre:"), cboGenre,
                    new Label("Sammlung:"), cboCollections, btnClearFilter);
        } else {
            hBoxSearch.getChildren().addAll(new Label("Genre:"), cboGenre, btnClearFilter);
        }

        if (rbSender.isSelected()) {
            cboGenre.setValue(ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.getValueSafe());

        } else if (rbFavourite.isSelected()) {
            cboGenre.setValue(ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.getValueSafe());
            CollectionData collectionData = progData.collectionList.getByName(ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.getValueSafe());
            cboCollections.getSelectionModel().select(collectionData);

        } else {
            cboGenre.setValue(ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.getValueSafe());
        }
    }

    private void setList() {
        if (rbSender.isSelected()) {
            ProgConfig.SMALL_RADIO_SELECTED_LIST.setValue(SmallRadioFactory.LIST_STATION);
        } else if (rbFavourite.isSelected()) {
            ProgConfig.SMALL_RADIO_SELECTED_LIST.setValue(SmallRadioFactory.LIST_FAVOURITE);
        } else {
            ProgConfig.SMALL_RADIO_SELECTED_LIST.setValue(SmallRadioFactory.LIST_HISTORY);
        }
    }

    private void initStartButton() {
        btnClearFilter.setTooltip(new Tooltip("Auswahl löschen"));
        btnClearFilter.getStyleClass().add("btnSmallGui");
        btnClearFilter.setGraphic(ProgIcons.ICON_BUTTON_RESET.getImageView());
        btnClearFilter.setOnAction(event -> {
            if (rbSender.isSelected()) {
                cboGenre.getSelectionModel().select(0);
            } else if (rbFavourite.isSelected()) {
                cboGenre.getSelectionModel().select(0);
                cboCollections.getSelectionModel().select(0);
            } else {
                cboGenre.getSelectionModel().select(0);
            }
        });

        btnRandom.setTooltip(new Tooltip("Einen Sender per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallGui");
        btnRandom.setGraphic(ProgIcons.ICON_BUTTON_RANDOM.getImageView());
        btnRandom.setOnAction(event -> {
            smallRadioGuiController.playRandomStation();
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.getStyleClass().add("btnSmallGui");
        btnStart.setGraphic(ProgIcons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> {
            smallRadioGuiController.playStation();
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.getStyleClass().add("btnSmallGui");
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStop.setOnAction(event -> StartFactory.stopRunningStation());
    }
}
