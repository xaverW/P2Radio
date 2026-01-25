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

package de.p2tools.p2radio.controller.stationweb.load;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import de.p2tools.p2lib.mediathek.filmlistload.P2LoadConst;
import de.p2tools.p2lib.mediathek.storedradiolist.StoredRadioDataFactory;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.stationload.ReadJsonFactory;
import de.p2tools.p2radio.tools.InputStreamProgressMonitor;
import de.p2tools.p2radio.tools.MLHttpClient;
import de.p2tools.p2radio.tools.ProgressMonitorInputStream;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.tukaani.xz.XZInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class WebLoadFactory {

    public static final double PROGRESS_MIN = 0.0;
    public static final double PROGRESS_MAX = 1.0;
    public static final double PROGRESS_INDETERMINATE = -1.0;

    private WebLoadFactory() {
    }

    private static double progress = 0;
    private static int countAll = 0;

    // Hier wird die Liste tatsächlich geladen
    // meldet PROGRESS
    public static boolean loadList(final StationList stationList) {
        boolean ret = false;
        try {
            String url = StoredRadioDataFactory.getStoredRadioList();
            load(url, stationList);
            if (stationList.size() > ProgConst.STATION_LIST_MIN_SIZE) {
                //dann hats geklappt
                return true;
            } else {
                P2Log.errorLog(645121547, "Laden von " + url + " hat nicht geklappt");
            }

            // dann nochmal damit
            stationList.clear(); // und den Rest wieder löschen
            load(P2LoadConst.RADIO_LIST_URL_1, stationList);
            if (stationList.size() > ProgConst.STATION_LIST_MIN_SIZE) {
                //dann hats geklappt
                return true;
            } else {
                P2Log.errorLog(451212547, "Laden von " + P2LoadConst.RADIO_LIST_URL_1 +
                        " hat auch nicht geklappt");
            }

            // und dann nochmal damit
            stationList.clear(); // und den Rest wieder löschen
            load(P2LoadConst.RADIO_LIST_URL_2, stationList);
            if (stationList.size() > ProgConst.STATION_LIST_MIN_SIZE) {
                //dann hats geklappt
                return true;
            } else {
                P2Log.errorLog(451212547, "Laden von " + P2LoadConst.RADIO_LIST_URL_2 +
                        " hat auch nicht geklappt");
            }

            // und dann nochmal damit
            stationList.clear(); // und den Rest wieder löschen
            load(P2LoadConst.RADIO_LIST_URL_3, stationList);
            if (stationList.size() > ProgConst.STATION_LIST_MIN_SIZE) {
                //dann hats geklappt
                return true;
            } else {
                P2Log.errorLog(951245789, "Laden von " + P2LoadConst.RADIO_LIST_URL_3 +
                        " hat auch nicht geklappt");
            }

            P2Log.errorLog(365236587, "==> Laden hat nicht geklappt");
        } catch (final Exception ex) {
            P2Log.errorLog(753215698, ex);
        }

        return ret;
    }

    private static void load(String url, final StationList stationList) {
        P2Duration.counterStart("ReadRadioBrowser.read()");
        countAll = 0;
        stationList.clear();

        List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(P2Log.LILNE2);

        // für die Progressanzeige
        ProgData.getInstance().pEventHandler.notifyListener(
                new P2Event(PEvents.LOAD_RADIO_LIST_START, "Senderliste downloaden"));

        try {
            logList.add("Senderliste aus URL laden: " + url);
            processFromWeb(new URL(url), stationList);

            if (ProgData.getInstance().worker.isStop()) {
                logList.add(" -> Senderliste laden abgebrochen");
                stationList.clear();

            } else {
                logList.add("   erstellt am:        " + stationList.getGenDate());
                logList.add("   Anzahl Gesamtliste: " + countAll);
                logList.add("   Anzahl verwendet:   " + stationList.size());
            }
        } catch (
                final MalformedURLException ex) {
            P2Log.errorLog(201010458, ex);
        }

        logList.add(P2Log.LILNE2);
        logList.add("");
        P2Log.sysLog(logList);

        P2Duration.counterStop("ReadRadioBrowser.read()");
    }

    /**
     * Download a process a senderlist from the web.
     *
     * @param source      source url as string
     * @param stationList the list to read to
     */
    private static void processFromWeb(URL source, StationList stationList) {
        final Request.Builder builder = new Request.Builder().url(source);
        builder.addHeader("User-Agent", ProgInfos.getUserAgent());

        // our progress monitor callback
        final InputStreamProgressMonitor monitor = new InputStreamProgressMonitor() {
            private int oldProgress = 0;

            @Override
            public void progress(long bytesRead, long size) {
                final int iProgress = (int) (bytesRead * 100/* zum Runden */ / size);
                if (iProgress != oldProgress) {
                    oldProgress = iProgress;
                    notifyProgress(1.0 * iProgress / 100);
                }
            }
        };

        try (Response response = MLHttpClient.getInstance().getHttpClient().newCall(builder.build()).execute();
             ResponseBody body = response.body()) {
            if (body != null && response.isSuccessful()) {
                try (InputStream input = new ProgressMonitorInputStream(body.byteStream(), body.contentLength(), monitor)) {
                    try (InputStream is = selectDecompressor(source.toString(), input);
                         JsonParser jp = new JsonFactory().createParser(is)) {
                        loadData(jp, stationList);
                    }
                }
            }
        } catch (final Exception ex) {
            P2Log.errorLog(830214789, ex, "Senderliste: " + source);
            stationList.clear();
        }
    }

    private static InputStream selectDecompressor(String source, InputStream in) throws Exception {
        if (source.endsWith(ProgConst.FORMAT_XZ)) {
            in = new XZInputStream(in);
        } else if (source.endsWith(ProgConst.FORMAT_ZIP)) {
            final ZipInputStream zipInputStream = new ZipInputStream(in);
            zipInputStream.getNextEntry();
            in = zipInputStream;
        }
        return in;
    }

    private static void loadData(JsonParser jp, StationList stationList) throws IOException {
        while (!ProgData.getInstance().worker.isStop() && (jp.nextToken()) != null) {
            if (jp.isExpectedStartObjectToken()) {
                final StationData station = new StationData();
                ReadJsonFactory.readJsonValue(station, jp);
                //etwa bei 1/3 der Sender
                //if (station.arr[Station.STATION_URL].equals(station.arr[Station.STATION_URL_RESOLVED])) {
                //  station.arr[Station.STATION_URL_RESOLVED] = "============";
                //}

                ++countAll;
                stationList.importStationOnlyWithNr(station);
            }
        }
    }

    private static void notifyProgress(double iProgress) {
        progress = iProgress;
        if (progress > PROGRESS_MAX) {
            progress = PROGRESS_MAX;
        }
        ProgData.getInstance().pEventHandler.notifyListener(
                new P2Event(PEvents.LOAD_RADIO_LIST_PROGRESS, "Senderliste downloaden", progress));
    }
}
