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

package de.p2tools.p2radio.controller.data.lastPlayed;

import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.station.Station;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LastPlayedList extends SimpleListProperty<LastPlayed> implements PDataList<LastPlayed> {

    public static final String TAG = "LastPlayedList";
    private final ProgData progData;
    private final LastPlayedStartsFactory favouriteStartsFactory;
    private int no = 0;

    private BooleanProperty favouriteChanged = new SimpleBooleanProperty(true);

    public LastPlayedList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
        this.favouriteStartsFactory = new LastPlayedStartsFactory(progData, this);
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
    public LastPlayed getNewItem() {
        return new LastPlayed();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(LastPlayed.class)) {
            add((LastPlayed) obj);
        }
    }

    public void sort() {
        Collections.sort(this);
    }

    @Override
    public synchronized boolean add(LastPlayed d) {
        d.setNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends LastPlayed> elements) {
        elements.stream().forEach(f -> {
            f.setNo(++no);
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(LastPlayed... var1) {
        for (LastPlayed f : var1) {
            f.setNo(++no);
        }
        return super.addAll(var1);
    }

    public synchronized boolean addFavourite(Favourite d) {
        LastPlayed lastPlayed = new LastPlayed();
        lastPlayed.setNo(++no);
        lastPlayed.setUrl(d.getUrl());
        return super.add(lastPlayed);
    }

    public synchronized boolean addStation(Station d) {
        LastPlayed lastPlayed = new LastPlayed();
        lastPlayed.setNo(++no);
        lastPlayed.setUrl(d.getUrl());
        return super.add(lastPlayed);
    }

//    public void addFavourite(boolean own) {
//        LastPlayed favourite = new LastPlayed();
//        favourite.setOwn(own);
//        favourite.setStationDate(new PLocalDate().getDateTime(PLocalDate.FORMAT_dd_MM_yyyy));
//        FavouriteAddOwnDialogController favouriteEditDialogController =
//                new FavouriteAddOwnDialogController(progData, favourite);
//
//        if (favouriteEditDialogController.isOk()) {
//            this.add(favourite);
//            progData.collectionList.updateNames();//könnte ja geändert sein
//        }
//    }

    public synchronized boolean remove(LastPlayed objects) {
        return super.remove(objects);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
    }

    public synchronized int countStartedAndRunningFavourites() {
        //es wird nach gestarteten und laufenden Favoriten gesucht
        int ret = 0;
        for (final LastPlayed favourite : this) {
            if (favourite.getStart() != null &&
                    (favourite.getStart().getStartStatus().isStarted() || favourite.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }

    public synchronized void addStationInList() {
        // bei Favoriten nach einem Programmstart/Neuladen der Senderliste
        // den Sender wieder eintragen
        PDuration.counterStart("FavouriteList.addSenderInList");
        int counter = 50; //todo das dauert sonst viel zu lang
        for (LastPlayed favourite : this) {
            --counter;
            if (counter < 0) {
                break;
            }
            favourite.setStation(progData.stationList.getSenderByUrl(favourite.getUrl()));
        }
        PDuration.counterStop("FavouriteList.addSenderInList");
    }

    public synchronized LastPlayed getUrlStation(String urlStation) {
        for (final LastPlayed dataFavourite : this) {
            if (dataFavourite.getUrl().equals(urlStation)) {
                return dataFavourite;
            }
        }
        return null;
    }

    public synchronized List<LastPlayed> getListOfStartsNotFinished(String source) {
        return favouriteStartsFactory.getListOfStartsNotFinished(source);
    }
}