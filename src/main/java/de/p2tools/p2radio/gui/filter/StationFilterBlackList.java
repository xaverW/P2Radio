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

import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class StationFilterBlackList extends HBox {

    private final ProgData progData;

    private final P2ToggleSwitch tglBlacklist = new P2ToggleSwitch("Blacklist:");

    public StationFilterBlackList() {
        progData = ProgData.getInstance();

        Label lblRight = new Label();
        tglBlacklist.setAllowIndeterminate(true);
        tglBlacklist.setLabelRight(lblRight, "ein", "aus", "nur");
        tglBlacklist.setTooltip(new Tooltip("Blacklist ein- ausschalten oder nur Sender aus der Blacklist anzeigen"));
        tglBlacklist.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().blacklistOnProperty());
        tglBlacklist.indeterminateProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().blacklistOnlyProperty());

        setSpacing(5);
        setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(tglBlacklist, Priority.ALWAYS);
        getChildren().addAll(tglBlacklist, lblRight);
    }
}
