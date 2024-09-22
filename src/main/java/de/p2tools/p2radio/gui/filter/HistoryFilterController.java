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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2ButtonClearFilterFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.filter.HistoryFilter;
import de.p2tools.p2radio.gui.HistoryGuiPack;
import de.p2tools.p2radio.tools.storedfilter.FilterCheckRegEx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HistoryFilterController extends VBox {

    private final ProgData progData;

    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final Button btnClearFilter = P2ButtonClearFilterFactory.getPButtonClear();

    private final HistoryFilter historyFilter;
    private final HistoryGuiPack historyGuiPack;

    public HistoryFilterController(HistoryGuiPack historyGuiPack) {
//        super(ProgConfig.HISTORY_GUI_FILTER_DIVIDER_ON);
        this.historyGuiPack = historyGuiPack;
        this.progData = ProgData.getInstance();
        this.historyFilter = progData.historyFilter;

        setPadding(new Insets(P2LibConst.PADDING));
        setSpacing(P2LibConst.DIST_BUTTON);

        cboGenre.setMaxWidth(Double.MAX_VALUE);
        cboGenre.setMinWidth(150);
        VBox vBoxColl = new VBox();
        vBoxColl.getChildren().addAll(new Label("Genre"), cboGenre);
        VBox.setVgrow(cboGenre, Priority.ALWAYS);

        getChildren().addAll(vBoxColl);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(10, 0, 0, 0));
        hBox.getChildren().add(btnClearFilter);
        getChildren().addAll(hBox);

        initFilter();
    }

    private void initFilter() {
        FilterCheckRegEx fN = new FilterCheckRegEx(cboGenre.getEditor());
        cboGenre.editableProperty().set(true);
        cboGenre.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cboGenre.setVisibleRowCount(25);
        cboGenre.valueProperty().bindBidirectional(historyFilter.genreFilterProperty());
        cboGenre.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                fN.checkPattern();
                historyFilter.setGenreFilter(newValue);
                progData.filteredHistoryList.setPredicate(historyFilter.getPredicate());
            }
        });
        cboGenre.setItems(progData.filterWorker.getAllGenreList());

        btnClearFilter.setOnAction(event -> {
            progData.filteredHistoryList.setPredicate(historyFilter.clearFilter());
        });
    }
}
