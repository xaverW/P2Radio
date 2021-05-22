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
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationTools;
import de.p2tools.p2radio.controller.data.station.StationXml;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class TableStation {

    private final ProgData progData;
    private final BooleanProperty geoMelden;
    private final BooleanProperty small;

    public TableStation(ProgData progData) {
        this.progData = progData;
        geoMelden = ProgConfig.SYSTEM_MARK_GEO;
        small = ProgConfig.SYSTEM_SMALL_ROW_TABLE;
    }

    public TableColumn[] initStationColumn(TableView table) {
        table.getColumns().clear();

        // bei Farbänderung der Schriftfarbe klappt es damit besser: Table.refresh_table(table)
        ProgConfig.SYSTEM_SMALL_ROW_TABLE.addListener((observableValue, s, t1) -> table.refresh());
        ProgColorList.STATION_NEW.colorProperty().addListener((a, b, c) -> table.refresh());
        ProgColorList.STATION_RUN.colorProperty().addListener((a, b, c) -> table.refresh());
        ProgColorList.STATION_ERROR.colorProperty().addListener((a, b, c) -> table.refresh());

        final TableColumn<Station, Integer> nrColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_NO]);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nrColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Station, String> nameColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_NAME]);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Station, String> genreColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_GENRE]);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Station, String> codecColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_CODEC]);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");

        final TableColumn<Station, Integer> bitrateColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_BITRATE]);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrateInt"));
        bitrateColumn.setCellFactory(cellFactoryBitrate);
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Station, String> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(cellFactoryStart);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Station, String> stateColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_STATE]);
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Station, String> countryColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_COUNTRY]);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Station, String> countryCodeColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_COUNTRY_CODE]);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");

        final TableColumn<Station, String> languageColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_LANGUAGE]);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenter");

        final TableColumn<Station, Integer> votesColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_VOTES]);
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
        votesColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Station, Integer> clickCountColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_CLICK_COUNT]);
        clickCountColumn.setCellValueFactory(new PropertyValueFactory<>("clickCount"));
        clickCountColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Station, Boolean> clickTrendColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_CLICK_TREND]);
        clickTrendColumn.setCellValueFactory(new PropertyValueFactory<>("clickTrend"));
        clickTrendColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Station, PDate> dateColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_DATE]);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.getStyleClass().add("alignCenter");

        final TableColumn<Station, String> websiteColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_WEBSITE]);
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        websiteColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Station, String> urlColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_URL]);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.getStyleClass().add("alignCenterLeft");

//        final TableColumn<Station, String> urlrColumn = new TableColumn<>(StationXml.COLUMN_NAMES[StationXml.STATION_URL_RESOLVED]);
//        urlrColumn.setCellValueFactory(new PropertyValueFactory<>("urlResolved"));
//        urlrColumn.getStyleClass().add("alignCenterLeft");

        nrColumn.setPrefWidth(50);
        nameColumn.setPrefWidth(80);
        genreColumn.setPrefWidth(180);
        codecColumn.setPrefWidth(230);

        addRowFact(table);

        return new TableColumn[]{
                nrColumn, nameColumn, genreColumn, codecColumn, bitrateColumn, startColumn,
                stateColumn, countryColumn, countryCodeColumn, languageColumn, votesColumn,
                clickCountColumn, clickTrendColumn, dateColumn, websiteColumn, urlColumn/*, urlrColumn*/
        };
    }

    private void addRowFact(TableView<Station> table) {
        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Station station, boolean empty) {
                super.updateItem(station, empty);

                if (station == null || empty) {
                    setStyle("");
                } else {
                    if (station.isNewStation()) {
                        // neue Sender
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.STATION_NEW.getCssFont());
                        }

                    } else if (station.getStart() != null && station.getStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(station.getStart().getStartStatus().getErrorMessage());
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

    private Callback<TableColumn<Station, Integer>, TableCell<Station, Integer>> cellFactoryBitrate
            = (final TableColumn<Station, Integer> param) -> {

        final TableCell<Station, Integer> cell = new TableCell<>() {

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

    private Callback<TableColumn<Station, String>, TableCell<Station, String>> cellFactoryStart
            = (final TableColumn<Station, String> param) -> {

        final TableCell<Station, String> cell = new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(4);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                Station station = getTableView().getItems().get(getIndex());
                final boolean playing = station.getStart() != null;
                final boolean error = station.getStart() != null ? station.getStart().getStartStatus().isStateError() : false;
                final boolean fav = station.isFavouriteUrl();

                final boolean set = progData.setDataList.size() > 1;

                if (playing) {
                    //stoppen
                    final Button btnPlay = new Button("");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_STATION_STOP_PLAY));
                    btnPlay.setOnAction((ActionEvent event) -> {
                        progData.startFactory.stopStation(station);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        btnPlay.setMaxHeight(18);
                        btnPlay.setMinHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);

                } else if (set) {
                    //läuft nix, mehre Sets
                    final ComboBox<SetData> cboSet;
                    cboSet = new ComboBox();
                    cboSet.getStyleClass().add("combo-box-icon");
                    cboSet.getItems().addAll(progData.setDataList);
                    cboSet.getSelectionModel().selectedItemProperty().addListener((v, ol, ne) -> {
                        progData.startFactory.playStation(station, ne);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        cboSet.setMinHeight(18);
                        cboSet.setMaxHeight(18);
                    }
                    hbox.getChildren().add(cboSet);

                } else {
                    //starten
                    final Button btnPlay = new Button("");
                    btnPlay.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_STATION_PLAY));
                    btnPlay.setOnAction((ActionEvent event) -> {
                        progData.startFactory.playStation(station);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (small.get()) {
                        btnPlay.setMaxHeight(18);
                        btnPlay.setMinHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);
                }

                final Button btnSave;
                btnSave = new Button("");
                btnSave.setTooltip(new Tooltip("als Favoriten sichern"));
                btnSave.setGraphic(new ImageView(ProgIcons.IMAGE_TABLE_STATION_SAVE));
                btnSave.setOnAction(event -> {
                    StationTools.saveStation(station);
                });

                if (small.get()) {
                    btnSave.setMaxHeight(18);
                    btnSave.setMinHeight(18);
                }
                hbox.getChildren().add(btnSave);

                setGraphic(hbox);
                setCellStyle(this, playing, error, fav);
            }
        };
        return cell;
    };

    private void setCellStyle(TableCell<Station, String> cell, boolean playing, boolean error, boolean fav) {
        TableRow<Station> currentRow = cell.getTableRow();
        if (currentRow == null) {
            return;
        }

        if (playing) {
            currentRow.setStyle(ProgColorList.STATION_RUN.getCssBackgroundSel());
        } else if (error) {
            currentRow.setStyle(ProgColorList.STATION_ERROR.getCssBackgroundSel());
        } else if (fav) {
            currentRow.setStyle(ProgColorList.STATION_FAVOURITE.getCssBackgroundSel());
        } else {
            currentRow.setStyle("");
        }
    }
}
