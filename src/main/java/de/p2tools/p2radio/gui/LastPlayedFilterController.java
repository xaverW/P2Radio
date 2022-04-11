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

package de.p2tools.p2radio.gui;

import de.p2tools.p2Lib.guiTools.pClosePane.PClosePaneV;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFilter;
import de.p2tools.p2radio.tools.storedFilter.FilterCheckRegEx;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LastPlayedFilterController extends PClosePaneV {

    private final VBox vBoxFilter;
    private final ProgData progData;

    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final Button btnClearFilter = new Button();

    private LastPlayedFilter lastPlayedFilter;

    public LastPlayedFilterController() {
        super(ProgConfig.LAST_PLAYED_GUI_FILTER_DIVIDER_ON, true);
        progData = ProgData.getInstance();
        lastPlayedFilter = progData.lastPlayedFilter;

        vBoxFilter = getVBoxAll();
        vBoxFilter.setPadding(new Insets(10));
        vBoxFilter.setSpacing(10);

        cboGenre.setMaxWidth(Double.MAX_VALUE);
        cboGenre.setMinWidth(150);
        VBox vBoxColl = new VBox();
        vBoxColl.getChildren().addAll(new Label("Genre"), cboGenre);
        VBox.setVgrow(cboGenre, Priority.ALWAYS);

        vBoxFilter.getChildren().addAll(vBoxColl);

        Separator sp = new Separator();
        sp.getStyleClass().add("pseperator1");
        sp.setMinHeight(0);
        sp.setPadding(new Insets(10));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(btnClearFilter);
        vBoxFilter.getChildren().addAll(new Label("    "), sp, hBox);
        initFilter();
    }

    private void initFilter() {
        FilterCheckRegEx fN = new FilterCheckRegEx(cboGenre.getEditor());
        cboGenre.editableProperty().set(true);
        cboGenre.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cboGenre.setVisibleRowCount(25);
        cboGenre.valueProperty().bindBidirectional(lastPlayedFilter.genreFilterProperty());
        cboGenre.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                fN.checkPattern();
                progData.filteredLastPlayedList.setPredicate(lastPlayedFilter.getPredicate());
            }
        });
        cboGenre.setItems(progData.filterWorker.getAllGenreList());

        btnClearFilter.setGraphic(new ProgIcons().ICON_BUTTON_CLEAR_FILTER);
        btnClearFilter.setTooltip(new Tooltip("Wieder alle Favoriten anzeigen"));
        btnClearFilter.setOnAction(event -> {
            progData.filteredLastPlayedList.setPredicate(lastPlayedFilter.clearFilter());
        });
    }
}
