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

package de.p2tools.p2radio.controller;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.WriteConfigFile;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.getNewStationList.radioBrowser.StationFieldNamesWeb;

import java.io.FileOutputStream;
import java.nio.file.Path;

public class ProgSaveFactory {

    private ProgSaveFactory() {
    }

    public static void saveAll() {
        PLog.sysLog("save all data");
        saveProgConfig();
        saveStationListXml();
    }

    public static void saveProgConfig() {
        //sind die Programmeinstellungen
        PLog.sysLog("save progConfig");

        final Path xmlFilePath = ProgInfos.getSettingsFile();
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, xmlFilePath);
        ProgConfig.addConfigData(configFile);

        WriteConfigFile writeConfigFile = new WriteConfigFile();
        writeConfigFile.addConfigFile(configFile);
        writeConfigFile.writeConfigFile();
    }

    public static void saveStationListXml() {
        //ist die Senderliste
        PLog.sysLog("save stationlist");
        final Path path = ProgInfos.getStationFileXml();

        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, path);
        configFile.addConfigs(ProgData.getInstance().stationList);

        WriteConfigFile writeConfigFile = new WriteConfigFile();
        writeConfigFile.addConfigFile(configFile);
        writeConfigFile.writeConfigFile(false);
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
                    jsonGenerator.writeStringField(StationFieldNamesWeb.NAME, station.arr[Station.STATION_NAME]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.GENRE, station.arr[Station.STATION_GENRE]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.CODEC, station.arr[Station.STATION_CODEC]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.BITRATE, station.arr[Station.STATION_BITRATE]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.COUNTRY, station.arr[Station.STATION_COUNTRY]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.COUNTRY_CODE, station.arr[Station.STATION_COUNTRY_CODE]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.STATE, station.arr[Station.STATION_STATE]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.LANGUAGE, station.arr[Station.STATION_LANGUAGE]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.VOTES, station.arr[Station.STATION_VOTES]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.CLICK_COUNT, station.arr[Station.STATION_CLICK_COUNT]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.CLICK_TREND, station.arr[Station.STATION_CLICK_TREND]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.URL, station.arr[Station.STATION_URL]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.URL_RESOLVED, station.arr[Station.STATION_URL_RESOLVED]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.HOMEPAGE, station.arr[Station.STATION_WEBSITE]);
                    jsonGenerator.writeStringField(StationFieldNamesWeb.LAST_CHANGE_TIME, station.arr[Station.STATION_DATE]);
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
