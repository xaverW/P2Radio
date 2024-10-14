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

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.Optional;

public class FavouriteGuiTableContextMenu {

    private final ProgData progData;
    private final FavouriteGuiController favouriteGuiController;
    private final TablePlayable tableView;

    public FavouriteGuiTableContextMenu(ProgData progData, FavouriteGuiController favouriteGuiController, TablePlayable tableView) {
        this.progData = progData;
        this.favouriteGuiController = favouriteGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(StationData stationData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, stationData);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, StationData stationData) {
        MenuItem miStart = new MenuItem("Sender abspielen");
        miStart.setDisable(stationData == null);
        miStart.setOnAction(a -> favouriteGuiController.playStation());
        contextMenu.getItems().addAll(miStart);

        Menu mStartStation = startStationWithSet(stationData); // Sender mit Set starten
        if (mStartStation != null) {
            mStartStation.setDisable(stationData == null);
            contextMenu.getItems().add(mStartStation);
        }

        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> favouriteGuiController.stopStation(false));
        MenuItem miStopAll = new MenuItem("Alle Sender stoppen");
        miStopAll.setOnAction(a -> favouriteGuiController.stopStation(true /* alle */));

        contextMenu.getItems().addAll(miStop, miStopAll, new SeparatorMenuItem());

        MenuItem miCopyUrl = new MenuItem("Sender (URL) kopieren");
        miCopyUrl.setOnAction(a -> favouriteGuiController.copyUrl());

        MenuItem miChange = new MenuItem("Favorit ändern");
        miChange.setOnAction(a -> FavouriteFactory.changeFavourite(false));
        MenuItem miRemove = new MenuItem("Favoriten löschen");
        miRemove.setOnAction(a -> FavouriteFactory.deleteFavourite(false));

        miStop.setDisable(stationData == null);
        miStopAll.setDisable(stationData == null);
        miCopyUrl.setDisable(stationData == null);
        miChange.setDisable(stationData == null);
        miRemove.setDisable(stationData == null);

        contextMenu.getItems().addAll(miCopyUrl, miChange, miRemove);

        final MenuItem miAutoStart = new MenuItem("Sender als AutoStart auswählen");
        miAutoStart.setOnAction(e -> AutoStartFactory.setFavouriteAutoStart());
        contextMenu.getItems().addAll(miAutoStart);
        miAutoStart.setDisable(stationData == null);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> tableView.resetTable());

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }

    private Menu startStationWithSet(StationData station) {
        final SetDataList list = progData.setDataList.getSetDataListButton();
        if (!list.isEmpty()) {
            Menu submenuSet = new Menu("Sender mit Set abspielen");

            if (station == null) {
                submenuSet.setDisable(true);
                return submenuSet;
            }

            list.stream().forEach(setData -> {
                final MenuItem item = new MenuItem(setData.getVisibleName());
                item.setOnAction(event -> {
                    final Optional<StationData> favourite = ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSel();
                    if (favourite.isPresent()) {
                        progData.startFactory.playPlayable(favourite.get(), setData);
                    }
                });
                submenuSet.getItems().add(item);
            });

            return submenuSet;
        }

        return null;
    }
}
