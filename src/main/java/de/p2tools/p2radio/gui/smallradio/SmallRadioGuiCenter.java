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

package de.p2tools.p2radio.gui.smallradio;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.guitools.ptable.P2TableFactory;
import de.p2tools.p2lib.guitools.table.P2RowMoveFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.filter.FilterFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.TableContextMenu;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TableRowStation;
import de.p2tools.p2radio.gui.tools.table.TableStation;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class SmallRadioGuiCenter extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableStation tableViewStation;
    private final TableStation tableViewFavourite;
    private final TableStation tableViewHistory;
    private final ProgData progData;
    private final SmallRadioGuiController smallRadioGuiController;
    private TableStation tableView;
    private FilteredList<StationData> filteredListStation;
    private FilteredList<StationData> filteredListFavourite;
    private FilteredList<StationData> filteredListHistory;

    public SmallRadioGuiCenter(SmallRadioGuiController smallRadioGuiController) {
        this.progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        tableViewStation = new TableStation(Table.TABLE_ENUM.SMALL_RADIO_STATION);
        tableViewFavourite = new TableStation(Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE);
        tableViewHistory = new TableStation(Table.TABLE_ENUM.SMALL_RADIO_HISTORY);
        tableView = tableViewStation;
        make();
        initTable();
        initListener();
    }

    private void make() {
        setSpacing(P2LibConst.DIST_BUTTON);
        setPadding(new Insets(0, 10, 0, 10));
        setAlignment(Pos.CENTER);

        this.getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);
    }

    public P2MaskerPane getMaskerPane() {
        return smallRadioGuiController.getMaskerPane();
    }

    private void tableRefresh() {
        P2TableFactory.refreshTable(tableViewStation);
        P2TableFactory.refreshTable(tableViewFavourite);
        P2TableFactory.refreshTable(tableViewHistory);
    }

    public void isShown() {
        tableView.requestFocus();
        setInfoSelected(tableView.getSelectionModel().getSelectedItem());
    }

    private void setInfoSelected(StationData stationData) {
        progData.stationInfoDialogController.setStation(stationData);
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> favourite = getSel();
        favourite.ifPresent(StartFactory::startStation);
    }

    public void saveTable() {
        Table.saveTable(tableViewStation, Table.TABLE_ENUM.SMALL_RADIO_STATION);
        Table.saveTable(tableViewFavourite, Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE);
        Table.saveTable(tableViewHistory, Table.TABLE_ENUM.SMALL_RADIO_HISTORY);
    }

    public Optional<StationData> getSel() {
        return getSel(true);
    }

    public Optional<StationData> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        }

        if (show) {
            P2Alert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    public ArrayList<StationData> getSelList() {
        final ArrayList<StationData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            P2Alert.showInfoNoSelection();
        }
        return ret;
    }

    public void selectPlayingStation() {
        StartFactory.selectPlayingStation(tableView);
    }

    public void selLastHistory() {
        P2RadioFactory.selLastHistory(tableViewStation);
        P2RadioFactory.selLastHistory(tableViewFavourite);
        P2RadioFactory.selLastHistory(tableViewHistory);
    }

    public void setNextStation() {
        P2TableFactory.selectNextRow(tableView);
    }

    public void setPreviousStation() {
        P2TableFactory.selectPreviousRow(tableView);
    }

    public void playRandomStation() {
        Random r = new Random();
        StationData stationData = tableView.getItems().get(r.nextInt(tableView.getItems().size()));
        tableView.getSelectionModel().clearSelection();
        if (stationData != null) {
            tableView.getSelectionModel().select(stationData);
            tableView.scrollTo(stationData);
            StartFactory.startStation(stationData);
        }
    }

    private void initListener() {
        progData.pEventHandler.addListener(new P2Listener(PEvents.SETDATA_CHANGED) {
            public void pingGui(P2Event event) {
                tableRefresh();
            }
        });
        ProgConfig.SMALL_RADIO_SELECTED_LIST.addListener((observable, oldValue, newValue) -> {
            loadTable();
        });

        // station
        ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
        // favourite
        ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
        ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
        // history
        ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
    }

    private void setFilter() {
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
            filteredListStation.setPredicate(FilterFactory.getStationPredicateSmallGui());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
            filteredListFavourite.setPredicate(FilterFactory.getFavoritePredicateSmallGui());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_HISTORY)) {
            filteredListHistory.setPredicate(FilterFactory.getHistoryPredicateSmallGui());
        }
    }

    private void initTable() {
        Table.setTable(tableViewStation);
        Table.setTable(tableViewFavourite);
        Table.setTable(tableViewHistory);

        tableViewStation.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewFavourite.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewHistory.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        filteredListStation = new FilteredList<>(progData.stationList, p -> true);
        SortedList<StationData> sortedList = new SortedList<>(filteredListStation);
        tableViewStation.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableViewStation.comparatorProperty());

        filteredListFavourite = new FilteredList<>(progData.favouriteList, p -> true);
        sortedList = new SortedList<>(filteredListFavourite);
        tableViewFavourite.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableViewFavourite.comparatorProperty());

        filteredListHistory = new FilteredList<>(progData.historyList, p -> true);
        sortedList = new SortedList<>(filteredListHistory);
        tableViewHistory.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableViewHistory.comparatorProperty());

        addTableListener(tableViewStation, TableContextMenu.SMALL_STATION, Table.TABLE_ENUM.SMALL_RADIO_STATION);
        addTableListener(tableViewFavourite, TableContextMenu.SMALL_FAVOURITE, Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE);
        addTableListener(tableViewHistory, TableContextMenu.SMALL_HISTORY, Table.TABLE_ENUM.SMALL_RADIO_HISTORY);

        loadTable();
    }

    private void loadTable() {
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
            tableView = tableViewStation;
            scrollPane.setContent(tableView);

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
            tableView = tableViewFavourite;
            scrollPane.setContent(tableView);

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_HISTORY)) {
            tableView = tableViewHistory;
            scrollPane.setContent(tableView);
        }

        tableRefresh();
        setFilter();
        setInfoSelected(tableView.getSelectionModel().getSelectedItem());
    }

    private void addTableListener(TableStation tableView, int forWhat, Table.TABLE_ENUM tableEnum) {
        // nur eine Zeile kann markiert werden!
        tableView.setRowFactory(new P2RowMoveFactory<>(tv -> {
            TableRowStation<StationData> row = new TableRowStation<>(tableEnum);
            row.hoverProperty().addListener((observable) -> {
                final StationData stationData = row.getItem();
                if (row.isHover() && stationData != null) { // null bei den leeren Zeilen unterhalb
                    setInfoSelected(stationData);
                } else if (stationData == null) {
                    setInfoSelected(tv.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        }, (tableEnum.equals(Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE) ? progData.favouriteList : null)));

        tableView.hoverProperty().addListener((o) -> {
            if (!tableView.isHover()) {
                setInfoSelected(tableView.getSelectionModel().getSelectedItem());
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                //wird auch durch FilmlistenUpdate ausgelöst
                Platform.runLater(() -> {
                    setInfoSelected(tableView.getSelectionModel().getSelectedItem());
                }));

        if (tableEnum == Table.TABLE_ENUM.SMALL_RADIO_STATION ||
                tableEnum == Table.TABLE_ENUM.SMALL_RADIO_HISTORY) {
            tableView.setOnMouseClicked(m -> {
                if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                    progData.stationInfoDialogController.showStationInfo();
                }
            });
        } else if (tableEnum == Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE) {
            tableView.setOnMouseClicked(m -> {
                if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                    Optional<StationData> stationData = getSel(true);
                    stationData.ifPresent(FavouriteFactory::changeFavourite);
                }
            });
        }

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                StationData stationData = getSel(false).orElse(null);
                ContextMenu contextMenu = new TableContextMenu(progData, tableView, forWhat)
                        .getContextMenu(stationData);
                tableView.setContextMenu(contextMenu);
            }
        });

        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (P2TableFactory.SPACE.match(event)) {
                P2TableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (P2TableFactory.SPACE_SHIFT.match(event)) {
                P2TableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }
        });
    }
}
