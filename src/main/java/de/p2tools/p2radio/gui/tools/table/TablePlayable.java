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

import de.p2tools.p2lib.guitools.ptable.P2CellCheckBox;
import de.p2tools.p2lib.guitools.ptable.P2CellIntMax;
import de.p2tools.p2lib.guitools.ptable.P2CellIntNull;
import de.p2tools.p2lib.guitools.ptable.P2CellLocalDate;
import de.p2tools.p2lib.tools.GermanStringIntSorter;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TablePlayable<T> extends TableView<T> {

    Table.TABLE_ENUM table_enum;

    public TablePlayable(Table.TABLE_ENUM table_enum) {
        super();
        this.table_enum = table_enum;

        initFileRunnerColumn();
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
//        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();

        final TableColumn<T, Integer> stationNoColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NO);
        stationNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        stationNoColumn.setCellFactory(new P2CellIntMax<T, Integer>().cellFactory);
        stationNoColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, String> stationNameColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NAME);
        stationNameColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        stationNameColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, Boolean> favouriteColumn = new TableColumn<>(StationDataXml.STATION_PROP_IS_FAVOURITE);
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        favouriteColumn.setCellFactory(new P2CellCheckBox().cellFactory);
        favouriteColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> collectionNameColumn = new TableColumn<>(StationDataXml.STATION_PROP_COLLECTION);
        collectionNameColumn.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
        collectionNameColumn.getStyleClass().add("alignCenterLeft");
        collectionNameColumn.setComparator(sorter);

        final TableColumn<T, Integer> startButtonColumn = new TableColumn<>("");
        startButtonColumn.getStyleClass().add("alignCenter");
        ProgConfig.SYSTEM_SMALL_ROW_TABLE.addListener((observable, oldValue, newValue) -> addStart(startButtonColumn));
        addStart(startButtonColumn);

        final TableColumn<T, Integer> ownGradeColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN_GRADE);
        ownGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ownGrade"));
        ownGradeColumn.setCellFactory(new CellGrade().cellFactoryGrade);

        final TableColumn<T, Integer> startsColumn = new TableColumn<>(StationDataXml.STATION_PROP_STARTS);
        startsColumn.setCellValueFactory(new PropertyValueFactory<>("starts"));
        startsColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, Integer> clickCountColumn = new TableColumn<>(StationDataXml.STATION_PROP_CLICK_COUNT);
        clickCountColumn.setCellValueFactory(new PropertyValueFactory<>("clickCount"));
        clickCountColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, Boolean> clickTrendColumn = new TableColumn<>(StationDataXml.STATION_PROP_CLICK_TREND);
        clickTrendColumn.setCellValueFactory(new PropertyValueFactory<>("clickTrend"));
        clickTrendColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<T, Integer> votesColumn = new TableColumn<>(StationDataXml.STATION_PROP_VOTES);
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
        votesColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<T, String> genreColumn = new TableColumn<>(StationDataXml.STATION_PROP_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, P2Date> codecColumn = new TableColumn<>(StationDataXml.STATION_PROP_CODEC);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, Integer> bitrateColumn = new TableColumn<>(StationDataXml.STATION_PROP_BITRATE);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
        bitrateColumn.setCellFactory(new P2CellIntNull().cellFactory);
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<T, Integer> ownColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN);
        ownColumn.setCellValueFactory(new PropertyValueFactory<>("own"));
        ownColumn.setCellFactory(new P2CellCheckBox().cellFactory);
        ownColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> stateColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATE);
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, P2Date> countryColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, P2Date> countryCodeColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY_CODE);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> languageColumn = new TableColumn<>(StationDataXml.STATION_PROP_LANGUAGE);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, LocalDate> stationDateColumn = new TableColumn<>(StationDataXml.STATION_PROP_DATE);
        stationDateColumn.setCellValueFactory(new PropertyValueFactory<>("stationDate"));
        stationDateColumn.setCellFactory(new P2CellLocalDate().cellFactory);
        stationDateColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> websiteColumn = new TableColumn<>(StationDataXml.STATION_PROP_WEBSITE);
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        websiteColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, String> stationUrlColumn = new TableColumn<>(StationDataXml.STATION_PROP_URL);
        stationUrlColumn.setCellValueFactory(new PropertyValueFactory<>("stationUrl"));
        stationUrlColumn.getStyleClass().add("alignCenterLeft");

        stationNoColumn.setPrefWidth(70);
        stationNameColumn.setPrefWidth(150);
        genreColumn.setPrefWidth(100);
        languageColumn.setPrefWidth(100);
        stateColumn.setPrefWidth(100);

        setRowFactory(tv -> new TableRowPlayable<>(table_enum));

        if (this.table_enum.equals(Table.TABLE_ENUM.STATION)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /*collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn, startsColumn,*/
                    clickCountColumn, clickTrendColumn, votesColumn,
                    genreColumn, codecColumn, bitrateColumn, ownColumn,
                    stateColumn, countryColumn, countryCodeColumn, languageColumn,
                    stationDateColumn, websiteColumn, stationUrlColumn);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.FAVOURITE)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn,/*isFavouriteColumn,*/ collectionNameColumn, startButtonColumn,
                    ownGradeColumn, startsColumn,
                    clickCountColumn, clickTrendColumn, votesColumn,
                    genreColumn, codecColumn, bitrateColumn, ownColumn,
                    stateColumn, countryColumn, countryCodeColumn, languageColumn,
                    stationDateColumn, websiteColumn, stationUrlColumn);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.HISTORY)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /*collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn,*/ startsColumn,
                    clickCountColumn, clickTrendColumn, votesColumn,
                    genreColumn, codecColumn, bitrateColumn, ownColumn,
                    stateColumn, countryColumn, countryCodeColumn, languageColumn,
                    stationDateColumn, websiteColumn, stationUrlColumn);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.SMALL_RADIO_STATION)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /*collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn, startsColumn,*/
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumn, websiteColumn, stationUrlColumn*/);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, /*isFavouriteColumn,*/collectionNameColumn, startButtonColumn,
                    ownGradeColumn, startsColumn,
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumn, websiteColumn, stationUrlColumn*/);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.SMALL_RADIO_HISTORY)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn,/*collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn,*/ startsColumn,
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumn, websiteColumn, stationUrlColumn*/);
        }
    }

    private void addStart(TableColumn startButtonColumn) {
        switch (this.table_enum) {
            case STATION:
                startButtonColumn.setCellFactory(new CellButtonStation().cellFactoryStart);
                break;
            case FAVOURITE:
                startButtonColumn.setCellFactory(new CellButtonFavourite().cellFactoryButton);
                break;
            case HISTORY:
                startButtonColumn.setCellFactory(new CellButtonHistory().cellFactoryButton);
                break;
            case SMALL_RADIO_STATION:
            case SMALL_RADIO_FAVOURITE:
            case SMALL_RADIO_HISTORY:
            default:
                startButtonColumn.setCellFactory(new CellStartSmallRadio().cellFactoryButton);
        }
    }
}
