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
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellFavouriteStart<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<Favourite, Integer>, TableCell<Favourite, Integer>> cellFactoryButton
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
                final boolean error = favourite.getStart() != null && favourite.getStart().getStartStatus().isStateError();

                if (playing) {
                    //dann stoppen
                    final Button btnPlay;
                    btnPlay = new Button("");
                    btnPlay.getStyleClass().add("btnSmallRadio");
                    btnPlay.setTooltip(new Tooltip("Sender stoppen"));
                    btnPlay.setGraphic(ProgIcons.Icons.IMAGE_TABLE_STATION_STOP_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        ProgData.getInstance().startFactory.stopFavourite(favourite);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
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
                        ProgData.getInstance().startFactory.playFavourite(favourite);
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
                btnDel.getStyleClass().add("btnSmallRadio");

                btnDel.setTooltip(new Tooltip("Favoriten löschen"));
                btnDel.setGraphic(ProgIcons.Icons.IMAGE_TABLE_FAVOURITE_DEL.getImageView());
                btnDel.setOnAction(event -> {
                    FavouriteFactory.deleteFavourite(favourite);
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
