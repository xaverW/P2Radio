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

package de.p2tools.p2radio.tools.storedFilter;

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.tools.stationListFilter.StationFilterFactory;

public class InitStoredFilter {

    public static void initFilter() {
        ProgData progData = ProgData.getInstance();

        //========================================================
        SelectedFilter sf = new SelectedFilter("Alle Sender");

        sf.setStationNameVis(true);
        sf.setGenreVis(true);
        sf.setUrlVis(false);
        sf.setCodecVis(false);
        sf.setCountryVis(false);

        sf.setMinMaxBitVis(false);
        sf.setMinBit(0);
        sf.setMaxBit(StationFilterFactory.FILTER_BITRATE_MAX);

        sf.setOnlyVis(false);
        sf.setOnlyNew(false);
        sf.setNoFavourites(false);
        sf.setNoDoubles(false);

        progData.storedFilters.getStoredFilterList().add(sf);

        //========================================================
        sf = new SelectedFilter("Bayern");

        sf.setStationNameVis(true);
        sf.setStationName("Bayern");
        sf.setGenreVis(true);
        sf.setUrlVis(false);
        sf.setCodecVis(true);
        sf.setCountryVis(true);

        sf.setMinMaxBitVis(true);
        sf.setMinBit(0);
        sf.setMaxBit(StationFilterFactory.FILTER_BITRATE_MAX);

        sf.setOnlyVis(false);
        sf.setOnlyNew(false);
        sf.setNoFavourites(false);
        sf.setNoDoubles(false);

        progData.storedFilters.getStoredFilterList().add(sf);

        //========================================================
        sf = new SelectedFilter("Rock, Trance");

        sf.setStationNameVis(true);
        sf.setGenreVis(true);
        sf.setGenre("Rock, Trance");
        sf.setUrlVis(false);
        sf.setCodecVis(true);
        sf.setCountryVis(false);

        sf.setMinMaxBitVis(true);
        sf.setMinBit(0);
        sf.setMaxBit(StationFilterFactory.FILTER_BITRATE_MAX);

        sf.setOnlyVis(false);
        sf.setOnlyNew(false);
        sf.setNoFavourites(false);
        sf.setNoDoubles(false);

        progData.storedFilters.getStoredFilterList().add(sf);

        //========================================================
        sf = new SelectedFilter("MP3");

        sf.setStationNameVis(true);
        sf.setGenreVis(true);
        sf.setUrlVis(false);
        sf.setCodecVis(true);
        sf.setCodec("mp3");
        sf.setCountryVis(false);

        sf.setMinMaxBitVis(true);
        sf.setMinBit(100);
        sf.setMaxBit(200);

        sf.setOnlyVis(false);
        sf.setOnlyNew(false);
        sf.setNoFavourites(false);
        sf.setNoDoubles(false);

        progData.storedFilters.getStoredFilterList().add(sf);

        //========================================================
        sf = new SelectedFilter("Deutsche Sender");

        sf.setStationNameVis(true);
        sf.setGenreVis(true);
        sf.setUrlVis(false);
        sf.setCodecVis(true);
        sf.setCountryVis(true);
        sf.setCountry("Germany");

        sf.setMinMaxBitVis(true);
        sf.setMinBit(0);
        sf.setMaxBit(StationFilterFactory.FILTER_BITRATE_MAX);

        sf.setOnlyVis(false);
        sf.setOnlyNew(false);
        sf.setNoFavourites(false);
        sf.setNoDoubles(false);

        progData.storedFilters.getStoredFilterList().add(sf);
    }
}
