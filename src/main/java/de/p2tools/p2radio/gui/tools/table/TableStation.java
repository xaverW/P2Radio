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

import de.p2tools.p2lib.tools.GermanStringIntSorter;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class TableStation extends TableView<StationData> {

    Table.TABLE_ENUM table_enum;

    public TableStation(Table.TABLE_ENUM table_enum) {
        super();
        this.table_enum = table_enum;

        initColumn();
    }

    public Table.TABLE_ENUM getETable() {
        return table_enum;
    }

    public void resetTable() {
        initColumn();
        Table.resetTable(this);
    }

    private void initColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();

        final TableColumn<StationData, Integer> stationNoColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NO);
        stationNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        stationNoColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryIntegerMax(stationNoColumn);

        final TableColumn<StationData, String> stationNameColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NAME);
        stationNameColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        stationNameColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(stationNameColumn);

        final TableColumn<StationData, Boolean> favouriteColumn = new TableColumn<>(StationDataXml.STATION_PROP_IS_FAVOURITE);
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        favouriteColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryBoolean(favouriteColumn);

        final TableColumn<StationData, String> collectionNameColumn = new TableColumn<>(StationDataXml.STATION_PROP_COLLECTION);
        collectionNameColumn.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
        collectionNameColumn.getStyleClass().add("alignCenterLeft");
        collectionNameColumn.setComparator(sorter);
        TableStationFactory.columnFactoryString(collectionNameColumn);

        final TableColumn<StationData, String> startButtonColumn = new TableColumn<>("");
        startButtonColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryButton(this.table_enum, startButtonColumn);
//        ProgConfig.SYSTEM_SMALL_ROW_TABLE.addListener((observable, oldValue, newValue) -> addStart(startButtonColumn));

        final TableColumn<StationData, Integer> ownGradeColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN_GRADE);
        ownGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ownGrade"));
        TableStationFactory.columnFactoryGrade(ownGradeColumn);

        final TableColumn<StationData, Integer> startsColumn = new TableColumn<>(StationDataXml.STATION_PROP_STARTS);
        startsColumn.setCellValueFactory(new PropertyValueFactory<>("starts"));
        startsColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryInteger(startsColumn);

        final TableColumn<StationData, Integer> clickCountColumn = new TableColumn<>(StationDataXml.STATION_PROP_CLICK_COUNT);
        clickCountColumn.setCellValueFactory(new PropertyValueFactory<>("clickCount"));
        clickCountColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryInteger(clickCountColumn);

        final TableColumn<StationData, Integer> clickTrendColumn = new TableColumn<>(StationDataXml.STATION_PROP_CLICK_TREND);
        clickTrendColumn.setCellValueFactory(new PropertyValueFactory<>("clickTrend"));
        clickTrendColumn.getStyleClass().add("alignCenterRightPadding_10");
        TableStationFactory.columnFactoryInteger(clickTrendColumn);

        final TableColumn<StationData, Integer> votesColumn = new TableColumn<>(StationDataXml.STATION_PROP_VOTES);
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
        votesColumn.getStyleClass().add("alignCenterRightPadding_10");
        TableStationFactory.columnFactoryInteger(votesColumn);

        final TableColumn<StationData, String> genreColumn = new TableColumn<>(StationDataXml.STATION_PROP_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(genreColumn);

        final TableColumn<StationData, String> codecColumn = new TableColumn<>(StationDataXml.STATION_PROP_CODEC);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryString(codecColumn);

        final TableColumn<StationData, Integer> bitrateColumn = new TableColumn<>(StationDataXml.STATION_PROP_BITRATE);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");
        TableStationFactory.columnFactoryInteger(bitrateColumn);

        final TableColumn<StationData, Boolean> ownColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN);
        ownColumn.setCellValueFactory(new PropertyValueFactory<>("own"));
        ownColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryBoolean(ownColumn);

        final TableColumn<StationData, String> stateColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATE);
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(stateColumn);

        final TableColumn<StationData, String> countryColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(countryColumn);

        final TableColumn<StationData, String> countryCodeColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY_CODE);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryString(countryCodeColumn);

        final TableColumn<StationData, String> languageColumn = new TableColumn<>(StationDataXml.STATION_PROP_LANGUAGE);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(languageColumn);

        final TableColumn<StationData, LocalDate> stationDateColumn = new TableColumn<>(StationDataXml.STATION_PROP_DATE);
        stationDateColumn.setCellValueFactory(new PropertyValueFactory<>("stationDate"));
        stationDateColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryLocalDate(stationDateColumn);

        final TableColumn<StationData, String> websiteColumn = new TableColumn<>(StationDataXml.STATION_PROP_WEBSITE);
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        websiteColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(websiteColumn);

        final TableColumn<StationData, String> stationUrlColumn = new TableColumn<>(StationDataXml.STATION_PROP_URL);
        stationUrlColumn.setCellValueFactory(new PropertyValueFactory<>("stationUrl"));
        stationUrlColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(stationUrlColumn);

        stationNoColumn.setPrefWidth(70);
        stationNameColumn.setPrefWidth(150);
        genreColumn.setPrefWidth(100);
        languageColumn.setPrefWidth(100);
        stateColumn.setPrefWidth(100);

        setRowFactory(tv -> new TableRowStation<>(table_enum));

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
}
