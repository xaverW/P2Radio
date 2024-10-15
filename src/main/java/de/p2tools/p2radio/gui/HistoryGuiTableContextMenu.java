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
import de.p2tools.p2radio.controller.data.history.HistoryFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.Optional;

public class HistoryGuiTableContextMenu {

    private final ProgData progData;
    private final HistoryGuiController historyGuiController;
    private final TablePlayable tableView;

    public HistoryGuiTableContextMenu(ProgData progData, HistoryGuiController historyGuiController, TablePlayable tableView) {
        this.progData = progData;
        this.historyGuiController = historyGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(StationData stationData) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, stationData);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, StationData station) {
        MenuItem miStart = new MenuItem("Sender abspielen");
        miStart.setOnAction(a -> historyGuiController.playStation());
        miStart.setDisable(station == null);
        contextMenu.getItems().addAll(miStart);

        Menu mStartStation = startStationWithSet(station); // Sender mit Set starten
        if (mStartStation != null) {
            mStartStation.setDisable(station == null);
            contextMenu.getItems().add(mStartStation);
        }

        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> historyGuiController.stopStation(false));
        miStop.setDisable(station == null);

        MenuItem miStopAll = new MenuItem("Alle Sender stoppen");
        miStopAll.setOnAction(a -> historyGuiController.stopStation(true /* alle */));
        miStopAll.setDisable(station == null);

        contextMenu.getItems().addAll(miStop, miStopAll);

        MenuItem miCopyUrl = new MenuItem("Sender (URL) kopieren");
        miCopyUrl.setOnAction(a -> historyGuiController.copyUrl());
        miCopyUrl.setDisable(station == null);

        MenuItem miRemove = new MenuItem("Sender aus History löschen");
        miRemove.setOnAction(a -> HistoryFactory.deleteHistory(false));
        miRemove.setDisable(station == null);

        contextMenu.getItems().addAll(new SeparatorMenuItem(), miCopyUrl, miRemove);

        if (station != null) {
            String stationUrl = station.getStationUrl();
            StationData stationData = progData.stationList.getSenderByUrl(stationUrl);
            if (stationData != null) {
                MenuItem miAddFavourite = new MenuItem("Sender als Favoriten speichern");
                miAddFavourite.setOnAction(a -> FavouriteFactory.favouriteStation(stationData));
                miAddFavourite.setDisable(station == null);
                contextMenu.getItems().addAll(miAddFavourite);
            }
        }

        final MenuItem miAutoStart = new MenuItem("Sender als AutoStart auswählen");
        miAutoStart.setOnAction(e -> AutoStartFactory.setHistoryAutoStart());
        contextMenu.getItems().addAll(miAutoStart);
        miAutoStart.setDisable(station == null);

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

            list.forEach(setData -> {
                final MenuItem item = new MenuItem(setData.getVisibleName());
                item.setOnAction(event -> {
                    final Optional<StationData> stationData =
                            ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSel();
                    stationData.ifPresent(data -> StartFactory.playPlayable(data, setData));
                });
                submenuSet.getItems().add(item);
            });

            return submenuSet;
        }

        return null;
    }
}
