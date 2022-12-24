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

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
    private final TablePlayable<StationData> tableView;
    private final ProgData progData;

    private final HistoryGuiPack historyGuiPack;

    public HistoryGuiController(HistoryGuiPack historyGuiPack) {
        progData = ProgData.getInstance();
        this.historyGuiPack = historyGuiPack;

        tableView = new TablePlayable(Table.TABLE_ENUM.HISTORY);

        getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        initTable();
        initListener();
    }

//    public void tableRefresh() {
//        PTableFactory.refreshTable(tableView);
////        int i = tableView.getSelectionModel().getSelectedIndex();
////        tableView.refresh();
////        if (i >= 0) {
////            tableView.getSelectionModel().select(i);
////            tableView.scrollTo(i);
////        }
//    }

    public void isShown() {
        tableView.requestFocus();
        setSelectedHistory();
    }

    public int getFavouritesShown() {
        return tableView.getItems().size();
    }

    public void copyUrl() {
        final Optional<StationData> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(favourite.get().getStationUrl());
    }

    private void setSelectedHistory() {
        StationData stationData = tableView.getSelectionModel().getSelectedItem();
        if (stationData != null) {
            StationData fav = progData.stationList.getSenderByUrl(stationData.getStationUrl());
            progData.stationInfoDialogController.setStation(fav);
        }
        historyGuiPack.stationDataObjectPropertyProperty().setValue(stationData);
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> stationData = getSel();
        if (stationData.isPresent()) {
            progData.startFactory.playPlayable(stationData.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.historyList.stream().forEach(stationData -> progData.startFactory.stopPlayable(stationData));

        } else {
            final Optional<StationData> stationData = getSel();
            if (stationData.isPresent()) {
                progData.startFactory.stopPlayable(stationData.get());
            }
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.HISTORY);
    }

    public ArrayList<StationData> getSelList() {
        final ArrayList<StationData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
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
            PAlert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    public void selUrl() {
        final String url = ProgConfig.SYSTEM_HISTORY.getValue();
        Optional<StationData> optional = tableView.getItems().stream().filter(favourite -> favourite.getStationUrl().equals(url)).findFirst();
        if (optional.isPresent()) {
            tableView.getSelectionModel().select(optional.get());
            int sel = tableView.getSelectionModel().getSelectedIndex();
            tableView.scrollTo(sel);
        }
    }

    public void setNextStation() {
        PTableFactory.selectNextRow(tableView);
    }

    public void setPreviousStation() {
        PTableFactory.selectPreviousRow(tableView);
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.REFRESH_TABLE) {
            public void pingGui(PEvent event) {
                PTableFactory.refreshTable(tableView);
            }
        });
        progData.favouriteList.addListener((observable, oldValue, newValue) -> tableView.refresh());
        progData.pEventHandler.addListener(new PListener(Events.SETDATA_CHANGED) {
            public void pingGui(PEvent event) {
                PTableFactory.refreshTable(tableView);
            }
        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                PTableFactory.refreshTable(tableView);
            }
        });
    }


    private void initTable() {
        Table.setTable(tableView);

        SortedList<StationData> sortedHistoryList = new SortedList<>(progData.filteredHistoryList);
        tableView.setItems(sortedHistoryList);
        sortedHistoryList.comparatorProperty().bind(tableView.comparatorProperty());
        Platform.runLater(() -> PTableFactory.refreshTable(tableView));

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<StationData> optionalDownload = getSel(false);
                StationData stationData;
                if (optionalDownload.isPresent()) {
                    stationData = optionalDownload.get();
                } else {
                    stationData = null;
                }
                ContextMenu contextMenu = new HistoryGuiTableContextMenu(progData, this, tableView).
                        getContextMenu(stationData);
                tableView.setContextMenu(contextMenu);
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> setSelectedHistory());
        });
        tableView.getItems().addListener((ListChangeListener<StationData>) c -> {
            if (tableView.getItems().size() == 1) {
                // wenns nur eine Zeile gibt, dann gleich selektieren
                tableView.getSelectionModel().select(0);
            }
        });
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (PTableFactory.SPACE.match(event)) {
                PTableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (PTableFactory.SPACE_SHIFT.match(event)) {
                PTableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }
        });
    }
}
