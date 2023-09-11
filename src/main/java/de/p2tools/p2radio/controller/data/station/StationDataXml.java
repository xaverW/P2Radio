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

package de.p2tools.p2radio.controller.data.station;


public class StationDataXml {
    public static final String TAG = "STATION_PROPERTY";

    public static final int STATION_PROP_STATION_NO_INT = 0;
    public static final String STATION_PROP_STATION_NO = "SenderNr";


    public static final int STATION_PROP_STATION_NEW_INT = 1;
    public static final String STATION_PROP_STATION_NEW = "Neu";

    public static final int STATION_PROP_STATION_NAME_INT = 2;
    public static final String STATION_PROP_STATION_NAME = "Sender";

    public static final int STATION_PROP_COLLECTION_INT = 3;
    public static final String STATION_PROP_COLLECTION = "Sammlung";

    public static final int STATION_PROP_OWN_GRADE_INT = 4;
    public static final String STATION_PROP_OWN_GRADE = "Eigene Bewertung";

    public static final int STATION_PROP_STARTS_INT = 5;
    public static final String STATION_PROP_STARTS = "Starts";

    public static final int STATION_PROP_CLICK_COUNT_INT = 6;
    public static final String STATION_PROP_CLICK_COUNT = "Clicks";

    public static final int STATION_PROP_CLICK_TREND_INT = 7;
    public static final String STATION_PROP_CLICK_TREND = "Trend";


    public static final int STATION_PROP_GENRE_INT = 8;
    public static final String STATION_PROP_GENRE = "Genre";

    public static final int STATION_PROP_CODEC_INT = 9;
    public static final String STATION_PROP_CODEC = "Codec";

    public static final int STATION_PROP_BITRATE_INT = 10;
    public static final String STATION_PROP_BITRATE = "Bitrate";

    public static final int STATION_PROP_OWN_INT = 11;
    public static final String STATION_PROP_OWN = "Eigener";


    public static final int STATION_PROP_COUNTRY_INT = 12;
    public static final String STATION_PROP_COUNTRY = "Land";

    public static final int STATION_PROP_STATE_INT = 13;
    public static final String STATION_PROP_STATE = "Region";

    public static final int STATION_PROP_COUNTRY_CODE_INT = 14;
    public static final String STATION_PROP_COUNTRY_CODE = "Länderkürzel";

    public static final int STATION_PROP_LANGUAGE_INT = 15;
    public static final String STATION_PROP_LANGUAGE = "Sprache";

    public static final int STATION_PROP_VOTES_INT = 16;
    public static final String STATION_PROP_VOTES = "Bewertung";

    public static final int STATION_PROP_DESCRIPTION_INT = 17;
    public static final String STATION_PROP_DESCRIPTION = "Beschreibung";

    public static final int STATION_PROP_DATE_INT = 18;
    public static final String STATION_PROP_DATE = "Datum";

    public static final int STATION_PROP_DATE_LONG_INT = 19;
    public static final String STATION_PROP_DATE_LONG = "DatumL";

    public static final int STATION_PROP_URL_INT = 20;
    public static final String STATION_PROP_URL = "URL";

    public static final int STATION_PROP_DOUBLE_URL_INT = 21;
    public static final String STATION_PROP_DOUBLE_URL = "Doppelt";

    public static final int STATION_PROP_IS_FAVOURITE_INT = 22;
    public static final String STATION_PROP_IS_FAVOURITE = "Favorite";

    public static final int STATION_PROP_BLACK_BLOCKED_URL_INT = 23;
    public static final String STATION_PROP_BLACK_BLOCKED_URL = "BlackBlocked";

    public static final int STATION_PROP_URL_RESOLVED_INT = 24;
    public static final String STATION_PROP_URL_RESOLVED = "URL-resolved";

    public static final int STATION_PROP_WEBSITE_INT = 25;
    public static final String STATION_PROP_WEBSITE = "Website";

    public static final int STATION_PROP_BUTTON1_INT = 26;
    public static final String STATION_PROP_BUTTON1 = "";
    public static final int STATION_PROP_BUTTON2_INT = 27;
    public static final String STATION_PROP_BUTTON2 = "";

    public static String[] COLUMN_NAMES = {
            STATION_PROP_STATION_NO, STATION_PROP_STATION_NEW,
            STATION_PROP_STATION_NAME, STATION_PROP_COLLECTION, STATION_PROP_OWN_GRADE,
            STATION_PROP_STARTS, STATION_PROP_CLICK_COUNT, STATION_PROP_CLICK_TREND, STATION_PROP_GENRE, STATION_PROP_CODEC,
            STATION_PROP_BITRATE, STATION_PROP_OWN,
            STATION_PROP_COUNTRY, STATION_PROP_STATE, STATION_PROP_COUNTRY_CODE, STATION_PROP_LANGUAGE, STATION_PROP_VOTES,
            STATION_PROP_DESCRIPTION, STATION_PROP_DATE, STATION_PROP_DATE_LONG,
            STATION_PROP_URL, STATION_PROP_DOUBLE_URL, STATION_PROP_IS_FAVOURITE, STATION_PROP_BLACK_BLOCKED_URL,
            STATION_PROP_URL_RESOLVED, STATION_PROP_WEBSITE,
            STATION_PROP_BUTTON1, STATION_PROP_BUTTON2
    };
    public static int MAX_ELEM = 28;
}
