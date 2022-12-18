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

import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HistoryList extends SimpleListProperty<StationData> implements PDataList<StationData> {

    public static final String TAG = "LastPlayedList";
    private final ProgData progData;
    private final HistoryStartsFactory favouriteStartsFactory;
    private int no = 0;

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
        return "Liste aller Favoriten";
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
        d.setNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends StationData> elements) {
        elements.stream().forEach(f -> {
            f.setNo(++no);
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(StationData... var1) {
        for (StationData f : var1) {
            f.setNo(++no);
        }
        return super.addAll(var1);
    }

    public synchronized void addStation(StationData station) {
        if (!checkUrl(station.getStationUrl())) {
            //dann gibts ihn noch nicht
            StationData lastPlayed = new StationData();
            lastPlayed.setStation(station);
            this.add(0, lastPlayed);
        }
        reCount();
    }

    private boolean checkUrl(String url) {
        boolean ret = false;
        Optional<StationData> opt = this.stream().filter(l -> l.getStationUrl().equals(url)).findFirst();
        if (opt.isPresent()) {
            ret = true;
            StationData lastPlayed = opt.get();
            this.remove(lastPlayed);
            this.add(0, lastPlayed);
        }
        return ret;
    }

    private void reCount() {
        for (int i = 0; i < this.size(); ++i) {
            this.get(i).setNo(i + 1);
        }
        while (this.size() > ProgConst.MAX_LAST_PLAYED_LIST_SIZE) {
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
        for (final StationData lastPlayed : this) {
            if (lastPlayed.getStart() != null &&
                    (lastPlayed.getStart().getStartStatus().isStarted() || lastPlayed.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }

    public synchronized void addStationInList() {
        // bei Favoriten nach einem Programmstart/Neuladen der Senderliste
        // den Sender wieder eintragen
        PDuration.counterStart("FavouriteList.addSenderInList");
        int counter = 50;
        for (StationData stationData : this) {
            --counter;
            if (counter < 0) {
                break;
            }
            stationData.setStation(progData.stationList.getSenderByUrl(stationData.getStationUrl()));
        }
        PDuration.counterStop("FavouriteList.addSenderInList");
    }

    public synchronized StationData getUrlStation(String urlStation) {
        for (final StationData dataStationData : this) {
            if (dataStationData.getStationUrl().equals(urlStation)) {
                return dataStationData;
            }
        }
        return null;
    }

    public synchronized List<StationData> getListOfStartsNotFinished(String source) {
        return favouriteStartsFactory.getListOfStartsNotFinished(source);
    }
}
