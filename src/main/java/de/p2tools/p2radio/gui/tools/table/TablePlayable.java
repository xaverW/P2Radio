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
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

        final GermanStringIntSorter sorter = GermanStringIntSorter.getInstance();

//        final TableColumn<T, Integer> nrColumn = new TableColumn<>(StationDataXml.STATION_PROP_NO);
//        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
//        nrColumn.setCellFactory(new CellNo().cellFactoryNo);
//        nrColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, Integer> senderNoColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NO);
        senderNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        senderNoColumn.setCellFactory(new CellNo().cellFactoryNo);
        senderNoColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, String> senderColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATION_NAME);
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        senderColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, String> collectionColumn = new TableColumn<>(StationDataXml.STATION_PROP_COLLECTION);
        collectionColumn.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
        collectionColumn.getStyleClass().add("alignCenterLeft");
        collectionColumn.setComparator(sorter);

        final TableColumn<T, Integer> startColumn = new TableColumn<>("");
        startColumn.getStyleClass().add("alignCenter");
        switch (this.table_enum) {
            case STATION:
                startColumn.setCellFactory(new CellStartStation().cellFactoryStart);
                break;
            case FAVOURITE:
                startColumn.setCellFactory(new CellStartFavourite().cellFactoryButton);
                break;
            case HISTORY:
                startColumn.setCellFactory(new CellStartHistory().cellFactoryButton);
                break;
            case SMALL_RADIO:
            default:
                startColumn.setCellFactory(new CellStartSmallRadio().cellFactoryButton);
        }

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

        final TableColumn<T, String> genreColumn = new TableColumn<>(StationDataXml.STATION_PROP_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, PDate> codecColumn = new TableColumn<>(StationDataXml.STATION_PROP_CODEC);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, Integer> bitrateColumn = new TableColumn<>(StationDataXml.STATION_PROP_BITRATE);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
//        bitrateColumn.setCellFactory(cellFactoryBitrate);
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<T, Integer> ownColumn = new TableColumn<>(StationDataXml.STATION_PROP_OWN);
        ownColumn.setCellValueFactory(new PropertyValueFactory<>("own"));
        ownColumn.setCellFactory(new PCheckBoxCell().cellFactoryBool);
        ownColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> stateColumn = new TableColumn<>(StationDataXml.STATION_PROP_STATE);
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        stateColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, PDate> countryColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, PDate> countryCodeColumn = new TableColumn<>(StationDataXml.STATION_PROP_COUNTRY_CODE);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> languageColumn = new TableColumn<>(StationDataXml.STATION_PROP_LANGUAGE);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, Integer> votesColumn = new TableColumn<>(StationDataXml.STATION_PROP_VOTES);
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
        votesColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<T, PLocalDate> datumColumn = new TableColumn<>(StationDataXml.STATION_PROP_DATE);
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("stationDate"));
        datumColumn.getStyleClass().add("alignCenter");

        final TableColumn<T, String> websiteColumn = new TableColumn<>(StationDataXml.STATION_PROP_WEBSITE);
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));
        websiteColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<T, String> urlColumn = new TableColumn<>(StationDataXml.STATION_PROP_URL);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("stationUrl"));
        urlColumn.getStyleClass().add("alignCenterLeft");

//        nrColumn.setPrefWidth(50);
        senderNoColumn.setPrefWidth(70);
        senderColumn.setPrefWidth(80);
        genreColumn.setPrefWidth(180);

        setRowFactory(tv -> new TableRowPlayable<>(table_enum));

        getColumns().addAll(
                /*nrColumn,*/ senderNoColumn,
                senderColumn, collectionColumn, startColumn);
        if (!this.table_enum.equals(Table.TABLE_ENUM.STATION)) {
            getColumns().addAll(ownGradeColumn);
        }
        getColumns().addAll(
                votesColumn);
        if (!this.table_enum.equals(Table.TABLE_ENUM.STATION)) {
            getColumns().addAll(startsColumn);
        }
        getColumns().addAll(
                clickCountColumn, clickTrendColumn,
                genreColumn, codecColumn, bitrateColumn, ownColumn, stateColumn,
                countryColumn, countryCodeColumn, languageColumn,
                datumColumn, websiteColumn, urlColumn);
    }
}
