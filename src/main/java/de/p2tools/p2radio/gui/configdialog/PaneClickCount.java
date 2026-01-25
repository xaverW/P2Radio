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
import de.p2tools.p2lib.guitools.grid.P2GridConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneClickCount {

    private final ProgData progData;

    private final P2ToggleSwitch tglClick = new P2ToggleSwitch("Start eines Senders melden (um Klicks zu zählen)");
    private final P2ToggleSwitch tglAsk = new P2ToggleSwitch("Vorher immer fragen");
    private final Stage stage;

    public PaneClickCount(Stage stage) {
        this.stage = stage;
        progData = ProgData.getInstance();
    }

    public void close() {
        tglClick.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_COUNT_CLICKS);
        tglAsk.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_ASK_COUNT_CLICKS);
    }

    public void make(Collection<TitledPane> result) {
        tglClick.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_COUNT_CLICKS);
        tglAsk.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_ASK_COUNT_CLICKS);

        final Button btnHelp = P2Button.helpButton(stage, "Klicks zählen",
                HelpText.CLICK_COUNT);

        GridPane gridPane = new GridPane(P2LibConst.DIST_GRIDPANE_VGAP, P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcComputedSizeAndHgrow(), P2GridConstraints.getCcPrefSize());
        gridPane.add(tglClick, 0, 0);
        gridPane.add(btnHelp, 1, 0);
        gridPane.add(tglAsk, 0, 1);

        TitledPane tpConfig = new TitledPane("Klicks zählen", gridPane);
        result.add(tpConfig);
    }
}
