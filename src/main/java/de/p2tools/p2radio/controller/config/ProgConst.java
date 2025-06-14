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

package de.p2tools.p2radio.controller.config;

public class ProgConst {

    public static final String PROGRAM_NAME = "P2Radio";
    public static final String USER_AGENT_DEFAULT = PROGRAM_NAME;
    public static final int MAX_USER_AGENT_SIZE = 100;
    public static final int LOAD_STATION_LIST_EVERY_DAYS = 7;
    public static final int START_COUNTER_MIN_TIME = 60; //nach 1 Minute gilt der Sender als gespielt
    public static final int MAX_HISTORY_LIST_SIZE = ProgData.debug ? 5 : 50;

    // settings file
    public static final String CONFIG_FILE = "p2radio.xml";
    public static final String STYLE_FILE = "style.css";

    public static final String STATION_FILE_XML = "sender.xml";
    public static final String STATION_FILE_JSON = "radio.json";


    // http://at1.api.radio-browser.info/json/stations
    public static final String STATION_LIST_URL = "http://all.api.radio-browser.info/json/stations";
//    public static final String STATION_LIST_URL = "https://de2.api.radio-browser.info/json/stations?limit=100";

//    public static final String STATION_LIST_URL = "https://atlist.de/radio.json";
//    public static final String STATION_LIST_URL = "https://atlist.de/radio_middle.json";
//    public static final String STATION_LIST_URL = "https://atlist.de/radio_small.json";

    public static int STATION_LIST_MIN_SIZE = 5_000; // die mind. Größe die geladen werden muss damit OK

//    public static final String STATION_LIST_URL = "https://atlist.de/stations";

    public static final String CONFIG_DIRECTORY = "p2Radio"; // im Homeverzeichnis

    // public static final String PROGRAM_ICON = "de/p2tools/p2radio/res/P2_24.png";
    public static final String PROGRAM_ICON = "de/p2tools/p2radio/res/p2r_logo_32.png";

    public static final String LOG_DIR = "Log";
    public static final String CSS_FILE = "de/p2tools/p2radio/radioFx.css";
    public static final String CSS_FILE_DARK_THEME = "de/p2tools/p2radio/radioFx-dark.css";

    public static final String FORMAT_ZIP = ".zip";
    public static final String FORMAT_XZ = ".xz";

    // Website
    public static final String URL_WEBSITE = "https://www.p2tools.de/";
    public static final String URL_WEBSITE_DOWNLOAD = "https://www.p2tools.de/p2radio/download.html";
    public static final String URL_WEBSITE_HELP = "https://www.p2tools.de/p2radio/manual/";

    // ProgrammUrls
    public static final String URL_WEBSITE_VLC = "http://www.videolan.org";

    public static final double GUI_FILTER_DIVIDER_LOCATION = 0.3;
    public static final double GUI_INFO_DIVIDER_LOCATION = 0.7;
    public static final double CONFIG_DIALOG_SET_DIVIDER = 0.2;

    public static final int MIN_TABLE_HEIGHT = 200;
    public static final int MIN_TEXTAREA_HEIGHT_LOW = 50;
}
