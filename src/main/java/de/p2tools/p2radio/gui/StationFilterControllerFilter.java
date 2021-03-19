/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.guiTools.pCheckComboBox.PCheckComboBox;
import de.p2tools.p2Lib.guiTools.pRange.PRangeBox;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.tools.stationListFilter.StationFilterFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StationFilterControllerFilter extends VBox {

    private final PRangeBox slBitrate = new PRangeBox(0, StationFilterFactory.FILTER_BITRATE_MAX);
    private final Label lblBitrate = new Label("Bitrate:");

    private final Label lblOnly = new Label("anzeigen");
    private final PCheckComboBox checkOnly = new PCheckComboBox();
    private final String ONLY_NEW = "nur neue";
    private final String NOT_FAVOURITES = "keine Favoriten";
    private final String NOT_DOUBLE = "keine doppelten";

    private final ProgData progData;

    public StationFilterControllerFilter() {
        super();
        progData = ProgData.getInstance();

        setPadding(new Insets(10));
        setSpacing(20);

        addSlider();
        addCheckFilter();
    }

    private void addSlider() {
        slBitrate.minValueProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().minBitProperty());
        slBitrate.maxValueProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().maxBitProperty());
        slBitrate.setValuePrefix("");
        slBitrate.setUnitSuffix(" Bit");

        // MinMax Bitrate
        VBox vBox = new VBox(3);
        vBox.getChildren().addAll(lblBitrate, slBitrate);
        vBox.visibleProperty().bind(progData.storedFilters.getActFilterSettings().minMaxBitVisProperty());
        vBox.managedProperty().bind(progData.storedFilters.getActFilterSettings().minMaxBitVisProperty());
        getChildren().addAll(vBox);
    }

    private void addCheckFilter() {
        checkOnly.addItem(ONLY_NEW, "+neu", "nur neue Sender anzeigen", progData.storedFilters.getActFilterSettings().onlyNewProperty());
        checkOnly.addItem(NOT_FAVOURITES, "-Favorit", "keine Sender aus den Favoriten anzeigen", progData.storedFilters.getActFilterSettings().noFavouritesProperty());
        checkOnly.addItem(NOT_DOUBLE, "-doppelt", "doppelte Sender nur einmal anzeigen", progData.storedFilters.getActFilterSettings().noDoublesProperty());

        VBox vBox = new VBox();
        vBox.getChildren().addAll(lblOnly, checkOnly);
        vBox.visibleProperty().bind(progData.storedFilters.getActFilterSettings().onlyVisProperty());
        vBox.managedProperty().bind(progData.storedFilters.getActFilterSettings().onlyVisProperty());
        getChildren().add(vBox);
    }
}
