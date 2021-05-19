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
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFilter;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.gui.tools.Listener;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.tools.storedFilter.FilterCheckRegEx;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class LastPlayedGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vBox = new VBox(0);
    private final ScrollPane scrollPane = new ScrollPane();

    private final TableView<LastPlayed> tableView = new TableView<>();
    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final Button btnReset = new Button();

    private LastPlayedGuiInfoController favouriteGuiInfoController;
    private LastPlayedFilter lastPlayedFilter = new LastPlayedFilter();

    private final ProgData progData;
    private boolean bound = false;
    private final FilteredList<LastPlayed> filteredLastPlayedList;
    private final SortedList<LastPlayed> sortedLastPlayedList;

    DoubleProperty splitPaneProperty = ProgConfig.LAST_PLAYED_GUI_DIVIDER;
    BooleanProperty boolInfoOn = ProgConfig.LAST_PLAYED_GUI_DIVIDER_ON;

    public LastPlayedGuiController() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        HBox hb = new HBox(10);
        hb.setPadding(new Insets(5));
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().addAll(new Label("Genre"), cboGenre);

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(hb, Priority.ALWAYS);
        hBox.getChildren().addAll(hb, btnReset);

        vBox.getChildren().addAll(hBox, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        boolInfoOn.addListener((observable, oldValue, newValue) -> setInfoPane());
        favouriteGuiInfoController = new LastPlayedGuiInfoController();
        filteredLastPlayedList = new FilteredList<>(progData.lastPlayedList, p -> true);
        sortedLastPlayedList = new SortedList<>(filteredLastPlayedList);

        setInfoPane();
        initFilter();
        initTable();
        initListener();
    }

    public void tableRefresh() {
        tableView.refresh();
    }

    public void isShown() {
        tableView.requestFocus();
        setSelectedFavourite();
    }

    public int getFavouritesShown() {
        return tableView.getItems().size();
    }

    public void copyUrl() {
        final Optional<LastPlayed> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(favourite.get().getUrl());
    }

    private void setSelectedFavourite() {
        LastPlayed favourite = tableView.getSelectionModel().getSelectedItem();
        if (favourite != null) {
            favouriteGuiInfoController.setLastPlayed(favourite);
            Station station = progData.stationList.getSenderByUrl(favourite.getUrl());
            progData.stationInfoDialogController.setStation(station);
        } else {
            favouriteGuiInfoController.setLastPlayed(null);
        }
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<LastPlayed> lastPlayed = getSel();
        if (lastPlayed.isPresent()) {
            progData.startFactory.playLastPlayed(lastPlayed.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.lastPlayedList.stream().forEach(lastPlayed -> progData.startFactory.stopLastPlayed(lastPlayed));

        } else {
            final Optional<LastPlayed> lastPlayed = getSel();
            if (lastPlayed.isPresent()) {
                progData.startFactory.stopLastPlayed(lastPlayed.get());
            }
        }
    }

    public void deleteHistory(boolean all) {
        if (all) {
            final ArrayList<LastPlayed> list = getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Sender aus der History gelöscht werden?";
            } else {
                text = "Sollen die Sender aus der History gelöscht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage,
                    "History löschen?", "History löschen?", text).equals(PAlert.BUTTON.YES)) {
                progData.lastPlayedList.removeAll(list);
//                StationListFactory.findAndMarkFavouriteStations(progData);
            }

        } else {
            final Optional<LastPlayed> favourite = getSel();
            if (favourite.isPresent()) {
                deleteHistory(favourite.get());
            }
        }
    }

    public void deleteHistory(LastPlayed favourite) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "History löschen?", "History löschen?",
                "Soll der Sender aus der History gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            progData.lastPlayedList.remove(favourite);
//            StationListFactory.findAndMarkFavouriteStations(progData);
        }
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE.LAST_PLAYED);
    }

    public ArrayList<LastPlayed> getSelList() {
        final ArrayList<LastPlayed> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<LastPlayed> getSel() {
        return getSel(true);
    }

    public Optional<LastPlayed> getSel(boolean show) {
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
        final String url = ProgConfig.SYSTEM_LAST_PLAYED.getValue();
        Optional<LastPlayed> optional = tableView.getItems().stream().filter(favourite -> favourite.getUrl().equals(url)).findFirst();
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
        Listener.addListener(new Listener(Listener.EREIGNIS_SETDATA_CHANGED, LastPlayedGuiController.class.getSimpleName()) {
            @Override
            public void pingFx() {
                tableView.refresh();
            }
        });
    }

    private void setInfoPane() {
        if (!boolInfoOn.getValue()) {
            if (bound) {
                splitPane.getDividers().get(0).positionProperty().unbindBidirectional(splitPaneProperty);
            }
            splitPane.getItems().clear();
            splitPane.getItems().add(vBox);
        } else {
            bound = true;
            splitPane.getItems().clear();
            splitPane.getItems().addAll(vBox, favouriteGuiInfoController);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);
            SplitPane.setResizableWithParent(vBox, true);
        }
    }

    private void initFilter() {
        FilterCheckRegEx fN = new FilterCheckRegEx(cboGenre.getEditor());
        cboGenre.editableProperty().set(true);
        cboGenre.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cboGenre.setVisibleRowCount(25);
        cboGenre.valueProperty().bindBidirectional(lastPlayedFilter.genreFilterProperty());
        cboGenre.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                fN.checkPattern();
                filteredLastPlayedList.setPredicate(lastPlayedFilter.getPredicate());
            }
        });
        cboGenre.setItems(progData.filterWorker.getAllGenreList());

        btnReset.setGraphic(new ProgIcons().ICON_BUTTON_RESET);
        btnReset.setTooltip(new Tooltip("Wieder alle Favoriten anzeigen"));
        btnReset.setOnAction(event -> {
            filteredLastPlayedList.setPredicate(lastPlayedFilter.clearFilter());
        });
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        new Table().setTable(tableView, Table.TABLE.LAST_PLAYED);
        tableView.setItems(sortedLastPlayedList);
        sortedLastPlayedList.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<LastPlayed> optionalDownload = getSel(false);
                LastPlayed lastPlayed;
                if (optionalDownload.isPresent()) {
                    lastPlayed = optionalDownload.get();
                } else {
                    lastPlayed = null;
                }
                ContextMenu contextMenu = new LastPlayedGuiTableContextMenu(progData, this, tableView).
                        getContextMenu(lastPlayed);
                tableView.setContextMenu(contextMenu);
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> setSelectedFavourite());
        });
        tableView.getItems().addListener((ListChangeListener<LastPlayed>) c -> {
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
