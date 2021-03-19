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

package de.p2tools.p2radio.controller.data.collection;

import de.p2tools.p2Lib.configFile.pData.PDataList;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@SuppressWarnings("serial")
public class CollectionList extends SimpleListProperty<CollectionData> implements PDataList<CollectionData> {

    public static final String COLLECTION_ALL = " ";
    public static final String TAG = "CollectionDataList";

    private int nr = 0;
    private final ProgData progData;

    public CollectionList(ProgData progData) {
        super(FXCollections.observableArrayList());
        this.progData = progData;
        addNewItem(new CollectionData(COLLECTION_ALL));
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste der Sammlungen";
    }

    @Override
    public CollectionData getNewItem() {
        return new CollectionData();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(CollectionData.class)) {
            add((CollectionData) obj);
        }
    }

    @Override
    public boolean add(CollectionData b) {
        if (searchName(b.getName())) {
            return false;
        }

        b.setNo(nr++);
        return super.add(b);
    }

    public boolean searchName(String name) {
        if (name == null || name.isEmpty()) {
            return true;
        }
        for (CollectionData cd : this) {
            if (cd.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void updateNames() {
        final ObservableList<String> list = FXCollections.observableArrayList();
        list.add(CollectionList.COLLECTION_ALL);

        progData.favouriteList.stream().forEach(favourite -> {
            if (!favourite.getCollectionName().isEmpty() &&
                    !list.contains(favourite.getCollectionName())) {
                list.add(favourite.getCollectionName());
            }
        });

        this.clear();
        list.stream().forEach(s -> {
            this.addNewName(s);
        });
    }

    public boolean addNewName(String name) {
        if (searchName(name)) {
            return false;
        }

        this.addNewItem(new CollectionData(name));
        return true;
    }

    public ObservableList<String> getNames() {
        final ObservableList<String> list = FXCollections.observableArrayList();
        this.stream().forEach(c -> list.add(c.getName()));
        return list;
    }
}
