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
import de.p2tools.p2lib.guitools.P2RowMoveFactory;
import de.p2tools.p2lib.guitools.P2TableFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TableRowStation;
import de.p2tools.p2radio.gui.tools.table.TableStation;
import javafx.application.Platform;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class HistoryGuiController extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableStation tableView;
    private final ProgData progData;

    private final HistoryGuiPack historyGuiPack;

    public HistoryGuiController(HistoryGuiPack historyGuiPack) {
        progData = ProgData.getInstance();
        this.historyGuiPack = historyGuiPack;

        tableView = new TableStation(Table.TABLE_ENUM.HISTORY);

        getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        initTable();
        initListener();
    }

    public void isShown() {
        tableView.requestFocus();
        setSelectedHistory(tableView.getSelectionModel().getSelectedItem());
    }

    public int getHistoryShown() {
        return tableView.getItems().size();
    }

    public void copyUrl() {
        final Optional<StationData> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        P2ToolsFactory.copyToClipboard(favourite.get().getStationUrl());
    }

    private void setSelectedHistory(StationData stationData) {
        if (stationData != null) {
            StationData fav = progData.stationList.getStationByUrl(stationData.getStationUrl());
            progData.stationInfoDialogController.setStation(fav);
        }
        historyGuiPack.stationDataObjectPropertyProperty().setValue(stationData);
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> stationData = getSel();
        if (stationData.isPresent()) {
            StartFactory.startStation(stationData.get());
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.HISTORY);
    }

    public ArrayList<StationData> getSelList() {
        final ArrayList<StationData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            P2Alert.showInfoNoSelection();
        }
        return ret;
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

    public void selLastHistory() {
        P2RadioFactory.selLastHistory(tableView);
    }

    public void setNextStation() {
        P2TableFactory.selectNextRow(tableView);
    }

    public void setPreviousStation() {
        P2TableFactory.selectPreviousRow(tableView);
    }

    private void initListener() {
        progData.favouriteList.addListener((observable, oldValue, newValue) -> tableView.refresh());
        progData.pEventHandler.addListener(new P2Listener(PEvents.SETDATA_CHANGED) {
            public void pingGui(P2Event event) {
                P2TableFactory.refreshTable(tableView);
            }
        });
    }

    private void initTable() {
        Table.setTable(tableView);

        SortedList<StationData> sortedHistoryList = new SortedList<>(progData.filteredHistoryList);
        tableView.setItems(sortedHistoryList);
        sortedHistoryList.comparatorProperty().bind(tableView.comparatorProperty());
        Platform.runLater(() -> P2TableFactory.refreshTable(tableView));

        tableView.setRowFactory(new P2RowMoveFactory<>(tv -> {
            TableRowStation<StationData> row = new TableRowStation<>(Table.TABLE_ENUM.STATION);
            row.hoverProperty().addListener((observable) -> {
                final StationData stationData = row.getItem();
                if (row.isHover() && stationData != null) { // null bei den leeren Zeilen unterhalb
                    setSelectedHistory(stationData);
                } else if (stationData == null) {
                    setSelectedHistory(tv.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        }));

        tableView.hoverProperty().addListener((o) -> {
            if (!tableView.isHover()) {
                setSelectedHistory(tableView.getSelectionModel().getSelectedItem());
            }
        });

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                progData.stationInfoDialogController.showStationInfo();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<StationData> optionalDownload = getSel(false);
                StationData stationData;
                stationData = optionalDownload.orElse(null);
                ContextMenu contextMenu = new TableContextMenu(progData, tableView,
                        TableContextMenu.HISTORY).getContextMenu(stationData);
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
