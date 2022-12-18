/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.radiosLoadFromWeb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2radio.controller.data.station.StationData;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.IOException;

public class ReadJsonFactory {
    private static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    private ReadJsonFactory() {
    }

    public static void readJsonValue(StationData stationData, JsonParser jp) throws IOException {
        JsonToken jsonToken;
        while ((jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }

            String name = jp.getCurrentName();
            if (jp.nextToken() == null) {
                break;
            }

            String value = jp.getValueAsString();
            if (name == null || name.isEmpty() || value == null) {
                continue;
            }
            value = value.trim();

            switch (name) {
                case StationFieldNamesWeb.NAME:
                    stationData.setStationName(value);
                    break;
                case StationFieldNamesWeb.GENRE:
                    stationData.setGenre(value);
                    break;
                case StationFieldNamesWeb.CODEC:
                    stationData.setCodec(value);
                    break;
                case StationFieldNamesWeb.BITRATE:
                    stationData.setBitrate(value);
                    break;
                case StationFieldNamesWeb.COUNTRY:
                    stationData.setCountry(value);
                    break;
                case StationFieldNamesWeb.COUNTRY_CODE:
                    stationData.setCountryCode(value);
                    break;
                case StationFieldNamesWeb.STATE:
                    stationData.setState(value);
                    break;
                case StationFieldNamesWeb.LANGUAGE:
                    stationData.setLanguage(value);
                    break;
                case StationFieldNamesWeb.VOTES:
                    stationData.setVotes(value);
                    break;
                case StationFieldNamesWeb.CLICK_COUNT:
                    stationData.setClickCount(value);
                    break;
                case StationFieldNamesWeb.CLICK_TREND:
                    stationData.setClickTrend(value);
                    break;
                case StationFieldNamesWeb.URL:
                    stationData.setStationUrl(value);
                    break;
                case StationFieldNamesWeb.URL_RESOLVED:
                    stationData.setStationUrlResolved(value);
                    break;
                case StationFieldNamesWeb.HOMEPAGE:
                    stationData.setWebsite(value);
                    break;
                case StationFieldNamesWeb.LAST_CHANGE_TIME:
                    //"2020-08-21 10:40:59"
                    try {
                        PDate pd = new PDate(sdf_date_time.parse(value));
                        stationData.setStationDate(pd.get_dd_MM_yyyy());
                    } catch (Exception ex) {
                        stationData.setStationDate(value);
                    }
                    break;
            }
        }
    }
}
