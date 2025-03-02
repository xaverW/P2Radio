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
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.P2RadioFactory;
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

    public static void playPlayable(StationData stationData, SetData setData) {
        if (setData == null) {
            setData = ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (setData == null) {
            new NoSetDialogController(ProgData.getInstance());
            return;
        }

        // und starten
        startUrlWithProgram(stationData, setData);
    }

    private static synchronized void startUrlWithProgram(StationData station, SetData setData) {
        // hier (und nur hier) wird der Sender gestartet
        final String url = station.getStationUrl();
        if (url.isEmpty()) {
            return;
        }

        stopRunningStation();

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

    public static void stopRunningStation(boolean wait) {
        if (nowPlayingStart != null) {
            nowPlayingStart.setRunning(false);

            if (nowPlayingStart.getProcess() != null) {
                nowPlayingStart.getProcess().destroy();

                if (wait) {
                    int count = 0;
                    while (nowPlayingStart.getProcess() != null) {
                        count += 1;
                        if (count > 8 /* sind 2 Sekunden */) {
                            // kann nicht ewig hier festhängen
                            break;
                        }

                        P2RadioFactory.pause(250);
                        System.out.println("wait to end");
                    }
                }
            }
        }

        finalizePlaying(nowPlayingStart);
    }

    private static void finalizePlaying(Start start) {
        // Aufräumen
        if (start == null) {
            return;
        }

        if (start.getStationData() != null) {
            start.getStationData().setError(start.isStateError());
            start.getStationData().setStart(null);
        } else {
            System.out.println("finalizePlaying: no stationData");
        }

        PlayingTitle.stopNowPlaying();
        StartFactory.finishedMsg(start);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
        nowPlayingStart = null;
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

        if (start.getStartCounter() > 0) {
            list.add("Restarts: " + start.getStartCounter());
        }

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
