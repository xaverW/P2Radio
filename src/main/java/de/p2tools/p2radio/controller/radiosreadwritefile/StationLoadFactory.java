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

package de.p2tools.p2radio.controller.radiosreadwritefile;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.p2tools.p2lib.tools.date.PLDateFactory;
import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.radiosloadfromweb.ReadJsonFactory;
import org.tukaani.xz.XZInputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class StationLoadFactory {

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
        PLog.sysLog(logList);

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
                final StationData stationData = new StationData();
                ReadJsonFactory.readJsonValue(stationData, jp);
                ++countAll;
                stationList.importStationOnlyWithNr(stationData);
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
                case StationList.KEY_STATION_DATE:
                    LocalDate pLocalDate = PLDateFactory.fromString(value);
                    stationList.setStationDate(pLocalDate);
                    break;
            }
        }
    }
}
