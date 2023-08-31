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

package de.p2tools.p2radio.controller.data.favourite;

import de.p2tools.p2lib.configfile.pdata.PDataList;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FavouriteList extends SimpleListProperty<StationData> implements PDataList<StationData> {

    public static final String TAG = "FavouriteList";
    private final ProgData progData;
    private final FavouriteStartsFactory favouriteStartsFactory;

    public FavouriteList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
        this.favouriteStartsFactory = new FavouriteStartsFactory(progData, this);
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
    public synchronized boolean add(StationData stationData) {
        progData.collectionList.addNewName(stationData.getCollectionName());
        stationData.setFavourite(true);
        return super.add(stationData);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends StationData> elements) {
        elements.stream().forEach(stationData -> {
            stationData.setFavourite(true);
            progData.collectionList.addNewName(stationData.getCollectionName());
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(StationData... var1) {
        for (StationData stationData : var1) {
            stationData.setFavourite(true);
            progData.collectionList.addNewName(stationData.getCollectionName());
        }
        return super.addAll(var1);
    }

    public synchronized boolean remove(StationData objects) {
        return super.remove(objects);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
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
        return favouriteStartsFactory.getListOfRunningStations(source);
    }
}
