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
import de.p2tools.p2lib.guitools.PButtonClearFilterFactory;
import de.p2tools.p2lib.guitools.PGuiTools;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StationFilterControllerClearFilter extends VBox {

    private final Button btnClearFilter = PButtonClearFilterFactory.getPButtonClear();
    private final Button btnEditFilter = new Button("");
    private final Button btnGoBack = new Button("");
    private final Button btnGoForward = new Button("");

    private final ProgData progData;

    public StationFilterControllerClearFilter() {
        super();
        progData = ProgData.getInstance();
        progData.stationFilterControllerClearFilter = this;
        setPadding(new Insets(0, P2LibConst.DIST_EDGE, P2LibConst.DIST_EDGE, P2LibConst.DIST_EDGE));
        setSpacing(P2LibConst.DIST_BUTTON);

        addButton();
    }

    private void addButton() {
        btnGoBack.setGraphic(ProgIconsP2Radio.ICON_BUTTON_BACKWARD.getImageView());
        btnGoBack.setOnAction(a -> progData.storedFilters.getStoredFiltersForwardBackward().goBackward());
        btnGoBack.disableProperty().bind(progData.storedFilters.getStoredFiltersForwardBackward().backwardProperty().not());
        btnGoBack.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnGoForward.setGraphic(ProgIconsP2Radio.ICON_BUTTON_FORWARD.getImageView());
        btnGoForward.setOnAction(a -> progData.storedFilters.getStoredFiltersForwardBackward().goForward());
        btnGoForward.disableProperty().bind(progData.storedFilters.getStoredFiltersForwardBackward().forwardProperty().not());
        btnGoForward.setTooltip(new Tooltip("letzte Filtereinstellung wieder herstellen"));

        btnClearFilter.setOnAction(a -> clearFilter());

        btnEditFilter.setGraphic(ProgIconsP2Radio.ICON_BUTTON_EDIT_FILTER.getImageView());
        btnEditFilter.setOnAction(a -> editFilter());
        btnEditFilter.setTooltip(new Tooltip("Filter ein/ausschalten"));

        HBox hBox = new HBox(P2LibConst.DIST_BUTTON);
        hBox.setAlignment(Pos.CENTER_RIGHT);
//        hBox.setPadding(new Insets(5, 0, 0, 0));
        hBox.getChildren().addAll(btnEditFilter, PGuiTools.getHBoxGrower(), btnGoBack, btnGoForward, btnClearFilter);
        getChildren().addAll(hBox);
    }

    private void clearFilter() {
        PDuration.onlyPing("Filter löschen");
        progData.storedFilters.clearFilter();
    }

    private void editFilter() {
        final StationFilterEditDialog editFilterDialog = new StationFilterEditDialog(progData);
    }
}
