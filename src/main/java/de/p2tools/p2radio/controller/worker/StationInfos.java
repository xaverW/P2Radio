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


package de.p2tools.p2radio.controller.worker;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;

public class StationInfos {

    private final ProgData progData;
    private int amount = 0; //Gesamtanzahl
    private int notStarted = 0; //davon gestartet, alle, egal ob warten, laden oder fertig
    private int started = 0; //davon gestartet, alle, egal ob warten, laden oder fertig
    private boolean search = false;

    public StationInfos(ProgData progData) {
        this.progData = progData;

        progData.pEventHandler.addListener(new PListener(Events.TIMER) {
            public void ping(PEvent event) {
                if (!progData.loadNewStationList.getPropLoadStationList()) {
                    //dann wird die Liste neu gebaut
                    generateInfos();
                }
            }
        });
//        Listener.addListener(new Listener(Listener.EVENT_TIMER, StationInfos.class.getSimpleName()) {
//            @Override
//            public void ping() {
//                if (!progData.loadNewStationList.getPropLoadStationList()) {
//                    //dann wird die Liste neu gebaut
//                    generateInfos();
//                }
//            }
//        });
    }


    public synchronized int getAmount() {
        return amount;
    }

    public int getNotStarted() {
        return notStarted;
    }

    public synchronized int getStarted() {
        return started;
    }

    private synchronized void generateInfos() {
        search = !search;
        if (!search) {
            //nur alle 2 Sekunden suchen
            return;
        }

        PDuration.counterStart("StationInfos.generateInfos");
        // generiert die Anzahl Favoriten
        clean();
        progData.stationList.stream().forEach(station -> {
            ++amount;
            if (station.getStart() != null) {
                ++started;
            } else {
                ++notStarted;
            }
        });
        PDuration.counterStop("StationInfos.generateInfos");
    }

    private synchronized void clean() {
        //DonwloadInfos
        amount = 0;
        notStarted = 0;
        started = 0;
    }
}
