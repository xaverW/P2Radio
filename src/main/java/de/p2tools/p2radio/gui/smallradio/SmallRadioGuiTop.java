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
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.filter.FilterFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class SmallRadioGuiTop extends HBox {

    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final Button btnClearFilter = new Button("");
    private final HBox hBoxSearch = new HBox(P2LibConst.DIST_BUTTON);

    private final Button btnClose = new Button();
    private final Button btnRadio = new Button();
    private final ProgData progData;
    private final SmallRadioGuiController smallRadioGuiController;

    public SmallRadioGuiTop(SmallRadioGuiController smallRadioGuiController) {
        this.progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        make();
        initCbo();
    }

    private void make() {
        setSpacing(P2LibConst.DIST_BUTTON);
        setPadding(new Insets(10, 10, 0, 10));
        setAlignment(Pos.CENTER);
        hBoxSearch.setAlignment(Pos.CENTER);
        getChildren().addAll(btnRadio, P2GuiTools.getHBoxGrower(), hBoxSearch, P2GuiTools.getHBoxGrower(), btnClose);

        btnClose.setTooltip(new Tooltip("Programm beenden"));
        btnClose.setOnAction(e -> {
            ProgQuitFactory.quit();
        });
        btnClose.setMaxWidth(Double.MAX_VALUE);
        btnClose.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnClose.setGraphic(ProgIcons.ICON_BUTTON_STOP.getImageView());

        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> smallRadioGuiController.close());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnRadio.setGraphic(ProgIcons.ICON_TOOLBAR_SMALL_RADIO_20.getImageView());

        btnClearFilter.setTooltip(new Tooltip("Filter löschen"));
        btnClearFilter.getStyleClass().addAll("btnFunction", "btnFunc-2");
        btnClearFilter.setGraphic(ProgIcons.ICON_BUTTON_CLEAR_FILTER.getImageView());
        btnClearFilter.setOnAction(event -> {
            if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
                cboGenre.getSelectionModel().select(0);
            } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
                cboGenre.getSelectionModel().select(0);
                cboCollections.getSelectionModel().select(0);
            } else {
                cboGenre.getSelectionModel().select(0);
            }
        });
    }

    public P2MaskerPane getMaskerPane() {
        return smallRadioGuiController.getMaskerPane();
    }

    public void isShown() {
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
                if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
                    ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.setValue(newValue);
                } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
                    ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.setValue(newValue);
                } else {
                    ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.setValue(newValue);
                }
            }
        });
        cboGenre.setItems(progData.filterWorker.getAllGenreList());
        ProgConfig.SMALL_RADIO_SELECTED_LIST.addListener((u, o, n) -> setHbCollection());
        setHbCollection();
    }

    private void setHbCollection() {
        hBoxSearch.getChildren().clear();

        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
            hBoxSearch.getChildren().addAll(new Label("Genre:"), cboGenre, btnClearFilter);
            cboGenre.setValue(ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.getValueSafe());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
            hBoxSearch.getChildren().addAll(new Label("Genre:"), cboGenre,
                    new Label("Sammlung:"), cboCollections, btnClearFilter);

            cboGenre.setValue(ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.getValueSafe());
            CollectionData collectionData = progData.collectionList.getByName(ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.getValueSafe());
            cboCollections.getSelectionModel().select(collectionData);

        } else {
            hBoxSearch.getChildren().addAll(new Label("Genre:"), cboGenre, btnClearFilter);
            cboGenre.setValue(ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.getValueSafe());
        }
    }
}
