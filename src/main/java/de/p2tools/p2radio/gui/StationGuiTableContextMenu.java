/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.gui.tools.table.Table;
import javafx.scene.control.*;

public class StationGuiTableContextMenu {

    private final ProgData progData;
    private final StationGuiController stationGuiController;
    private final TableView tableView;

    public StationGuiTableContextMenu(ProgData progData, StationGuiController stationGuiController, TableView tableView) {
        this.progData = progData;
        this.stationGuiController = stationGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(Station station) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, station);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Station station) {
        // Start/Save
        MenuItem miStart = new MenuItem("Sender abspielen");
        miStart.setOnAction(a -> stationGuiController.playStationUrl());
        MenuItem miSave = new MenuItem("Sender speichern");
        miSave.setOnAction(a -> stationGuiController.saveStation());
        contextMenu.getItems().addAll(miStart, miSave);

        miStart.setDisable(station == null);
        miSave.setDisable(station == null);

        Menu mFilter = addFilter(station);// Filter
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(mFilter);

        Menu mStartStation = startStationWithSet(station); // Sender mit Set starten
        if (mStartStation != null) {
            contextMenu.getItems().add(mStartStation);
        }

        Menu mBlacklist = addBlacklist(station);// Blacklist
        Menu mCopyUrl = copyUrl(station);// URL kopieren
        contextMenu.getItems().addAll(mBlacklist, /*mBookmark,*/ mCopyUrl);

        MenuItem miStationInfo = new MenuItem("Senderinformation anzeigen");
        miStationInfo.setOnAction(a -> stationGuiController.showStationInfo());
        miStationInfo.setDisable(station == null);

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(miStationInfo);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.STATION));
        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }

    private Menu addFilter(Station station) {
        Menu submenuFilter = new Menu("Filter");
        if (station == null) {
            submenuFilter.setDisable(true);
            return submenuFilter;
        }

        final MenuItem miFilterStationName = new MenuItem("nach Sendername filtern");
        miFilterStationName.setOnAction(event -> progData.storedFilters.getActFilterSettings().setNameAndVis(station.getName()));

        final MenuItem miFilterGenre = new MenuItem("nach Genre filtern");
        miFilterGenre.setOnAction(event -> progData.storedFilters.getActFilterSettings().setGenreAndVis(station.getGenre()));

        final MenuItem miFilterCodec = new MenuItem("nach Codec filtern");
        miFilterCodec.setOnAction(event -> progData.storedFilters.getActFilterSettings().setCodecAndVis(station.getCodec()));

        submenuFilter.getItems().addAll(miFilterStationName, miFilterGenre, miFilterCodec);
        return submenuFilter;
    }

    private Menu startStationWithSet(Station station) {
        final SetDataList list = progData.setDataList.getSetDataListButton();
        if (!list.isEmpty()) {
            Menu submenuSet = new Menu("Sender mit Set abspielen");

            if (station == null) {
                submenuSet.setDisable(true);
                return submenuSet;
            }

            list.stream().forEach(setData -> {
                final MenuItem item = new MenuItem(setData.getVisibleName());
                item.setOnAction(event -> stationGuiController.playStationUrlWithSet(setData));
                submenuSet.getItems().add(item);
            });

            return submenuSet;
        }

        return null;
    }

    private Menu addBlacklist(Station station) {
        Menu submenuBlacklist = new Menu("Blacklist");
        if (station == null) {
            submenuBlacklist.setDisable(true);
            return submenuBlacklist;
        }

        final MenuItem miBlackChannel = new MenuItem("Sendername in die Blacklist einfügen");
        miBlackChannel.setOnAction(event -> progData.blackDataList.addAndNotify(new BlackData(station.getName(), "")));
        final MenuItem miBlackTheme = new MenuItem("Genre in die Blacklist einfügen");
        miBlackTheme.setOnAction(event -> progData.blackDataList.addAndNotify(new BlackData("", station.getGenre())));
        final MenuItem miBlackChannelTheme = new MenuItem("Sendername und Genre in die Blacklist einfügen");
        miBlackChannelTheme.setOnAction(event -> progData.blackDataList.addAndNotify(new BlackData(station.getName(), station.getGenre())));

        submenuBlacklist.getItems().addAll(miBlackChannel, miBlackTheme, miBlackChannelTheme);
        return submenuBlacklist;
    }

    private Menu copyUrl(Station station) {
        final Menu subMenuURL = new Menu("Sender-URL kopieren");
        if (station == null) {
            subMenuURL.setDisable(true);
            return subMenuURL;
        }

        final String uNormal = station.getUrl();
        MenuItem item;
        item = new MenuItem("Sender-URL kopieren");
        item.setOnAction(a -> PSystemUtils.copyToClipboard(station.getUrl()));
        subMenuURL.getItems().add(item);

        return subMenuURL;
    }
}
