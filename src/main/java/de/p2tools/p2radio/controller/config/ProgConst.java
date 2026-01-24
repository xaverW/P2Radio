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

    public static final String STATION_LIST_URL = "https://de2.api.radio-browser.info/json/stations?limit=100000";
    // public static final String STATION_LIST_URL = "http://all.api.radio-browser.info/json/stations";
    // public static final String STATION_LIST_URL = "https://de2.api.radio-browser.info/json/stations?limit=100";

    // http://all.api.radio-browser.info/json/url/stationuuid
    public static final String STATION_CLICK_COUNT_URL = "http://all.api.radio-browser.info/json/url/";


    public static int STATION_LIST_MIN_SIZE = 5000; // die mind. Größe die geladen werden muss damit OK

    public static final String CONFIG_DIRECTORY = "p2Radio"; // im Homeverzeichnis

    public static final String PROGRAM_ICON = "de/p2tools/p2radio/res/p2r_logo_32.png";

    public static final String LOG_DIR = "Log";
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

    public static final String ICON_COLOR_DARK_1 = "#ffffff";
    public static final String ICON_COLOR_DARK_2 = "#000080";
    public static final String ICON_COLOR_LIGHT_1 = "#333333";
    public static final String ICON_COLOR_LIGHT_2 = "#4d66cc";

    public static final String GUI_COLOR_DARK_1 = "#cccccc";
    public static final String GUI_COLOR_DARK_2 = "#000080";
    public static final String GUI_COLOR_LIGHT_1 = "#666666";
    public static final String GUI_COLOR_LIGHT_2 = "#4d66cc";

    public static final String GUI_BACKGROUND_DARK_1 = "#333333";
    public static final String GUI_BACKGROUND_DARK_2 = "#a1a1a1";
    public static final String GUI_BACKGROUND_LIGHT_1 = "#cccccc";
    public static final String GUI_BACKGROUND_LIGHT_2 = "#d8d8d8";

    public static final String GUI_TITLE_BAR_DARK_1 = "#666666";
    public static final String GUI_TITLE_BAR_DARK_2 = "#000080";
    public static final String GUI_TITLE_BAR_LIGHT_1 = "#d8d8d8";
    public static final String GUI_TITLE_BAR_LIGHT_2 = "#99b3ff";

    public static final String GUI_TITLE_BAR_SEL_DARK_1 = "#333333";
    public static final String GUI_TITLE_BAR_SEL_DARK_2 = "#414180";
    public static final String GUI_TITLE_BAR_SEL_LIGHT_1 = "#999999";
    public static final String GUI_TITLE_BAR_SEL_LIGHT_2 = "#4d66cc";

    public static final boolean GUI_BACKGROUND_TRANSPARENT_DARK_1 = true;
    public static final boolean GUI_BACKGROUND_TRANSPARENT_DARK_2 = false;
    public static final boolean GUI_BACKGROUND_TRANSPARENT_LIGHT_1 = true;
    public static final boolean GUI_BACKGROUND_TRANSPARENT_LIGHT_2 = false;

    public static final boolean GUI_TITLE_BAR_TRANSPARENT_DARK_1 = true;
    public static final boolean GUI_TITLE_BAR_TRANSPARENT_DARK_2 = false;
    public static final boolean GUI_TITLE_BAR_TRANSPARENT_LIGHT_1 = true;
    public static final boolean GUI_TITLE_BAR_TRANSPARENT_LIGHT_2 = false;

    public static final boolean GUI_TITLE_BAR_SEL_TRANSPARENT_DARK_1 = false;
    public static final boolean GUI_TITLE_BAR_SEL_TRANSPARENT_DARK_2 = false;
    public static final boolean GUI_TITLE_BAR_SEL_TRANSPARENT_LIGHT_1 = false;
    public static final boolean GUI_TITLE_BAR_SEL_TRANSPARENT_LIGHT_2 = false;


}
