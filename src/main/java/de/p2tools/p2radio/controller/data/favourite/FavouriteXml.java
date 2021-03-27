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

package de.p2tools.p2radio.controller.data.favourite;


import de.p2tools.p2Lib.configFile.pData.PDataSample;

public class FavouriteXml extends PDataSample<Favourite> {
    public static final String TAG = "Favourite";

    public static final int FAVOURITE_NO = 0;
    public static final int FAVOURITE_STATION_NO = 1;
    public static final int FAVOURITE_STATION = 2;
    public static final int FAVOURITE_COLLECTION = 3;
    public static final int FAVOURITE_GRADE = 4;
    public static final int FAVOURITE_GENRE = 5;
    public static final int FAVOURITE_CODEC = 6;
    public static final int FAVOURITE_BITRATE = 7;
    public static final int FAVOURITE_OWN = 8;
    public static final int FAVOURITE_CLICK_COUNT = 9;

    public static final int FAVOURITE_BUTTON1 = 10;
    public static final int FAVOURITE_BUTTON2 = 11;

    public static final int FAVOURITE_COUNTRY = 12;
    public static final int FAVOURITE_COUNTRY_CODE = 13;
    public static final int FAVOURITE_LANGUAGE = 14;
    public static final int FAVOURITE_DESCRIPTION = 15;

    public static final int FAVOURITE_URL = 16;
    public static final int FAVOURITE_WEBSITE = 17;
    public static final int FAVOURITE_DATE = 18;
    public static final int FAVOURITE_DATE_LONG = 19;

    public static final String[] COLUMN_NAMES = {"Nr",
            "SenderNr",
            "Sender",
            "Sammlung",
            "Bewertung",
            "Genre",
            "Codec",
            "Bitrate",
            "Eigener",
            "Starts",
            "",
            "",
            "Land",
            "Land",
            "Sprache",
            "Beschreibung",
            "URL",
            "Website",
            "Datum",
            "DatumL"};

    public static int MAX_ELEM = COLUMN_NAMES.length;
}
