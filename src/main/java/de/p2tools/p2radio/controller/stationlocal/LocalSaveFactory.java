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


package de.p2tools.p2radio.controller.stationlocal;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.stationload.StationFieldNames;

import java.io.FileOutputStream;
import java.nio.file.Path;

public class LocalSaveFactory {

    private LocalSaveFactory() {
        //Json war schneller, beim Lesen und besonders beim Schreiben!
        //ProgSaveFactory.saveStationListXml();//370ms
        //SenderSaveFactory.saveStationListJson();//90ms
    }

    public static void saveStationListJson() {
        P2Duration.counterStart("ProgSaveFactory.saveStationListJson");
        Path file = ProgInfos.getStationFileJson();
        StationList stationList = ProgData.getInstance().stationList;

        try {
            try (FileOutputStream fos = new FileOutputStream(file.toFile());
                 JsonGenerator jsonGenerator =
                         new JsonFactory().createGenerator(fos, JsonEncoding.UTF8).useDefaultPrettyPrinter()) {

                jsonGenerator.writeStartArray();

                //Datum der Liste
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField(StationList.KEY_STATION_DATE, P2LDateFactory.toString(stationList.getStationDate()));
                jsonGenerator.writeEndObject();

                // station
                for (StationData station : stationList) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeStringField(StationFieldNames.NAME, station.getStationName());
                    if (station.isNewStation()) {
                        jsonGenerator.writeStringField(StationFieldNames.NEW, "true");
                    }
                    jsonGenerator.writeStringField(StationFieldNames.GENRE, station.getGenre());
                    jsonGenerator.writeStringField(StationFieldNames.CODEC, station.getCodec());
                    jsonGenerator.writeStringField(StationFieldNames.BITRATE, station.getBitrateStr());
                    jsonGenerator.writeStringField(StationFieldNames.COUNTRY, station.getCountry());
                    jsonGenerator.writeStringField(StationFieldNames.COUNTRY_CODE, station.getCountryCode());
                    jsonGenerator.writeStringField(StationFieldNames.STATE, station.getState());
                    jsonGenerator.writeStringField(StationFieldNames.LANGUAGE, station.getLanguage());
                    jsonGenerator.writeStringField(StationFieldNames.VOTES, station.getVotes() + "");
                    jsonGenerator.writeStringField(StationFieldNames.CLICK_COUNT, station.getClickCount() + "");
                    jsonGenerator.writeStringField(StationFieldNames.CLICK_TREND, station.getClickTrend() + "");
                    jsonGenerator.writeStringField(StationFieldNames.URL, station.getStationUrl());
                    jsonGenerator.writeStringField(StationFieldNames.URL_RESOLVED, station.getStationUrlResolved());
                    jsonGenerator.writeStringField(StationFieldNames.HOMEPAGE, station.getWebsite());
                    jsonGenerator.writeStringField(StationFieldNames.LAST_CHANGE_TIME, station.getStationDateLastChange().toString());
                    jsonGenerator.writeEndObject();
                }

                jsonGenerator.writeEndArray();
            }
        } catch (Exception ex) {
            P2Log.errorLog(846930145, ex, "nach: " + file);
        }
        P2Duration.counterStop("ProgSaveFactory.saveStationListJson");
    }
}
