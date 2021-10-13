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

import de.p2tools.p2Lib.tools.log.PDebugLog;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFilter;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SmallRadioBottom {

    ProgData progData;
    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final Button btnClear = new Button("");
    private final Button btnRandom = new Button("");
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private Button btnRadio;

    private final SmallRadioGuiPack smallRadioGuiPack;
    private final SmallRadioGuiController smallRadioGuiController;
    private FavouriteFilter favouriteFilter = new FavouriteFilter();
    StringProperty selectedCollectionName = ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME;


    public SmallRadioBottom(SmallRadioGuiPack smallRadioGuiPack, SmallRadioGuiController smallRadioGuiController) {
        progData = ProgData.getInstance();
        this.smallRadioGuiPack = smallRadioGuiPack;
        this.smallRadioGuiController = smallRadioGuiController;
        initBottom();
        initStartButton();
    }

    private void initBottom() {
        btnRadio = new Button("");
        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> smallRadioGuiPack.changeGui());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().add("btnTab");
        btnRadio.setGraphic(new ProgIcons().ICON_TOOLBAR_SMALL_RADIO_20);

        cboCollections.setMaxWidth(Double.MAX_VALUE);
        cboCollections.setMinWidth(150);
        cboCollections.setItems(progData.collectionList);

        CollectionData collectionData = progData.collectionList.getByName(selectedCollectionName.getValueSafe());
        favouriteFilter.setCollectionData(collectionData);
        smallRadioGuiPack.getFiltertFavourite().setPredicate(favouriteFilter.getPredicate());

        cboCollections.getSelectionModel().select(collectionData);
        cboCollections.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            if (n == null) {
                return;
            }
            selectedCollectionName.setValue(n.getName());
            favouriteFilter.setCollectionData(n);
            smallRadioGuiPack.getFiltertFavourite().setPredicate(favouriteFilter.getPredicate());
            PDebugLog.sysLog(selectedCollectionName.getValueSafe());
        });


        HBox hBoxSpace1 = new HBox();
        HBox.setHgrow(hBoxSpace1, Priority.ALWAYS);
        HBox hBoxSpace2 = new HBox();
        HBox.setHgrow(hBoxSpace2, Priority.ALWAYS);


        HBox hBoxCollect = new HBox(5);
        hBoxCollect.setAlignment(Pos.CENTER_LEFT);
        hBoxCollect.getChildren().addAll(new Label("Sammlung:"), cboCollections, btnClear);

        HBox hBoxButton = new HBox(5);
        hBoxButton.setAlignment(Pos.CENTER_RIGHT);
        Separator sp = new Separator(Orientation.VERTICAL);
        sp.setPadding(new Insets(0, 5, 0, 5));
        hBoxButton.getChildren().addAll(btnRandom, sp, btnPrev, btnNext, btnStart, btnStop);

        smallRadioGuiPack.getHBoxBottom().getChildren().addAll(btnRadio,
                hBoxSpace2, hBoxCollect, hBoxSpace1, hBoxButton);
    }

    private void initStartButton() {
        btnClear.setTooltip(new Tooltip("Einen Sender per Zufall starten"));
        btnClear.getStyleClass().add("btnSmallRadio");
        btnClear.setGraphic(new ProgIcons().ICON_BUTTON_RESET);
        btnClear.setOnAction(event -> {
            cboCollections.getSelectionModel().select(0);
        });

        btnRandom.setTooltip(new Tooltip("Einen Sender per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallRadio");
        btnRandom.setGraphic(new ProgIcons().ICON_BUTTON_RANDOM);
        btnRandom.setOnAction(event -> {
            smallRadioGuiController.playRandomStation();
        });

        btnPrev.setTooltip(new Tooltip("vorherigen Sender auswählen"));
        btnPrev.getStyleClass().add("btnSmallRadio");
        btnPrev.setGraphic(new ProgIcons().ICON_BUTTON_PREV);
        btnPrev.setOnAction(event -> {
            smallRadioGuiController.setPreviousStation();
        });

        btnNext.setTooltip(new Tooltip("nächsten Sender auswählen"));
        btnNext.getStyleClass().add("btnSmallRadio");
        btnNext.setGraphic(new ProgIcons().ICON_BUTTON_NEXT);
        btnNext.setOnAction(event -> {
            smallRadioGuiController.setNextStation();
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.getStyleClass().add("btnSmallRadio");
        btnStart.setGraphic(new ProgIcons().ICON_BUTTON_PLAY);
        btnStart.setOnAction(event -> {
            smallRadioGuiController.playStation();
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.getStyleClass().add("btnSmallRadio");
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP_PLAY);
        btnStop.setOnAction(event -> progData.startFactory.stopAll());
    }
}
