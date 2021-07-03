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

import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFilter;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class SmallRadioGuiPack extends SmallRadioDialog {

    ProgData progData;
    private final HBox hBox = new HBox();
    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final SmallRadioGuiController smallRadioGuiController;
    private Button btnOk;
    private FavouriteFilter favouriteFilter = new FavouriteFilter();


    public SmallRadioGuiPack() {
        super(null, ProgConfig.SMALL_RADIO_SIZE, "Radiobrowser");

        progData = ProgData.getInstance();
        smallRadioGuiController = new SmallRadioGuiController();
        init();
    }

    @Override
    public void make() {
        initBottom();
        pack();
    }

    public void close() {
        smallRadioGuiController.saveTable();
        Platform.runLater(() -> {
                    progData.primaryStage.show();
                    progData.primaryStage.getScene().getWindow().setWidth(PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.getScene().getWindow().setHeight(PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
                }
        );
        super.close();
    }

    private void initBottom() {
        btnOk = new Button("_Ok");
        btnOk.setDisable(false);
        btnOk.setOnAction(a -> {
            close();
        });

        cboCollections.setMaxWidth(Double.MAX_VALUE);
        cboCollections.setMinWidth(150);
        cboCollections.setItems(progData.collectionList);
        cboCollections.valueProperty().bindBidirectional(favouriteFilter.collectionNameFilterProperty());
        cboCollections.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            progData.filteredFavourites.setPredicate(favouriteFilter.getPredicate());
        });


        HBox hBoxSpace = new HBox();
        HBox.setHgrow(hBoxSpace, Priority.ALWAYS);

        btnOk.getStyleClass().add("btnStartDialog");

        gethBoxBottom().getChildren().addAll(new Label("Sammlung"), cboCollections, hBoxSpace, btnOk);
    }

    private void pack() {
        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(smallRadioGuiController, Priority.ALWAYS);
        hBox.getChildren().addAll(smallRadioGuiController);

        getvBoxCenter().getChildren().add(hBox);
    }
}
