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

package de.p2tools.p2radio.controller.data.favourite;

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavouriteStartsFactory {
    private final ProgData progData;
    private final FavouriteList favouriteList;

    public FavouriteStartsFactory(ProgData progData, FavouriteList favouriteList) {
        this.progData = progData;
        this.favouriteList = favouriteList;
    }

    /**
     * Return a List of running stations.
     *
     * @param source Use QUELLE_XXX constants
     * @return A list with all station objects.
     */
    synchronized List<StationData> getListOfRunningStations(String source) {
        final List<StationData> activeStationData = new ArrayList<>();

        activeStationData.addAll(favouriteList.stream()
                .filter(favourite -> favourite.getStart() != null && favourite.getStart().getStartStatus().isStateStartedRun())
                .filter(download -> source.equals(FavouriteConstants.ALL) /*|| download.getSource().equals(source)*/)
                .collect(Collectors.toList()));

        return activeStationData;
    }
}