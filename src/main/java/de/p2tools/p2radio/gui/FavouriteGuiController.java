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
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.gui.dialog.FavouriteEditDialogController;
import de.p2tools.p2radio.gui.tools.Listener;
import de.p2tools.p2radio.gui.tools.table.Table;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Optional;

public class FavouriteGuiController extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final VBox vBox = new VBox(0);
    private final ScrollPane scrollPane = new ScrollPane();
    private final TableView<Favourite> tableView = new TableView<>();

    private final ProgData progData;
    private boolean bound = false;
    private final FilteredList<Favourite> filteredFavourites;
    private final SortedList<Favourite> sortedFavourites;
    private FavouriteGuiInfoController favouriteGuiInfoController;

    DoubleProperty splitPaneProperty = ProgConfig.FAVOURITE_GUI_DIVIDER;
    BooleanProperty boolInfoOn = ProgConfig.FAVOURITE_GUI_DIVIDER_ON;

    public FavouriteGuiController() {
        progData = ProgData.getInstance();

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
        favouriteGuiInfoController = new FavouriteGuiInfoController();
        filteredFavourites = progData.filteredFavourites;

        sortedFavourites = new SortedList<>(filteredFavourites);

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

    public FilteredList<Favourite> getFilteredList() {
        return filteredFavourites;
    }

    private void setSelectedFavourite() {
        Favourite favourite = tableView.getSelectionModel().getSelectedItem();
        if (favourite != null) {
            favouriteGuiInfoController.setFavourite(favourite);
            Station station = progData.stationList.getSenderByUrl(favourite.getStationUrl());
            progData.stationInfoDialogController.setStation(station);
        } else {
            favouriteGuiInfoController.setFavourite(null);
        }
    }

    public void playStation() {
        // bezieht sich auf den ausgew??hlten Favoriten
        final Optional<Favourite> favourite = getSel();
        if (favourite.isPresent()) {
            progData.startFactory.playFavourite(favourite.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.favouriteList.stream().forEach(f -> progData.startFactory.stopFavourite(f));

        } else {
            final Optional<Favourite> favourite = getSel();
            if (favourite.isPresent()) {
                progData.startFactory.stopFavourite(favourite.get());
            }
        }
    }

    public void deleteFavourite(boolean all) {
        if (all) {
            final ArrayList<Favourite> list = getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Favorit gel??scht werden?";
            } else {
                text = "Sollen die Favoriten gel??scht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten l??schen?", "Favoriten l??schen?", text)
                    .equals(PAlert.BUTTON.YES)) {
                progData.favouriteList.removeAll(list);
                StationListFactory.findAndMarkFavouriteStations(progData);
            }

        } else {
            final Optional<Favourite> favourite = getSel();
            if (favourite.isPresent()) {
                deleteFavourite(favourite.get());
            }
        }
    }

    public void deleteFavourite(Favourite favourite) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten l??schen?", "Favoriten l??schen?",
                "Soll der Favorite gel??scht werden?").equals(PAlert.BUTTON.YES)) {
            progData.favouriteList.remove(favourite);
            StationListFactory.findAndMarkFavouriteStations(progData);
        }
    }

    public void changeFavourite(boolean allSel) {
        ArrayList<Favourite> list = new ArrayList<>();
        ArrayList<Favourite> listCopy = new ArrayList<>();
        if (allSel) {
            list.addAll(getSelList());
        } else {
            final Optional<Favourite> favourite = getSel();
            if (favourite.isPresent()) {
                list.add(favourite.get());
            }
        }

        if (list.isEmpty()) {
            return;
        }
        list.stream().forEach(f -> {
            Favourite favouriteCopy = f.getCopy();
            listCopy.add(favouriteCopy);
        });

        FavouriteEditDialogController favouriteEditDialogController =
                new FavouriteEditDialogController(progData, listCopy);

        if (favouriteEditDialogController.isOk()) {
            for (int i = 0; i < listCopy.size(); ++i) {
                final Favourite f, fCopy;
                f = list.get(i);
                fCopy = listCopy.get(i);
                f.copyToMe(fCopy);
            }
            progData.collectionList.updateNames();//k??nnte ja ge??ndert sein
        }
    }

    public void saveTable() {
        new Table().saveTable(tableView, Table.TABLE.FAVOURITE);
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
        Listener.addListener(new Listener(Listener.EVENT_SETDATA_CHANGED, FavouriteGuiController.class.getSimpleName()) {
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

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        new Table().setTable(tableView, Table.TABLE.FAVOURITE);
        tableView.setItems(sortedFavourites);
        sortedFavourites.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                changeFavourite(false);
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<Favourite> optionalDownload = getSel(false);
                Favourite favourite;
                if (optionalDownload.isPresent()) {
                    favourite = optionalDownload.get();
                } else {
                    favourite = null;
                }
                ContextMenu contextMenu = new FavouriteGuiTableContextMenu(progData, this, tableView).getContextMenu(favourite);
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
