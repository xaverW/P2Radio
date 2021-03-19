/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.tools.stationListFilter;

import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;
import de.p2tools.p2radio.tools.storedFilter.Filter;

import java.util.regex.Pattern;

public class StationFilterFactory {

    public static final int FILTER_BITRATE_MIN = 0;
    public static final int FILTER_BITRATE_MAX = 320;

    public static boolean checkCodec(Filter sender, Station station) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equalsIgnoreCase(station.arr[StationXml.STATION_CODEC])) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkSenderName(Filter senderName, Station station) {
        if (senderName.exact) {
            // da ist keine Form optimal?? aber so passt es zur Sortierung der Themenliste
            if (!senderName.filter.equalsIgnoreCase(station.getName())) {
                return false;
            }
        } else {
            if (!check(senderName, station.getName())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkGenre(Filter filter, Station station) {
        // nur ein Suchbegriff muss passen
        if (filter.exact) {
            if (!filter.filter.equalsIgnoreCase(station.getGenre())) {
                return false;
            }
        } else {
            if (!check(filter, station.getGenre())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkCountry(Filter sender, Station station) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equalsIgnoreCase(station.arr[StationXml.STATION_COUNTRY])) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkUrl(Filter url, Station station) {
        if (!check(url, station.arr[StationXml.STATION_WEBSITE])
                && !check(url, station.arr[StationXml.STATION_URL])) {
            return false;
        }
        return true;
    }

    public static boolean checkBitrateMin(int filterBitrate, long radioBitrate) {
        return filterBitrate == 0 || radioBitrate == 0 || radioBitrate >= filterBitrate;
    }

    public static boolean checkBitrateMax(int filterBitrate, long radioBitrate) {
        return filterBitrate == FILTER_BITRATE_MAX || radioBitrate == 0
                || radioBitrate <= filterBitrate;
    }

    private static boolean check(Filter filter, String im) {
        // wenn einer passt, dann ists gut
        if (filter.filterArr.length == 1) {
            return check(filter.filterArr[0], filter.pattern, im);
        }

        if (filter.filterAnd) {
            // Suchbegriffe müssen alle passen
            for (final String s : filter.filterArr) {
                // dann jeden Suchbegriff checken
                if (!im.toLowerCase().contains(s)) {
                    return false;
                }
            }
            return true;

        } else {
            // nur ein Suchbegriff muss passen
            for (final String s : filter.filterArr) {
                // dann jeden Suchbegriff checken
                if (im.toLowerCase().contains(s)) {
                    return true;
                }
            }
        }

        // nix wars
        return false;
    }

    private static boolean check(String filter, Pattern pattern, String im) {
        if (pattern != null) {
            // dann ists eine RegEx
            return (pattern.matcher(im).matches());
        }
        if (im.toLowerCase().contains(filter)) {
            // wenn einer passt, dann ists gut
            return true;
        }

        // nix wars
        return false;
    }
}
