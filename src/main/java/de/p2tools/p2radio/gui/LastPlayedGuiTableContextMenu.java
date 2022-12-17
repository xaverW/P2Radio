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
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFactory;
import de.p2tools.p2radio.controller.data.station.StationFactory;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.Optional;

public class LastPlayedGuiTableContextMenu {

    private final ProgData progData;
    private final LastPlayedGuiController lastPlayedGuiController;
    private final TablePlayable tableView;

    public LastPlayedGuiTableContextMenu(ProgData progData, LastPlayedGuiController lastPlayedGuiController, TablePlayable tableView) {
        this.progData = progData;
        this.lastPlayedGuiController = lastPlayedGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(Favourite lastPlayed) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, lastPlayed);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Favourite lastPlayed) {
        MenuItem miStart = new MenuItem("Sender starten");
        miStart.setOnAction(a -> lastPlayedGuiController.playStation());
        miStart.setDisable(lastPlayed == null);
        contextMenu.getItems().addAll(miStart);

        Menu mStartStation = startStationWithSet(lastPlayed); // Sender mit Set starten
        if (mStartStation != null) {
            mStartStation.setDisable(lastPlayed == null);
            contextMenu.getItems().add(mStartStation);
        }

        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> lastPlayedGuiController.stopStation(false));
        MenuItem miStopAll = new MenuItem("alle Sender stoppen");
        miStopAll.setOnAction(a -> lastPlayedGuiController.stopStation(true /* alle */));
        MenuItem miCopyUrl = new MenuItem("Sender (URL) kopieren");
        miCopyUrl.setOnAction(a -> lastPlayedGuiController.copyUrl());
        MenuItem miRemove = new MenuItem("Sender aus History löschen");
        miRemove.setOnAction(a -> LastPlayedFactory.deleteHistory(false));

        miStop.setDisable(lastPlayed == null);
        miStopAll.setDisable(lastPlayed == null);
        miCopyUrl.setDisable(lastPlayed == null);
        miRemove.setDisable(lastPlayed == null);
        contextMenu.getItems().addAll(miStop, miStopAll, miCopyUrl, miRemove);

        if (lastPlayed != null) {
            String stationUrl = lastPlayed.getStationUrl();
            Favourite favourite = progData.stationList.getSenderByUrl(stationUrl);
            if (favourite != null) {
                MenuItem miAddFavourite = new MenuItem("Sender als Favoriten speichern");
                miAddFavourite.setOnAction(a -> StationFactory.favouriteStation(favourite));
                miAddFavourite.setDisable(lastPlayed == null);
                contextMenu.getItems().addAll(miAddFavourite);
            }
        }

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> tableView.resetTable());
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }

    private Menu startStationWithSet(Favourite station) {
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
                    final Optional<Favourite> lastPlayed = ProgData.getInstance().lastPlayedGuiController.getSel();
                    if (lastPlayed.isPresent()) {
                        progData.startFactory.playLastPlayed(lastPlayed.get(), setData);
                    }
                });
                submenuSet.getItems().add(item);
            });

            return submenuSet;
        }

        return null;
    }
}
