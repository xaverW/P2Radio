/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.controller.data.bookmark;

import de.p2tools.p2lib.configfile.pdata.P2DataList;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class BookmarkList extends SimpleListProperty<BookmarkData> implements P2DataList<BookmarkData> {

    public static final String TAG = "BookmarkList";
    private FilteredList<BookmarkData> filteredList = null;
    private SortedList<BookmarkData> sortedList = null;

    public BookmarkList() {
        super(FXCollections.observableArrayList());
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Bookmarks";
    }

    @Override
    public BookmarkData getNewItem() {
        return new BookmarkData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(BookmarkData.class)) {
            BookmarkData d = (BookmarkData) obj;
            this.add(d);
        }
    }

    public FilteredList<BookmarkData> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
        }
        return filteredList;
    }

    public SortedList<BookmarkData> getSortedList() {
        filteredList = getFilteredList();
        if (sortedList == null) {
            sortedList = new SortedList<>(filteredList);
        }
        return sortedList;
    }

    //===============
    //===============
    public boolean checkIfUrlAlreadyIn(String url) {
        // wenn url gefunden, dann true zurück
        for (BookmarkData bookmarkData : this) {
            if (bookmarkData.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}
