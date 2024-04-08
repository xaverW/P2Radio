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
import java.util.Optional;

public class HistoryList extends SimpleListProperty<StationData> implements P2DataList<StationData> {

    public static final String TAG = "HistoryList" + P2Data.TAGGER + "LastPlayedList";
    private final ProgData progData;
    private final HistoryStartsFactory favouriteStartsFactory;
//    private int no = 0;

    public HistoryList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
        this.favouriteStartsFactory = new HistoryStartsFactory(progData, this);
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
//        d.setStationNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends StationData> elements) {
//        elements.stream().forEach(f -> {
//            f.setStationNo(++no);
//        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(StationData... var1) {
//        for (StationData f : var1) {
//            f.setStationNo(++no);
//        }
        return super.addAll(var1);
    }

    public synchronized void addStation(StationData station) {
        if (!checkUrl(station.getStationUrl())) {
            //dann gibts ihn noch nicht
            StationData stationData = new StationData();
            stationData.setStation(station);
            this.add(0, stationData);
        }
        reCount();
    }

    private boolean checkUrl(String url) {
        boolean ret = false;
        Optional<StationData> opt = this.stream().filter(l -> l.getStationUrl().equals(url)).findFirst();
        if (opt.isPresent()) {
            ret = true;
            StationData stationData = opt.get();
            this.remove(stationData);
            this.add(0, stationData);
        }
        return ret;
    }

    private void reCount() {
        for (int i = 0; i < this.size(); ++i) {
            this.get(i).setStationNo(i + 1);
        }
        while (this.size() > ProgConst.MAX_HISTORY_LIST_SIZE) {
            this.remove(this.size() - 1);
        }
    }

    public synchronized boolean remove(StationData objects) {
        return super.remove(objects);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
    }

    public synchronized int countStartedAndRunningFavourites() {
        //es wird nach gestarteten und laufenden Favoriten gesucht
        int ret = 0;
        for (final StationData stationData : this) {
            if (stationData.getStart() != null &&
                    (stationData.getStart().getStartStatus().isStarted() || stationData.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }

//    public synchronized StationData getUrlStation(String urlStation) {
//        for (final StationData dataStationData : this) {
//            if (dataStationData.getStationUrl().equals(urlStation)) {
//                return dataStationData;
//            }
//        }
//        return null;
//    }
//
//    public synchronized List<StationData> getListOfStartsNotFinished(String source) {
//        return favouriteStartsFactory.getListOfStartsNotFinished(source);
//    }
}
