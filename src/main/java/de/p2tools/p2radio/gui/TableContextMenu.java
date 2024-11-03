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

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.P2SystemUtils;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.history.HistoryFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.TableStation;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.Optional;

public class TableContextMenu {

    public static int STATION = 0;
    public static int FAVOURITE = 1;
    public static int HISTORY = 2;
    public static int SMALL_STATION = 3;
    public static int SMALL_FAVOURITE = 4;
    public static int SMALL_HISTORY = 5;
    private int forWhat = STATION;

    private final ProgData progData;
    private final TableStation tableView;

    public TableContextMenu(ProgData progData, TableStation tableView, int forWhat) {
        this.progData = progData;
        this.tableView = tableView;
        this.forWhat = forWhat;
    }

    public ContextMenu getContextMenu(StationData station) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, station);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, StationData station) {
        // Start/Save
        MenuItem miStart = new MenuItem("Sender abspielen");
        miStart.setOnAction(a -> playStation());
        miStart.setDisable(station == null);
        contextMenu.getItems().addAll(miStart);

        Menu mStartStation = startStationWithSet(station); // Sender mit Set starten
        if (mStartStation != null) {
            contextMenu.getItems().add(mStartStation);
            mStartStation.setDisable(station == null);
        }

        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> StartFactory.stopRunningStation());
        miStop.setDisable(station == null);
        contextMenu.getItems().addAll(miStop);

        if (forWhat == STATION) {
            Menu mFilter = addFilter(station);// Filter
            Menu mBlacklist = addBlacklist(station);// Blacklist
            contextMenu.getItems().addAll(new SeparatorMenuItem(), mFilter, mBlacklist);
        }

        contextMenu.getItems().add(new SeparatorMenuItem());

        MenuItem miUrl = new MenuItem("Sender-URL kopieren");
        miUrl.setOnAction(a -> P2SystemUtils.copyToClipboard(station.getStationUrl()));
        miUrl.setDisable(station == null);
        contextMenu.getItems().addAll(miUrl);

        if (forWhat == STATION) {
            MenuItem miSave = new MenuItem("Sender als Favoriten speichern");
            miSave.setOnAction(a -> FavouriteFactory.favouriteStationList());
            miSave.setDisable(station == null);
            contextMenu.getItems().addAll(miSave);
            MenuItem miStationInfo = new MenuItem("Senderinformation anzeigen");
            miStationInfo.setOnAction(a -> progData.stationInfoDialogController.showStationInfo());
            miStationInfo.setDisable(station == null);
            contextMenu.getItems().addAll(miStationInfo);
        }

        if (forWhat == SMALL_STATION) {
            MenuItem miSave = new MenuItem("Sender als Favoriten speichern");
            miSave.setOnAction(a -> FavouriteFactory.favouriteStation(station));
            miSave.setDisable(station == null);
            contextMenu.getItems().addAll(miSave);
        }

        if (forWhat == FAVOURITE) {
            MenuItem miChange = new MenuItem("Favorit ändern");
            miChange.setOnAction(a -> FavouriteFactory.changeFavourite(false));
            miChange.setDisable(station == null);

            MenuItem miRemove = new MenuItem("Favoriten löschen");
            miRemove.setOnAction(a -> FavouriteFactory.deleteFavourite(false));
            miRemove.setDisable(station == null);

            contextMenu.getItems().addAll(miChange, miRemove);
        }

        if (forWhat == SMALL_FAVOURITE) {
            MenuItem miChange = new MenuItem("Favorit ändern");
            miChange.setOnAction(a -> FavouriteFactory.changeFavourite(station));
            miChange.setDisable(station == null);

            MenuItem miRemove = new MenuItem("Favoriten löschen");
            miRemove.setOnAction(a -> FavouriteFactory.deleteFavourite(station));
            miRemove.setDisable(station == null);

            contextMenu.getItems().addAll(miChange, miRemove);
        }

        if (forWhat == HISTORY || forWhat == SMALL_HISTORY) {
            MenuItem miRemove = new MenuItem("Sender aus History löschen");
            miRemove.setOnAction(a -> HistoryFactory.deleteHistory(station));
            miRemove.setDisable(station == null);
            contextMenu.getItems().addAll(miRemove);
        }

        if (forWhat == HISTORY || forWhat == SMALL_HISTORY) {
            if (station != null) {
                String stationUrl = station.getStationUrl();
                StationData stationData = progData.stationList.getStationByUrl(stationUrl);
                if (stationData != null) {
                    MenuItem miAddFavourite = new MenuItem("Sender als Favoriten speichern");
                    miAddFavourite.setOnAction(a -> FavouriteFactory.favouriteStation(stationData));
                    contextMenu.getItems().addAll(miAddFavourite);
                }
            }
        }

        final MenuItem miAutoStart = new MenuItem("Sender als AutoStart auswählen");
        miAutoStart.setOnAction(e -> AutoStartFactory.setAutoStart(station));
        miAutoStart.setDisable(station == null);
        contextMenu.getItems().addAll(miAutoStart);

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
            Menu submenuSet = new Menu("Sender mit Programm abspielen");

            if (station == null) {
                submenuSet.setDisable(true);
                return submenuSet;
            }

            list.forEach(setData -> {
                final MenuItem item = new MenuItem(setData.getVisibleName());
                item.setOnAction(event -> playStationWithSet(setData));
                submenuSet.getItems().add(item);
            });

            return submenuSet;
        }

        return null;
    }

    private Optional<StationData> getSel() {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            P2Alert.showInfoNoSelection();
            return Optional.empty();
        }
    }

    private void playStation() {
        // Menü/Button: Sender (URL) abspielen
        final Optional<StationData> stationSelection = getSel();
        stationSelection.ifPresent(StartFactory::playPlayable);
    }

    public void playStationWithSet(SetData psetData) {
        final Optional<StationData> sel = getSel();
        if (sel.isEmpty()) {
            return;
        }

        StartFactory.playPlayable(sel.get(), psetData);
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
