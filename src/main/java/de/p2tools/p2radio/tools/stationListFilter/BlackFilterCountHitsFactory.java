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
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;

public class BlackFilterCountHitsFactory {

    private final static ProgData progData = ProgData.getInstance();

    private BlackFilterCountHitsFactory() {
    }

    public static synchronized void countHits(boolean abort) {
        // hier wird die Blacklist gegen die Senderliste gefilter und die Treffer
        // für jeden Blacklisteintrag ermittelt

        PDuration.counterStart("BlackFilterCountHitsFactory.countHits");
        progData.blackDataList.clearCounter();

        final StationList stationList = progData.stationList;
        if (stationList != null) {
            stationList.parallelStream().forEach(station -> applyBlacklistFilters(station, abort));
        }

        PDuration.counterStop("BlackFilterCountHitsFactory.countHits");
    }


    private static void applyBlacklistFilters(StationData station, boolean abort) {
        // zum Sortieren ist es sinnvoll, dass ALLE MÖGLICHEN Treffer gesucht werden
        for (final BlackData blackData : progData.blackDataList) {
            if (BlackFilterFactory.checkNameGenreWithFilter(blackData.fName, blackData.fGenre, station)) {
                blackData.incCountHits();
                if (abort) {
                    return;
                }
            }
        }
    }
}
