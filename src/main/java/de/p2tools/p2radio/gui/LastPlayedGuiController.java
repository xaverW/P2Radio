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
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFilter;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Orientation;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class LastPlayedGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vBox = new VBox(0);
    private final ScrollPane scrollPane = new ScrollPane();
    private final TablePlayable<Favourite> tableView;
    private final ProgData progData;
    private final LastPlayedGuiInfoController lastPlayedGuiInfoController;
    private final LastPlayedFilter lastPlayedFilter = new LastPlayedFilter();
    DoubleProperty splitPaneProperty = ProgConfig.LAST_PLAYED_GUI_DIVIDER;
    BooleanProperty boolInfoOn = ProgConfig.LAST_PLAYED_GUI_DIVIDER_ON;
    private boolean bound = false;

    public LastPlayedGuiController() {
        progData = ProgData.getInstance();
        tableView = new TablePlayable(Table.TABLE_ENUM.LAST_PLAYED);

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        splitPane.setOrientation(Orientation.VERTICAL);
        getChildren().addAll(splitPane);

        vBox.getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        boolInfoOn.addListener((observable, oldValue, newValue) -> setInfoPane());
        lastPlayedGuiInfoController = new LastPlayedGuiInfoController();

        setInfoPane();
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
        final Optional<Favourite> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(favourite.get().getStationUrl());
    }

    private void setSelectedFavourite() {
        Favourite favourite = tableView.getSelectionModel().getSelectedItem();
        if (favourite != null) {
            lastPlayedGuiInfoController.setLastPlayed(favourite);
            Favourite fav = progData.stationList.getSenderByUrl(favourite.getStationUrl());
            progData.stationInfoDialogController.setStation(fav);
        } else {
            lastPlayedGuiInfoController.setLastPlayed(null);
        }
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<Favourite> lastPlayed = getSel();
        if (lastPlayed.isPresent()) {
            progData.startFactory.playLastPlayed(lastPlayed.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.lastPlayedList.stream().forEach(lastPlayed -> progData.startFactory.stopLastPlayed(lastPlayed));

        } else {
            final Optional<Favourite> lastPlayed = getSel();
            if (lastPlayed.isPresent()) {
                progData.startFactory.stopLastPlayed(lastPlayed.get());
            }
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.LAST_PLAYED);
    }

    public ArrayList<Favourite> getSelList() {
        final ArrayList<Favourite> ret = new ArrayList<>();
        ret.addAll(tableView.getSelectionModel().getSelectedItems());
        if (ret.isEmpty()) {
            PAlert.showInfoNoSelection();
        }
        return ret;
    }

    public Optional<Favourite> getSel() {
        return getSel(true);
    }

    public Optional<Favourite> getSel(boolean show) {
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
        Optional<Favourite> optional = tableView.getItems().stream().filter(favourite -> favourite.getStationUrl().equals(url)).findFirst();
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
        progData.favouriteList.addListener((observable, oldValue, newValue) -> tableView.refresh());
        progData.pEventHandler.addListener(new PListener(Events.SETDATA_CHANGED) {
            public void pingGui(PEvent event) {
                tableView.refresh();
            }
        });

//        Listener.addListener(new Listener(Listener.EVENT_SETDATA_CHANGED, LastPlayedGuiController.class.getSimpleName()) {
//            @Override
//            public void pingFx() {
//                tableView.refresh();
//            }
//        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                PTableFactory.refreshTable(tableView);
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
            splitPane.getItems().addAll(vBox, lastPlayedGuiInfoController);
            splitPane.getDividers().get(0).positionProperty().bindBidirectional(splitPaneProperty);
            SplitPane.setResizableWithParent(vBox, true);
        }
    }

    private void initTable() {
        Table.setTable(tableView);

        SortedList<Favourite> sortedLastPlayedList = new SortedList<>(progData.filteredLastPlayedList);
        tableView.setItems(sortedLastPlayedList);
        sortedLastPlayedList.comparatorProperty().bind(tableView.comparatorProperty());
        Platform.runLater(() -> PTableFactory.refreshTable(tableView));

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<Favourite> optionalDownload = getSel(false);
                Favourite lastPlayed;
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
        tableView.getItems().addListener((ListChangeListener<Favourite>) c -> {
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
