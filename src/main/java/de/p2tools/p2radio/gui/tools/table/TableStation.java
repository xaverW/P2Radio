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

import de.p2tools.p2lib.guitools.ptable.P2TableFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.GermanStringIntSorter;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import de.p2tools.p2radio.controller.pevent.PEvents;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private void refreshTable() {
        P2TableFactory.refreshTable(this);
    }

    private void initColumn() {
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // brauchmer auf jeden Fall fürs Umschalten dark
        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> refreshTable());
        ProgConfig.SYSTEM_SMALL_ROW_TABLE.addListener((observableValue, s, t1) -> refresh());
        ProgData.getInstance().pEventHandler.addListener(new P2Listener(PEvents.REFRESH_TABLE) {
            @Override
            public void pingGui(P2Event runEvent) {
                refreshTable();
            }
        });

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();

        final TableColumn<StationData, Integer> stationNoColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NO);
        stationNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        stationNoColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryIntegerMax(this.table_enum, stationNoColumn);

        final TableColumn<StationData, String> stationNameColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NAME);
        stationNameColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        stationNameColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(this.table_enum, stationNameColumn);

        final TableColumn<StationData, Boolean> favouriteColumn = new TableColumn<>(StationDataXml.STATION_PROP_IS_FAVOURITE);
        favouriteColumn.setCellValueFactory(new PropertyValueFactory<>("favourite"));
        favouriteColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryBoolean(this.table_enum, favouriteColumn);

        final TableColumn<StationData, Boolean> isNewStation = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NEW);
        isNewStation.setCellValueFactory(new PropertyValueFactory<>("newStation"));
        isNewStation.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryBoolean(this.table_enum, isNewStation);

        final TableColumn<StationData, String> collectionNameColumn = new TableColumn<>(StationDataXml.STATION_PROP_COLLECTION);
        collectionNameColumn.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
        collectionNameColumn.getStyleClass().add("alignCenterLeft");
        collectionNameColumn.setComparator(sorter);
        TableStationFactory.columnFactoryString(this.table_enum, collectionNameColumn);

        final TableColumn<StationData, String> startButtonColumn = new TableColumn<>("");
        startButtonColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryButton(this.table_enum, startButtonColumn);
