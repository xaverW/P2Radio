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
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.dialog.NoSetDialogController;

import java.util.Random;

public class StartFactory {
    ProgData progData;

    public StartFactory(ProgData progData) {
        this.progData = progData;
    }

    public void stopPlayable(StationData favourite) {
        if (favourite.getStart() != null) {
            favourite.getStart().stopStart();
        }
    }

    public void stopAll() {
        stopAllStations();
        stopAllFavourites();
        stopAllHistory();
    }

    public void stopAllStations() {
        progData.stationList.stream().forEach(station -> progData.startFactory.stopPlayable(station));
    }

    public void stopAllFavourites() {
        progData.favouriteList.stream().forEach(favourite -> progData.startFactory.stopPlayable(favourite));
    }

    public void stopAllHistory() {
        progData.historyList.stream().forEach(stationData -> progData.startFactory.stopPlayable(stationData));
    }

    public StationData playRandomStation() {
        Random r = new Random();
        StationData station = progData.stationList.get(r.nextInt(progData.stationList.size()));
        if (station != null) {
            playPlayable(station);
        }
        return station;
    }

    public void playPlayable(StationData favourite) {
        playPlayable(favourite, null);
    }

    public void playPlayable(StationData stationData, SetData data) {
        if (!ProgConfig.SYSTEM_SHOW_MSG_SETDATA_CHANGED.getValue()) {
            P2Alert.showInfoAlert("Sender starten", "Änderungen beim Sender-Start",
                    "Die Einstellungen zum Starten eines Senders haben " +
                            "sich geändert. Wenn es Probleme gibt, bitte die Einstellungen überprüfen.");
            ProgConfig.SYSTEM_SHOW_MSG_SETDATA_CHANGED.setValue(true);
        }
        SetData setData = checkSetData(data);
        if (setData == null) {
            return;
        }
        // und starten
        startUrlWithProgram(stationData, setData);
    }

    private SetData checkSetData(SetData setData) {
        SetData sd = setData;
        if (sd == null) {
            sd = ProgData.getInstance().setDataList.getSetDataPlay();
        }
        if (sd == null) {
            new NoSetDialogController(ProgData.getInstance());
        }

        return sd;
    }

    private synchronized void startUrlWithProgram(StationData station, SetData setData) {
        final String url = station.getStationUrl();
        if (!url.isEmpty()) {
            progData.historyList.addStation(station);

            progData.startFactory.stopAll();
            ProgConfig.SYSTEM_HISTORY.setValue(url);

            final Start start = new Start(setData, station);
            station.setStart(start);
            start.initStart();

            startStart(start);
        }
    }

    private synchronized void startStart(Start start) {
        StartPlayingStation startPlayingStation = new StartPlayingStation(progData, start);
        start.getStarter().setStartPlayingStation(startPlayingStation);
        startPlayingStation.start();
    }
}
