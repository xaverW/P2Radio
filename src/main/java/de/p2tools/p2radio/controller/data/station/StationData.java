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

    public void copyToMe(StationData stationData) {
        start = stationData.getStart();

        Config[] configs = stationData.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }

    public void setStation(StationData station) {
        if (station == null) {
            // bei gespeicherten Sendern kann es den Sender nicht mehr geben
            setStationNo(ProgConst.NUMBER_DONT_USED);
            return;
        }
//        int no = getStationNo();
        String collectionName = getCollectionName();
        int ownGrade = getOwnGrade();
        int starts = getStarts();

        //copy
        copyToMe(station);
        //reset
//        setStationNo(no);
        setCollectionName(collectionName);
        setOwnGrade(ownGrade);
        setStarts(starts);
    }
}
