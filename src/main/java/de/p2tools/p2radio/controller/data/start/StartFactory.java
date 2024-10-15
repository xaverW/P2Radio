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

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.dialog.NoSetDialogController;

import java.util.Random;

public class StartFactory {
    private StartFactory() {
    }

    public static void stopPlayable(StationData stationData) {
        if (stationData.getStart() != null) {
            stationData.getStart().stopStart();
        }
    }

    public static boolean isPlaying() {
        final ProgData progData = ProgData.getInstance();
        final StationData stationData = progData.stationPlaying;
        if (stationData.getStart() != null &&
                (stationData.getStart().getStartStatus().isStarted() ||
                        stationData.getStart().getStartStatus().isStateStartedRun())) {
            return true;
        } else {
            return false;
        }
    }

    public static void stopAll() {
        final ProgData progData = ProgData.getInstance();
        stopPlayable(progData.stationPlaying);

        stopPlayable(progData.stationLastPlayed);
        stopPlayable(progData.stationAutoStart);

        stopAllStations();
        stopAllFavourites();
        stopAllHistory();
    }

    public static void stopAllStations() {
        final ProgData progData = ProgData.getInstance();
        progData.stationList.forEach(station -> stopPlayable(station));
    }

    public static void stopAllFavourites() {
        final ProgData progData = ProgData.getInstance();
        progData.favouriteList.forEach(favourite -> stopPlayable(favourite));
    }

    public static void stopAllHistory() {
        final ProgData progData = ProgData.getInstance();
        progData.historyList.forEach(stationData -> stopPlayable(stationData));
    }

    public static StationData playRandomStation() {
        final ProgData progData = ProgData.getInstance();
        Random r = new Random();
        StationData station = progData.stationList.get(r.nextInt(progData.stationList.size()));
        if (station != null) {
            playPlayable(station);
        }
        return station;
    }

    public static void playPlayable(StationData stationData) {
        playPlayable(stationData, null);
    }

    public static void playPlayable(StationData stationData, SetData data) {
        SetData setData = checkSetData(data);
        if (setData == null) {
            return;
        }
        // und starten
        startUrlWithProgram(stationData, setData);
    }

    private static SetData checkSetData(SetData setData) {
        SetData sd = setData;
        if (sd == null) {
            sd = de.p2tools.p2radio.controller.config.ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (sd == null) {
            new NoSetDialogController(de.p2tools.p2radio.controller.config.ProgData.getInstance());
        }

        return sd;
    }

    private static synchronized void startUrlWithProgram(StationData station, SetData setData) {
        final ProgData progData = ProgData.getInstance();
        stopAll();
        progData.stationPlaying = station;
        progData.stationLastPlayed.copyToMe(station);

        final String url = station.getStationUrl();
        if (url.isEmpty()) {
            return;
        }

        progData.historyList.addStation(station);
        ProgConfig.SYSTEM_HISTORY.setValue(url);

        final Start start = new Start(setData, station);
        station.setStart(start);
        start.initStart();

        StartPlayingStation startPlayingStation = new StartPlayingStation(progData, start);
        start.getStarter().setStartPlayingStation(startPlayingStation);
        startPlayingStation.start();
    }
}
