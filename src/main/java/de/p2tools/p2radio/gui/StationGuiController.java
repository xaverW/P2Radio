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
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationTools;
import de.p2tools.p2radio.gui.tools.table.Table;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.util.ArrayList;
import java.util.Optional;

public class StationGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();

    private final TabPane infoTab = new TabPane();
    private final TilePane tilePaneButton = new TilePane();
    private StationGuiInfoController stationGuiInfoController;
    private final TableView<Station> tableView = new TableView<>();

    private final ProgData progData;
    private boolean bound = false;
    private final SortedList<Station> sortedList;
    private final KeyCombination STRG_A = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
    private final KeyCombination SPACE = new KeyCodeCombination(KeyCode.SPACE);

    DoubleProperty splitPaneProperty = ProgConfig.STATION_GUI_DIVIDER;
    BooleanProperty boolInfoOn = ProgConfig.STATION_GUI_DIVIDER_ON;

    public StationGuiController() {
        progData = ProgData.getInstance();
        sortedList = progData.stationListBlackFiltered.getSortedList();
        sortedList.addListener((ListChangeListener<Station>) c -> {
            selectStation();
        });

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        initInfoPane();
        setInfoPane();
        initTable();
        initListener();
    }

    public void isShown() {
        tableView.requestFocus();
        setStation();
    }

    public void tableRefresh() {
        tableView.refresh();
    }

    public int getStationCount() {
        return tableView.getItems().size();
    }

    public int getSelCount() {
        return tableView.getSelectionModel().getSelectedItems().size();
    }


    private void setStation() {
        Station station = tableView.getSelectionModel().getSelectedItem();
        stationGuiInfoController.setStation(station);
        progData.stationInfoDialogController.setStation(station);
    }

    private void selectStation() {
        Platform.runLater(() -> {
            if ((tableView.getItems().size() == 0)) {
                return;
            }

            Station selStation = tableView.getSelectionModel().getSelectedItem();
            if (selStation != null) {
                tableView.scrollTo(selStation);
            } else {
                tableView.scrollTo(0);
                tableView.getSelectionModel().select(0);
            }
        });
    }

    public void playStation() {
        // Menü/Button Sender (URL) abspielen
        final Optional<Station> stationSelection = getSel();
        if (stationSelection.isPresent()) {
            progData.startFactory.playStation(stationSelection.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.stationList.stream().forEach(f -> progData.startFactory.stopStation(f));

        } else {
            final Optional<Station> station = getSel();
            if (station.isPresent()) {
                progData.startFactory.stopStation(station.get());
            }
        }
    }

    public void playStationWithSet(SetData psetData) {
        //Url mit Prognr. starten
        final Optional<Station> sel = getSel();
        if (!sel.isPresent()) {
            return;
        }

        progData.startFactory.playStation(sel.get(), psetData);
    }

    public void saveStation() {
        final ArrayList<Station> list = getSelList();
        StationTools.saveStation(list);
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE.STATION);
    }

    public void refreshTable() {
        Table.refresh_table(tableView);
    }

    public ArrayList<Station> getSelList() {
        final ArrayList<Station> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<Station> getSel() {
        return getSel(true);
    }

    public Optional<Station> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            if (show) {
                PAlert.showInfoNoSelection();
            }
            return Optional.empty();
        }
    }

    public void selUrl() {
        final String url = ProgConfig.SYSTEM_LAST_PLAYED.getValue();
        Optional<Station> optional = tableView.getItems().stream().
                filter(station -> station.getUrl().equals(url)).findFirst();
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
        progData.setDataList.listChangedProperty().addListener((observable, oldValue, newValue) -> {
            if (progData.setDataList.getSetDataListButton().size() > 1) {
                boolInfoOn.set(true);
            }
            setInfoPane();
        });
    }

    private void initInfoPane() {
        stationGuiInfoController = new StationGuiInfoController();
        boolInfoOn.addListener((observable, oldValue, newValue) -> setInfoPane());

        tilePaneButton.setVgap(15);
        tilePaneButton.setHgap(15);
        tilePaneButton.setPadding(new Insets(10));
        tilePaneButton.setStyle("-fx-border-color: -fx-text-box-border; " +
                "-fx-border-radius: 5px; " +
                "-fx-border-width: 1;");
    }

    private void setInfoPane() {
        if (boolInfoOn.getValue()) {
            bound = true;
            setInfoTabPane();
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);

        } else {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(splitPaneProperty);
            }

            if (splitPane.getItems().size() != 1) {
                splitPane.getItems().clear();
                splitPane.getItems().add(scrollPane);
            }
        }
    }

    private void setInfoTabPane() {
        if (splitPane.getItems().size() != 2 || splitPane.getItems().get(1) != stationGuiInfoController) {
            splitPane.getItems().clear();
            splitPane.getItems().addAll(scrollPane, stationGuiInfoController);
            SplitPane.setResizableWithParent(stationGuiInfoController, false);
        }
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);

        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        new Table().setTable(tableView, Table.TABLE.STATION);

        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                progData.stationInfoDialogController.showStationInfo();
            }
        });

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<Station> optionalStation = getSel(false);
                Station station;
                if (optionalStation.isPresent()) {
                    station = optionalStation.get();
                } else {
                    station = null;
                }
                ContextMenu contextMenu = new StationGuiTableContextMenu(progData, this, tableView).getContextMenu(station);
                tableView.setContextMenu(contextMenu);
            }
        });

        tableView.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (SPACE.match(event)) {
                PTableFactory.scrollVisibleRange(tableView);
                event.consume();
            }

            if (STRG_A.match(event)) {
                if (tableView.getItems().size() > 3_000) {
                    // bei sehr langen Listen dauert das seeeeeehr lange
                    PLog.sysLog("STRG-A: lange Liste -> verhindern");
                    event.consume();
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(this::setStation)
        );

    }
}
