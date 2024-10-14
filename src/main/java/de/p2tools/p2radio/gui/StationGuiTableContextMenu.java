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

import de.p2tools.p2lib.tools.P2SystemUtils;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class StationGuiTableContextMenu {

    private final ProgData progData;
    private final StationGuiController stationGuiController;
    private final TablePlayable tableView;

    public StationGuiTableContextMenu(ProgData progData, StationGuiController stationGuiController, TablePlayable tableView) {
        this.progData = progData;
        this.stationGuiController = stationGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(StationData station) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, station);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, StationData station) {
        // Start/Save
        MenuItem miStart = new MenuItem("Sender abspielen");
        miStart.setOnAction(a -> stationGuiController.playStation());
        miStart.setDisable(station == null);
        contextMenu.getItems().addAll(miStart);

        Menu mStartStation = startStationWithSet(station); // Sender mit Set starten
        if (mStartStation != null) {
            contextMenu.getItems().add(mStartStation);
            mStartStation.setDisable(station == null);
        }

        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> stationGuiController.stopStation(false));
        miStop.setDisable(station == null);

        MenuItem miStopAll = new MenuItem("Alle Sender stoppen");
        miStopAll.setOnAction(a -> stationGuiController.stopStation(true /* alle */));
        miStopAll.setDisable(station == null);

        contextMenu.getItems().addAll(miStop, miStopAll);

        Menu mFilter = addFilter(station);// Filter
        Menu mBlacklist = addBlacklist(station);// Blacklist

        contextMenu.getItems().addAll(new SeparatorMenuItem(), mFilter, mBlacklist);
        contextMenu.getItems().add(new SeparatorMenuItem());

        MenuItem miUrl = new MenuItem("Sender-URL kopieren");
        miUrl.setOnAction(a -> P2SystemUtils.copyToClipboard(station.getStationUrl()));
        miUrl.setDisable(station == null);
        contextMenu.getItems().addAll(miUrl);

        MenuItem miSave = new MenuItem("Sender als Favoriten speichern");
        miSave.setOnAction(a -> FavouriteFactory.favouriteStationList());
        miSave.setDisable(station == null);
        contextMenu.getItems().addAll(miSave);

        final MenuItem miAutoStart = new MenuItem("Sender als AutoStart auswählen");
        miAutoStart.setOnAction(e -> AutoStartFactory.setStationAutoStart());
        miAutoStart.setDisable(station == null);
        contextMenu.getItems().addAll(miAutoStart);

        MenuItem miStationInfo = new MenuItem("Senderinformation anzeigen");
        miStationInfo.setOnAction(a -> progData.stationInfoDialogController.showStationInfo());
        miStationInfo.setDisable(station == null);
        contextMenu.getItems().addAll(miStationInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> tableView.resetTable());
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }

    private Menu addFilter(StationData station) {
        Menu submenuFilter = new Menu("Filter");
        if (station == null) {
            submenuFilter.setDisable(true);
            return submenuFilter;
        }

        final MenuItem miFilterStationName = new MenuItem("nach Sendername filtern");
        miFilterStationName.setOnAction(event -> progData.storedFilters.getActFilterSettings().setNameAndVis(station.getStationName()));

        final MenuItem miFilterGenre = new MenuItem("nach Genre filtern");
        miFilterGenre.setOnAction(event -> progData.storedFilters.getActFilterSettings().setGenreAndVis(station.getGenre()));

        final MenuItem miFilterCodec = new MenuItem("nach Codec filtern");
        miFilterCodec.setOnAction(event -> progData.storedFilters.getActFilterSettings().setCodecAndVis(station.getCodec()));

        submenuFilter.getItems().addAll(miFilterStationName, miFilterGenre, miFilterCodec);
        return submenuFilter;
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
                item.setOnAction(event -> stationGuiController.playStationWithSet(setData));
                submenuSet.getItems().add(item);
            });

            return submenuSet;
        }

        return null;
    }

    private Menu addBlacklist(StationData station) {
        Menu submenuBlacklist = new Menu("Blacklist");
        if (station == null) {
            submenuBlacklist.setDisable(true);
            return submenuBlacklist;
        }

        final MenuItem miBlackChannel = new MenuItem("Sendername in die Blacklist einfügen");
        miBlackChannel.setOnAction(event -> progData.blackDataList.addAndNotify(new BlackData(station.getStationName(), "")));
        final MenuItem miBlackTheme = new MenuItem("Genre in die Blacklist einfügen");
        miBlackTheme.setOnAction(event -> progData.blackDataList.addAndNotify(new BlackData("", station.getGenre())));
        final MenuItem miBlackChannelTheme = new MenuItem("Sendername und Genre in die Blacklist einfügen");
        miBlackChannelTheme.setOnAction(event -> progData.blackDataList.addAndNotify(new BlackData(station.getStationName(), station.getGenre())));

        submenuBlacklist.getItems().addAll(miBlackChannel, miBlackTheme, miBlackChannelTheme);
        return submenuBlacklist;
    }
}
