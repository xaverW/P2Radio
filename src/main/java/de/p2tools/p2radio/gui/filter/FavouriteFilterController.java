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

package de.p2tools.p2radio.gui.filter;

import de.p2tools.p2Lib.guiTools.PButtonClearFilter;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.filter.FavouriteFilter;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.FavouriteGuiPack;
import de.p2tools.p2radio.tools.storedFilter.FilterCheckRegEx;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FavouriteFilterController extends FilterController {

    private final VBox vBoxFilter;
    private final ProgData progData;

    private final ComboBox<CollectionData> cboCollections = new ComboBox<>();
    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final PToggleSwitch tglOwn = new PToggleSwitch("eigene Sender");
    private final PToggleSwitch tglGrade = new PToggleSwitch("positiv bewertete Sender");
    private final PButtonClearFilter btnClearFilter = new PButtonClearFilter();

    private final FavouriteFilter favouriteFilter;
    private final FilteredList<StationData> filteredStationData;
    private final FavouriteGuiPack favouriteGuiPack;

    public FavouriteFilterController(FavouriteGuiPack favouriteGuiPack) {
        super(ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON);
        progData = ProgData.getInstance();
        this.favouriteGuiPack = favouriteGuiPack;

        vBoxFilter = getVBoxFilter(true);

        favouriteFilter = progData.favouriteFilter;
        filteredStationData = progData.filteredStationData;

        cboCollections.setMaxWidth(Double.MAX_VALUE);
        cboCollections.setMinWidth(150);
        VBox vBoxColl = new VBox();
        vBoxColl.getChildren().addAll(new Label("Sammlung"), cboCollections);
        VBox.setVgrow(cboCollections, Priority.ALWAYS);

        cboGenre.setMaxWidth(Double.MAX_VALUE);
        cboGenre.setMinWidth(150);
        VBox vBoxGenre = new VBox();
        vBoxGenre.getChildren().addAll(new Label("Genre"), cboGenre);
        VBox.setVgrow(cboGenre, Priority.ALWAYS);

        vBoxFilter.getChildren().addAll(vBoxColl, vBoxGenre,
                new Label("    "), tglGrade, tglOwn);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(10, 0, 0, 0));
        hBox.getChildren().add(btnClearFilter);
        vBoxFilter.getChildren().addAll(hBox);

        initFilter();
    }

    private void initFilter() {
        cboCollections.setItems(progData.collectionList);
        cboCollections.getSelectionModel().select(favouriteFilter.getCollectionData());
        filteredStationData.setPredicate(favouriteFilter.getPredicate());

        cboCollections.getSelectionModel().selectedItemProperty().addListener((u, o, n) -> {
            if (n == null) {
                return;
            }
            favouriteFilter.setCollectionData(n);
            filteredStationData.setPredicate(favouriteFilter.getPredicate());
        });

        FilterCheckRegEx fN = new FilterCheckRegEx(cboGenre.getEditor());
        cboGenre.editableProperty().set(true);
        cboGenre.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cboGenre.setVisibleRowCount(25);
        cboGenre.valueProperty().bindBidirectional(favouriteFilter.genreFilterProperty());
        cboGenre.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                fN.checkPattern();
                filteredStationData.setPredicate(favouriteFilter.getPredicate());
            }
        });
        cboGenre.setItems(progData.filterWorker.getAllGenreList());

        tglOwn.setTooltip(new Tooltip("Nur eigene Sender anzeigen"));
        tglOwn.selectedProperty().bindBidirectional(favouriteFilter.ownFilterProperty());
        tglOwn.selectedProperty().addListener((u, o, n) -> {
            filteredStationData.setPredicate(favouriteFilter.getPredicate());
        });

        tglGrade.setTooltip(new Tooltip("Nur positiv bewertete Sender anzeigen"));
        tglGrade.selectedProperty().bindBidirectional(favouriteFilter.gradeFilterProperty());
        tglGrade.selectedProperty().addListener((u, o, n) -> {
            filteredStationData.setPredicate(favouriteFilter.getPredicate());
        });

        btnClearFilter.setOnAction(event -> {
            favouriteFilter.clearFilter();
            filteredStationData.setPredicate(favouriteFilter.getPredicate());
            cboCollections.getSelectionModel().select(favouriteFilter.getCollectionData());
        });
    }
}
