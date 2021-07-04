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

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFilter;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SmallRadioBottom {

    ProgData progData;
    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private Button btnOk;

    private final SmallRadioGuiPack smallRadioGuiPack;
    private final SmallRadioGuiController smallRadioGuiController;
    private FavouriteFilter favouriteFilter = new FavouriteFilter();


    public SmallRadioBottom(SmallRadioGuiPack smallRadioGuiPack, SmallRadioGuiController smallRadioGuiController) {
        progData = ProgData.getInstance();
        this.smallRadioGuiPack = smallRadioGuiPack;
        this.smallRadioGuiController = smallRadioGuiController;
        initBottom();
        initStartButton();
    }

    private void initBottom() {
        btnOk = new Button("_Ok");
        btnOk.getStyleClass().add("btnSmallRadio");
        btnOk.setDisable(false);
        btnOk.setOnAction(a -> {
            smallRadioGuiPack.close();
        });

        cboCollections.setMaxWidth(Double.MAX_VALUE);
        cboCollections.setMinWidth(150);
        cboCollections.setItems(progData.collectionList);
        cboCollections.valueProperty().bindBidirectional(favouriteFilter.collectionNameFilterProperty());
        cboCollections.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            progData.filteredFavourites.setPredicate(favouriteFilter.getPredicate());
        });


        HBox hBoxSpace1 = new HBox();
        HBox.setHgrow(hBoxSpace1, Priority.ALWAYS);
        HBox hBoxSpace2 = new HBox();
        HBox.setHgrow(hBoxSpace2, Priority.ALWAYS);


        HBox hBoxButton = new HBox(5);
        hBoxButton.getChildren().addAll(btnPrev, btnNext, btnStart, btnStop);

        smallRadioGuiPack.getHBoxBottom().getChildren().addAll(new Label("Sammlung"), cboCollections,
                hBoxSpace1, hBoxButton, hBoxSpace2, btnOk);
    }

    private void initStartButton() {
        btnPrev.setTooltip(new Tooltip("weniger Informationen zum Sender anzeigen"));
        btnPrev.getStyleClass().add("btnSmallRadio");
        btnPrev.setGraphic(new ProgIcons().ICON_BUTTON_PREV);
        btnPrev.setOnAction(event -> {
            smallRadioGuiController.setPreviousStation();
        });

        btnNext.setTooltip(new Tooltip("weniger Informationen zum Sender anzeigen"));
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
