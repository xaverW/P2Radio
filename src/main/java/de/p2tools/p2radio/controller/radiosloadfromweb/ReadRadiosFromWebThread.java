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

package de.p2tools.p2radio.controller.radiosloadfromweb;

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.controller.pevent.RunEventRadio;

public class ReadRadiosFromWebThread {

    public ReadRadiosFromWebThread() {
    }

    public void loadNewStationList(StationList stationList) {
        // Senderliste importieren, URL automatisch wählen
        Thread th = new Thread(new ReadStationsThread(stationList));
        th.setName("ReadStationsThread");
        th.start();
    }

    private synchronized void reportFinished(boolean ok) {
        ProgData.getInstance().pEventHandler.notifyListener(
                new RunEventRadio(PEvents.READ_STATIONS, RunEventRadio.NOTIFY.FINISHED,
                        "", "Senderliste geladen", 0, !ok));
    }

    private class ReadStationsThread implements Runnable {
        private final StationList stationList;

        public ReadStationsThread(StationList stationList) {
            this.stationList = stationList;
        }

        @Override
        public void run() {
            runReadStationsThread(stationList);
        }

        private boolean runReadStationsThread(StationList stationList) {
            boolean ret;
            this.stationList.clear();
            P2Log.sysLog("komplette Liste laden");

            //und jetzt File/Url laden
            ret = new ReadRadiosFromWeb().readList(stationList);
            if (!ret || ProgData.getInstance().loadNewStationList.isStop()) {
                // wenn abgebrochen wurde, nicht weitermachen
                P2Log.errorLog(951235497, "Es konnten keine Sender geladen werden!");
                ret = false;
            }

            reportFinished(ret);
            return ret;
        }
    }
}
