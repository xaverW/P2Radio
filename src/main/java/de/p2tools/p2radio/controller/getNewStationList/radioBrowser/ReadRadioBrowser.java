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
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.controller.data.playable.PlayableXml;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ReadRadioBrowser {

    static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private double progress = 0;
    private int countAll = 0;

    /*
    Hier wird die Liste tatsächlich geladen (von Datei/URL)
     */
    public boolean readList(final StationList stationList) {
        boolean ret = false;
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
                //etwa bei 1/3 der Sender
                //if (station.arr[Station.STATION_URL].equals(station.arr[Station.STATION_URL_RESOLVED])) {
                //  station.arr[Station.STATION_URL_RESOLVED] = "============";
                //}

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
                    station.arr[PlayableXml.STATION_PROP_STATION_NAME_INT] = value;
                    break;
                case StationFieldNamesWeb.GENRE:
                    station.arr[PlayableXml.STATION_PROP_GENRE_INT] = value;
                    break;
                case StationFieldNamesWeb.CODEC:
                    station.arr[PlayableXml.STATION_PROP_CODEC_INT] = value;
                    break;
                case StationFieldNamesWeb.BITRATE:
                    station.arr[PlayableXml.STATION_PROP_BITRATE_INT] = value;
                    break;
                case StationFieldNamesWeb.COUNTRY:
                    station.arr[PlayableXml.STATION_PROP_COUNTRY_INT] = value;
                    break;
                case StationFieldNamesWeb.COUNTRY_CODE:
                    station.arr[PlayableXml.STATION_PROP_COUNTRY_CODE_INT] = value;
                    break;
                case StationFieldNamesWeb.STATE:
                    station.arr[PlayableXml.STATION_PROP_STATE_INT] = value;
                    break;
                case StationFieldNamesWeb.LANGUAGE:
                    station.arr[PlayableXml.STATION_PROP_LANGUAGE_INT] = value;
                    break;
                case StationFieldNamesWeb.VOTES:
                    station.arr[PlayableXml.STATION_PROP_VOTES_INT] = value;
                    break;
                case StationFieldNamesWeb.CLICK_COUNT:
                    station.arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT] = value;
                    break;
                case StationFieldNamesWeb.CLICK_TREND:
                    station.arr[PlayableXml.STATION_PROP_CLICK_TREND_INT] = value;
                    break;
                case StationFieldNamesWeb.URL:
                    station.arr[PlayableXml.STATION_PROP_URL_INT] = value;
                    break;
                case StationFieldNamesWeb.URL_RESOLVED:
                    station.arr[PlayableXml.STATION_PROP_URL_RESOLVED_INT] = value;
                    break;
                case StationFieldNamesWeb.HOMEPAGE:
                    station.arr[PlayableXml.STATION_PROP_WEBSITE_INT] = value;
                    break;
                case StationFieldNamesWeb.LAST_CHANGE_TIME:
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

    private void notifyStart(String url) {
        progress = 0;
        ProgData.getInstance().pEventHandler.notifyListener(
                new RunEventRadio(Events.READ_STATIONS, RunEventRadio.NOTIFY.START,
                        url, "Senderliste downloaden", 0, false));
    }

    private void notifyProgress(String url, double iProgress) {
        progress = iProgress;
        if (progress > RunEventRadio.PROGRESS_MAX) {
            progress = RunEventRadio.PROGRESS_MAX;
        }
        ProgData.getInstance().pEventHandler.notifyListener(
                new RunEventRadio(Events.READ_STATIONS, RunEventRadio.NOTIFY.PROGRESS,
                        url, "Senderliste downloaden", progress, false));
    }
}
