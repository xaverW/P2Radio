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

package de.p2tools.p2radio.tools.stationlistfilter;

import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.tools.storedfilter.Filter;

import java.util.regex.Pattern;

public class StationFilterFactory {

    public static final int FILTER_BITRATE_MIN = 0;
    public static final int FILTER_BITRATE_MAX = 320; // kbit/s

    public static boolean checkCodec(Filter sender, StationData station) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equalsIgnoreCase(station.getCodec())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkSenderName(Filter senderName, StationData station) {
        if (senderName.exact) {
            // da ist keine Form optimal?? aber so passt es zur Sortierung der Themenliste
            return senderName.filter.equalsIgnoreCase(station.getStationName());
        } else {
            return check(senderName, station.getStationName());
        }
    }

    public static boolean checkGenre(Filter filter, StationData station) {
        // nur ein Suchbegriff muss passen
        if (filter.exact) {
            return filter.filter.equalsIgnoreCase(station.getGenre());
        } else {
            return check(filter, station.getGenre());
        }
    }

    public static boolean checkCountry(Filter sender, StationData station) {
        // nur ein Suchbegriff muss passen
        for (final String s : sender.filterArr) {
            // dann jeden Suchbegriff checken
            if (s.equalsIgnoreCase(station.getCountry())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkUrl(Filter url, StationData station) {
        return check(url, station.getWebsite())
                || check(url, station.getStationUrl());
    }

    public static boolean checkSomewhere(Filter somewhere, StationData station) {
        return check(somewhere, station.getStationName())
                || check(somewhere, station.getGenre())
                || check(somewhere, station.getWebsite())
                || check(somewhere, station.getStationUrl());
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
        // wenn einer passt, dann ists gut
        return im.toLowerCase().contains(filter);
    }
}
