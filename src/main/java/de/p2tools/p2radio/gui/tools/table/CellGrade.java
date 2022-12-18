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

import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class CellGrade<S, T> extends TableCell<S, T> {

    public final Callback<TableColumn<StationData, Integer>, TableCell<StationData, Integer>> cellFactoryGrade
            = (final TableColumn<StationData, Integer> param) -> {

        final TableCell<StationData, Integer> cell = new TableCell<>() {

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
                            l.setGraphic(ProgIcons.Icons.IMAGE_TABLE_FAVOURITE_GRADE.getImageView());
                            hBox.getChildren().add(l);
                        }
                    }
                    setGraphic(hBox);
                }
            }
        };
        return cell;
    };
}
