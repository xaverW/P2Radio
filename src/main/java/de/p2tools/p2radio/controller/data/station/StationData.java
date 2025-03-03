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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2radio.controller.data.start.StartDto;

public final class StationData extends StationDataProperty {

    private StartDto startDto = null;
    public final String TAG;

    public StationData() {
        TAG = "Station" + TAGGER + "Favourite";
    }

    public StationData(String tag) {
        TAG = tag;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    //==============================================
    // Get/Set
    //==============================================
    public StartDto getStart() {
        return startDto;
    }

    public void setStart(StartDto startDto) {
        this.startDto = startDto;
    }

    public StationData getCopy() {
        final StationData ret = new StationData(getTag());
        ret.startDto = startDto;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(StationData stationData) {
        startDto = stationData.getStart();

        Config[] configs = stationData.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }

    public void setStation(StationData station) {
        // beim Anlegen einer neuen History
        // und nach dem Neuladen einer Radioliste, für Favourite/History
        if (station == null) {
            // bei gespeicherten Sendern kann es den Sender nicht mehr geben
            setStationNo(P2LibConst.NUMBER_NOT_STARTED);
            return;
        }

        String collectionName = getCollectionName();
        String description = getDescription();
        int ownGrade = getOwnGrade();
        int starts = getStarts();

        //copy
        copyToMe(station);

        //reset
        setCollectionName(collectionName);
        setDescription(description);
        setOwnGrade(ownGrade);
        setStarts(starts);
    }

    public void switchOffAuto() {
        setStationUrl("");
    }

    public boolean isAuto() {
        return !getStationUrl().isEmpty();
    }
}
