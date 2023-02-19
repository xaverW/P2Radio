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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class SmallRadioGuiTableContextMenu {

    private final SmallRadioGuiController smallRadioGuiController;
    private final TablePlayable tableView;

    public SmallRadioGuiTableContextMenu(SmallRadioGuiController smallRadioGuiController, TablePlayable tableView) {
        this.smallRadioGuiController = smallRadioGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenuStation(StationData stationData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu1(contextMenu, stationData);
        getMenuStation(contextMenu, stationData);
        getMenu2(contextMenu, stationData);
        return contextMenu;
    }

    public ContextMenu getContextMenuFavourite(StationData stationData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu1(contextMenu, stationData);
        getMenuFavourite(contextMenu, stationData);
        getMenu2(contextMenu, stationData);
        return contextMenu;
    }

    public ContextMenu getContextMenuHistory(StationData stationData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu1(contextMenu, stationData);
        getMenu2(contextMenu, stationData);
        return contextMenu;
    }

    private void getMenu1(ContextMenu contextMenu, StationData favourite) {
        MenuItem miStart = new MenuItem("Sender starten");
        miStart.setOnAction(a -> smallRadioGuiController.playStation());
        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> smallRadioGuiController.stopStation(false));
        MenuItem miStopAll = new MenuItem("Alle Sender stoppen");
        miStopAll.setOnAction(a -> smallRadioGuiController.stopStation(true /* alle */));
        MenuItem miCopyUrl = new MenuItem("Sender (URL) kopieren");
        miCopyUrl.setOnAction(a -> smallRadioGuiController.copyUrl());

        miStart.setDisable(favourite == null);
        miStop.setDisable(favourite == null);
        miStopAll.setDisable(favourite == null);
        miCopyUrl.setDisable(favourite == null);

        contextMenu.getItems().addAll(miStart, miStop, miStopAll, miCopyUrl);
    }

    private void getMenu2(ContextMenu contextMenu, StationData favourite) {
        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> tableView.resetTable());

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }

    private void getMenuStation(ContextMenu contextMenu, StationData stationData) {
        MenuItem miSave = new MenuItem("Sender als Favoriten speichern");
        miSave.setOnAction(a -> FavouriteFactory.favouriteStation(stationData));
        miSave.setDisable(stationData == null);

        contextMenu.getItems().addAll(new SeparatorMenuItem(), miSave);
    }

    private void getMenuFavourite(ContextMenu contextMenu, StationData stationData) {
        MenuItem miChange = new MenuItem("Favorit ändern");
        miChange.setOnAction(a -> FavouriteFactory.changeFavourite(stationData));
        MenuItem miRemove = new MenuItem("Favoriten löschen");
        miRemove.setOnAction(a -> FavouriteFactory.deleteFavourite(stationData));

        miChange.setDisable(stationData == null);
        miRemove.setDisable(stationData == null);

        contextMenu.getItems().addAll(new SeparatorMenuItem(), miChange, miRemove);
    }
}
