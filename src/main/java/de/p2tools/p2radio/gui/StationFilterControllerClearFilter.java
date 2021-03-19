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

import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StationFilterControllerClearFilter extends VBox {

    private final Button btnClearFilter = new Button("");
    private final Button btnEditFilter = new Button("");
    private final Button btnGoBack = new Button("");
    private final Button btnGoForward = new Button("");

    private final ProgData progData;

    public StationFilterControllerClearFilter() {
        super();
        progData = ProgData.getInstance();
        progData.stationFilterControllerClearFilter = this;

        setPadding(new Insets(10));
        setSpacing(20);

        addButton();
    }

    private void addButton() {
        btnGoBack.setGraphic(new ProgIcons().ICON_BUTTON_BACKWARD);
        btnGoBack.setOnAction(a -> progData.storedFilters.getStoredFiltersForwardBackward().goBackward());
        btnGoBack.disableProperty().bind(progData.storedFilters.getStoredFiltersForwardBackward().backwardProperty().not());
        btnGoBack.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));
        btnGoForward.setGraphic(new ProgIcons().ICON_BUTTON_FORWARD);
        btnGoForward.setOnAction(a -> progData.storedFilters.getStoredFiltersForwardBackward().goForward());
        btnGoForward.disableProperty().bind(progData.storedFilters.getStoredFiltersForwardBackward().forwardProperty().not());
        btnGoForward.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnClearFilter.setGraphic(new ProgIcons().ICON_BUTTON_RESET);
        btnClearFilter.setOnAction(a -> clearFilter());
        btnClearFilter.setTooltip(new Tooltip("Textfilter löschen, ein zweiter Klick löscht alle Filter"));

        btnEditFilter.setGraphic(new ProgIcons().ICON_BUTTON_EDIT_FILTER);
        btnEditFilter.setOnAction(a -> editFilter());
        btnEditFilter.setTooltip(new Tooltip("Filter ein/ausschalten"));

        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(btnGoBack, btnGoForward);
        HBox.setHgrow(hBox, Priority.ALWAYS);

        final Button btnHelp = PButton.helpButton("Filter", HelpText.GUI_STATION_FILTER);

        HBox hBoxAll = new HBox(5);
        hBoxAll.getChildren().addAll(hBox, btnClearFilter, btnEditFilter, btnHelp);
        getChildren().addAll(hBoxAll);
    }

    private void clearFilter() {
        PDuration.onlyPing("Filter löschen");
        progData.storedFilters.clearFilter();
    }

    private void editFilter() {
        final StationFilterEditDialog editFilterDialog = new StationFilterEditDialog(progData);
    }
}
