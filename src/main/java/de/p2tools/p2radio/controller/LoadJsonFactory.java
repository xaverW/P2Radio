/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.getNewStationList.radioBrowser.StationFieldNamesWeb;
import org.apache.commons.lang3.time.FastDateFormat;
import org.tukaani.xz.XZInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class LoadJsonFactory {

    private static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private static int countAll = 0;

    public static boolean readList() {
        //Json laden ~500ms
        boolean ret = false;

        final String pathJson = ProgInfos.getStationFileJsonString();
        final StationList stationList = ProgData.getInstance().stationList;

        read(pathJson, stationList);
        if (!stationList.isEmpty()) {
            //dann hats geklappt
            ret = true;
        }
        return ret;
    }

    private static void read(String sourceFile, final StationList stationList) {
        // lokale Datei laden
        PDuration.counterStart("LoadJsonFactory.read()");

        countAll = 0;
        List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(PLog.LILNE2);

        stationList.clear();
        logList.add("gespeicherte Senderliste aus Datei laden: " + sourceFile);
        processFromFile(sourceFile, stationList);

        logList.add("   erstellt am:        " + stationList.getGenDate());
        logList.add("   Anzahl Gesamtliste: " + countAll);
        logList.add("   Anzahl verwendet:   " + stationList.size());
        logList.add(PLog.LILNE2);
        logList.add("");
        PLog.addSysLog(logList);

        PDuration.counterStop("LoadJsonFactory.read()");
    }

    /**
     * Read a locally available stationList.
     *
     * @param source      file path as string
     * @param stationList the list to read to
     */
    private static void processFromFile(String source, StationList stationList) {
        try (InputStream in = selectDecompressor(source, new FileInputStream(source));
             JsonParser jp = new JsonFactory().createParser(in)) {
            readData(jp, stationList);
        } catch (final FileNotFoundException ex) {
            PLog.errorLog(894512369, "SenderListe existiert nicht: " + source);
            stationList.clear();
        } catch (final Exception ex) {
            PLog.errorLog(945123641, ex, "SenderListe: " + source);
            stationList.clear();
        }
    }

    private static InputStream selectDecompressor(String source, InputStream in) throws Exception {
        //vorerst mal drin lassen todo
        if (source.endsWith(ProgConst.FORMAT_XZ)) {
            in = new XZInputStream(in);
        } else if (source.endsWith(ProgConst.FORMAT_ZIP)) {
            final ZipInputStream zipInputStream = new ZipInputStream(in);
            zipInputStream.getNextEntry();
            in = zipInputStream;
        }
        return in;
    }

    private static void readData(JsonParser jp, StationList stationList) throws IOException {
        boolean meta = false;
        while (!ProgData.getInstance().loadNewStationList.isStop() && (jp.nextToken()) != null) {
            if (!meta && jp.isExpectedStartObjectToken()) {
                getMeta(stationList, jp);
                meta = true;
            }
            if (jp.isExpectedStartObjectToken()) {
                final Station station = new Station();
                addValue(station, jp);
                ++countAll;
                station.init(); // damit wird auch das Datum! gesetzt
                stationList.importStationOnlyWithNr(station);
            }
        }
        return;
    }

    private static void getMeta(StationList stationList, JsonParser jp) throws IOException {
        JsonToken jsonToken;
        while ((jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }

            String name = jp.getCurrentName();
            if (jp.nextToken() == null) {
                break;
            }

            String value = jp.getValueAsString().trim();
            if (name == null || name.isEmpty() || value == null) {
                continue;
            }

            switch (name) {
                case "stationDate":
                    PLocalDate pLocalDate = new PLocalDate(value);
                    stationList.getMeta().setStationDate(pLocalDate);
                    break;
            }
        }
    }

    private static void addValue(Station station, JsonParser jp) throws IOException {
        JsonToken jsonToken;
        while ((jsonToken = jp.nextToken()) != null) {
            if (jsonToken == JsonToken.END_OBJECT) {
                break;
            }

            String name = jp.getCurrentName();
            if (jp.nextToken() == null) {
                break;
            }

            String value = jp.getValueAsString().trim();
            if (name == null || name.isEmpty() || value == null) {
                continue;
            }

            switch (name) {
                case StationFieldNamesWeb.NAME:
                    station.arr[Station.STATION_NAME] = value;
                    break;
                case StationFieldNamesWeb.GENRE:
                    station.arr[Station.STATION_GENRE] = value;
                    break;
                case StationFieldNamesWeb.CODEC:
                    station.arr[Station.STATION_CODEC] = value;
                    break;
                case StationFieldNamesWeb.BITRATE:
                    station.arr[Station.STATION_BITRATE] = value;
                    break;
                case StationFieldNamesWeb.COUNTRY:
                    station.arr[Station.STATION_COUNTRY] = value;
                    break;
                case StationFieldNamesWeb.COUNTRY_CODE:
                    station.arr[Station.STATION_COUNTRY_CODE] = value;
                    break;
                case StationFieldNamesWeb.STATE:
                    station.arr[Station.STATION_STATE] = value;
                    break;
                case StationFieldNamesWeb.LANGUAGE:
                    station.arr[Station.STATION_LANGUAGE] = value;
                    break;
                case StationFieldNamesWeb.VOTES:
                    station.arr[Station.STATION_VOTES] = value;
                    break;
                case StationFieldNamesWeb.CLICK_COUNT:
                    station.arr[Station.STATION_CLICK_COUNT] = value;
                    break;
                case StationFieldNamesWeb.CLICK_TREND:
                    station.arr[Station.STATION_CLICK_TREND] = value;
                    break;
                case StationFieldNamesWeb.URL:
                    station.arr[Station.STATION_URL] = value;
                    break;
//                case StationFieldNamesWeb.URL_RESOLVED:
//                    station.arr[Station.STATION_URL_RESOLVED] = value;
//                    break;
                case StationFieldNamesWeb.HOMEPAGE:
                    station.arr[Station.STATION_WEBSITE] = value;
                    break;
                case StationFieldNamesWeb.LAST_CHANGE_TIME:
                    //"2020-08-21 10:40:59"
                    try {
                        PDate pd = new PDate(sdf_date_time.parse(value));
                        station.arr[Station.STATION_DATE] = pd.get_dd_MM_yyyy();
                    } catch (Exception ex) {
                        station.arr[Station.STATION_DATE] = value;
                    }
                    break;
            }
        }
    }
}
