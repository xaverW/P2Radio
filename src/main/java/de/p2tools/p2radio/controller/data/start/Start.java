/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;

public final class Start extends StartProps {

    private final StartStatus startStatus = new StartStatus();
    private Starter starter = new Starter(this);

    private Station station = null;
    private Favourite favourite = null;
    private LastPlayed lastPlayed = null;
    private SetData setData = null;

    public Start() {
    }

    public Start(SetData setData, Station station) {
        if (station == null) {
            // bei gespeicherten Favoriten kann es den Sender nicht mehr geben
            setStationNo(FavouriteConstants.STATION_NUMBER_NOT_FOUND);
        }

        this.station = station;
        setStationNo(station.getNo());
        setStationName(station.arr[StationXml.STATION_NAME]);
        setUrl(station.getUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public Start(SetData setData, Favourite favourite) {
        this.favourite = favourite;
        setStationName(favourite.getStationName());
        setUrl(favourite.getUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public Start(SetData setData, LastPlayed lastPlayed) {
        this.lastPlayed = lastPlayed;
        setStationName(lastPlayed.getStationName());
        setUrl(lastPlayed.getUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public StartStatus getStartStatus() {
        return startStatus;
    }

    public Starter getStarter() {
        return starter;
    }

    public void initStart() {
        getStarter().setStartTime();
        getStarter().setRestartCounter(0);
        getStartStatus().setState(FavouriteConstants.STATE_INIT);
        getStartStatus().setErrorMessage("");
    }

    public void stopStart() {
        if (getStartStatus().isStateError()) {
            // damit fehlerhafte nicht wieder starten
//            getStart().setRestartCounter(ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART.get());
        } else {
            getStartStatus().setStateStopped();
        }

        setNo(FavouriteConstants.FAVOURITE_NUMBER_NOT_STARTED);
    }

    //==============================================
    // Get/Set
    //==============================================

    public String getStationUrl() {
        if (station != null) {
            return station.getUrl();
        } else {
            return getUrl();
        }
    }

    public Station getStation() {
        return station;
    }

    public Favourite getFavourite() {
        return favourite;
    }

    public SetData getSetData() {
        return setData;
    }

    public void setSetData(SetData setData) {
        this.setData = setData;
        setSetDataId(setData.getId());
    }

    public Start getCopy() {
        final Start ret = new Start();
        ret.starter = starter;
        ret.station = station;
        ret.setData = setData;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(Start start) {
        starter = start.starter;
        station = start.station;
        setData = start.setData;

        Config[] configs = start.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
