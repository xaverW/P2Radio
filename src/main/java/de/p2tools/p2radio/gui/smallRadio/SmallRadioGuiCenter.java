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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PGuiTools;
import de.p2tools.p2Lib.guiTools.PTableFactory;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.filter.FilterFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class SmallRadioGuiCenter extends HBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final Button btnPrev = new Button();
    private final Button btnNext = new Button();
    private final Button btnClose = new Button();
    private final Button btnRadio = new Button();
    private final TablePlayable<StationData> tableViewStation;
    private final TablePlayable<StationData> tableViewFavourite;
    private final TablePlayable<StationData> tableViewHistory;
    private final ProgData progData;
    private final SmallRadioGuiController smallRadioGuiController;
    private TablePlayable<StationData> tableView;
    private FilteredList<StationData> filteredListStation;
    private FilteredList<StationData> filteredListFavourite;
    private FilteredList<StationData> filteredListHistory;

    public SmallRadioGuiCenter(SmallRadioGuiController smallRadioGuiController) {
        this.progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        tableViewStation = new TablePlayable<>(Table.TABLE_ENUM.SMALL_RADIO_STATION);
        tableViewFavourite = new TablePlayable<>(Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE);
        tableViewHistory = new TablePlayable<>(Table.TABLE_ENUM.SMALL_RADIO_HISTORY);
        tableView = tableViewStation;

        make();
        initTable();
        initListener();
    }

    private void make() {
        setSpacing(5);
        setPadding(new Insets(10, 10, 0, 10));
        setAlignment(Pos.CENTER);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        VBox vBoxLeft = new VBox();
        vBoxLeft.setAlignment(Pos.CENTER);
        VBox vBoxRight = new VBox();
        vBoxRight.setAlignment(Pos.CENTER);
        vBoxLeft.getChildren().addAll(btnRadio, PGuiTools.getVBoxGrower(), btnPrev, PGuiTools.getVBoxGrower());
        vBoxRight.getChildren().addAll(btnClose, PGuiTools.getVBoxGrower(), btnNext, PGuiTools.getVBoxGrower());
        getChildren().addAll(vBoxLeft, scrollPane, vBoxRight);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        btnClose.setTooltip(new Tooltip("Programm beenden"));
        btnClose.setOnAction(e -> {
            smallRadioGuiController.close();
            ProgQuitFactory.quit(progData.primaryStage, true);
        });
        btnClose.setMaxWidth(Double.MAX_VALUE);
        btnClose.getStyleClass().add("btnTab");
        btnClose.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP.getImageView());

        btnRadio.setTooltip(new Tooltip("große Programmoberfläche anzeigen"));
        btnRadio.setOnAction(e -> smallRadioGuiController.changeGui());
        btnRadio.setMaxWidth(Double.MAX_VALUE);
        btnRadio.getStyleClass().add("btnTab");
        btnRadio.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_SMALL_RADIO_20.getImageView());

        btnPrev.setTooltip(new Tooltip("vorherigen Sender auswählen"));
        btnPrev.getStyleClass().add("btnSmallRadio");
        btnPrev.setGraphic(ProgIcons.Icons.ICON_BUTTON_PREV.getImageView());
        btnPrev.setOnAction(event -> {
            setPreviousStation();
        });

        btnNext.setTooltip(new Tooltip("nächsten Sender auswählen"));
        btnNext.getStyleClass().add("btnSmallRadio");
        btnNext.setGraphic(ProgIcons.Icons.ICON_BUTTON_NEXT.getImageView());
        btnNext.setOnAction(event -> {
            setNextStation();
        });
    }

    public PMaskerPane getMaskerPane() {
        return smallRadioGuiController.getMaskerPane();
    }

    public void tableRefresh() {
        PTableFactory.refreshTable(tableView);
    }

    public void isShown() {
        tableView.requestFocus();
        setSelectedFavourite();
    }

    private void setSelectedFavourite() {
        StationData stationData = tableView.getSelectionModel().getSelectedItem();
        progData.stationInfoDialogController.setStation(stationData);
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> favourite = getSel();
        if (favourite.isPresent()) {
            progData.startFactory.playPlayable(favourite.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.favouriteList.stream().forEach(f -> progData.startFactory.stopPlayable(f));

        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                progData.startFactory.stopPlayable(favourite.get());
            }
        }
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
            PAlert.showInfoNoSelection();
        }
        return Optional.empty();
    }

    public ArrayList<StationData> getSelList() {
        final ArrayList<StationData> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
        }
        return ret;
    }

    public void setNextStation() {
        PTableFactory.selectNextRow(tableView);
    }

    public void setPreviousStation() {
        PTableFactory.selectPreviousRow(tableView);
    }

    public void playRandomStation() {
        Random r = new Random();
        StationData stationData = tableView.getItems().get(r.nextInt(tableView.getItems().size()));
        tableView.getSelectionModel().clearSelection();
        if (stationData != null) {
            tableView.getSelectionModel().select(stationData);
            tableView.scrollTo(stationData);
            progData.startFactory.playPlayable(stationData);
        }
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.SETDATA_CHANGED) {
            public void pingGui(PEvent event) {
                tableRefresh();
            }
        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                tableRefresh();
            }
        });
        ProgConfig.SMALL_RADIO_SELECTED_LIST.addListener((observable, oldValue, newValue) -> {
            loadTable();
        });
        ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
        ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
        ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
        ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.addListener((observable, oldValue, newValue) -> {
            setFilter();
        });
    }

    private void setFilter() {
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_STATION)) {
            filteredListStation.setPredicate(FilterFactory.getStationPredicateSmallGui());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_FAVOURITE)) {
            filteredListFavourite.setPredicate(FilterFactory.getFavoritePredicateSmallGui());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_HISTORY)) {
            filteredListHistory.setPredicate(FilterFactory.getHistoryPredicateSmallGui());
        }
    }

    private void loadTable() {
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_STATION)) {
            tableView = tableViewStation;
            scrollPane.setContent(tableView);

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_FAVOURITE)) {
            tableView = tableViewFavourite;
            scrollPane.setContent(tableView);

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_HISTORY)) {
            tableView = tableViewHistory;
            scrollPane.setContent(tableView);
        }

        setFilter();
    }

    private void initTable() {
        Table.setTable(tableViewStation);
        Table.setTable(tableViewFavourite);
        Table.setTable(tableViewHistory);
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

        loadTable();

//        Platform.runLater(() -> PTableFactory.refreshTable(tableView));
        tableViewStation.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                progData.stationInfoDialogController.showStationInfo();
            }
        });
        tableViewFavourite.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                Optional<StationData> stationData = getSel(true);
                if (stationData.isPresent()) {
                    FavouriteFactory.changeFavourite(stationData.get());
                }
            }
        });
        addTableListener(tableViewStation);
        addTableListener(tableViewFavourite);
        addTableListener(tableViewHistory);
    }

    private void addTableListener(TablePlayable tableView) {
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<StationData> optionalDownload = getSel(false);
                StationData favourite;
                if (optionalDownload.isPresent()) {
                    favourite = optionalDownload.get();
                } else {
                    favourite = null;
                }
                if (tableView.equals(tableViewStation)) {
                    ContextMenu contextMenu = new SmallRadioGuiTableContextMenu(progData.smallRadioGuiController, tableView)
                            .getContextMenuStation(favourite);
                    tableView.setContextMenu(contextMenu);

                } else if (tableView.equals(tableViewFavourite)) {
                    ContextMenu contextMenu = new SmallRadioGuiTableContextMenu(progData.smallRadioGuiController, tableView)
                            .getContextMenuFavourite(favourite);
                    tableView.setContextMenu(contextMenu);

                } else {
                    ContextMenu contextMenu = new SmallRadioGuiTableContextMenu(progData.smallRadioGuiController, tableView)
                            .getContextMenuHistory(favourite);
                    tableView.setContextMenu(contextMenu);
                }
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedFavourite();
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
