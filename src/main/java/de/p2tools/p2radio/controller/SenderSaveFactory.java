/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;

import java.io.FileOutputStream;
import java.nio.file.Path;

public class SenderSaveFactory {

    public final static String NAME = "name";
    public final static String URL = "url";
    public final static String URL_RESOLVED = "url_resolved";
    public final static String HOMEPAGE = "homepage";
    public final static String GENRE = "tags";
    public final static String COUNTRY = "country";
    public final static String COUNTRY_CODE = "countrycode";
    public final static String STATE = "state";
    public final static String LANGUAGE = "language";
    public final static String VOTES = "votes";
    public final static String LAST_CHANGE_TIME = "lastchangetime";
    public final static String CODEC = "codec";
    public final static String BITRATE = "bitrate";
    public final static String CLICK_COUNT = "clickcount";
    public final static String CLICK_TREND = "clicktrend";

    private SenderSaveFactory() {
        //Json war schneller, beim Lesen und besonders beim Schreiben!
        //ProgSaveFactory.saveStationListXml();//370ms
        //SenderSaveFactory.saveStationListJson();//90ms
    }

    public static void saveStationListJson() {
        PDuration.counterStart("ProgSaveFactory.saveStationListJson");
        Path file = ProgInfos.getStationFileJson();
        StationList stationList = ProgData.getInstance().stationList;

        try {
            try (FileOutputStream fos = new FileOutputStream(file.toFile());
                 JsonGenerator jsonGenerator =
                         new JsonFactory().createGenerator(fos, JsonEncoding.UTF8).useDefaultPrettyPrinter()) {

                jsonGenerator.writeStartArray();
                jsonGenerator.writeStartObject();
                for (Config meta : stationList.getMeta().getConfigsArr()) {
                    jsonGenerator.writeStringField(meta.getKey(), meta.getActValueString());
                }
                jsonGenerator.writeEndObject();

                for (Station station : stationList) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField(SenderSaveFactory.NAME, station.arr[Station.STATION_NAME]);
                    jsonGenerator.writeStringField(SenderSaveFactory.GENRE, station.arr[Station.STATION_GENRE]);
                    jsonGenerator.writeStringField(SenderSaveFactory.CODEC, station.arr[Station.STATION_CODEC]);
                    jsonGenerator.writeStringField(SenderSaveFactory.BITRATE, station.arr[Station.STATION_BITRATE]);
                    jsonGenerator.writeStringField(SenderSaveFactory.COUNTRY, station.arr[Station.STATION_COUNTRY]);
                    jsonGenerator.writeStringField(SenderSaveFactory.COUNTRY_CODE, station.arr[Station.STATION_COUNTRY_CODE]);
                    jsonGenerator.writeStringField(SenderSaveFactory.STATE, station.arr[Station.STATION_STATE]);
                    jsonGenerator.writeStringField(SenderSaveFactory.LANGUAGE, station.arr[Station.STATION_LANGUAGE]);
                    jsonGenerator.writeStringField(SenderSaveFactory.VOTES, station.arr[Station.STATION_VOTES]);
                    jsonGenerator.writeStringField(SenderSaveFactory.CLICK_COUNT, station.arr[Station.STATION_CLICK_COUNT]);
                    jsonGenerator.writeStringField(SenderSaveFactory.CLICK_TREND, station.arr[Station.STATION_CLICK_TREND]);
                    jsonGenerator.writeStringField(SenderSaveFactory.URL, station.arr[Station.STATION_URL]);
                    jsonGenerator.writeStringField(SenderSaveFactory.URL_RESOLVED, station.arr[Station.STATION_URL_RESOLVED]);
                    jsonGenerator.writeStringField(SenderSaveFactory.HOMEPAGE, station.arr[Station.STATION_WEBSITE]);
                    jsonGenerator.writeStringField(SenderSaveFactory.LAST_CHANGE_TIME, station.arr[Station.STATION_DATE]);
                    jsonGenerator.writeEndObject();
                }

                jsonGenerator.writeEndArray();
            }
        } catch (Exception ex) {
            PLog.errorLog(846930145, ex, "nach: " + file);
        }
        PDuration.counterStop("ProgSaveFactory.saveStationListJson");
    }
}
