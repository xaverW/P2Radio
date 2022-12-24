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
import de.p2tools.p2radio.gui.filter.HistoryFilterController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HistoryGuiPack {

    private final SplitPane splitPaneFilter = new SplitPane();
    private final SplitPane splitPaneInfo = new SplitPane();

    private final HistoryGuiController historyGuiController;
    private final HistoryFilterController historyFilterController;
    private final HistoryGuiInfoController historyGuiInfoController;

    private final ProgData progData;
    private final ObjectProperty<StationData> stationDataObjectProperty = new SimpleObjectProperty<>(null);
    private boolean boundFilter = false;
    private boolean boundInfo = false;

    public HistoryGuiPack() {
        progData = ProgData.getInstance();

        historyFilterController = new HistoryFilterController(this);
        historyGuiInfoController = new HistoryGuiInfoController(this);
        historyGuiController = new HistoryGuiController(this);
        progData.historyGuiPack = this;
    }

    public HistoryGuiController getHistoryGuiController() {
        return historyGuiController;
    }

    public HistoryFilterController getHistoryFilterController() {
        return historyFilterController;
    }

    public HistoryGuiInfoController getHistoryGuiInfoController() {
        return historyGuiInfoController;
    }

    public void closeSplit() {
        ProgConfig.HISTORY_GUI_FILTER_DIVIDER_ON.setValue(!ProgConfig.HISTORY_GUI_FILTER_DIVIDER_ON.get());
    }

    public Pane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.HISTORY);
        menuController.setId("history-menu-pane");

        //Filter
        splitPaneFilter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ProgConfig.HISTORY_GUI_FILTER_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setSplitFilter());
        SplitPane.setResizableWithParent(historyFilterController, false);
        setSplitFilter();

        //Info
        splitPaneInfo.setOrientation(Orientation.VERTICAL);
        ProgConfig.HISTORY_GUI_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setSplitInfo());
        SplitPane.setResizableWithParent(historyGuiInfoController, false);
        setSplitInfo();

        final HBox hBox = new HBox();
        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(splitPaneFilter, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPaneFilter, menuController);

        return hBox;
    }

    public ObjectProperty<StationData> stationDataObjectPropertyProperty() {
        return stationDataObjectProperty;
    }

    private void setSplitFilter() {
        if (ProgConfig.HISTORY_GUI_FILTER_DIVIDER_ON.getValue()) {
            splitPaneFilter.getItems().clear();
            splitPaneFilter.getItems().addAll(historyFilterController, splitPaneInfo);
            if (!boundFilter) {
                boundFilter = true;
                splitPaneFilter.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.HISTORY_GUI_FILTER_DIVIDER);
            }

        } else {
            if (boundFilter) {
                boundFilter = false;
                splitPaneFilter.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.HISTORY_GUI_FILTER_DIVIDER);
            }
            splitPaneFilter.getItems().clear();
            splitPaneFilter.getItems().addAll(splitPaneInfo);
        }
    }

    private void setSplitInfo() {
        if (ProgConfig.HISTORY_GUI_DIVIDER_ON.getValue()) {
            splitPaneInfo.getItems().clear();
            splitPaneInfo.getItems().addAll(historyGuiController, historyGuiInfoController);
            if (!boundInfo) {
                boundInfo = true;
                splitPaneInfo.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.HISTORY_GUI_DIVIDER);
            }

        } else {
            if (boundInfo) {
                boundInfo = false;
                splitPaneInfo.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.HISTORY_GUI_DIVIDER);
            }
            splitPaneInfo.getItems().clear();
            splitPaneInfo.getItems().add(historyGuiController);
        }
    }
}
