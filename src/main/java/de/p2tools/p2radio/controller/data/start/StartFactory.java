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
    private static PlayingThread nowPlayingThread = null; // ist der aktuell laufende PLAYING_THREAD

    private StartFactory() {
    }

    // ============
    // Starten
    // ============
    public static void startStation(StationData stationData) {
        startStation(stationData, null);
    }

    public synchronized static void startStation(StationData stationData, SetData setData) {
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
        stopStation();

        if (nowPlayingThread != null) {
            // dann wurde nicht beendet
            P2Log.errorLog(958584587, "Konnte Sender nicht stoppen: " + nowPlayingThread.getStationData().getStationName());
            nowPlayingThread.getStationData().setNowPlaying(false);
            nowPlayingThread = null;
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

        ProgData.getInstance().stationLastPlayed.copyToMe(station);
        ProgData.getInstance().historyList.addStation(station);
        ProgConfig.SYSTEM_HISTORY.setValue(url);

        nowPlayingThread = new PlayingThread(setData, station);
        nowPlayingThread.start();
        P2ToolsFactory.waitWhile(3_000, nowPlayingThread.isStartingProperty());

        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
    }

    // ============
    // Beenden
    // ============
    public static void stopStation() {
        stopStation(true);
    }

    public static void stopStation(boolean wait) {
        System.out.println("stopRunningStation");

        if (nowPlayingThread == null) {
            System.out.println("stopRunningStation - nix");
            return;
        }

        if (nowPlayingThread.getProcess() != null) {
            nowPlayingThread.getProcess().destroy();

            if (wait) {
                System.out.println("isProcessAlive");
                P2ToolsFactory.waitWhile(3_000, (a) -> isProcessAlive());
            }
        }

        System.out.println("isRunningProperty");
        P2ToolsFactory.waitWhile(3_000, nowPlayingThread.isRunningProperty());

        finalizePlaying(nowPlayingThread);
        nowPlayingThread = null;
    }

    private static synchronized boolean isProcessAlive() {
        return nowPlayingThread != null &&
                nowPlayingThread.getProcess() != null &&
                nowPlayingThread.getProcess().isAlive();
    }

    public static void finalizePlaying(PlayingThread playingThread) {
        // Aufräumen
        System.out.println("finalizePlaying");

        if (playingThread == null) {
            System.out.println("finalizePlaying -- null");
            return;
        }

        if (playingThread.getStationData() != null) {
            playingThread.getStationData().setError(playingThread.isStateError());
            playingThread.getStationData().setNowPlaying(false);
        }

        PlayingTitle.stopNowPlaying();
        StartFactory.finishedMsg(playingThread);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
    }

    // ========
    // Melden
    // ========
    public static void startMsg(PlayingThread playingThread) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Sender abspielen");

        list.add("URL: " + playingThread.getStationData().getStationUrl());
        list.add("Startzeit: " + P2DateConst.F_FORMAT_dd_MM_yyyy___HH__mm__ss.format(playingThread.getStartTime()));
        list.add("Programmaufruf: " + playingThread.getProgramCall());
        list.add("Programmaufruf[]: " + playingThread.getProgramCallArray());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list.toArray(new String[]{}));
    }

    public static void finishedMsg(PlayingThread playingThread) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(P2Log.LILNE3);
        list.add("Sender abspielen beendet");
        list.add("Startzeit: " + P2DateConst.F_FORMAT_dd_MM_yyyy___HH__mm__ss.format(playingThread.getStartTime()));
        list.add("Endzeit: " + P2DateConst.DT_FORMATTER_dd_MM_yyyy___HH__mm__ss.format(LocalDateTime.now()));

        final long dauer = playingThread.getStartTime().diffInMinutes();
        if (dauer == 0) {
            list.add("Dauer: " + playingThread.getStartTime().diffInSeconds() + " s");
            //list.add("Dauer: <1 Min.");
        } else {
            list.add("Dauer: " + playingThread.getStartTime().diffInMinutes() + " Min");
        }

        list.add("URL: " + playingThread.getStationData().getStationUrl());
        list.add("Programmaufruf: " + playingThread.getProgramCall());
        list.add("Programmaufruf[]: " + playingThread.getProgramCallArray());

        list.add(P2Log.LILNE_EMPTY);
        P2Log.sysLog(list);
    }
}
