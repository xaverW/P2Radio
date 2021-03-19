/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.config;


import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.configFile.pConfData.PColorList;
import de.p2tools.p2Lib.configFile.pData.PData;
import javafx.scene.paint.Color;

public class ProgColorList extends PColorList {

    private ProgColorList() {
        super();
    }

    public static final PColorData STATION_NEW = addNewKey("COLOR__STATION_NEW",
            Color.rgb(0, 0, 240),
            Color.rgb(0, 0, 240), "Tabelle Sender, neue");

    public static final PColorData STATION_RUN = addNewKey("COLOR__FAVOURITE_RUN",
            Color.rgb(255, 245, 176),
            Color.rgb(174, 150, 85), "Tabelle Sender/Favoriten, läuft");

    public static final PColorData STATION_ERROR = addNewKey("COLOR__FAVOURITE_ERROR",
            Color.rgb(255, 233, 233),
            Color.rgb(163, 82, 82), "Tabelle Sender/Favoriten, fehlerhaft");

    // Filter wenn RegEx
    public static final PColorData FILTER_REGEX = addNewKey("COLOR__FILTER_REGEX",
            Color.rgb(225, 255, 225),
            Color.rgb(128, 179, 213), "Filter ist RegEx");

    public static final PColorData FILTER_REGEX_ERROR = addNewKey("COLOR__FILTER_REGEX_ERROR",
            Color.rgb(255, 230, 230),
            Color.rgb(170, 0, 0), "Filter ist Regex, fehlerhaft");

    public static PData getConfigsData() {
        return PColorList.getPData();
    }
}
