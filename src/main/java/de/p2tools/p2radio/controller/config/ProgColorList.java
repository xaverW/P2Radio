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


import de.p2tools.p2lib.data.P2ColorData;
import de.p2tools.p2lib.data.P2ColorList;
import javafx.scene.paint.Color;

public class ProgColorList extends P2ColorList {

    public synchronized static P2ColorList getInstance() {
        return P2ColorList.getInst();
    }

    // new
    public static final P2ColorData STATION_NEW_BG = addNewKey("COLOR__STATION_NEW_BG",
            Color.rgb(210, 210, 255),
            Color.rgb(210, 210, 255),
            false, "Neuer Sender, Tabellenzeile");
    public static final P2ColorData STATION_NEW_KEY = addNewKey("COLOR__STATION_NEW",
            Color.rgb(0, 0, 240),
            Color.rgb(0, 0, 240),
            true, "Neuer Sender, Schriftfarbe");

    // fav
    public static final P2ColorData STATION_FAVOURITE_BG = addNewKey("COLOR__IS_FAVOURITE_BG",
            Color.rgb(255, 251, 243),
            Color.rgb(255, 251, 243),
            false, "Sender ist ein Favorit, Tabellenzeile");
    public static final P2ColorData STATION_FAVOURITE_KEY = addNewKey("COLOR__IS_FAVOURITE",
            Color.rgb(122, 0, 0),
            Color.rgb(122, 0, 0),
            true, "Sender ist ein Favorit, Schriftfarbe");

    // run
    public static final P2ColorData STATION_RUN_BG = addNewKey("COLOR__FAVOURITE_RUN_BG",
            Color.rgb(198, 255, 198),
            Color.rgb(198, 255, 198),
            true, "Sender läuft, Tabellenzeile");
    public static final P2ColorData STATION_RUN_KEY = addNewKey("COLOR__FAVOURITE_RUN",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Sender läuft, Schriftfarbe");

    // error
    public static final P2ColorData STATION_ERROR_BG = addNewKey("COLOR__FAVOURITE_ERROR_BG",
            Color.rgb(163, 82, 82),
            Color.rgb(163, 82, 82),
            true, "Sender ist fehlerhaft, Tabellenzeile");
    public static final P2ColorData STATION_ERROR_KEY = addNewKey("COLOR__FAVOURITE_ERROR",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Sender ist fehlerhaft, Schriftfarbe");

    public static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < getInstance().size(); ++i) {
            getInstance().get(i).setColorTheme(dark);
        }
    }
}
