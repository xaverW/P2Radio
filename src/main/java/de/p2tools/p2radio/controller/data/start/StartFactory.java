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
    ProgData progData;

    public StartFactory(ProgData progData) {
        this.progData = progData;
    }

    public void stopPlayable(StationData favourite) {
        if (favourite.getStart() != null) {
            favourite.getStart().stopStart();
        }
    }

//    public void stopStation(Favourite station) {
//        if (station.getStart() != null) {
//            station.getStart().stopStart();
//        }
//    }

//    public void stopFavourite(Favourite favourite) {
//        if (favourite.getStart() != null) {
//            favourite.getStart().stopStart();
//        }
//    }

//    public void stopLastPlayed(Favourite lastPlayed) {
//        if (lastPlayed.getStart() != null) {
//            lastPlayed.getStart().stopStart();
//        }
//    }

    public void stopAll() {
        stopAllStations();
        stopAllFavourites();
        stopAllLastPlayed();
    }

    public void stopAllStations() {
        progData.stationList.stream().forEach(station -> progData.startFactory.stopPlayable(station));
    }

    public void stopAllFavourites() {
        progData.favouriteList.stream().forEach(favourite -> progData.startFactory.stopPlayable(favourite));
    }

    public void stopAllLastPlayed() {
        progData.lastPlayedList.stream().forEach(lastPlayed -> progData.startFactory.stopPlayable(lastPlayed));
    }

    public StationData playRandomStation() {
        Random r = new Random();
        StationData station = progData.stationList.get(r.nextInt(progData.stationList.size()));
        if (station != null) {
            playPlayable(station);
        }
        return station;
    }

//    public void playStation(Favourite station) {
//        playPlayable(station, null);
//    }


//    public void playStation(Favourite station, SetData data) {
//        SetData setData = checkSetData(data);
//        if (setData == null) {
//            return;
//        }
//        // und starten
//        startUrlWithProgram(station, setData);
//    }

    public void playPlayable(StationData favourite) {
        playPlayable(favourite, null);
    }

    public void playPlayable(StationData favourite, SetData data) {
        SetData setData = checkSetData(data);
        if (setData == null) {
            return;
        }
        // und starten
        startUrlWithProgram(favourite, setData);
    }

//    public void playFavourite(Favourite favourite) {
//        playPlayable(favourite, null);
//    }
//
//    public void playFavourite(Favourite favourite, SetData data) {
//        SetData setData = checkSetData(data);
//        if (setData == null) {
//            return;
//        }
//        // und starten
//        startUrlWithProgram(favourite, setData);
//    }

//    public void playLastPlayed(Favourite lastPlayed) {
//        playLastPlayed(lastPlayed, null);
//    }
//
//    public void playLastPlayed(Favourite lastPlayed, SetData data) {
//        SetData setData = checkSetData(data);
//        if (setData == null) {
//            return;
//        }
//        // und starten
//        startUrlWithProgram(lastPlayed, setData);
//    }

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

//    private synchronized void startUrlWithProgram(Favourite station, SetData setData) {
//        final String url = station.getStationUrl();
//        if (!url.isEmpty()) {
//            progData.lastPlayedList.addStation(station);
//
//            progData.startFactory.stopAll();
//            ProgConfig.SYSTEM_LAST_PLAYED.setValue(url);
//
//            final Start start = new Start(setData, station);
//            station.setStart(start);
//            start.initStart();
//
//            startStart(start);
//        }
//    }

    private synchronized void startUrlWithProgram(StationData station, SetData setData) {
        final String url = station.getStationUrl();
        if (!url.isEmpty()) {
            progData.lastPlayedList.addStation(station);

            progData.startFactory.stopAll();
            ProgConfig.SYSTEM_LAST_PLAYED.setValue(url);

            final Start start = new Start(setData, station);
            station.setStart(start);
            start.initStart();

            startStart(start);
        }
    }

//    private synchronized void startUrlWithProgram(Favourite favourite, SetData setData) {
//        final String url = favourite.getStationUrl();
//        if (!url.isEmpty()) {
//            //todo wenn lastPlayed dann brauchts das eigenlich nicht
//            progData.lastPlayedList.addFavourite(favourite);
//
//            progData.startFactory.stopAll();
//            ProgConfig.SYSTEM_LAST_PLAYED.setValue(url);
//
//            final Start start = new Start(setData, favourite);
//            favourite.setStart(start);
//            start.initStart();
//
//            startStart(start);
//        }
//    }

    private synchronized void startStart(Start start) {
        StartPlayingStation startPlayingStation = new StartPlayingStation(progData, start);
        start.getStarter().setStartPlayingStation(startPlayingStation);
        startPlayingStation.start();
    }
}
