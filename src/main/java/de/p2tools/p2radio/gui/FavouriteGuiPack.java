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
import de.p2tools.p2radio.gui.filter.FavouriteFilterController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;

public class FavouriteGuiPack {

    private final SplitPane splitPaneFilter = new SplitPane();
    private final SplitPane splitPaneInfo = new SplitPane();

    private final FavouriteGuiController favouriteGuiController;
    private final FavouriteFilterController favouriteFilterController;
    private final PaneFavouriteInfo paneFavouriteInfo;

    private final P2ClosePaneController infoControllerFilter;
    private final P2ClosePaneController infoControllerInfo;

    private final ProgData progData;
    private final ObjectProperty<StationData> stationDataObjectProperty = new SimpleObjectProperty<>(null);
    private final BooleanProperty boundFilter = new SimpleBooleanProperty(false);
    private final BooleanProperty boundInfo = new SimpleBooleanProperty(false);

    public FavouriteGuiPack() {
        progData = ProgData.getInstance();

        favouriteFilterController = new FavouriteFilterController(this);
        paneFavouriteInfo = new PaneFavouriteInfo(this);
        favouriteGuiController = new FavouriteGuiController(this);

        ArrayList<P2ClosePaneDto> list = new ArrayList<>();
        P2ClosePaneDto infoDto = new P2ClosePaneDto(favouriteFilterController,
                ProgConfig.FAVOURITE__FILTER_IS_RIP,
                ProgConfig.FAVOURITE__FILTER_DIALOG_SIZE, ProgData.FAVOURITE_TAB_ON,
                "Filter", "Favoriten", true,
                progData.maskerPane.getVisibleProperty());
        list.add(infoDto);
        infoControllerFilter = new P2ClosePaneController(list, ProgConfig.FAVOURITE__FILTER_IS_SHOWING);

        list = new ArrayList<>();
        infoDto = new P2ClosePaneDto(paneFavouriteInfo,
                ProgConfig.FAVOURITE__INFO_PANE_IS_RIP,
                ProgConfig.FAVOURITE__INFO_DIALOG_SIZE, ProgData.FAVOURITE_TAB_ON,
                "Info", "Favoriten", false,
                progData.maskerPane.getVisibleProperty());
        list.add(infoDto);
        infoControllerInfo = new P2ClosePaneController(list, ProgConfig.FAVOURITE__INFO_IS_SHOWING);

        progData.favouriteGuiPack = this;
    }

    public FavouriteFilterController getFavouriteFilterController() {
        return favouriteFilterController;
    }

    public FavouriteGuiController getFavouriteGuiController() {
        return favouriteGuiController;
    }

    public PaneFavouriteInfo getFavouriteGuiInfoController() {
        return paneFavouriteInfo;
    }

    public Pane pack() {
        //Filter
        splitPaneFilter.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        SplitPane.setResizableWithParent(infoControllerFilter, false);

        ProgConfig.FAVOURITE__FILTER_IS_SHOWING.addListener((observable, oldValue, newValue) -> setSplitFilter());
        setSplitFilter();

        //Info
        splitPaneInfo.setOrientation(Orientation.VERTICAL);
        ProgConfig.FAVOURITE__INFO_IS_SHOWING.addListener((observable, oldValue, newValue) -> setSplitInfo());
        SplitPane.setResizableWithParent(paneFavouriteInfo, false);
        setSplitInfo();


        final MenuController menuController = new MenuController(MenuController.StartupMode.FAVOURITE);
        menuController.setId("favorite-menu-pane");

        final HBox hBox = new HBox();
        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(infoControllerFilter, Priority.ALWAYS);
        hBox.getChildren().addAll(splitPaneFilter, menuController);

        return hBox;
    }

    public ObjectProperty<StationData> stationDataObjectPropertyProperty() {
        return stationDataObjectProperty;
    }

    private void setSplitFilter() {
        P2ClosePaneFactory.setSplit(boundFilter, splitPaneFilter,
                infoControllerFilter, true, splitPaneInfo,
                ProgConfig.FAVOURITE__FILTER_DIVIDER, ProgConfig.FAVOURITE__FILTER_IS_SHOWING);
    }

    private void setSplitInfo() {
        P2ClosePaneFactory.setSplit(boundInfo, splitPaneInfo,
                infoControllerInfo, false, favouriteGuiController,
                ProgConfig.FAVOURITE__INFO_DIVIDER, ProgConfig.FAVOURITE__INFO_IS_SHOWING);

    }
}
