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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2Lib.guiTools.PGuiTools;
import de.p2tools.p2Lib.tools.log.PDebugLog;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFilter;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SmallRadioGuiBottom extends HBox {

    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final Button btnClearFilter = new Button("");
    private final Button btnRandom = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final RadioButton rbSender = new RadioButton("Sender");
    private final RadioButton rbFavourite = new RadioButton("Favoriten");
    private final RadioButton rbLastPlayed = new RadioButton("History");
    private final SmallRadioGuiController smallRadioGuiController;
    private final FavouriteFilter favouriteFilter = new FavouriteFilter();
    Button btnRadio = new Button();
    ProgData progData;
    StringProperty selectedCollectionName = ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME;

    public SmallRadioGuiBottom(SmallRadioGuiController smallRadioGuiController) {
        progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        initBottom();
        initStartButton();
    }

    private void initBottom() {
        setPadding(new Insets(5, 10, 5, 10));

        //Collection
        ToggleGroup tg = new ToggleGroup();
        rbSender.setToggleGroup(tg);
        rbFavourite.setToggleGroup(tg);
        rbLastPlayed.setToggleGroup(tg);

        cboCollections.setMaxWidth(Double.MAX_VALUE);
        cboCollections.setMinWidth(150);
        cboCollections.setItems(progData.collectionList);

        CollectionData collectionData = progData.collectionList.getByName(selectedCollectionName.getValueSafe());
        favouriteFilter.setCollectionData(collectionData);
        smallRadioGuiController.getFiltertFavourite().setPredicate(favouriteFilter.getPredicate());

        cboCollections.getSelectionModel().select(collectionData);
        cboCollections.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            if (n == null) {
                return;
            }
            selectedCollectionName.setValue(n.getName());
            favouriteFilter.setCollectionData(n);
            smallRadioGuiController.getFiltertFavourite().setPredicate(favouriteFilter.getPredicate());
            PDebugLog.sysLog(selectedCollectionName.getValueSafe());
        });

        HBox hBoxRb = new HBox(5);
        hBoxRb.getChildren().addAll(rbSender, rbFavourite, rbLastPlayed);
        VBox vbColl = new VBox(5);
        vbColl.getChildren().addAll(hBoxRb, cboCollections);

        HBox hBoxCollect = new HBox(15);
        hBoxCollect.setAlignment(Pos.CENTER);
        hBoxCollect.getChildren().addAll(new Label("Sammlung:"), vbColl, btnClearFilter);


        HBox hBoxButton = new HBox(5);
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        hBoxButton.getChildren().addAll(btnStart, btnStop, PGuiTools.getHDistance(20), btnRandom);

        setAlignment(Pos.CENTER);
        getChildren().addAll(btnRadio, PGuiTools.getHBoxGrower(), hBoxCollect, PGuiTools.getHBoxGrower(), hBoxButton);
    }

    private void initStartButton() {
        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> smallRadioGuiController.changeGui());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().add("btnTab");
        btnRadio.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_SMALL_RADIO_20.getImageView());

        btnClearFilter.setTooltip(new Tooltip("Auswahl löschen"));
        btnClearFilter.getStyleClass().add("btnSmallRadio");
        btnClearFilter.setGraphic(ProgIcons.Icons.ICON_BUTTON_RESET.getImageView());
        btnClearFilter.setOnAction(event -> {
            cboCollections.getSelectionModel().select(0);
        });

        btnRandom.setTooltip(new Tooltip("Einen Sender per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallRadio");
        btnRandom.setGraphic(ProgIcons.Icons.ICON_BUTTON_RANDOM.getImageView());
        btnRandom.setOnAction(event -> {
            smallRadioGuiController.playRandomStation();
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.getStyleClass().add("btnSmallRadio");
        btnStart.setGraphic(ProgIcons.Icons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> {
            smallRadioGuiController.playStation();
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.getStyleClass().add("btnSmallRadio");
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStop.setOnAction(event -> progData.startFactory.stopAll());
    }
}
