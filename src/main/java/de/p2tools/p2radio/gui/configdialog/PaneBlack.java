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

package de.p2tools.p2radio.gui.configdialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.prange.P2RangeBox;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.gui.tools.HelpText;
import de.p2tools.p2radio.tools.stationlistfilter.StationFilterFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneBlack {

    private final P2RangeBox slBitrate = new P2RangeBox("", true, 0, StationFilterFactory.FILTER_BITRATE_MAX);
    private final Stage stage;
    private final ChangeListener<Number> changeListenerMin;
    private final ChangeListener<Number> changeListenerMax;

    public PaneBlack(Stage stage, BooleanProperty blackChanged) {
        this.stage = stage;

        changeListenerMin = (observable, oldValue, newValue) -> blackChanged.set(true);
        changeListenerMax = (observable, oldValue, newValue) -> blackChanged.set(true);
        slBitrate.setUnitSuffix("kbit/s");
    }

    public void close() {
        slBitrate.minValueProperty().unbindBidirectional(ProgConfig.SYSTEM_BLACKLIST_MIN_BITRATE);
        slBitrate.maxValueProperty().unbindBidirectional(ProgConfig.SYSTEM_BLACKLIST_MAX_BITRATE);
        slBitrate.minValueProperty().removeListener(changeListenerMin);
        slBitrate.maxValueProperty().removeListener(changeListenerMax);
    }

    public void makeBlack(Collection<TitledPane> result) {
        initBitrateFilter();
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));

        TitledPane tpConfig = new TitledPane("Blacklist allgemein", gridPane);
        result.add(tpConfig);

        final Button btnHelpBitrate = P2Button.helpButton(stage, "Blacklist",
                HelpText.BLACKLIST_BITRATE);

        int row = 0;
        gridPane.add(new Label("Nur Sender mit der Bitrate anzeigen:"), 0, row, 2, 1);

        gridPane.add(new Label("Bitrate:"), 0, ++row);
        gridPane.add(slBitrate, 1, row);
        gridPane.add(btnHelpBitrate, 2, row);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize());
    }

    private void initBitrateFilter() {
        slBitrate.minValueProperty().bindBidirectional(ProgConfig.SYSTEM_BLACKLIST_MIN_BITRATE);
        slBitrate.maxValueProperty().bindBidirectional(ProgConfig.SYSTEM_BLACKLIST_MAX_BITRATE);
        slBitrate.minValueProperty().addListener(changeListenerMin);
        slBitrate.maxValueProperty().addListener(changeListenerMax);
    }
}