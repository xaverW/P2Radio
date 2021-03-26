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

import de.p2tools.p2Lib.guiTools.PCheckBoxCell;
import de.p2tools.p2Lib.tools.GermanStringIntSorter;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableFavourite {

    private final ProgData progData;
    private final BooleanProperty geoMelden;
    private final BooleanProperty small;

    public TableFavourite(ProgData progData) {
        this.progData = progData;
        geoMelden = ProgConfig.SYSTEM_MARK_GEO;
        small = ProgConfig.SYSTEM_SMALL_ROW_TABLE_FAVOURITE;
    }

    public TableColumn[] initFavouriteColumn(TableView table) {
        table.getColumns().clear();

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();
        ProgConfig.SYSTEM_SMALL_ROW_TABLE_FAVOURITE.addListener((observableValue, s, t1) -> table.refresh());
        ProgColorList.STATION_RUN.colorProperty().addListener((a, b, c) -> table.refresh());
        ProgColorList.STATION_ERROR.colorProperty().addListener((a, b, c) -> table.refresh());

        final TableColumn<Favourite, Integer> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nrColumn.setCellFactory(cellFactoryNo);
        nrColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, Integer> senderNoColumn = new TableColumn<>("SenderNr");
        senderNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        senderNoColumn.setCellFactory(cellFactorySenderNo);
        senderNoColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, String> senderColumn = new TableColumn<>("Sender");
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        senderColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, String> collectionColumn = new TableColumn<>("Sammlung");
        collectionColumn.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
        collectionColumn.getStyleClass().add("alignCenterLeft");
        collectionColumn.setComparator(sorter);

        final TableColumn<Favourite, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, PDate> codecColumn = new TableColumn<>("Codec");
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, Integer> bitrateColumn = new TableColumn<>("Bitrate");
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
        bitrateColumn.setCellFactory(cellFactoryBitrate);
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Favourite, Integer> gradeColumn = new TableColumn<>("Bewertung");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeColumn.setCellFactory(cellFactoryGrade);
//        gradeColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Favourite, Integer> ownColumn = new TableColumn<>("eigene");
        ownColumn.setCellValueFactory(new PropertyValueFactory<>("own"));
        ownColumn.setCellFactory(new PCheckBoxCell().cellFactoryBool);
        ownColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(cellFactoryButton);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, PDate> countryColumn = new TableColumn<>("Land");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, PDate> countryCodeColumn = new TableColumn<>("Länderkürzel");
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, String> languageColumn = new TableColumn<>("Sprache");
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, PDate> datumColumn = new TableColumn<>("Datum");
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("stationDate"));
        datumColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.getStyleClass().add("alignCenterLeft");

        nrColumn.setPrefWidth(50);
        senderNoColumn.setPrefWidth(70);
        senderColumn.setPrefWidth(80);
        genreColumn.setPrefWidth(180);

        addRowFact(table);

        return new TableColumn[]{
                nrColumn, senderNoColumn,
                senderColumn, collectionColumn, genreColumn, codecColumn,
                bitrateColumn, gradeColumn, ownColumn, startColumn, countryColumn, countryCodeColumn, languageColumn,
                datumColumn, urlColumn
        };
    }

    private void addRowFact(TableView<Favourite> table) {
        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Favourite favourite, boolean empty) {
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

    private Callback<TableColumn<Favourite, Integer>, TableCell<Favourite, Integer>> cellFactoryBitrate
            = (final TableColumn<Favourite, Integer> param) -> {

        final TableCell<Favourite, Integer> cell = new TableCell<>() {

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
    private Callback<TableColumn<Favourite, Integer>, TableCell<Favourite, Integer>> cellFactoryGrade
            = (final TableColumn<Favourite, Integer> param) -> {

        final TableCell<Favourite, Integer> cell = new TableCell<>() {

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

    private Callback<TableColumn<Favourite, Integer>, TableCell<Favourite, Integer>> cellFactoryButton
            = (final TableColumn<Favourite, Integer> param) -> {

        final TableCell<Favourite, Integer> cell = new TableCell<Favourite, Integer>() {

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

                Favourite favourite = getTableView().getItems().get(getIndex());
                final boolean playing = favourite.getStart() != null;
                final boolean error = favourite.getStart() != null ? favourite.getStart().getStartStatus().isStateError() : false;
                final boolean set = progData.setDataList.size() > 1;

                if (playing) {
                    //dann stoppen
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_STATION_STOP_PLAY));
                    btnPlay.setOnAction((ActionEvent event) -> {
                        progData.startFactory.stopFavourite(favourite);
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
                        progData.startFactory.playFavourite(favourite, ne);
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
                        progData.startFactory.playFavourite(favourite);
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
                    progData.favouriteGuiController.deleteFavourite(favourite);
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

    private void setCellStyle(TableCell<Favourite, Integer> cell, boolean playing, boolean error) {
        TableRow<Favourite> currentRow = cell.getTableRow();
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

    private Callback<TableColumn<Favourite, Integer>, TableCell<Favourite, Integer>> cellFactoryNo
            = (final TableColumn<Favourite, Integer> param) -> {

        final TableCell<Favourite, Integer> cell = new TableCell<Favourite, Integer>() {

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

    private Callback<TableColumn<Favourite, Integer>, TableCell<Favourite, Integer>> cellFactorySenderNo
            = (final TableColumn<Favourite, Integer> param) -> {

        final TableCell<Favourite, Integer> cell = new TableCell<Favourite, Integer>() {

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
