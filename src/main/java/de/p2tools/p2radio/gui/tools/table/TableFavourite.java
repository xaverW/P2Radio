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
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.playable.PlayableXml;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableFavourite extends PTable<Favourite> {

    public TableFavourite(Table.TABLE_ENUM table_enum, ProgData progData) {
        super(table_enum);
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

        final TableColumn<Favourite, Integer> nrColumn = new TableColumn<>(PlayableXml.STATION_PROP_NO);
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        nrColumn.setCellFactory(new CellNo().cellFactoryNo);
        nrColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, Integer> senderNoColumn = new TableColumn<>(PlayableXml.STATION_PROP_STATION_NO);
        senderNoColumn.setCellValueFactory(new PropertyValueFactory<>("stationNo"));
        senderNoColumn.setCellFactory(new CellNo().cellFactoryNo);
        senderNoColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, String> senderColumn = new TableColumn<>(PlayableXml.STATION_PROP_STATION_NAME);
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        senderColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, String> collectionColumn = new TableColumn<>(PlayableXml.STATION_PROP_COLLECTION);
        collectionColumn.setCellValueFactory(new PropertyValueFactory<>("collectionName"));
        collectionColumn.getStyleClass().add("alignCenterLeft");
        collectionColumn.setComparator(sorter);

        final TableColumn<Favourite, Integer> gradeColumn = new TableColumn<>(PlayableXml.STATION_PROP_GRADE);
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        gradeColumn.setCellFactory(new CellGrade().cellFactoryGrade);

        final TableColumn<Favourite, Integer> clickCountColumn = new TableColumn<>(PlayableXml.STATION_PROP_CLICK_COUNT);
        clickCountColumn.setCellValueFactory(new PropertyValueFactory<>("clickCount"));
        clickCountColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, String> genreColumn = new TableColumn<>(PlayableXml.STATION_PROP_GENRE);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, PDate> codecColumn = new TableColumn<>(PlayableXml.STATION_PROP_CODEC);
        codecColumn.setCellValueFactory(new PropertyValueFactory<>("codec"));
        codecColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, Integer> bitrateColumn = new TableColumn<>(PlayableXml.STATION_PROP_BITRATE);
        bitrateColumn.setCellValueFactory(new PropertyValueFactory<>("bitrate"));
//        bitrateColumn.setCellFactory(cellFactoryBitrate);
        bitrateColumn.getStyleClass().add("alignCenterRightPadding_10");

        final TableColumn<Favourite, Integer> ownColumn = new TableColumn<>(PlayableXml.STATION_PROP_OWN);
        ownColumn.setCellValueFactory(new PropertyValueFactory<>("own"));
        ownColumn.setCellFactory(new PCheckBoxCell().cellFactoryBool);
        ownColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, Integer> startColumn = new TableColumn<>("");
        startColumn.setCellFactory(new CellFavouriteStart().cellFactoryButton);
        startColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, PDate> countryColumn = new TableColumn<>(PlayableXml.STATION_PROP_COUNTRY);
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, PDate> countryCodeColumn = new TableColumn<>(PlayableXml.STATION_PROP_COUNTRY_CODE);
        countryCodeColumn.setCellValueFactory(new PropertyValueFactory<>("countryCode"));
        countryCodeColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, String> languageColumn = new TableColumn<>(PlayableXml.STATION_PROP_LANGUAGE);
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<Favourite, PLocalDate> datumColumn = new TableColumn<>(PlayableXml.STATION_PROP_DATE);
        datumColumn.setCellValueFactory(new PropertyValueFactory<>("stationDate"));
        datumColumn.getStyleClass().add("alignCenter");

        final TableColumn<Favourite, String> urlColumn = new TableColumn<>(PlayableXml.STATION_PROP_URL);
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("stationUrl"));
        urlColumn.getStyleClass().add("alignCenterLeft");

        nrColumn.setPrefWidth(50);
        senderNoColumn.setPrefWidth(70);
        senderColumn.setPrefWidth(80);
        genreColumn.setPrefWidth(180);

        addRowFact();
        getColumns().addAll(
                nrColumn, senderNoColumn,
                senderColumn, collectionColumn, gradeColumn, clickCountColumn, genreColumn,
                codecColumn, bitrateColumn, ownColumn, startColumn, countryColumn, countryCodeColumn, languageColumn,
                datumColumn, urlColumn);
    }

    private void addRowFact() {
        setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(Favourite favourite, boolean empty) {
                super.updateItem(favourite, empty);
                setStyle("");
                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle("");
                }

                if (favourite != null && !empty) {
                    if (favourite.getStart() != null && favourite.getStart().getStartStatus().isStateError()) {
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(favourite.getStart().getStartStatus().getErrorMessage());
                        setTooltip(tooltip);
                    }

                    final boolean playing = favourite.getStart() != null;
                    final boolean error = favourite.getStart() != null && favourite.getStart().getStartStatus().isStateError();
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
                    }
                }
            }
        });
    }
}
