package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.dialog.CountClickDialogController;
import de.p2tools.p2radio.tools.MLHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ClickCounterFactory {
    private ClickCounterFactory() {
    }

    public static void countClick(StationData stationData) {
        if (stationData.getStationUuid().isEmpty()) {
            // dann gehts eh nicht
            return;
        }

        if (ProgConfig.SYSTEM_ASK_COUNT_CLICKS.get()) {
            // dann vorher fragen
            new CountClickDialogController(ProgData.getInstance());
        }

        if (!ProgConfig.SYSTEM_COUNT_CLICKS.get()) {
            // und nur, wenn er will
            return;
        }

        // dann wird gezählt
        load(stationData);
    }

    private static void load(StationData stationData) {
        String u = ProgConst.STATION_CLICK_COUNT_URL + stationData.getStationUuid();
        try {
            URL url = new URL(u);
            final Request.Builder builder = new Request.Builder().url(url);
            builder.addHeader("User-Agent", ProgInfos.getUserAgent());

            try (Response response = MLHttpClient.getInstance().getHttpClient().newCall(builder.build()).execute();
                 ResponseBody body = response.body()) {
                if (body != null && response.isSuccessful()) {
                    InputStream input = body.byteStream();
                    BufferedReader buff = new BufferedReader(new InputStreamReader(input));
                    String inStr;
                    P2Log.sysLog("");
                    P2Log.sysLog("=== Click melden ========");
                    P2Log.sysLog(stationData.getStationName());
                    while ((inStr = buff.readLine()) != null) {
                        P2Log.sysLog(inStr.trim());
                    }
                    P2Log.sysLog("=========================");
                }
            } catch (final Exception ex) {
                P2Log.errorLog(636254789, ex, u);
            }
        } catch (Exception ex) {
            P2Log.errorLog(854541259, ex, u);
        }
    }
}
