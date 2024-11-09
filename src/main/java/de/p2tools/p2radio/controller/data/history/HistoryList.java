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

package de.p2tools.p2radio.controller.data.history;

import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataList;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;
import java.util.Collections;

public class HistoryList extends SimpleListProperty<StationData> implements P2DataList<StationData> {

    public static final String TAG = "HistoryList" + P2Data.TAGGER + "LastPlayedList";
    private final ProgData progData;

    public HistoryList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller gespielten Sender";
    }

    @Override
    public StationData getNewItem() {
        return new StationData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(StationData.class)) {
            add((StationData) obj);
        }
    }

    public void sort() {
        Collections.sort(this);
    }

    @Override
    public synchronized boolean add(StationData d) {
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends StationData> elements) {
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(StationData... var1) {
        return super.addAll(var1);
    }

    public synchronized void addStation(StationData stationData) {
        if (!this.contains(stationData)) {
            //dann ist er noch nicht drin
            this.add(stationData);
            reCount();
        }
    }

    private void reCount() {
        StationData stationData = null;
        while (this.size() > ProgConst.MAX_HISTORY_LIST_SIZE) {
            // ältesten suchen
            for (StationData sd : this) {
                if (stationData == null ||
                        stationData.getStationDateLastStart().isAfter(sd.getStationDateLastStart())) {
                    stationData = sd;
                }
            }

            this.remove(stationData);
            stationData = null;
        }
    }
}
