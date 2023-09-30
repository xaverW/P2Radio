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
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2lib.tools.events.PEvent;
import de.p2tools.p2lib.tools.events.PListener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.StationGuiPack;
import de.p2tools.p2radio.tools.stationlistfilter.BlackFilterFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StationFilterController extends FilterController {

    private final VBox vBoxAll;
    private final VBox vBoxBottom;
    private final ProgData progData;

    private final P2ToggleSwitch tglBlacklist = new P2ToggleSwitch("Blacklist:");
    private final StationGuiPack stationGuiPack;
    private final StationFilterControllerTextFilter sender;
    private final StationFilterControllerFilter filter;
    private final StationFilterControllerClearFilter clearFilter;
    private final StationFilterControllerProfiles profiles;

    public StationFilterController(StationGuiPack stationGuiPack) {
        super(ProgConfig.STATION_GUI_FILTER_DIVIDER_ON);
        progData = ProgData.getInstance();
        this.stationGuiPack = stationGuiPack;
        this.vBoxAll = getVBoxAll();
        this.vBoxAll.setSpacing(P2LibConst.DIST_BUTTON);
        this.vBoxAll.setSpacing(0);

        sender = new StationFilterControllerTextFilter();//hat separator am ende??
        filter = new StationFilterControllerFilter();
        clearFilter = new StationFilterControllerClearFilter();
        profiles = new StationFilterControllerProfiles();

        Separator sp = new Separator();
        sp.getStyleClass().add("pseperator3");
        sp.setMinHeight(0);
        sp.setPadding(new Insets(0, P2LibConst.DIST_EDGE, P2LibConst.DIST_EDGE, P2LibConst.DIST_EDGE));

//        setSpacing(P2LibConst.DIST_BUTTON);

        VBox.setVgrow(clearFilter, Priority.ALWAYS);
        vBoxAll.getChildren().addAll(sender, filter, clearFilter, sp, profiles);

        Label lblRight = new Label();
        tglBlacklist.setAllowIndeterminate(true);
        tglBlacklist.setLabelRight(lblRight, "ein", "aus", "nur");
        tglBlacklist.setTooltip(new Tooltip("Blacklist ein- ausschalten oder nur Sender aus der Blacklist anzeigen"));
        tglBlacklist.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().blacklistOnProperty());
        tglBlacklist.indeterminateProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().blacklistOnlyProperty());

        //Anzeige der Blacklist
        HBox hBox = new HBox(5);
        HBox.setHgrow(tglBlacklist, Priority.ALWAYS);
        hBox.getChildren().addAll(tglBlacklist, lblRight);
        vBoxBottom = getVBoxBottom();//zum Schluss, damit untern in der VBoxAll
        vBoxBottom.getChildren().addAll(hBox);

        progData.pEventHandler.addListener(new PListener(Events.BLACKLIST_CHANGED) {
            public void pingGui(PEvent event) {
                setBlack();
            }
        });
        setBlack();
    }

    private VBox getVBoxBottom() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("extra-pane");
        vBox.setPadding(new Insets(P2LibConst.DIST_EDGE));
        vBox.setSpacing(P2LibConst.DIST_BUTTON);
        vBox.setMaxWidth(Double.MAX_VALUE);
        vBoxAll.getChildren().addAll(vBox);
        return vBox;
    }

    private void setBlack() {
        if (BlackFilterFactory.isBlackEmpty()) {
            //gibt keine Black-Einträge, dann auch die toggle "ausschalten"
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
