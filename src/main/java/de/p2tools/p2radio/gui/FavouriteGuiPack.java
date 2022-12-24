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
import de.p2tools.p2radio.gui.filter.FavouriteFilterController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class FavouriteGuiPack {

    private final SplitPane splitPaneFilter = new SplitPane();
    private final SplitPane splitPaneInfo = new SplitPane();

    private final FavouriteFilterController favouriteFilterController;
    private final FavouriteGuiController favouriteGuiController;
    private final FavouriteGuiInfoController favouriteGuiInfoController;

    private final ProgData progData;
    private final ObjectProperty<StationData> stationDataObjectProperty = new SimpleObjectProperty<>(null);
    private boolean boundFilter = false;
    private boolean boundInfo = false;


    public FavouriteGuiPack() {
        progData = ProgData.getInstance();

        favouriteFilterController = new FavouriteFilterController(this);
        favouriteGuiInfoController = new FavouriteGuiInfoController(this);
        favouriteGuiController = new FavouriteGuiController(this);

        progData.favouriteGuiPack = this;
    }

    public FavouriteFilterController getFavouriteFilterController() {
        return favouriteFilterController;
    }

    public FavouriteGuiController getFavouriteGuiController() {
        return favouriteGuiController;
    }

    public FavouriteGuiInfoController getFavouriteGuiInfoController() {
        return favouriteGuiInfoController;
    }

    public void closeSplit() {
        ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON.setValue(!ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON.get());
    }

    public Pane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.FAVOURITE);
        menuController.setId("favorite-menu-pane");

        //Filter
        splitPaneFilter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(favouriteFilterController, false);
        ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setSplitFilter());
        setSplitFilter();

        //Info
        splitPaneInfo.setOrientation(Orientation.VERTICAL);
        ProgConfig.FAVOURITE_GUI_DIVIDER_ON.addListener((observable, oldValue, newValue) -> setSplitInfo());
        SplitPane.setResizableWithParent(favouriteGuiInfoController, false);
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
        if (ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON.getValue()) {
            splitPaneFilter.getItems().clear();
            splitPaneFilter.getItems().addAll(favouriteFilterController, splitPaneInfo);
            if (!boundFilter) {
                boundFilter = true;
                splitPaneFilter.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER);
            }
        } else {
            if (boundFilter) {
                boundFilter = false;
                splitPaneFilter.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER);
            }
            splitPaneFilter.getItems().clear();
            splitPaneFilter.getItems().addAll(splitPaneInfo);
        }
    }

    private void setSplitInfo() {
        if (ProgConfig.FAVOURITE_GUI_DIVIDER_ON.getValue()) {
            splitPaneInfo.getItems().clear();
            splitPaneInfo.getItems().addAll(favouriteGuiController, favouriteGuiInfoController);
            if (!boundInfo) {
                boundInfo = true;
                splitPaneInfo.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.FAVOURITE_GUI_DIVIDER);
            }
        } else {
            if (boundInfo) {
                boundInfo = false;
                splitPaneInfo.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.FAVOURITE_GUI_DIVIDER);
            }
            splitPaneInfo.getItems().clear();
            splitPaneInfo.getItems().add(favouriteGuiController);
        }
    }
}
