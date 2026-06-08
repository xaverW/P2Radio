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

import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneController;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneDto;
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.filter.StationFilterController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class StationGuiPack {

    private final SplitPane splitPaneFilter = new SplitPane();
    private final SplitPane splitPaneInfo = new SplitPane();

    private final StationFilterController stationFilterController;
    private final StationGuiController stationGuiController;
    private final PaneStationInfo paneStationInfo;

    private final P2ClosePaneController infoControllerFilter;
    private final P2ClosePaneController infoControllerInfo;

    private final ProgData progData;
    private final ObjectProperty<StationData> stationDataObjectProperty = new SimpleObjectProperty<>(null);
    private final BooleanProperty boundFilter = new SimpleBooleanProperty(false);
    private final BooleanProperty boundInfo = new SimpleBooleanProperty(false);

    public StationGuiPack() {
        progData = ProgData.getInstance();

        stationGuiController = new StationGuiController(this);
        stationFilterController = new StationFilterController();
        paneStationInfo = new PaneStationInfo(this);

        P2ClosePaneDto infoDto = new P2ClosePaneDto(stationFilterController,
                ProgConfig.STATION__FILTER_IS_RIP,
                ProgConfig.STATION__FILTER_DIALOG_SIZE, ProgData.STATION_TAB_ON,
                "Filter", "Sender", true,
                progData.maskerPane.getVisibleProperty());
        infoControllerFilter = new P2ClosePaneController(infoDto, ProgConfig.STATION__FILTER_IS_SHOWING);

        infoDto = new P2ClosePaneDto(paneStationInfo,
                ProgConfig.STATION__INFO_PANE_IS_RIP,
                ProgConfig.STATION__INFO_DIALOG_SIZE, ProgData.STATION_TAB_ON,
                "Info", "Sender", false,
                progData.maskerPane.getVisibleProperty());
        infoControllerInfo = new P2ClosePaneController(infoDto, ProgConfig.STATION__INFO_IS_SHOWING);

        progData.stationGuiPack = this;
    }

    public StationFilterController getStationFilterController() {
        return stationFilterController;
    }

    public StationGuiController getStationGuiController() {
        return stationGuiController;
    }

    public PaneStationInfo getStationGuiInfoController() {
        return paneStationInfo;
    }

    public Pane pack() {
        //Filter
        SplitPane.setResizableWithParent(infoControllerFilter, false);
        ProgConfig.STATION__FILTER_IS_SHOWING.addListener((observable, oldValue, newValue) -> setSplitFilter());
        setSplitFilter();

        //Info
        splitPaneInfo.setOrientation(Orientation.VERTICAL);
        SplitPane.setResizableWithParent(paneStationInfo, false);
        ProgConfig.STATION__INFO_IS_SHOWING.addListener((observable, oldValue, newValue) -> setSplitInfo());
        setSplitInfo();


        final MenuController menuController = new MenuController(MenuController.StartupMode.STATION);
        menuController.setId("station-menu-pane");

        final HBox hBox = new HBox();
        HBox.setHgrow(splitPaneFilter, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPaneFilter, menuController);

        return hBox;
    }

    public ObjectProperty<StationData> stationDataObjectPropertyProperty() {
        return stationDataObjectProperty;
    }

    private void setSplitFilter() {
        P2ClosePaneFactory.setSplit(boundFilter, splitPaneFilter,
                infoControllerFilter, true, splitPaneInfo,
                ProgConfig.STATION__FILTER_DIVIDER, ProgConfig.STATION__FILTER_IS_SHOWING);
    }

    private void setSplitInfo() {
        P2ClosePaneFactory.setSplit(boundInfo, splitPaneInfo,
                infoControllerInfo, false, stationGuiController,
                ProgConfig.STATION__INFO_DIVIDER, ProgConfig.STATION__INFO_IS_SHOWING);

    }
}
