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

package de.p2tools.p2radio.controller.data.lastPlayed;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.data.Playable;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.start.Start;
import de.p2tools.p2radio.controller.data.station.Station;

public final class LastPlayed extends LastPlayedProps implements Playable {

    private Start start = null;

    public LastPlayed() {
    }

    public LastPlayed(Station station) {
        setStation(station);
    }

    public LastPlayed(Favourite favourite) {
        setFavourite(favourite);
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

    public void setStation(Station station) {
        if (station == null) {
            // bei gespeicherten Sendern kann es den Sender nicht mehr geben
            setStationNo(FavouriteConstants.STATION_NUMBER_NOT_FOUND);
            return;
        }

        setStationNo(station.getNo());
        setStationName(station.getStationName());
        setGenre(station.getGenre());
        setCodec(station.getCodec());
        setBitrate(station.getBitrateInt());
        setCountry(station.getCountry());
        setCountryCode(station.getCountryCode());
        setLanguage(station.getLanguage());
        setWebsite(station.getWebsite());
        setStationUrl(station.getStationUrl());
        setStationDate(new PLocalDate().getDateTime(PLocalDate.FORMAT_dd_MM_yyyy));
    }

    public void setFavourite(Favourite favourite) {
        if (favourite == null) {
            // bei gespeicherten Sendern kann es den Sender nicht mehr geben
            setStationNo(FavouriteConstants.STATION_NUMBER_NOT_FOUND);
            return;
        }

        setStationNo(favourite.getNo());
        setStationName(favourite.getStationName());
        setGenre(favourite.getGenre());
        setCodec(favourite.getCodec());
        setBitrate(favourite.getBitrate());
        setCountry(favourite.getCountry());
        setCountryCode(favourite.getCountryCode());
        setLanguage(favourite.getLanguage());
        setWebsite(favourite.getWebsite());
        setStationUrl(favourite.getStationUrl());
        setStationDate(new PLocalDate().getDateTime(PLocalDate.FORMAT_dd_MM_yyyy));
    }

    public boolean isStation() {
        return false;
    }

    public boolean isFavourite() {
        return false;
    }

    public boolean isLastPlayed() {
        return true;
    }

    public LastPlayed getCopy() {
        final LastPlayed ret = new LastPlayed();
        ret.start = start;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(LastPlayed favourite) {
        start = favourite.start;

        Config[] configs = favourite.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
