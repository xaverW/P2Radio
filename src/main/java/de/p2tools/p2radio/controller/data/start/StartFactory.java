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

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;
import de.p2tools.p2radio.gui.dialog.NoSetDialogController;

public class StartFactory {
    ProgData progData;

    public StartFactory(ProgData progData) {
        this.progData = progData;
    }

    public void stopStation(Station station) {
        if (station.getStart() != null) {
            station.getStart().stopStart();
        }
    }


    public void stopFavourite(Favourite favourite) {
        if (favourite.getStart() != null) {
            favourite.getStart().stopStart();
        }
    }

    public void stopAll() {
        stopAllStation();
        stopAllFavourite();
    }

    public void stopAllStation() {
        progData.stationList.stream().forEach(station -> progData.startFactory.stopStation(station));
    }

    public void stopAllFavourite() {
        progData.favouriteList.stream().forEach(favourite -> progData.startFactory.stopFavourite(favourite));
    }


    public void playStation(Station station) {
        playStation(station, null);
    }

    public void playStation(Station station, SetData data) {
        SetData setData = checkSetData(data);
        if (setData == null) {
            return;
        }

        // und starten
        startUrlWithProgram(station, setData);
    }

    public void playFavourite(Favourite favourite) {
        playFavourite(favourite, null);
    }

    public void playFavourite(Favourite favourite, SetData data) {
        SetData setData = checkSetData(data);
        if (setData == null) {
            return;
        }

        // und starten
        startUrlWithProgram(favourite, setData);
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

    private synchronized void startUrlWithProgram(Station station, SetData setData) {
        final String url = station.arr[StationXml.STATION_URL];
        if (!url.isEmpty()) {
            progData.startFactory.stopAll();

            final Start start = new Start(setData, station);
            station.setStart(start);
            start.initStart();

            StartPlayingStation startPlayingStation = new StartPlayingStation(progData, start);
            start.getStarter().setSt_externalProgramDownload(startPlayingStation);
            startPlayingStation.start();
        }
    }

    private synchronized void startUrlWithProgram(Favourite favourite, SetData setData) {
        final String url = favourite.getUrl();
        if (!url.isEmpty()) {
            progData.startFactory.stopAll();

            final Start start = new Start(setData, favourite);
            favourite.setStart(start);
            start.initStart();

            StartPlayingStation startPlayingStation = new StartPlayingStation(progData, start);
            start.getStarter().setSt_externalProgramDownload(startPlayingStation);
            startPlayingStation.start();
        }
    }
}
