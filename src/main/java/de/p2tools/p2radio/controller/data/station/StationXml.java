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

public class StationXml {

//    {"changeuuid":"610cafba-71d8-40fc-bf68-1456ec973b9d",
//    "stationuuid":"941ef6f1-0699-4821-95b1-2b678e3ff62e",
//    "serveruuid":null,
//    "name":"\tBest FM",
//    "url":"http://stream.bestfm.sk/128.mp3",
//    "url_resolved":"http://stream.bestfm.sk/128.mp3",
//    "homepage":"http://bestfm.sk/",
//    "favicon":"",
//    "tags":"",
//    "country":"Slovakia",
//    "countrycode":"SK",
//    "iso_3166_2":null,
//    "state":"",
//    "language":"",
//    "languagecodes":"",
//    "votes":2,
//    "lastchangetime":"2022-11-01 08:42:32",
//    "lastchangetime_iso8601":"2022-11-01T08:42:32Z",
//    "codec":"MP3",
//    "bitrate":128,
//    "hls":0,
//    "lastcheckok":1,
//    "lastchecktime":"2022-11-29 10:56:00",
//    "lastchecktime_iso8601":"2022-11-29T10:56:00Z",
//    "lastcheckoktime":"2022-11-29 10:56:00",
//    "lastcheckoktime_iso8601":"2022-11-29T10:56:00Z",
//    "lastlocalchecktime":"",
//    "lastlocalchecktime_iso8601":null,
//    "clicktimestamp":"2022-11-29 13:21:28",
//    "clicktimestamp_iso8601":"2022-11-29T13:21:28Z",
//    "clickcount":38,
//    "clicktrend":-3,
//    "ssl_error":0,
//    "geo_lat":null,
//    "geo_long":null,
//    "has_extended_info":false}

    public static final String TAG = "Station";
    public static final String TAG_JSON_LIST = "X";

    public static final int STATION_NO = 0;
    public static final int STATION_NEW = 1;
    public static final int STATION_NAME = 2;
    public static final int STATION_GENRE = 3;
    public static final int STATION_CODEC = 4;
    public static final int STATION_BITRATE = 5;
    public static final int STATION_PLAY = 6;
    public static final int STATION_RECORD = 7;
    public static final int STATION_STATE = 8;
    public static final int STATION_COUNTRY = 9;
    public static final int STATION_COUNTRY_CODE = 10;
    public static final int STATION_LANGUAGE = 11;
    public static final int STATION_VOTES = 12;
    public static final int STATION_CLICK_COUNT = 13;
    public static final int STATION_CLICK_TREND = 14;
    public static final int STATION_DATE = 15;
    public static final int STATION_WEBSITE = 16;
    public static final int STATION_URL = 17;
    public static final int STATION_URL_RESOLVED = 18;

    public static final int MAX_ELEM = 19;
    public static final String[] COLUMN_NAMES = {
            "Nr",
            "Neu",
            "Name",
            "Genre",
            "Codec",
            "Bitrate",
            "",
            "",
            "Region",
            "Land",
            "Länderkürzel",
            "Sprache",
            "Bewertung",
            "Klickzahl",
            "Trend",
            "Datum",
            "Website",
            "Url",
            "Url Res",
    };
    public final String[] arr = new String[]{
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    }; // ist einen Tick schneller, hoffentlich :)
}
