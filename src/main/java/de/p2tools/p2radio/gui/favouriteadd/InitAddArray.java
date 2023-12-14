/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.gui.favouriteadd;

import de.p2tools.p2radio.controller.data.station.StationData;

import java.util.ArrayList;

public class InitAddArray {

    private InitAddArray() {
    }

    public static AddFavouriteData[] initInfoArrayNewFavourite(ArrayList<StationData> list) {
        // Arr anlegen
        AddFavouriteData[] addFavouriteData = new AddFavouriteData[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            addFavouriteData[i] = new AddFavouriteData();
            addFavouriteData[i].stationData = list.get(i); // das sind ja schon Kopien
        }
        return addFavouriteData;
    }

    public static AddFavouriteData[] initInfoArrayFavourite(ArrayList<StationData> podcastArrayList) {
        // Favoriten ändern
        // Arr anlegen
        AddFavouriteData[] addFavouriteData = new AddFavouriteData[podcastArrayList.size()];
        for (int i = 0; i < podcastArrayList.size(); ++i) {
            addFavouriteData[i] = new AddFavouriteData();
            addFavouriteData[i].stationData = podcastArrayList.get(i).getCopy();
            addFavouriteData[i].stationDataOrg = podcastArrayList.get(i);
        }
        return addFavouriteData;
    }
}

