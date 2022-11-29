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
import de.p2tools.p2radio.controller.data.playable.PlayableXml;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;
import org.apache.commons.lang3.time.FastDateFormat;
import org.tukaani.xz.XZInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class SenderLoadFactory {

    private static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private static int countAll = 0;

    public static boolean readList() {
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
        //vorerst mal drin lassen
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

            String value = jp.getValueAsString();
            if (name == null || name.isEmpty() || value == null) {
                continue;
            }
            value = value.trim();

            switch (name) {
                case SenderSaveFactory.NAME:
                    station.arr[PlayableXml.STATION_PROP_STATION_NAME_INT] = value;
                    break;
                case SenderSaveFactory.GENRE:
                    station.arr[PlayableXml.STATION_PROP_GENRE_INT] = value;
                    break;
                case SenderSaveFactory.CODEC:
                    station.arr[PlayableXml.STATION_PROP_CODEC_INT] = value;
                    break;
                case SenderSaveFactory.BITRATE:
                    station.setBitrate(value);
//                    station.arr[PlayableXml.STATION_PROP_BITRATE_INT] = value;
                    break;
                case SenderSaveFactory.COUNTRY:
                    station.arr[PlayableXml.STATION_PROP_COUNTRY_INT] = value;
                    break;
                case SenderSaveFactory.COUNTRY_CODE:
                    station.arr[PlayableXml.STATION_PROP_COUNTRY_CODE_INT] = value;
                    break;
                case SenderSaveFactory.STATE:
                    station.arr[PlayableXml.STATION_PROP_STATE_INT] = value;
                    break;
                case SenderSaveFactory.LANGUAGE:
                    station.arr[PlayableXml.STATION_PROP_LANGUAGE_INT] = value;
                    break;
                case SenderSaveFactory.VOTES:
                    station.setVotes(value);
//                    station.arr[PlayableXml.STATION_PROP_VOTES_INT] = value;
                    break;
                case SenderSaveFactory.CLICK_COUNT:
                    station.setClickCount(value);
//                    station.arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT] = value;
                    break;
                case SenderSaveFactory.CLICK_TREND:
                    station.setClickTrend(value);
//                    station.arr[PlayableXml.STATION_PROP_CLICK_TREND_INT] = value;
                    break;
                case SenderSaveFactory.URL:
                    station.arr[PlayableXml.STATION_PROP_URL_INT] = value;
                    break;
                case SenderSaveFactory.URL_RESOLVED:
                    station.arr[PlayableXml.STATION_PROP_URL_RESOLVED_INT] = value;
                    break;
                case SenderSaveFactory.HOMEPAGE:
                    station.arr[PlayableXml.STATION_PROP_WEBSITE_INT] = value;
                    break;
                case SenderSaveFactory.LAST_CHANGE_TIME:
                    //"2020-08-21 10:40:59"
                    try {
                        PDate pd = new PDate(sdf_date_time.parse(value));
                        station.arr[PlayableXml.STATION_PROP_DATE_INT] = pd.get_dd_MM_yyyy();
                    } catch (Exception ex) {
                        station.arr[PlayableXml.STATION_PROP_DATE_INT] = value;
                    }
                    break;
            }
        }
    }
}
