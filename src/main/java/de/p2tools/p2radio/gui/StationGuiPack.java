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

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.filter.StationFilterController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;

public class StationGuiPack {

    private final SplitPane splitPaneFilter = new SplitPane();
    private final SplitPane splitPaneInfo = new SplitPane();
    private final HBox hBox = new HBox();
    private final TilePane tilePaneButton = new TilePane();

    private final StationFilterController stationFilterController;
    private final StationGuiController stationGuiController;
    private final StationGuiInfoController stationGuiInfoController;

    private final ProgData progData;
    private final ObjectProperty<StationData> stationDataObjectProperty = new SimpleObjectProperty<>(null);
    private boolean boundInfo = false;
    private boolean boundFilter = false;

    public StationGuiPack() {
        progData = ProgData.getInstance();
        stationFilterController = new StationFilterController(this);
        stationGuiController = new StationGuiController(this);
        stationGuiInfoController = new StationGuiInfoController(this);

        progData.stationGuiPack = this;
    }

    public StationFilterController getStationFilterController() {
        return stationFilterController;
    }

    public StationGuiController getStationGuiController() {
        return stationGuiController;
    }

    public StationGuiInfoController getStationGuiInfoController() {
        return stationGuiInfoController;
    }

    public Pane pack() {
        // Menü
        final MenuController menuController = new MenuController(MenuController.StartupMode.STATION);
        menuController.setId("station-menu-pane");

        //vertikal
        splitPaneFilter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(stationFilterController, false);
        ProgConfig.STATION_GUI_FILTER_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setSplitFilter());
        setSplitFilter();

        //horizontal
        SplitPane.setResizableWithParent(stationGuiInfoController, false);
        splitPaneInfo.setOrientation(Orientation.VERTICAL);
        progData.setDataList.listChangedProperty().addListener((observable, oldValue, newValue) -> {
            if (progData.setDataList.getSetDataListButton().size() > 1) {
                ProgConfig.STATION_GUI_DIVIDER_ON.set(true);
            }
            setSpliltPaneInfo();
        });
        ProgConfig.STATION_GUI_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setSpliltPaneInfo());
        tilePaneButton.setVgap(15);
        tilePaneButton.setHgap(15);
        tilePaneButton.setPadding(new Insets(10));
        tilePaneButton.setStyle("-fx-border-color: -fx-text-box-border; " +
                "-fx-border-radius: 5px; " +
                "-fx-border-width: 1;");
        setSpliltPaneInfo();

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPaneFilter, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPaneFilter, menuController);

        return hBox;
    }

    public void closeSplitVert() {
        ProgConfig.STATION_GUI_FILTER_DIVIDER_ON.setValue(!ProgConfig.STATION_GUI_FILTER_DIVIDER_ON.get());
    }

    public ObjectProperty<StationData> stationDataObjectPropertyProperty() {
        return stationDataObjectProperty;
    }

    private void setSplitFilter() {
        if (ProgConfig.STATION_GUI_FILTER_DIVIDER_ON.getValue()) {
            splitPaneFilter.getItems().clear();
            splitPaneFilter.getItems().addAll(stationFilterController, splitPaneInfo);
            if (!boundFilter) {
                boundFilter = true;
                splitPaneFilter.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.STATION_GUI_FILTER_DIVIDER);
            }
        } else {
            if (boundFilter) {
                boundFilter = false;
                splitPaneFilter.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.STATION_GUI_FILTER_DIVIDER);
            }
            splitPaneFilter.getItems().clear();
            splitPaneFilter.getItems().addAll(splitPaneInfo);
        }
    }

    private void setSpliltPaneInfo() {
        if (ProgConfig.STATION_GUI_DIVIDER_ON.getValue()) {
            splitPaneInfo.getItems().clear();
            splitPaneInfo.getItems().addAll(stationGuiController, stationGuiInfoController);
            if (!boundInfo) {
                boundInfo = true;
                splitPaneInfo.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.STATION_GUI_DIVIDER);
            }
        } else {
            if (boundInfo) {
                boundInfo = false;
                splitPaneInfo.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.STATION_GUI_DIVIDER);
            }
            splitPaneInfo.getItems().clear();
            splitPaneInfo.getItems().add(stationGuiController);
        }
    }
}
