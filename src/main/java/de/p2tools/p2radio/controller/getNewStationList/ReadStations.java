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

package de.p2tools.p2radio.controller.getNewStationList;

import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.getNewStationList.radioBrowser.ReadRadioBrowser;

import javax.swing.event.EventListenerList;

public class ReadStations {
    //bis jetzt nur einer: RadioBrowser

    private final EventListenerList eventListenerList;
    private final ReadRadioBrowser readRadioBrowser;

    public ReadStations() {
        eventListenerList = new EventListenerList();
        readRadioBrowser = new ReadRadioBrowser();
        readRadioBrowser.addAdListener(new EventListenerLoadRadioList() {
            @Override
            public synchronized void start(EventLoadRadioList event) {
                for (final EventListenerLoadRadioList l : eventListenerList.getListeners(EventListenerLoadRadioList.class)) {
                    l.start(event);
                }
            }

            @Override
            public synchronized void progress(EventLoadRadioList event) {
                for (final EventListenerLoadRadioList l : eventListenerList.getListeners(EventListenerLoadRadioList.class)) {
                    l.progress(event);
                }
            }

            @Override
            public synchronized void finished(EventLoadRadioList event) {
            }
        });
    }

    public void addAdListener(EventListenerLoadRadioList listener) {
        eventListenerList.add(EventListenerLoadRadioList.class, listener);
    }

    public void loadNewStationList(StationList stationList) {
        // Senderliste importieren, URL automatisch wählen
        Thread th = new Thread(new ReadStationsThread(stationList));
        th.setName("ReadStationsThread");
        th.start();
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
            PLog.addSysLog("komplette Liste laden");

            //und jetzt File/Url laden
            ret = readRadioBrowser.readList(stationList);
            if (!ret || ProgData.getInstance().loadNewStationList.isStop()) {
                // wenn abgebrochen wurde, nicht weitermachen
                PLog.errorLog(951235497, "Es konnten keine Sender geladen werden!");
                ret = false;
            }

            reportFinished(ret);
            return ret;
        }
    }

    private synchronized void reportFinished(boolean ok) {
        for (final EventListenerLoadRadioList l : eventListenerList.getListeners(EventListenerLoadRadioList.class)) {
            l.finished(new EventLoadRadioList("", "", 0, 0, !ok));
        }
    }
}
