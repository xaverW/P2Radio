/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.tools.storedfilter;

import de.p2tools.p2lib.configfile.pdata.P2DataList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class SelectedFilterListWorker extends SimpleListProperty<SelectedFilter> implements P2DataList<SelectedFilter> {
    public static final String TAG = "SelectedFilterList";

    public SelectedFilterListWorker() {
        super(FXCollections.observableArrayList());
    }


    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Filtereinstellungen";
    }

    @Override
    public SelectedFilter getNewItem() {
        return new SelectedFilter();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(SelectedFilter.class)) {
            super.add((SelectedFilter) obj);
        }
    }


}
