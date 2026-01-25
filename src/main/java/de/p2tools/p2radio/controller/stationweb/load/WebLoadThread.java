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

package de.p2tools.p2radio.controller.stationweb.load;

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationList;

public class WebLoadThread {

    private WebLoadThread() {
    }

    public static void loadStationList(StationList stationList) {
        // Senderliste importieren, URL automatisch wählen
        Thread th = new Thread(new LoadStationThread(stationList));
        th.setName("ReadStationsThread");
        th.start();
    }

    private static class LoadStationThread implements Runnable {
        private final StationList stationList;

        public LoadStationThread(StationList stationList) {
            this.stationList = stationList;
        }

        @Override
        public void run() {
            boolean ok;
            this.stationList.clear();
            P2Log.sysLog("komplette Liste laden");

            //und jetzt File/Url laden
            ok = WebLoadFactory.loadList(stationList);
            if (!ok || ProgData.getInstance().worker.isStop()) {
                // wenn abgebrochen wurde, nicht weitermachen
                P2Log.errorLog(951235497, "Es konnten keine Sender geladen werden!");
                ok = false;
            }

            // Laden ist durch
            WebAfterLoadFactory.afterWebLoad(ok);
        }
    }
}
