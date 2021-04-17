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

package de.p2tools.p2radio.controller.getNewStationList.radioBrowser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.tools.InputStreamProgressMonitor;
import de.p2tools.p2radio.tools.MLHttpClient;
import de.p2tools.p2radio.tools.ProgressMonitorInputStream;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.time.FastDateFormat;
import org.tukaani.xz.XZInputStream;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ReadRadioBrowser {

    static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private final EventListenerList listeners = new EventListenerList();
    private double progress = 0;
    private int countAll = 0;

    public void addAdListener(EventListenerLoadRadioList listener) {
        listeners.add(EventListenerLoadRadioList.class, listener);
    }

    /*
    Hier wird die Liste tatsächlich geladen (von Datei/URL)
     */
    public boolean readList(final StationList stationList) {
        boolean ret = false;

//        if (ProgData.debug) {
//            //ToDo
//            //URL laden, vorerst!!!!!!!!!!!!!!!!
//            //sonst: http://all.api.radio-browser.info/xml/stations
//            //oder   http://all.api.radio-browser.info/json/stations
//            String sourceFileUrl = "http://localhost:8080/stations";
//            read(sourceFileUrl, stationList);
//            if (!stationList.isEmpty()) {
//                //dann hats geklappt
//                ret = true;
//            }
//
//        } else {
        try {
            String updateUrl = ProgConst.STATION_LIST_URL;
            read(updateUrl, stationList);
            if (!stationList.isEmpty()) {
                //dann hats geklappt
                ret = true;
            }
        } catch (final Exception ex) {
            PLog.errorLog(201020354, ex);
        }


//        try {
//            final ArrayList<String> usedUrls = new ArrayList<>();
//            String updateUrl;
//            final int maxRetries = 3; // 3x probieren, eine Liste zu laden
//            updateUrl = StationListUrlsFactory.getRandRadioListUrl(usedUrls);
//            if (updateUrl.isEmpty()) {
//                return false;
//            }
//            for (int i = 0; i < maxRetries; ++i) {
//                read(updateUrl, stationList);
//                updateUrl = StationListUrlsFactory.getRandRadioListUrl(usedUrls);
//                if (!stationList.isEmpty()) {
//                    //dann hats geklappt
//                    ret = true;
//                    break;
//                }
//            }
//        } catch (final Exception ex) {
//            PLog.errorLog(984512067, ex);
//        }
//        }

        return ret;
    }

    private void read(String url, final StationList stationList) {
        PDuration.counterStart("ReadRadioBrowser.read()");
        countAll = 0;
        List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(PLog.LILNE2);


        notifyStart(url); // für die Progressanzeige
        stationList.clear();
        try {
            logList.add("Senderliste aus URL laden: " + url);
            processFromWeb(new URL(url), stationList);

            if (ProgData.getInstance().loadNewStationList.isStop()) {
                logList.add(" -> Senderliste laden abgebrochen");
                stationList.clear();

            } else {
                logList.add("   erstellt am:        " + stationList.getGenDate());
                logList.add("   Anzahl Gesamtliste: " + countAll);
                logList.add("   Anzahl verwendet:   " + stationList.size());
            }
        } catch (
                final MalformedURLException ex) {
            PLog.errorLog(201010458, ex);
        }

        logList.add(PLog.LILNE2);
        logList.add("");
        PLog.addSysLog(logList);

        notifyFinished(url);
        PDuration.counterStop("ReadRadioBrowser.read()");
    }

    /**
     * Download a process a senderlist from the web.
     *
     * @param source      source url as string
     * @param stationList the list to read to
     */
    private void processFromWeb(URL source, StationList stationList) {
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
                    notifyProgress(source.toString(), 1.0 * iProgress / 100);
                }
            }
        };

        try (Response response = MLHttpClient.getInstance().getHttpClient().newCall(builder.build()).execute();
             ResponseBody body = response.body()) {
            if (body != null && response.isSuccessful()) {
                try (InputStream input = new ProgressMonitorInputStream(body.byteStream(), body.contentLength(), monitor)) {
                    try (InputStream is = selectDecompressor(source.toString(), input);
                         JsonParser jp = new JsonFactory().createParser(is)) {
                        readData(jp, stationList);
                    }
                }
            }
        } catch (final Exception ex) {
            PLog.errorLog(830214789, ex, "Senderliste: " + source);
            stationList.clear();
        }
    }

    private InputStream selectDecompressor(String source, InputStream in) throws Exception {
        if (source.endsWith(ProgConst.FORMAT_XZ)) {
            in = new XZInputStream(in);
        } else if (source.endsWith(ProgConst.FORMAT_ZIP)) {
            final ZipInputStream zipInputStream = new ZipInputStream(in);
            zipInputStream.getNextEntry();
            in = zipInputStream;
        }
        return in;
    }

    private void readData(JsonParser jp, StationList stationList) throws IOException {
        while (!ProgData.getInstance().loadNewStationList.isStop() && (jp.nextToken()) != null) {
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

    private void addValue(Station station, JsonParser jp) throws IOException {
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
                case StationFieldNamesWeb.URL_RESOLVED:
                    station.arr[Station.STATION_URL_RESOLVED] = value;
                    break;
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

    private void notifyStart(String url) {
        progress = 0;
        for (final EventListenerLoadRadioList l : listeners.getListeners(EventListenerLoadRadioList.class)) {
            l.start(new EventLoadRadioList(url, "Senderliste downloaden", 0, 0, false));
        }
    }

    private void notifyProgress(String url, double iProgress) {
        progress = iProgress;
        if (progress > EventListenerLoadRadioList.PROGRESS_MAX) {
            progress = EventListenerLoadRadioList.PROGRESS_MAX;
        }
        for (final EventListenerLoadRadioList l : listeners.getListeners(EventListenerLoadRadioList.class)) {
            l.progress(new EventLoadRadioList(url, "Senderliste downloaden", progress, 0, false));
        }
    }

    private void notifyFinished(String url) {
        for (final EventListenerLoadRadioList l : listeners.getListeners(EventListenerLoadRadioList.class)) {
            l.finished(new EventLoadRadioList(url, "Senderliste geladen", progress, 0, false));
        }
    }
}