//        ProgConfig.SYSTEM_SMALL_ROW_TABLE.addListener((observable, oldValue, newValue) -> addStart(startButtonColumn));

        final TableColumn<StationData, Integer> ownGradeColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN_GRADE);
        ownGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ownGrade"));
        TableStationFactory.columnFactoryGrade(this.table_enum, ownGradeColumn);

        final TableColumn<StationData, Integer> startsColumn = new TableColumn<>(StationDataXml.STATION_PROP_STARTS);
        startsColumn.setCellValueFactory(new PropertyValueFactory<>("starts"));
        startsColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryInteger(this.table_enum, startsColumn);

        final TableColumn<StationData, Integer> clickCountColumn = new TableColumn<>(StationDataXml.STATION_PROP_CLICK_COUNT);
        clickCountColumn.setCellValueFactory(new PropertyValueFactory<>("clickCount"));
        clickCountColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryInteger(this.table_enum, clickCountColumn);

        final TableColumn<StationData, Integer> clickTrendColumn = new TableColumn<>(StationDataXml.STATION_PROP_CLICK_TREND);
        clickTrendColumn.setCellValueFactory(new PropertyValueFactory<>("clickTrend"));
        clickTrendColumn.getStyleClass().add("alignCenterRightPadding_10");
        TableStationFactory.columnFactoryInteger(this.table_enum, clickTrendColumn);

        final TableColumn<StationData, Integer> votesColumn = new TableColumn<>(StationDataXml.STATION_PROP_VOTES);
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
        votesColumn.getStyleClass().add("alignCenterRightPadding_10");
        TableStationFactory.columnFactoryInteger(this.table_enum, votesColumn);

        final TableColumn<StationData, String> genreColumn = new TableColumn<>(StationDataXml.STATION_PROP_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(this.table_enum, genreColumn);

        final TableColumn<StationData, String> codecColumn = new TableColumn<>(StationDataXml.STATION_PROP_CODEC);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryString(this.table_enum, codecColumn);

        final TableColumn<StationData, Integer> bitrateColumn = new TableColumn<>(StationDataXml.STATION_PROP_BITRATE);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");
        TableStationFactory.columnFactoryInteger(this.table_enum, bitrateColumn);

        final TableColumn<StationData, Boolean> ownColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN);
        ownColumn.setCellValueFactory(new PropertyValueFactory<>("own"));
        ownColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryBoolean(this.table_enum, ownColumn);

        final TableColumn<StationData, String> stateColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATE);
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(this.table_enum, stateColumn);

        final TableColumn<StationData, String> countryColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(this.table_enum, countryColumn);

        final TableColumn<StationData, String> countryCodeColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY_CODE);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryString(this.table_enum, countryCodeColumn);

        final TableColumn<StationData, String> languageColumn = new TableColumn<>(StationDataXml.STATION_PROP_LANGUAGE);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(this.table_enum, languageColumn);

        final TableColumn<StationData, LocalDate> stationDateColumnLastChange = new TableColumn<>(StationDataXml.STATION_PROP_DATE);
        stationDateColumnLastChange.setCellValueFactory(new PropertyValueFactory<>("stationDateLastChange"));
        stationDateColumnLastChange.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryLocalDate(this.table_enum, stationDateColumnLastChange);

        final TableColumn<StationData, LocalDateTime> stationDateLastStartColumn = new TableColumn<>(StationDataXml.STATION_PROP_DATE_LAST_START);
        stationDateLastStartColumn.setCellValueFactory(new PropertyValueFactory<>("stationDateLastStart"));
        stationDateLastStartColumn.getStyleClass().add("alignCenter");
        TableStationFactory.columnFactoryLocalDateTime(this.table_enum, stationDateLastStartColumn);

        final TableColumn<StationData, String> websiteColumn = new TableColumn<>(StationDataXml.STATION_PROP_WEBSITE);
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        websiteColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryHyperLink(this.table_enum, websiteColumn);

        final TableColumn<StationData, String> stationUrlColumn = new TableColumn<>(StationDataXml.STATION_PROP_URL);
        stationUrlColumn.setCellValueFactory(new PropertyValueFactory<>("stationUrl"));
        stationUrlColumn.getStyleClass().add("alignCenterLeft");
        TableStationFactory.columnFactoryString(this.table_enum, stationUrlColumn);

        stationNoColumn.setPrefWidth(70);
        stationNameColumn.setPrefWidth(150);
        genreColumn.setPrefWidth(100);
        stateColumn.setPrefWidth(100);
        countryColumn.setPrefWidth(100);
        languageColumn.setPrefWidth(100);
        websiteColumn.setPrefWidth(200);
        stationUrlColumn.setPrefWidth(200);

        setRowFactory(tv -> new TableRowStation<>(table_enum));

        if (this.table_enum.equals(Table.TABLE_ENUM.STATION)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, isNewStation, /*collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn, startsColumn,*/
                    clickCountColumn, clickTrendColumn, votesColumn,
                    genreColumn, codecColumn, bitrateColumn, ownColumn,
                    stateColumn, countryColumn, countryCodeColumn, languageColumn,
                    stationDateColumnLastChange/*, stationDateLastStartColumn*/, websiteColumn, stationUrlColumn);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.FAVOURITE)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, /*isFavouriteColumn, isNewStation*/ collectionNameColumn, startButtonColumn,
                    ownGradeColumn, startsColumn,
                    clickCountColumn, clickTrendColumn, votesColumn,
                    genreColumn, codecColumn, bitrateColumn, ownColumn,
                    stateColumn, countryColumn, countryCodeColumn, languageColumn,
                    stationDateColumnLastChange, stationDateLastStartColumn, websiteColumn, stationUrlColumn);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.HISTORY)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /* isNewStation, collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn,*/ startsColumn,
                    clickCountColumn, clickTrendColumn, votesColumn,
                    genreColumn, codecColumn, bitrateColumn, ownColumn,
                    stateColumn, countryColumn, countryCodeColumn, languageColumn,
                    stationDateColumnLastChange, stationDateLastStartColumn, websiteColumn, stationUrlColumn);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.SMALL_RADIO_STATION)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /* isNewStation, collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn, startsColumn,*/
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumn, stationDateLastStartColumn,*/, websiteColumn /*stationUrlColumn*/);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.SMALL_RADIO_FAVOURITE)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, /*isFavouriteColumn, isNewStation */collectionNameColumn, startButtonColumn,
                    ownGradeColumn, startsColumn,
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumns, tationDateLastStartColumn,*/, websiteColumn /*stationUrlColumn*/);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.SMALL_RADIO_HISTORY)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /* isNewStation, collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn,*/ startsColumn,
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumn, stationDateLastStartColumn,*/, websiteColumn /*stationUrlColumn*/);

        } else if (this.table_enum.equals(Table.TABLE_ENUM.OWN_AUTOSTART)) {
            getColumns().addAll(
                    stationNoColumn, stationNameColumn, favouriteColumn, /* isNewStation, collectionNameColumn,*/ startButtonColumn,
                    /*ownGradeColumn,*/ startsColumn,
                    /*clickCountColumn, clickTrendColumn, votesColumn,*/
                    genreColumn, codecColumn, bitrateColumn, /*ownColumn,*/
                    /*stateColumn, countryColumn,*/ countryCodeColumn, languageColumn/*,
                    stationDateColumn, stationDateLastStartColumn,*/, websiteColumn /*stationUrlColumn*/);
        }
    }
}
