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
import javafx.scene.paint.Color;

public class ProgColorList extends PColorList {

    public static final PColorData STATION_NEW_BG = addNewKey("COLOR__STATION_NEW_BG",
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            false, "Neuer Sender, Tabellenzeile");
    public static final PColorData STATION_NEW = addNewKey("COLOR__STATION_NEW",
            Color.rgb(0, 0, 240),
            Color.rgb(0, 0, 240),
            true, "Neuer Sender, Schriftfarbe");

    public static final PColorData STATION_FAVOURITE_BG = addNewKey("COLOR__IS_FAVOURITE_BG",
            Color.rgb(224, 238, 255),
            Color.rgb(224, 238, 255), "Sender ist ein Favorit, Tabellenzeile");
    public static final PColorData STATION_FAVOURITE = addNewKey("COLOR__IS_FAVOURITE",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Sender ist ein Favorit, Schriftfarbe");

    public static final PColorData STATION_RUN_BG = addNewKey("COLOR__FAVOURITE_RUN_BG",
            Color.rgb(255, 245, 176),
            Color.rgb(174, 150, 85), "Sender läuft, Tabellenzeile");
    public static final PColorData STATION_RUN = addNewKey("COLOR__FAVOURITE_RUN",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Sender läuft, Schriftfarbe");

    public static final PColorData STATION_ERROR_BG = addNewKey("COLOR__FAVOURITE_ERROR_BG",
            Color.rgb(255, 233, 233),
            Color.rgb(163, 82, 82), "Sender ist fehlerhaft, Tabellenzeile");
    public static final PColorData STATION_ERROR = addNewKey("COLOR__FAVOURITE_ERROR",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Sender ist fehlerhaft, Schriftfarbe");

    public synchronized static PColorList getInstance() {
        return PColorList.getInst();
    }

    public static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < getInstance().size(); ++i) {
            getInstance().get(i).setColorTheme(dark);
        }
    }
}
