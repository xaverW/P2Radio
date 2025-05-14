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
import de.p2tools.p2lib.guitools.P2RowFactory;
import de.p2tools.p2lib.guitools.P2TableFactory;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TableRowStation;
import de.p2tools.p2radio.gui.tools.table.TableStation;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class StationGuiController extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();

    private final TableStation tableView;
    private final ProgData progData;
    private final KeyCombination STRG_A = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
    private final KeyCombination SPACE = new KeyCodeCombination(KeyCode.SPACE);

    private final StationGuiPack stationGuiPack;

    public StationGuiController(StationGuiPack stationGuiPack) {
        progData = ProgData.getInstance();
        this.stationGuiPack = stationGuiPack;

        tableView = new TableStation(Table.TABLE_ENUM.STATION);

        getChildren().addAll(scrollPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        initTable();
        initListener();
    }

    public void isShown() {
        tableView.requestFocus();
        setStation(tableView.getSelectionModel().getSelectedItem());
    }

    public int getStationCount() {
        return tableView.getItems().size();
    }

    private void setStation(StationData station) {
        progData.stationInfoDialogController.setStation(station);
        stationGuiPack.stationDataObjectPropertyProperty().setValue(station);
    }

    public void playStation() {
        // Menü/Button: Sender (URL) abspielen
        final Optional<StationData> stationSelection = getSel();
        stationSelection.ifPresent(StartFactory::startStation);
    }

    public void playStationWithSet(SetData psetData) {
        final Optional<StationData> sel = getSel();
        if (sel.isEmpty()) {
            return;
        }

        StartFactory.startStation(sel.get(), psetData);
    }


    public void playRandomStation() {
        if (tableView.getItems().isEmpty()) {
            return;
        }

        int rInt = new Random().nextInt(tableView.getItems().size());
        StationData station = tableView.getItems().get(rInt);
        if (station != null) {
            tableView.getSelectionModel().select(station);
            tableView.scrollTo(station);
            StartFactory.startStation(station);
        }
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE_ENUM.STATION);
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
        } else {
            if (show) {
                P2Alert.showInfoNoSelection();
            }
            return Optional.empty();
        }
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

    private void selectStation() {
        Platform.runLater(() -> {
            if ((tableView.getItems().isEmpty())) {
                return;
            }

            StationData selStation = tableView.getSelectionModel().getSelectedItem();
            if (selStation != null) {
                tableView.scrollTo(selStation);
            } else {
                tableView.scrollTo(0);
                tableView.getSelectionModel().select(0);
            }
        });
    }

    private void initListener() {
        progData.favouriteList.addListener((observable, oldValue, newValue) -> P2TableFactory.refreshTable(tableView));
        progData.stationListBlackFiltered.getSortedList().addListener((ListChangeListener<StationData>) c -> {
            selectStation();
        });
    }

    private void initTable() {
        Table.setTable(tableView);
        tableView.setTableMenuButtonVisible(true);

        SortedList<StationData> sortedList = progData.stationListBlackFiltered.getSortedList();
        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());


        tableView.setRowFactory(new P2RowFactory<>(tableView -> {
            TableRowStation<StationData> row = new TableRowStation<>(Table.TABLE_ENUM.STATION);
            row.hoverProperty().addListener((observable) -> {
                final StationData stationData = row.getItem();
                if (row.isHover() && stationData != null) { // null bei den leeren Zeilen unterhalb
                    setStation(stationData);
                } else if (stationData == null) {
                    setStation(tableView.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        }));
        tableView.hoverProperty().addListener((o) -> {
            if (!tableView.isHover()) {
                setStation(tableView.getSelectionModel().getSelectedItem());
            }
        });


        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                progData.stationInfoDialogController.showStationInfo();
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<StationData> optionalStation = getSel(false);
                StationData station;
                station = optionalStation.orElse(null);
                ContextMenu contextMenu = new TableContextMenu(progData, tableView, TableContextMenu.STATION)
                        .getContextMenu(station);
                tableView.setContextMenu(contextMenu);
            }
        });

        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (SPACE.match(event)) {
                P2TableFactory.scrollVisibleRangeDown(tableView);
                event.consume();
            }
            if (P2TableFactory.SPACE_SHIFT.match(event)) {
                P2TableFactory.scrollVisibleRangeUp(tableView);
                event.consume();
            }

            if (STRG_A.match(event)) {
                if (tableView.getItems().size() > 3_000) {
                    // bei sehr langen Listen dauert das seeeeeehr lange
                    P2Log.sysLog("STRG-A: lange Liste -> verhindern");
                    event.consume();
                }
            }
        });
    }
}
