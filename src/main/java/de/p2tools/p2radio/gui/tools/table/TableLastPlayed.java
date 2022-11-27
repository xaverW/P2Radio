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

package de.p2tools.p2radio.gui.tools.table;

import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFactory;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedXml;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableLastPlayed extends PTable<LastPlayed> {

    private final ProgData progData;
    private final BooleanProperty small;
    private final Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryBitrate
            = (final TableColumn<LastPlayed, Integer> param) -> {

        final TableCell<LastPlayed, Integer> cell = new TableCell<>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }
            }
        };
        return cell;
    };
    private final Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryButton
            = (final TableColumn<LastPlayed, Integer> param) -> {

        final TableCell<LastPlayed, Integer> cell = new TableCell<>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                LastPlayed lastPlayed = getTableView().getItems().get(getIndex());
                final boolean playing = lastPlayed.getStart() != null;

                if (playing) {
                    //dann stoppen
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.getStyleClass().add("btnSmallRadio");
                    btnPlay.setTooltip(new Tooltip("Sender stoppen"));
                    btnPlay.setGraphic(ProgIcons.Icons.IMAGE_TABLE_STATION_STOP_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        progData.startFactory.stopLastPlayed(lastPlayed);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        btnPlay.setMinHeight(18);
                        btnPlay.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);

                } else {
                    //starten, nur ein Set
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.getStyleClass().add("btnSmallRadio");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(ProgIcons.Icons.IMAGE_TABLE_STATION_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        progData.startFactory.playLastPlayed(lastPlayed);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        btnPlay.setMinHeight(18);
                        btnPlay.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);
                }

                final Button btnDel;
                btnDel = new Button("");
                btnDel.getStyleClass().add("btnSmallRadio");
                btnDel.setTooltip(new Tooltip("Sender aus History löschen"));
                btnDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_FAVOURITE_DEL.getImageView());
                btnDel.setOnAction(event -> {
                    LastPlayedFactory.deleteHistory(lastPlayed);
                });

                if (small.get()) {
                    btnDel.setMinHeight(18);
                    btnDel.setMaxHeight(18);
                }
                hbox.getChildren().add(btnDel);
                setGraphic(hbox);
            }
        };
        return cell;
    };
    private final Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryNo
            = (final TableColumn<LastPlayed, Integer> param) -> {

        final TableCell<LastPlayed, Integer> cell = new TableCell<LastPlayed, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == FavouriteConstants.FAVOURITE_NUMBER_NOT_STARTED) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }
            }
        };
        return cell;
    };
    private final Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactorySenderNo
            = (final TableColumn<LastPlayed, Integer> param) -> {

        final TableCell<LastPlayed, Integer> cell = new TableCell<LastPlayed, Integer>() {

            @Override
            public void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == FavouriteConstants.STATION_NUMBER_NOT_FOUND) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }
            }
        };
        return cell;
    };

    public TableLastPlayed(Table.TABLE_ENUM table_enum, ProgData progData) {
        super(table_enum);
        this.table_enum = table_enum;
        this.progData = progData;

        initFileRunnerColumn();
        small = ProgConfig.SYSTEM_SMALL_ROW_TABLE;
    }

    public Table.TABLE_ENUM getETable() {
        return table_enum;
    }

    public void resetTable() {
        initFileRunnerColumn();
        Table.resetTable(this);
    }

    private void initFileRunnerColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<LastPlayed, Integer> nrColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_NO]);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nrColumn.setCellFactory(cellFactoryNo);
        nrColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<LastPlayed, Integer> senderNoColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_STATION_NO]);
        senderNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        senderNoColumn.setCellFactory(cellFactorySenderNo);
        senderNoColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<LastPlayed, String> senderColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_STATION]);
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        senderColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<LastPlayed, Integer> clickCountColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_CLICK_COUNT]);
        clickCountColumn.setCellValueFactory(new PropertyValueFactory<>("clickCount"));
        clickCountColumn.getStyleClass().add("alignCenter");

        final TableColumn<LastPlayed, String> genreColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_GENRE]);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<LastPlayed, PDate> codecColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_CODEC]);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");

        final TableColumn<LastPlayed, Integer> bitrateColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_BITRATE]);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
        bitrateColumn.setCellFactory(cellFactoryBitrate);
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<LastPlayed, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(cellFactoryButton);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<LastPlayed, PDate> countryColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_COUNTRY]);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<LastPlayed, PDate> countryCodeColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_COUNTRY_CODE]);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");

        final TableColumn<LastPlayed, String> languageColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_LANGUAGE]);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<LastPlayed, PDate> datumColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_DATE]);
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("stationDate"));
        datumColumn.getStyleClass().add("alignCenter");

        final TableColumn<LastPlayed, String> urlColumn = new TableColumn<>(LastPlayedXml.COLUMN_NAMES[LastPlayedXml.FAVOURITE_URL]);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("stationUrl"));
        urlColumn.getStyleClass().add("alignCenterLeft");

        nrColumn.setPrefWidth(50);
        senderNoColumn.setPrefWidth(70);
        senderColumn.setPrefWidth(80);
        genreColumn.setPrefWidth(180);

        addRowFact();
        getColumns().addAll(
                nrColumn, senderNoColumn,
                senderColumn, clickCountColumn, genreColumn,
                codecColumn, bitrateColumn, startColumn, countryColumn, countryCodeColumn, languageColumn,
                datumColumn, urlColumn);
    }

    private void addRowFact() {
        setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(LastPlayed lastPlayed, boolean empty) {
                super.updateItem(lastPlayed, empty);

                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        getSelectionModel().clearSelection();
                    }
                });

                setStyle("");
                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle("");
                }

                if (lastPlayed != null && !empty) {
                    if (lastPlayed.getStart() != null && lastPlayed.getStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(lastPlayed.getStart().getStartStatus().getErrorMessage());
                        setTooltip(tooltip);
                    }

                    final boolean fav = progData.favouriteList.getUrlStation(lastPlayed.getStationUrl()) != null;
                    final boolean playing = lastPlayed.getStart() != null;
                    final boolean error = lastPlayed.getStart() != null && lastPlayed.getStart().getStartStatus().isStateError();

                    if (error) {
                        if (ProgColorList.STATION_ERROR_BG.isUse()) {
                            setStyle(ProgColorList.STATION_ERROR_BG.getCssBackgroundAndSel());
                        }
                        if (ProgColorList.STATION_ERROR.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.STATION_ERROR.getCssFont());
                            }
                        }

                    } else if (playing) {
                        if (ProgColorList.STATION_RUN_BG.isUse()) {
                            setStyle(ProgColorList.STATION_RUN_BG.getCssBackgroundAndSel());
                        }
                        if (ProgColorList.STATION_RUN.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.STATION_RUN.getCssFont());
                            }
                        }

                    } else if (fav) {
                        if (ProgColorList.STATION_FAVOURITE_BG.isUse()) {
                            setStyle(ProgColorList.STATION_FAVOURITE_BG.getCssBackgroundAndSel());
                        }
                        if (ProgColorList.STATION_FAVOURITE.isUse()) {
                            System.out.println("isUse");
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.STATION_FAVOURITE.getCssFont());
                            }
                        }
                    }
                }
            }
        });
    }
}
