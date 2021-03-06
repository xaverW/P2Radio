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

import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.Listener;
import de.p2tools.p2radio.tools.stationListFilter.BlackFilterFactory;
import de.p2tools.p2radio.tools.stationListFilter.StationListFilter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StationFilterController extends FilterController {

    private final VBox vBoxBottom;
    private final ProgData progData;

    private final PToggleSwitch tglBlacklist = new PToggleSwitch("Blacklist:");

    StationFilterControllerTextFilter sender;
    StationFilterControllerFilter filter;
    StationFilterControllerClearFilter clearFilter;
    StationFilterControllerProfiles profiles;

    public StationFilterController() {
        super();
        progData = ProgData.getInstance();

        sender = new StationFilterControllerTextFilter();//hat separator am ende??
        filter = new StationFilterControllerFilter();
        clearFilter = new StationFilterControllerClearFilter();
        profiles = new StationFilterControllerProfiles();

        Separator sp1 = new Separator();
        sp1.getStyleClass().add("pseperator3");
        sp1.setPadding(new Insets(10));

        final VBox vBoxTop = getVBoxTop();
        vBoxTop.setSpacing(0);
        HBox hBoxs = new HBox();
        VBox.setVgrow(hBoxs, Priority.ALWAYS);
        vBoxTop.getChildren().addAll(sender, filter, hBoxs, clearFilter, sp1, profiles);

        Label lblRight = new Label();
        tglBlacklist.setAllowIndeterminate(true);
        tglBlacklist.setLabelRight(lblRight, "ein", "aus", "nur");
        tglBlacklist.setTooltip(new Tooltip("Blacklist ein- ausschalten oder nur Sender aus der Blacklist anzeigen"));
        tglBlacklist.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().blacklistOnProperty());
        tglBlacklist.indeterminateProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().blacklistOnlyProperty());

        vBoxBottom = getVBoxBottom();
        HBox hBox = new HBox(10);
        HBox.setHgrow(tglBlacklist, Priority.ALWAYS);
        hBox.getChildren().addAll(tglBlacklist, lblRight);
        vBoxBottom.getChildren().addAll(hBox);
        Listener.addListener(new Listener(Listener.EVENT_BLACKLIST_CHANGED, StationListFilter.class.getSimpleName()) {
            @Override
            public void pingFx() {
                setBlack();
            }
        });
        setBlack();
    }

    private void setBlack() {
        if (BlackFilterFactory.isBlackEmpty()) {
            //gibt keine Black-Eintr??ge, dann auch die toggle "ausschalten"
            progData.storedFilters.getActFilterSettings().setBlacklistOn(false);
            progData.storedFilters.getActFilterSettings().setBlacklistOnly(false);
            //und dann auch nicht anzeigen
            vBoxBottom.setVisible(false);
            vBoxBottom.setManaged(false);

        } else {
            //anzeigen
            vBoxBottom.setVisible(true);
            vBoxBottom.setManaged(true);
        }
    }
}
