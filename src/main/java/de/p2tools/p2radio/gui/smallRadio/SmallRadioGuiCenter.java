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
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.filter.FilterFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.gui.FavouriteGuiInfoController;
import de.p2tools.p2radio.gui.dialog.FavouriteEditDialogController;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TablePlayable;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
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
    private final TablePlayable<StationData> tableView;
    private final ProgData progData;
    private final FavouriteGuiInfoController favouriteGuiInfoController;
    private final Button btnPrev = new Button();
    private final Button btnNext = new Button();
    private final Button btnClose = new Button();
    private final Button btnRadio = new Button();
    private final SmallRadioGuiController smallRadioGuiController;
    private FilteredList<StationData> filteredList;

    public SmallRadioGuiCenter(SmallRadioGuiController smallRadioGuiController) {
        progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        tableView = new TablePlayable<>(Table.TABLE_ENUM.SMALL_RADIO);

        make();
        favouriteGuiInfoController = new FavouriteGuiInfoController();

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
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox vBoxLeft = new VBox();
        vBoxLeft.setAlignment(Pos.CENTER);
        VBox vBoxRight = new VBox();
        vBoxRight.setAlignment(Pos.CENTER);
        vBoxLeft.getChildren().addAll(btnRadio, PGuiTools.getVBoxGrower(), btnPrev, PGuiTools.getVBoxGrower());
        vBoxRight.getChildren().addAll(btnClose, PGuiTools.getVBoxGrower(), btnNext, PGuiTools.getVBoxGrower());
        getChildren().addAll(vBoxLeft, scrollPane, vBoxRight);

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
        final Optional<StationData> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(favourite.get().getStationUrl());
    }

    private void setSelectedFavourite() {
        StationData favourite = tableView.getSelectionModel().getSelectedItem();
        if (favourite != null) {
            favouriteGuiInfoController.setFavourite(favourite);
            StationData fav = progData.stationList.getSenderByUrl(favourite.getStationUrl());
            progData.stationInfoDialogController.setStation(fav);
        } else {
            favouriteGuiInfoController.setFavourite(null);
        }
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

    public void deleteFavourite(boolean all) {
        if (all) {
            final ArrayList<StationData> list = getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Favorit gelöscht werden?";
            } else {
                text = "Sollen die Favoriten gelöscht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?",
                    "Favoriten löschen?", text).equals(PAlert.BUTTON.YES)) {
                progData.favouriteList.removeAll(list);
                StationListFactory.findAndMarkFavouriteStations(progData);
            }

        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                deleteFavourite(favourite.get());
            }
        }
    }

    public void deleteFavourite(StationData favourite) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?",
                "Favoriten löschen?",
                "Soll der Favorite gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            progData.favouriteList.remove(favourite);
            StationListFactory.findAndMarkFavouriteStations(progData);
        }
    }

    public void changeFavourite(boolean allSel) {
        ArrayList<StationData> list = new ArrayList<>();
        ArrayList<StationData> listCopy = new ArrayList<>();
        if (allSel) {
            list.addAll(getSelList());
        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                list.add(favourite.get());
            }
        }

        if (list.isEmpty()) {
            return;
        }
        list.stream().forEach(f -> {
            StationData favouriteCopy = f.getCopy();
            listCopy.add(favouriteCopy);
        });

        FavouriteEditDialogController favouriteEditDialogController =
                new FavouriteEditDialogController(progData, listCopy);

        if (favouriteEditDialogController.isOk()) {
            for (int i = 0; i < listCopy.size(); ++i) {
                final StationData f, fCopy;
                f = list.get(i);
                fCopy = listCopy.get(i);
                f.copyToMe(fCopy);
            }
            progData.collectionList.updateNames();//könnte ja geändert sein
        }
    }

    public void saveTable() {
        Table.saveTable(tableView, Table.TABLE_ENUM.SMALL_RADIO);
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
        final String url = ProgConfig.SYSTEM_LAST_PLAYED.getValue();
        Optional<StationData> optional = tableView.getItems().stream()
                .filter(favourite -> favourite.getStationUrl().equals(url)).findFirst();
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

    public void playRandomStation() {
        Random r = new Random();
        StationData favourite = tableView.getItems().get(r.nextInt(tableView.getItems().size()));
        tableView.getSelectionModel().clearSelection();
        if (favourite != null) {
            progData.startFactory.playPlayable(favourite);
            tableView.getSelectionModel().select(favourite);
            tableView.scrollTo(favourite);
        }
    }

    private void initListener() {
        progData.pEventHandler.addListener(new PListener(Events.SETDATA_CHANGED) {
            public void pingGui(PEvent event) {
                tableView.refresh();
            }
        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            @Override
            public void pingGui(PEvent runEvent) {
                PTableFactory.refreshTable(tableView);
            }
        });
        ProgConfig.SMALL_RADIO_SELECTED_LIST.addListener((observable, oldValue, newValue) -> {
            loadTable();
        });
        ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.addListener((observable, oldValue, newValue) -> {
            FilterFactory.setFilter(filteredList);
        });
        ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.addListener((observable, oldValue, newValue) -> {
            FilterFactory.setFilter(filteredList);
        });
        ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.addListener((observable, oldValue, newValue) -> {
            FilterFactory.setFilter(filteredList);
        });
        ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.addListener((observable, oldValue, newValue) -> {
            FilterFactory.setFilter(filteredList);
        });
    }

    private void loadTable() {
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_STATION)) {
            filteredList = new FilteredList<>(progData.stationList, p -> true);
            SortedList<StationData> stationData = new SortedList<>(filteredList);
            tableView.setItems(stationData);
            stationData.comparatorProperty().bind(tableView.comparatorProperty());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_FAVOURITE)) {
            filteredList = new FilteredList<>(progData.favouriteList, p -> true);
            SortedList<StationData> stationData = new SortedList<>(filteredList);
            tableView.setItems(stationData);
            stationData.comparatorProperty().bind(tableView.comparatorProperty());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(SmallRadioFactory.LIST_HISTORY)) {
            filteredList = new FilteredList<>(progData.historyList, p -> true);
            SortedList<StationData> stationData = new SortedList<>(filteredList);
            tableView.setItems(stationData);
            stationData.comparatorProperty().bind(tableView.comparatorProperty());
        }

        FilterFactory.setFilter(filteredList);
    }

    private void initTable() {
        Table.setTable(tableView);
        loadTable();

        Platform.runLater(() -> PTableFactory.refreshTable(tableView));
        tableView.setOnMouseClicked(m -> {
            if (m.getButton().equals(MouseButton.PRIMARY) && m.getClickCount() == 2) {
                changeFavourite(false);
            }
        });
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                final Optional<StationData> optionalDownload = getSel(false);
                StationData favourite;
                if (optionalDownload.isPresent()) {
                    favourite = optionalDownload.get();
                } else {
                    favourite = null;
                }
                ContextMenu contextMenu = new SmallRadioGuiTableContextMenu(progData, progData.smallRadioGuiController, tableView)
                        .getContextMenu(favourite);
                tableView.setContextMenu(contextMenu);
            }
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> setSelectedFavourite());
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
