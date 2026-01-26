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


package de.p2tools.p2radio.controller.data.filter;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_boolProp;
import de.p2tools.p2lib.configfile.config.Config_pData;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.collection.CollectionList;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFilterXml;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.tools.storedfilter.Filter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FavouriteFilter extends FavouriteFilterXml {

    private final BooleanProperty ownFilter = new SimpleBooleanProperty(false);
    private final BooleanProperty gradeFilter = new SimpleBooleanProperty(false);
    private final StringProperty genreFilter = new SimpleStringProperty("");
    private CollectionData collectionData = new CollectionData(CollectionList.COLLECTION_ALL);

    private static boolean check(String filter, String im) {
        if (Filter.isPattern(filter)) {
            Pattern pattern = Filter.makePattern(filter);
            if (pattern == null) {
                return im.toLowerCase().contains(filter);
            }

            // dann ists eine RegEx
            return (pattern.matcher(im).matches());
        }

        // wenn einer passt, dann ists gut
        return im.toLowerCase().contains(filter);
    }

    public void clearFilter() {
        collectionData = ProgData.getInstance().collectionList.getByName(CollectionList.COLLECTION_ALL);
        ownFilter.set(false);
        gradeFilter.set(false);
        genreFilter.set("");
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_pData(collectionData));
        list.add(new Config_boolProp("Eigene", COLUMN_NAMES[FAVOURITE_FILTER_OWN], ownFilter));
        list.add(new Config_boolProp("Grade", COLUMN_NAMES[FAVOURITE_FILTER_GRADE], gradeFilter));
        list.add(new Config_stringProp("Genre", COLUMN_NAMES[FAVOURITE_FILTER_GENRE], genreFilter));
        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public Predicate<StationData> getPredicate() {
        Predicate<StationData> predicate = favourite -> true;

        if (collectionData != null && !collectionData.getName().isEmpty() &&
                !collectionData.getName().contains(CollectionList.COLLECTION_ALL)) {
            predicate = predicate.and(favourite -> favourite.getCollectionName().equals(collectionData.getName()));
        }
        if (ownFilter.get()) {
            predicate = predicate.and(favourite -> favourite.isOwn());
        }
        if (gradeFilter.get()) {
            predicate = predicate.and(favourite -> favourite.getOwnGrade() > 0);
        }
        if (!genreFilter.get().isEmpty()) {
            predicate = predicate.and(favourite -> check(genreFilter.get(), favourite.getGenre()));
        }
        return predicate;
    }

    public CollectionData getCollectionData() {
        return collectionData;
    }

    public void setCollectionData(CollectionData collectionData) {
        this.collectionData = collectionData;
    }

    public boolean isOwnFilter() {
        return ownFilter.get();
    }

    public void setOwnFilter(boolean ownFilter) {
        this.ownFilter.set(ownFilter);
    }

    public BooleanProperty ownFilterProperty() {
        return ownFilter;
    }

    public boolean isGradeFilter() {
        return gradeFilter.get();
    }

    public void setGradeFilter(boolean gradeFilter) {
        this.gradeFilter.set(gradeFilter);
    }

    public BooleanProperty gradeFilterProperty() {
        return gradeFilter;
    }

    public String getGenreFilter() {
        return genreFilter.get();
    }

    public void setGenreFilter(String genreFilter) {
        this.genreFilter.set(genreFilter);
    }

    public StringProperty genreFilterProperty() {
        return genreFilter;
    }

}
