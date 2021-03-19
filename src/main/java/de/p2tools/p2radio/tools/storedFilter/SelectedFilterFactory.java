/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.tools.storedFilter;

import java.util.ArrayList;

public class SelectedFilterFactory {

    public static SelectedFilter getFilterCopy(SelectedFilter sfFrom) {
        SelectedFilter sf = new SelectedFilter();
        copyFilter(sfFrom, sf);
        return sf;
    }

    public static void copyFilter(SelectedFilter sfFrom, SelectedFilter sfTo) {
        sfTo.setName(sfFrom.getName());

        sfTo.setCodecVis(sfFrom.isCodecVis());
        sfTo.setCodec(sfFrom.getCodec());
        sfTo.setGenreVis(sfFrom.isGenreVis());
        sfTo.setGenre(sfFrom.getGenre());
        sfTo.setCountryVis(sfFrom.isCountryVis());
        sfTo.setCountry(sfFrom.getCountry());
        sfTo.setStationNameVis(sfFrom.isStationNameVis());
        sfTo.setStationName(sfFrom.getStationName());
        sfTo.setUrlVis(sfFrom.isUrlVis());
        sfTo.setUrl(sfFrom.getUrl());

        sfTo.setMinMaxBitVis(sfFrom.isMinMaxBitVis());
        sfTo.setMinBit(sfFrom.getMinBit());
        sfTo.setMaxBit(sfFrom.getMaxBit());

        sfTo.setOnlyVis(sfFrom.isOnlyVis());
        sfTo.setOnlyNew(sfFrom.isOnlyNew());
        sfTo.setNoFavourites(sfFrom.isNoFavourites());
        sfTo.setNoDoubles(sfFrom.isNoDoubles());

        sfTo.setBlacklistOn(sfFrom.isBlacklistOn());
        sfTo.setBlacklistOnly(sfFrom.isBlacklistOnly());
    }

    public static boolean compareFilterWithoutNameOfFilter(SelectedFilter sfFrom, SelectedFilter sfTo) {
        if (sfFrom == null || sfTo == null) {
            return false;
        }

        for (int i = 0; i < sfFrom.sfBooleanPropArr.length; i++) {
            if (sfFrom.sfBooleanPropArr[i].get() != sfTo.sfBooleanPropArr[i].get()) {
                return false;
            }
        }
        for (int i = 0; i < sfFrom.sfIntegerPropArr.length; i++) {
            if (sfFrom.sfIntegerPropArr[i].get() != sfTo.sfIntegerPropArr[i].get()) {
                return false;
            }
        }
        // nur die Filter (nicht den Namen) vergleichen
        for (int i = 1; i < sfFrom.sfStringPropArr.length; i++) {
            if (!sfFrom.sfStringPropArr[i].getValueSafe().equals(sfTo.sfStringPropArr[i].getValueSafe())) {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<String> printFilter(SelectedFilter sf) {
        ArrayList<String> list = new ArrayList<>();
        list.add("getName " + sf.getName());

        list.add("");
        list.add("isCodecVis " + sf.isCodecVis());
        list.add("getCodec " + sf.getCodec());
        list.add("isGenre " + sf.isGenreVis());
        list.add("getGenre " + sf.getGenre());
        list.add("isCountryVis " + sf.isCountryVis());
        list.add("getCountry " + sf.getCountry());
        list.add("isStationNameVis " + sf.isStationNameVis());
        list.add("getStationName " + sf.getStationName());
        list.add("isUrlVis " + sf.isUrlVis());
        list.add("getUrl " + sf.getUrl());

        list.add("");
        list.add("isMinMaxBitVis " + sf.isMinMaxBitVis());
        list.add("getMinBit " + sf.getMinBit());
        list.add("getMaxBit " + sf.getMaxBit());

        list.add("");
        list.add("isOnlyVis " + sf.isOnlyVis());
        list.add("isOnlyNew " + sf.isOnlyNew());
        list.add("isNoFavourites " + sf.isNoFavourites());
        list.add("isNoDoubles " + sf.isNoDoubles());

        list.add("");
        list.add("isBlacklistOn " + sf.isBlacklistOn());
        list.add("isBlacklistOnly " + sf.isBlacklistOnly());

        return list;
    }
}
