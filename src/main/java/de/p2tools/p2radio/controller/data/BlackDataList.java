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

package de.p2tools.p2radio.controller.data;

import de.p2tools.p2lib.configfile.pdata.PDataList;
import de.p2tools.p2lib.tools.events.PEvent;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.tools.stationlistfilter.BlackFilterCountHitsFactory;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class BlackDataList extends SimpleListProperty<BlackData> implements PDataList<BlackData> {

    public static final String TAG = "BlackDataList";
    private final ProgData progData;
    private int nr = 0;

    public BlackDataList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller BlackData";
    }

    @Override
    public BlackData getNewItem() {
        return new BlackData();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(BlackData.class)) {
            super.add((BlackData) obj);
        }
    }

    public synchronized boolean add(BlackData b) {
        b.setNo(nr++);
        return super.add(b);
    }

    public synchronized boolean addAndNotify(BlackData b) {
        // aus dem (Maus) Menü, in die BlackList einfügen
        b.setNo(nr++);
        final boolean ret = super.add(b);
        filterListAndNotifyListeners();
        return ret;
    }

    public synchronized boolean removeAndNotify(Object b) {
        final boolean ret = super.remove(b);
        filterListAndNotifyListeners();
        return ret;
    }

    public synchronized void clearAndNotify() {
        super.clear();
        filterListAndNotifyListeners();
    }

    public synchronized void filterListAndNotifyListeners() {
        progData.stationList.filterListWithBlacklist(true);
        progData.pEventHandler.notifyListener(new PEvent(Events.BLACKLIST_CHANGED));
    }

    public synchronized void clearCounter() {
        for (final BlackData blackData : this) {
            blackData.setCountHits(0);
        }
    }

    public synchronized void sortIncCounter(boolean searchHitsBefore) {
        if (searchHitsBefore) {
            // zuerst ohne Abbruch Treffer suchen
            BlackFilterCountHitsFactory.countHits(false);

            // und dann sortieren
            Collections.sort(this, Comparator.comparingInt(BlackDataProps::getCountHits).reversed());

            // dann die tatsächlichen Trefferzahlen ermitteln
            BlackFilterCountHitsFactory.countHits(true);
        }

        // und dann endgültig sortieren
        Collections.sort(this, Comparator.comparingInt(BlackDataProps::getCountHits).reversed());

        // zum Schluss noch neu nummerieren 1, 2, ...
        int i = 0;
        for (BlackData blackData : this) {
            blackData.setNo(++i);
        }
    }
}
