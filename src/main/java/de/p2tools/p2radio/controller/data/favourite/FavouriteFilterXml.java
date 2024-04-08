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


import de.p2tools.p2lib.configfile.pdata.P2DataSample;
import de.p2tools.p2radio.controller.data.filter.FavouriteFilter;

public class FavouriteFilterXml extends P2DataSample<FavouriteFilter> {
    public static final String TAG = "FavouriteFilter";

    public static final int FAVOURITE_FILTER_COLLECTION_NAME = 0;
    public static final int FAVOURITE_FILTER_OWN = 1;
    public static final int FAVOURITE_FILTER_GRADE = 2;
    public static final int FAVOURITE_FILTER_GENRE = 3;

    public static final String[] COLUMN_NAMES = {
            "Sammlung",
            "Eigene",
            "Grade",
            "Genre"
    };
    public static int MAX_ELEM = COLUMN_NAMES.length;
}
