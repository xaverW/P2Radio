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

import de.p2tools.p2Lib.configFile.pData.PDataSample;
import org.apache.commons.lang3.time.FastDateFormat;

public class StationXml extends PDataSample<Station> {

    static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    static final FastDateFormat sdf_date = FastDateFormat.getInstance("dd.MM.yyyy");

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

    @Override
    public int compareTo(Station arg0) {
        int ret;
        if ((ret = sorter.compare(arr[STATION_NAME], arg0.arr[STATION_NAME])) == 0) {
            return sorter.compare(arr[STATION_GENRE], arg0.arr[STATION_GENRE]);
        }
        return ret;
    }
}
