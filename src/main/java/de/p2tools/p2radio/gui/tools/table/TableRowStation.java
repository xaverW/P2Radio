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

public class TableRowStation<T extends StationData> extends javafx.scene.control.TableRow {
    Table.TABLE_ENUM tableEnum;

    public TableRowStation(Table.TABLE_ENUM tableEnum) {
        this.tableEnum = tableEnum;
    }

    @Override
    public void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setStyle("");
            setTooltip(null);

        } else {
            StationData stationData = (StationData) item;
            if (stationData.getStart() != null && stationData.getStart().getStartStatus().isStateError()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText(stationData.getStart().getStartStatus().getErrorMessage());
                setTooltip(tooltip);
            }

            final boolean error = stationData.getStart() != null
                    && stationData.getStart().getStartStatus().isStateError();
            final boolean playing = stationData.getStart() != null; // schießt error mit ein!
            final boolean fav = stationData.isFavourite();
            final boolean newStation = stationData.isNewStation();

            setStyle("");
            if (error) {
                if (ProgColorList.STATION_ERROR_BG.isUse()) {
                    setStyle(ProgColorList.STATION_ERROR_BG.getCssBackground());
                }

            } else if (playing) {
                if (ProgColorList.STATION_RUN_BG.isUse()) {
                    setStyle(ProgColorList.STATION_RUN_BG.getCssBackground());
                }

            } else if (fav
                    && (tableEnum.equals(Table.TABLE_ENUM.STATION)
                    || tableEnum.equals(Table.TABLE_ENUM.HISTORY)
                    || tableEnum.equals(Table.TABLE_ENUM.SMALL_RADIO_STATION)
                    || tableEnum.equals(Table.TABLE_ENUM.SMALL_RADIO_HISTORY))
            ) {

                if (ProgColorList.STATION_FAVOURITE_BG.isUse()) {
                    setStyle(ProgColorList.STATION_FAVOURITE_BG.getCssBackground());
                }

            } else if (newStation) {
                if (ProgColorList.STATION_NEW_BG.isUse()) {
                    setStyle(ProgColorList.STATION_NEW_BG.getCssBackground());
                }
            }
        }
    }
}
