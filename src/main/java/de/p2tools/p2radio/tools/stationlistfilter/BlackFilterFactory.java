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

import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationDataProperty;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.tools.storedfilter.Filter;

import java.util.stream.Stream;

public class BlackFilterFactory {

    private final static ProgData progData = ProgData.getInstance();
    private static int minBitrate = StationFilterFactory.FILTER_BITRATE_MIN;
    private static int maxBitrate = StationFilterFactory.FILTER_BITRATE_MAX;

    private BlackFilterFactory() {
    }

    public static synchronized void getBlackFiltered() {
        // hier wird die komplette Senderliste gegen die Blacklist gefiltert
        // mit der Liste wird dann im TabSender weiter gearbeitet
        loadCurrentFilterSettings();

        PDuration.counterStart("StationListBlackFilter.getBlackFiltered");
        if (progData.stationList != null) {

            Stream<StationData> initialStream = progData.stationList.stream();
            if (progData.storedFilters.getActFilterSettings().isBlacklistOn()) {
                //blacklist in ON
                P2Log.sysLog("StationListBlackFilter - isBlacklistOn");
                initialStream = initialStream.filter(f -> !f.isBlackBlocked());

            } else if (progData.storedFilters.getActFilterSettings().isBlacklistOnly()) {
                //blacklist in ONLY
                P2Log.sysLog("StationListBlackFilter - isBlacklistOnly");
                initialStream = initialStream.filter(StationDataProperty::isBlackBlocked);

            } else {
                //blacklist in OFF
                P2Log.sysLog("StationListBlackFilter - isBlacklistOff");
            }

            P2Log.sysLog("START: BlackFilterFactory-getBlackFiltered");
            progData.stationListBlackFiltered.setAll(initialStream.toList());
        }
        PDuration.counterStop("StationListBlackFilter.getBlackFiltered");
    }

    public static synchronized void markStationBlack() {
        // die Liste der Sender durchgehen und gegen die Blacklist checken
        // und geblockte Sender markieren
        PDuration.counterStart("StationListBlackFilter.markStationBlack");
        final StationList stationList = progData.stationList;
        loadCurrentFilterSettings();

        P2Log.sysLog("START: markStationBlack");
        stationList.forEach(station -> {
            station.setBlackBlocked(checkBlock(station));
        });
        PDuration.counterStop("StationListBlackFilter.markStationBlack");
    }

    /**
     * Abo und Blacklist prüfen
     *
     * @param name
     * @param genre
     * @param station
     * @return
     */
    public static boolean checkNameGenreWithFilter(Filter name, Filter genre, StationData station) {
        if (!name.empty && !StationFilterFactory.checkSenderName(name, station)) {
            return false;
        }
        return genre.empty || StationFilterFactory.checkGenre(genre, station);
    }

    public static boolean isBlackEmpty() {
        //liefert, ob es keine "black" gibt
        return ProgConfig.SYSTEM_BLACKLIST_MIN_BITRATE.get() == StationFilterFactory.FILTER_BITRATE_MIN &&
                ProgConfig.SYSTEM_BLACKLIST_MAX_BITRATE.get() == StationFilterFactory.FILTER_BITRATE_MAX &&
                progData.blackDataList.isEmpty();
    }

    private static synchronized boolean checkBlock(StationData station) {
        // hier werden die Sender gegen die Blacklist geprüft
        // true wenn geblockt
        if (station.getBitrate() != 0 && minBitrate > StationFilterFactory.FILTER_BITRATE_MIN &&
                station.getBitrate() < minBitrate) {
            return true;
        }
        if (station.getBitrate() != 0 && maxBitrate < StationFilterFactory.FILTER_BITRATE_MAX &&
                station.getBitrate() > maxBitrate) {
            return true;
        }

        // wegen der Möglichkeit "Whiteliste" muss das extra geprüft werden
        if (progData.blackDataList.isEmpty()) {
            return false;
        }

        return blockWithBlacklistFilters(station, false);
    }

    private static boolean blockWithBlacklistFilters(StationData station, boolean countHits) {
        for (final BlackData blackData : progData.blackDataList) {
            if (checkNameGenreWithFilter(blackData.fName, blackData.fGenre, station)) {
                if (countHits) {
                    blackData.incCountHits();
                }
                return !ProgConfig.SYSTEM_BLACKLIST_IS_WHITELIST.get();
            }
        }
        return ProgConfig.SYSTEM_BLACKLIST_IS_WHITELIST.get();
    }

    /**
     * Load current filter settings from config
     */
    private static void loadCurrentFilterSettings() {
        minBitrate = ProgConfig.SYSTEM_BLACKLIST_MIN_BITRATE.get();
        maxBitrate = ProgConfig.SYSTEM_BLACKLIST_MAX_BITRATE.get();
    }
}
