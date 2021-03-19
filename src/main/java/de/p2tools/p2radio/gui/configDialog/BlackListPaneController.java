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

package de.p2tools.p2radio.gui.configDialog;

import de.p2tools.p2Lib.dialogs.accordion.PAccordionPane;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pRange.PRangeBox;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.HelpText;
import de.p2tools.p2radio.tools.stationListFilter.StationFilterFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class BlackListPaneController extends PAccordionPane {

    private final ProgData progData;
    private final PRangeBox slBitrate = new PRangeBox(0, StationFilterFactory.FILTER_BITRATE_MAX);

    IntegerProperty propMinBit = ProgConfig.SYSTEM_BLACKLIST_MIN_BITRATE;
    IntegerProperty propMaxBit = ProgConfig.SYSTEM_BLACKLIST_MAX_BITRATE;
    private final BooleanProperty blackChanged;

    private BlackPane blackPane;
    private final Stage stage;

    public BlackListPaneController(Stage stage, BooleanProperty blackChanged) {
        super(stage, ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_BLACKLIST);
        this.stage = stage;
        this.blackChanged = blackChanged;
        progData = ProgData.getInstance();

        init();
    }

    public void close() {
        super.close();
        blackPane.close();
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        makeBlack(result);
        blackPane = new BlackPane(stage, blackChanged);
        blackPane.makeBlackTable(result);
        return result;
    }

    private void makeBlack(Collection<TitledPane> result) {
        initBitrateFilter();
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Blacklist allgemein", gridPane);
        result.add(tpConfig);

        final Button btnHelpBitrate = PButton.helpButton(stage, "Blacklist",
                HelpText.BLACKLIST_BITRATE);

        int row = 0;
        gridPane.add(new Label("nur Sender mit der Bitrate anzeigen:"), 0, ++row, 2, 1);

        gridPane.add(new Label("Bitrate:"), 0, ++row);
        gridPane.add(slBitrate, 1, row);
        gridPane.add(btnHelpBitrate, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());
    }

    private void initBitrateFilter() {
        slBitrate.minValueProperty().bindBidirectional(propMinBit);
        slBitrate.maxValueProperty().bindBidirectional(propMaxBit);
        slBitrate.setValuePrefix("");
        slBitrate.setUnitSuffix(" Bit");
        slBitrate.maxValueProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
        slBitrate.minValueProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
    }
}