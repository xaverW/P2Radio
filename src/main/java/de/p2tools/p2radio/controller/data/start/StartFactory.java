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

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.dialog.NoSetDialogController;
import javafx.scene.control.TableView;

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

        ClickCounterFactory.countClick(station);

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
        System.out.println("stopStation");
        if (nowPlayingThread == null) {
            System.out.println("stopStation - nix");
            return;
        }

        if (nowPlayingThread.isRunning()) {
            nowPlayingThread.killProcess();
            if (wait) {
                System.out.println("stopStation - wait");
                P2ToolsFactory.waitWhile(3_000, nowPlayingThread.isRunningProperty());
            }
        }
    }

    public static void selectPlayingStation(TableView<StationData> tableView) {
        StationData act;
        if (nowPlayingThread != null && nowPlayingThread.isRunning()) {
            act = nowPlayingThread.getStationData();
        } else {
            act = null;
        }

        if (act != null) {
            if (tableView.getItems().contains(act)) {
                tableView.getSelectionModel().clearSelection();
                tableView.getSelectionModel().select(act);
                tableView.scrollTo(act);
            } else {
                P2Alert.showInfoAlert("Sender suchen",
                        "Laufenden Sender markieren",
                        "Der aktuell laufende Sender " +
                                "ist nicht in der Liste");
            }
        }
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
