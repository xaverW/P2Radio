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

package de.p2tools.p2radio.tools.stationListFilter;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.tools.storedFilter.Filter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlackFilterFactory {

    private final static ProgData progData = ProgData.getInstance();
    private static int minBitrate = 0;
    private static int maxBitrate = StationFilterFactory.FILTER_BITRATE_MAX;

    private BlackFilterFactory() {
    }

    public static synchronized void getBlackFiltered() {
        // hier wird die komplette Senderliste gegen die Blacklist gefiltert
        // mit der Liste wird dann im TabSender weiter gearbeitet
        loadCurrentFilterSettings();

        PDuration.counterStart("StationListBlackFilter.getBlackFiltered");
        if (progData.stationList != null) {

            Stream<Station> initialStream = progData.stationList.stream();
            if (progData.storedFilters.getActFilterSettings().isBlacklistOn()) {
                //blacklist in ON
                PLog.sysLog("StationListBlackFilter - isBlacklistOn");
                initialStream = initialStream.filter(f -> !f.isBlackBlocked());

            } else if (progData.storedFilters.getActFilterSettings().isBlacklistOnly()) {
                //blacklist in ONLY
                PLog.sysLog("StationListBlackFilter - isBlacklistOnly");
                initialStream = initialStream.filter(f -> f.isBlackBlocked());

            } else {
                //blacklist in OFF
                PLog.sysLog("StationListBlackFilter - isBlacklistOff");
            }

            //todo
            PLog.sysLog("START: BlackFilterFactory-getBlackFiltered");
            progData.stationListBlackFiltered.addAll(initialStream.collect(Collectors.toList()));
        }
        PDuration.counterStop("StationListBlackFilter.getBlackFiltered");
    }

    public static synchronized void markStationBlack() {
        PDuration.counterStart("StationListBlackFilter.markStationBlack");
        final StationList stationList = progData.stationList;
        loadCurrentFilterSettings();

        PLog.sysLog("START: markStationBlack");
        stationList.stream().forEach(station -> {
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
    public static boolean checkNameGenreWithFilter(Filter name, Filter genre, Station station) {
        if (!name.empty && !StationFilterFactory.checkSenderName(name, station)) {
            return false;
        }
        return genre.empty || StationFilterFactory.checkGenre(genre, station);
    }

    public static boolean isBlackEmpty() {
        //liefert, ob es keine "black" gibt
        return ProgConfig.SYSTEM_BLACKLIST_MIN_BITRATE.get() == 0 &&
                ProgConfig.SYSTEM_BLACKLIST_MAX_BITRATE.get() == StationFilterFactory.FILTER_BITRATE_MAX &&
                progData.blackDataList.isEmpty();
    }

    private static synchronized boolean checkBlock(Station station) {
        // hier werden die Sender gegen die Blacklist geprüft
        if (station.getBitrateInt() != 0 && minBitrate > 0 &&
                station.getBitrateInt() < minBitrate) {
            return true;
        }
        if (station.getBitrateInt() != 0 && maxBitrate < StationFilterFactory.FILTER_BITRATE_MAX &&
                station.getBitrateInt() > maxBitrate) {
            return true;
        }

        // wegen der Möglichkeit "Whiteliste" muss das extra geprüft werden
        if (progData.blackDataList.isEmpty()) {
            return false;
        }

        return blockWithBlacklistFilters(station, false);
    }

    /**
     * Apply filters to station.
     *
     * @param station item to be filtered
     * @return true if station can be displayed
     */

    private static boolean blockWithBlacklistFilters(Station station, boolean countHits) {
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
