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

package de.p2tools.p2radio.controller.data.station;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.data.start.Start;

public final class StationData extends StationDataProperty {

    private Start start = null;

    public StationData() {
    }

    public StationData(StationData station, String collectionName) {
        setStation(station);
        setCollectionName(collectionName);
        setStationUrl(station.getStationUrl());
    }

    //==============================================
    // Get/Set
    //==============================================
    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public StationData getCopy() {
        final StationData ret = new StationData();
        ret.start = start;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(StationData playable) {
        start = playable.getStart();

        Config[] configs = playable.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }

    public boolean isStation() {
        return false;
    }

    public void setStation(StationData station) {
        if (station == null) {
            // bei gespeicherten Sendern kann es den Sender nicht mehr geben
            setStationNo(ProgConst.NUMBER_DONT_USED);
            return;
        }

        setStationNo(station.getNo());
        setStationName(station.getStationName());
        setGenre(station.getGenre());
        setCodec(station.getCodec());
        setBitrate(station.getBitrate());
        setCountry(station.getCountry());
        setCountryCode(station.getCountryCode());
        setLanguage(station.getLanguage());
        setWebsite(station.getWebsite());
        setStationUrl(station.getStationUrl());
        setStationDate(station.getStationDate());
    }

    public boolean isFavourite() {
        return true;
    }

    public void setFavourite(StationData stationData) {
        if (stationData == null) {
            // bei gespeicherten Sendern kann es den Sender nicht mehr geben
            setStationNo(ProgConst.NUMBER_DONT_USED);
            return;
        }

        setStationNo(stationData.getNo());
        setStationName(stationData.getStationName());
        setGenre(stationData.getGenre());
        setCodec(stationData.getCodec());
        setBitrate(stationData.getBitrate());
        setCountry(stationData.getCountry());
        setCountryCode(stationData.getCountryCode());
        setLanguage(stationData.getLanguage());
        setWebsite(stationData.getWebsite());
        setStationUrl(stationData.getStationUrl());
        setStationDate(new PLocalDate().getDateTime(PLocalDate.FORMAT_dd_MM_yyyy));
    }

    public boolean isHistory() {
        return false;
    }

}
