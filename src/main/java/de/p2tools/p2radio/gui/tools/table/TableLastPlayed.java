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

import de.p2tools.p2Lib.tools.GermanStringIntSorter;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedXml;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableLastPlayed {

    private final ProgData progData;
    private final BooleanProperty geoMelden;
    private final BooleanProperty small;

    public TableLastPlayed(ProgData progData) {
        this.progData = progData;
        geoMelden = ProgConfig.SYSTEM_MARK_GEO;
        small = ProgConfig.SYSTEM_SMALL_ROW_TABLE_FAVOURITE;
    }

    public TableColumn[] initLastPlayedColumn(TableView table) {
        table.getColumns().clear();

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();
        ProgConfig.SYSTEM_SMALL_ROW_TABLE_FAVOURITE.addListener((observableValue, s, t1) -> table.refresh());
        ProgColorList.STATION_RUN.colorProperty().addListener((a, b, c) -> table.refresh());
        ProgColorList.STATION_ERROR.colorProperty().addListener((a, b, c) -> table.refresh());

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
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.getStyleClass().add("alignCenterLeft");

        nrColumn.setPrefWidth(50);
        senderNoColumn.setPrefWidth(70);
        senderColumn.setPrefWidth(80);
        genreColumn.setPrefWidth(180);

        addRowFact(table);

        return new TableColumn[]{
                nrColumn, senderNoColumn,
                senderColumn, clickCountColumn, genreColumn,
                codecColumn, bitrateColumn, startColumn, countryColumn, countryCodeColumn, languageColumn,
                datumColumn, urlColumn
        };
    }

    private void addRowFact(TableView<LastPlayed> table) {
        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(LastPlayed favourite, boolean empty) {
                super.updateItem(favourite, empty);

                if (favourite == null || empty) {
                    setStyle("");

                } else {
                    if (favourite.getStart() != null && favourite.getStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(favourite.getStart().getStartStatus().getErrorMessage());
                        setTooltip(tooltip);

                    } else {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle("");
                        }
                    }
                }
            }
        });
    }

    private Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryBitrate
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
    private Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryGrade
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
                    HBox hBox = new HBox(3);
                    hBox.setAlignment(Pos.CENTER);
                    for (int i = 0; i < FavouriteConstants.MAX_FAVOURITE_GRADE; ++i) {
                        if (item.longValue() > i) {
                            Label l = new Label();
                            l.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_FAVOURITE_GRADE.getUrl()));
                            hBox.getChildren().add(l);
                        }
                    }
                    setGraphic(hBox);
                }
            }
        };
        return cell;
    };

    private Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryButton
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
                final boolean error = lastPlayed.getStart() != null ? lastPlayed.getStart().getStartStatus().isStateError() : false;
                final boolean set = progData.setDataList.size() > 1;

                if (playing) {
                    //dann stoppen
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_STATION_STOP_PLAY));
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

                } else if (set) {
                    //läuft nix, mehre Sets
                    final ComboBox<SetData> cboSet;
                    cboSet = new ComboBox();
                    cboSet.getStyleClass().add("combo-box-icon");
                    cboSet.getItems().addAll(progData.setDataList);
                    cboSet.getSelectionModel().selectedItemProperty().addListener((v, ol, ne) -> {
                        progData.startFactory.playLastPlayed(lastPlayed, ne);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        cboSet.setMinHeight(18);
                        cboSet.setMaxHeight(18);
                    }
                    hbox.getChildren().add(cboSet);

                } else {
                    //starten, nur ein Set
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_STATION_PLAY));
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
                btnDel.setTooltip(new Tooltip("Favoriten löschen"));
                btnDel.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_FAVOURITE_DEL));
                btnDel.setOnAction(event -> {
                    progData.lastPlayedGuiController.deleteHistory(lastPlayed);
                });

                if (small.get()) {
                    btnDel.setMinHeight(18);
                    btnDel.setMaxHeight(18);
                }
                hbox.getChildren().add(btnDel);

                setGraphic(hbox);
                setCellStyle(this, playing, error);
            }
        };
        return cell;
    };

    private void setCellStyle(TableCell<LastPlayed, Integer> cell, boolean playing, boolean error) {
        TableRow<LastPlayed> currentRow = cell.getTableRow();
        if (currentRow == null) {
            return;
        }

        if (playing) {
            currentRow.setStyle(ProgColorList.STATION_RUN.getCssBackgroundSel());
        } else if (error) {
            currentRow.setStyle(ProgColorList.STATION_ERROR.getCssBackgroundSel());
        } else {
            currentRow.setStyle("");
        }
    }

    private Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactoryNo
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

    private Callback<TableColumn<LastPlayed, Integer>, TableCell<LastPlayed, Integer>> cellFactorySenderNo
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
}
