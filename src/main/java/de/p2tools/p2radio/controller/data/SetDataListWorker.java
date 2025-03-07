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

import de.p2tools.p2lib.configfile.pdata.P2DataList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.Collection;

public class SetDataListWorker extends SimpleListProperty<SetData> implements P2DataList<SetData> {
    // Liste aller Programmsets
    public static final String PATTERN_PATH_VLC = "PFAD_VLC";
    public static final String TAG = "SetDataList";

    private final BooleanProperty listChanged = new SimpleBooleanProperty(true);

    public SetDataListWorker() {
        super(FXCollections.observableArrayList());
    }

    public boolean isListChanged() {
        return listChanged.get();
    }

    public BooleanProperty listChangedProperty() {
        return listChanged;
    }

    public void setListChanged() {
        this.listChanged.set(!listChanged.get());
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Sets";
    }

    @Override
    public SetData getNewItem() {
        return new SetData();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(SetData.class)) {
            super.add((SetData) obj);
        }
    }

    @Override
    public boolean add(SetData setData) {
        checkSetDataName(setData);
        return super.add(setData);
    }

    @Override
    public boolean addAll(Collection<? extends SetData> elements) {
        elements.stream().forEach(sd -> checkSetDataName(sd));
        return super.addAll(elements);
    }

    @Override
    public boolean addAll(int i, Collection<? extends SetData> elements) {
        elements.stream().forEach(sd -> checkSetDataName(sd));
        return super.addAll(i, elements);
    }

    void checkSetDataName(SetData setData) {
        // Id auf einmaligkeit prüfen und leere visibleNames füllen
        // dient der geänderten Funktion -> SetData im Abo
        String setDataId = setData.getId();
        boolean found = false;
        int id = 0;

        do {

            for (SetData sd : this) {
                if (sd.getId().equals(setDataId)) {
                    found = true;
                    setDataId = setData.getId() + "-" + ++id;
                    break;
                }
                found = false;
            }

        } while (found);

        setData.setId(setDataId);
        if (setData.getVisibleName().isEmpty()) {
            setData.setVisibleName(setData.getId());
        }
    }
}
