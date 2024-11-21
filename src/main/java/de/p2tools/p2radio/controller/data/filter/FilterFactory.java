/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.data.filter;

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.collection.CollectionList;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.collections.transformation.FilteredList;

import java.util.function.Predicate;


public class FilterFactory {

    public static final String LIST_STATION = "s";
    public static final String LIST_FAVOURITE = "f";
    public static final String LIST_HISTORY = "h";

    private FilterFactory() {
    }

    public static void setFilter(FilteredList<StationData> filteredList) {
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
            filteredList.setPredicate(getStationPredicateSmallGui());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
            filteredList.setPredicate(getFavoritePredicateSmallGui());

        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_HISTORY)) {
            filteredList.setPredicate(getHistoryPredicateSmallGui());
        }
    }


    public static Predicate<StationData> getStationPredicateSmallGui() {
        Predicate<StationData> predicate = stationData -> true;
        final String genre = ProgConfig.SMALL_RADIO_SELECTED_STATION_GENRE.getValueSafe().toLowerCase();
        if (!genre.isEmpty()) {
            predicate = predicate.and(stationData -> check(genre, stationData.getGenre()));
        }
        return predicate;
    }

    public static Predicate<StationData> getFavoritePredicateSmallGui() {
        Predicate<StationData> predicate = favourite -> true;
        final CollectionData collectionData = ProgData.getInstance()
                .collectionList.getByName(ProgConfig.SMALL_RADIO_SELECTED_COLLECTION_NAME.getValueSafe());

        if (collectionData != null && !collectionData.getName().isEmpty() &&
                !collectionData.getName().contains(CollectionList.COLLECTION_ALL)) {
            predicate = predicate.and(stationData -> stationData.getCollectionName().equals(collectionData.getName()));
        }

        final String genre = ProgConfig.SMALL_RADIO_SELECTED_FAVOURITE_GENRE.getValueSafe().toLowerCase();
        if (!genre.isEmpty()) {
            predicate = predicate.and(stationData -> check(genre, stationData.getGenre()));
        }

        return predicate;
    }

    public static Predicate<StationData> getHistoryPredicateSmallGui() {
        Predicate<StationData> predicate = favourite -> true;
        final String genre = ProgConfig.SMALL_RADIO_SELECTED_HISTORY_GENRE.getValueSafe().toLowerCase();
        if (!genre.isEmpty()) {
            predicate = predicate.and(stationData -> check(genre, stationData.getGenre()));
        }
        return predicate;
    }

    private static boolean check(String filter, String im) {
        return im.toLowerCase().contains(filter);
    }
}
