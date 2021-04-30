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
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.gui.tools.Listener;

public class FavouriteInfos {

    private int amount = 0; //Gesamtanzahl
    private int notStarted = 0; //davon gestartet, alle, egal ob warten, laden oder fertig
    private int started = 0; //davon gestartet, alle, egal ob warten, laden oder fertig
    private boolean search = false;

    private final ProgData progData;

    public FavouriteInfos(ProgData progData) {
        this.progData = progData;
        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, FavouriteInfos.class.getSimpleName()) {
            @Override
            public void ping() {
                generateFavouriteInfos();
            }
        });
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

    private synchronized void generateFavouriteInfos() {
        search = !search;
        if (!search) {
            //nur alle 2 Sekunden suchen
            return;
        }

        PDuration.counterStart("FavouriteInfos.generateInfos");
        // generiert die Anzahl Favoriten
        clean();
        for (final Favourite favourite : progData.favouriteList) {
            ++amount;
            if (favourite.getStart() != null) {
                ++started;
            } else {
                ++notStarted;
            }
        }
        PDuration.counterStop("FavouriteInfos.generateInfos");
    }

    private synchronized void clean() {
        //DonwloadInfos
        amount = 0;
        notStarted = 0;
        started = 0;
    }
}
