/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.scene.control.Tooltip;

public class TableRowPlayable<T extends StationData> extends javafx.scene.control.TableRow {
    Table.TABLE_ENUM table_enum;

    public TableRowPlayable(Table.TABLE_ENUM table_enum) {
        this.table_enum = table_enum;
    }

    @Override
    public void updateItem(Object f, boolean empty) {
        super.updateItem(f, empty);

        StationData stationData = (StationData) f;
        setStyle("");
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).setStyle("");
        }

        if (stationData != null && !empty) {
            final boolean fav = stationData.isFavourite();
            final boolean playing = stationData.getStart() != null;
            final boolean error = stationData.getStart() != null && stationData.getStart().getStartStatus().isStateError();
            final boolean newStation = stationData.isNewStation();

            if (stationData.getStart() != null && stationData.getStart().getStartStatus().isStateError()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText(stationData.getStart().getStartStatus().getErrorMessage());
                setTooltip(tooltip);
            }

            if (error) {
                if (ProgColorList.STATION_ERROR_BG.isUse()) {
                    setStyle(ProgColorList.STATION_ERROR_BG.getCssBackground());
                }
                if (ProgColorList.STATION_ERROR.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_ERROR.getCssFont());
                    }
                }

            } else if (playing) {
                if (ProgColorList.STATION_RUN_BG.isUse()) {
                    setStyle(ProgColorList.STATION_RUN_BG.getCssBackground());
                }
                if (ProgColorList.STATION_RUN.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_RUN.getCssFont());
                    }
                }

            } else if (newStation) {
                // neue Sender
                if (ProgColorList.STATION_NEW_BG.isUse()) {
                    setStyle(ProgColorList.STATION_NEW_BG.getCssBackground());
                }
                if (ProgColorList.STATION_NEW.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_NEW.getCssFont());
                    }
                }

            } else if (table_enum.equals(Table.TABLE_ENUM.STATION) && fav) {
                if (ProgColorList.STATION_FAVOURITE_BG.isUse()) {
                    setStyle(ProgColorList.STATION_FAVOURITE_BG.getCssBackground());
                }
                if (ProgColorList.STATION_FAVOURITE.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_FAVOURITE.getCssFont());
                    }
                }
            }
        }
    }
}
