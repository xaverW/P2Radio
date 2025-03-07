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

import java.time.LocalDateTime;

public class StartFactory {
    private static Start startPlaying = null;

    private StartFactory() {
    }

    public static void stopRunningPlayProcess() {
        if (startPlaying != null &&
                startPlaying.getProcess() != null) {
            startPlaying.getProcess().destroy();
        }
        PlayingTitle.stopNowPlaying();
        startPlaying = null;
    }

    public static void stopRunningStation() {
        if (startPlaying != null) {
            startPlaying.stopStart();
        }
        PlayingTitle.stopNowPlaying();
        startPlaying = null;
    }

    public static void playPlayable(StationData stationData) {
        playPlayable(stationData, null);
    }

    public static void playPlayable(StationData stationData, SetData data) {
        SetData setData = getSetData(data);
        if (setData == null) {
            return;
        }
        // und starten
        startUrlWithProgram(stationData, setData);
    }

    private static SetData getSetData(SetData setData) {
        SetData sd = setData;
        if (sd == null) {
            sd = ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (sd == null) {
            new NoSetDialogController(ProgData.getInstance());
        }

        return sd;
    }

    private static synchronized void startUrlWithProgram(StationData station, SetData setData) {
        final String url = station.getStationUrl();
        if (url.isEmpty()) {
            return;
        }

        stopRunningStation();
        final ProgData progData = ProgData.getInstance();
        progData.stationLastPlayed.copyToMe(station);

        station.setStationDateLastStart(LocalDateTime.now());
        progData.historyList.addStation(station);
        ProgConfig.SYSTEM_HISTORY.setValue(url);

        final Start start = new Start(setData, station);
        startPlaying = start;
        station.setStart(start);
        start.initStart();

        StartPlayingStation startPlayingStation = new StartPlayingStation(progData, start);
        startPlayingStation.start();
    }
}
