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

import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.dialog.FavouriteAddOwnDialogController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FavouriteList extends SimpleListProperty<Favourite> implements PDataList<Favourite> {

    public static final String TAG = "FavouriteList";
    private final ProgData progData;
    private final FavouriteStartsFactory favouriteStartsFactory;
    private int no = 0;

    private BooleanProperty favouriteChanged = new SimpleBooleanProperty(true);

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
    public Favourite getNewItem() {
        return new Favourite();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(Favourite.class)) {
            add((Favourite) obj);
        }
    }

    public void sort() {
        Collections.sort(this);
    }

    @Override
    public synchronized boolean add(Favourite d) {
        progData.collectionList.addNewName(d.getCollectionName());
        d.setNo(++no);
        return super.add(d);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends Favourite> elements) {
        elements.stream().forEach(f -> {
            f.setNo(++no);
            progData.collectionList.addNewName(f.getCollectionName());
        });
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(Favourite... var1) {
        for (Favourite f : var1) {
            f.setNo(++no);
            progData.collectionList.addNewName(f.getCollectionName());
        }
        return super.addAll(var1);
    }

    public void addFavourite(boolean own) {
        Favourite favourite = new Favourite();
        favourite.setOwn(own);
        favourite.setStationDate(new PLocalDate().getDateTime(PLocalDate.FORMAT_dd_MM_yyyy));
        FavouriteAddOwnDialogController favouriteEditDialogController =
                new FavouriteAddOwnDialogController(progData, favourite);

        if (favouriteEditDialogController.isOk()) {
            this.add(favourite);
            progData.collectionList.updateNames();//könnte ja geändert sein
        }
    }

    public synchronized boolean remove(Favourite objects) {
        return super.remove(objects);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> objects) {
        return super.removeAll(objects);
    }

    public synchronized int countStartedAndRunningFavourites() {
        // es wird nach gestarteten und laufenden Favoriten gesucht
        int ret = 0;
        for (final Favourite favourite : this) {
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
        for (Favourite favourite : this) {
            --counter;
            if (counter < 0) {
                break;
            }
            favourite.setStation(progData.stationList.getSenderByUrl(favourite.getUrl()));
        }
        PDuration.counterStop("FavouriteList.addSenderInList");
    }

    public synchronized Favourite getUrlStation(String urlStation) {
        for (final Favourite dataFavourite : this) {
            if (dataFavourite.getUrl().equals(urlStation)) {
                return dataFavourite;
            }
        }
        return null;
    }

    public synchronized List<Favourite> getListOfStartsNotFinished(String source) {
        return favouriteStartsFactory.getListOfStartsNotFinished(source);
    }
}
