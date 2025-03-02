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


package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.gui.dialog.NoSetDialogController;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class StartFactory {
    private static Start nowPlayingStart = null; // ist der aktuell laufende START

    private StartFactory() {
    }

    // ============
    // Starten
    // ============
    public static void playPlayable(StationData stationData) {
        playPlayable(stationData, null);
    }

    public synchronized static void playPlayable(StationData stationData, SetData setData) {
        P2Log.sysLog("====================================");
        P2Log.sysLog("Sender starten: " + stationData.getStationName());

        if (setData == null) {
            setData = ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (setData == null) {
            new NoSetDialogController(ProgData.getInstance());
            return;
        }

        // erst mal alles stoppen
        stopRunningStation();

        if (nowPlayingStart != null) {
            // dann wurde nicht beendet
            P2Log.errorLog(958584587, "Konnte Sender nicht stoppen: " + stationData.getStationName());
            nowPlayingStart = null;
        }

        // und jetzt starten
        startUrlWithProgram(stationData, setData);
    }

    private static void startUrlWithProgram(StationData station, SetData setData) {
        // hier (und nur hier) wird der Sender gestartet
        final String url = station.getStationUrl();
        if (url.isEmpty()) {
            P2Log.sysLog("Sender hat keine URL: " + station.getStationName());
            return;
        }

        station.setStationDateLastStart(LocalDateTime.now());
        ProgData.getInstance().stationLastPlayed.copyToMe(station);
        ProgData.getInstance().historyList.addStation(station);
        ProgConfig.SYSTEM_HISTORY.setValue(url);

        nowPlayingStart = new Start(setData, station);
        station.setStart(nowPlayingStart);
        nowPlayingStart.initStart();
    }

    // ============
    // Beenden
    // ============
    public static void stopRunningStation() {
        stopRunningStation(true);
    }

    static synchronized boolean isProcess() {
        return nowPlayingStart != null &&
                nowPlayingStart.getProcess() != null &&
                nowPlayingStart.getProcess().isAlive();
    }

    public static void stopRunningStation(boolean wait) {
        System.out.println("stopRunningStation");

        if (nowPlayingStart == null) {
            System.out.println("stopRunningStation - nix");
            return;
        }

        nowPlayingStart.setStopFromButton(true);

        if (nowPlayingStart.getProcess() != null) {
            nowPlayingStart.getProcess().destroy();

            if (wait) {
                P2ToolsFactory.waitWhile(3_000, (a) -> isProcess());
            }
        }

        finalizePlaying(nowPlayingStart);
        nowPlayingStart = null;
    }

    private static void finalizePlaying(Start start) {
        // Aufräumen
        System.out.println("finalizePlaying");

        if (start == null) {
            return;
        }

        if (start.getStationData() != null) {
            start.getStationData().setError(start.isStateError());
            start.getStationData().setStart(null);
        }

        PlayingTitle.stopNowPlaying();
        StartFactory.finishedMsg(start);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
    }

    // ========
    // Melden
    // ========
    public static void startMsg(Start start) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Sender abspielen");

        list.add("URL: " + start.getStationData().getStationUrl());
        list.add("Startzeit: " + P2DateConst.F_FORMAT_dd_MM_yyyy___HH__mm__ss.format(start.getStartTime()));
        list.add("Programmaufruf: " + start.getProgramCall());
        list.add("Programmaufruf[]: " + start.getProgramCallArray());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list.toArray(new String[list.size()]));
    }

    public static void finishedMsg(Start start) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Sender abspielen beendet");
        list.add("Startzeit: " + P2DateConst.F_FORMAT_dd_MM_yyyy___HH__mm__ss.format(start.getStartTime()));
        list.add("Endzeit: " + P2DateConst.DT_FORMATTER_dd_MM_yyyy___HH__mm__ss.format(LocalDateTime.now()));

        final long dauer = start.getStartTime().diffInMinutes();
        if (dauer == 0) {
            list.add("Dauer: " + start.getStartTime().diffInSeconds() + " s");
            //list.add("Dauer: <1 Min.");
        } else {
            list.add("Dauer: " + start.getStartTime().diffInMinutes() + " Min");
        }

        list.add("URL: " + start.getStationData().getStationUrl());
        list.add("Programmaufruf: " + start.getProgramCall());
        list.add("Programmaufruf[]: " + start.getProgramCallArray());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list);
    }
}
