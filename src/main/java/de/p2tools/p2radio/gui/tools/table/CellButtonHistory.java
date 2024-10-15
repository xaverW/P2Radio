/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.history.HistoryFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellButtonHistory<S, T> extends TableCell<S, T> {
    public final Callback<TableColumn<StationData, Integer>, TableCell<StationData, Integer>> cellFactoryButton
            = (final TableColumn<StationData, Integer> param) -> {

        final TableCell<StationData, Integer> cell = new TableCell<>() {

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

                StationData stationData = getTableView().getItems().get(getIndex());
                final boolean playing = stationData.getStart() != null;

                if (playing) {
                    //dann stoppen
                    final Button btnStop;
                    btnStop = new Button("");
                    btnStop.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnStop.setTooltip(new Tooltip("Sender stoppen"));
                    btnStop.setGraphic(ProgIcons.IMAGE_TABLE_STATION_STOP_PLAY.getImageView());
                    btnStop.setOnAction((ActionEvent event) -> {
                        StartFactory.stopRunningStation();
//                        StartFactory.stopPlayable(stationData);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
                        btnStop.setMinHeight(18);
                        btnStop.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btnStop);

                } else {
                    //starten, nur ein Set
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(ProgIcons.IMAGE_TABLE_STATION_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        StartFactory.playPlayable(stationData);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
                        btnPlay.setMinHeight(18);
                        btnPlay.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);
                }

                final Button btnDel;
                btnDel = new Button("");
                btnDel.getStyleClass().addAll("btnFunction", "btnFuncTable");
                btnDel.setTooltip(new Tooltip("Sender aus History löschen"));
                btnDel.setGraphic(ProgIcons.IMAGE_TABLE_FAVOURITE_DEL.getImageView());
                btnDel.setOnAction(event -> {
                    HistoryFactory.deleteHistory(stationData);
                });

                if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
                    btnDel.setMinHeight(18);
                    btnDel.setMaxHeight(18);
                }
                hbox.getChildren().add(btnDel);
                setGraphic(hbox);
            }
        };
        return cell;
    };

}
