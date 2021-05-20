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


package de.p2tools.p2radio.controller.data.favourite;

import de.p2tools.p2radio.controller.data.collection.CollectionData;
import de.p2tools.p2radio.controller.data.collection.CollectionList;
import de.p2tools.p2radio.tools.storedFilter.Filter;
import javafx.beans.property.*;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FavouriteFilter {

    private ObjectProperty<CollectionData> collectionNameFilter = new SimpleObjectProperty<>(null);
    private BooleanProperty ownFilter = new SimpleBooleanProperty(false);
    private BooleanProperty gradeFilter = new SimpleBooleanProperty(false);
    private StringProperty genreFilter = new SimpleStringProperty("");

    public Predicate<Favourite> clearFilter() {
        collectionNameFilter.setValue(null);
        ownFilter.set(false);
        gradeFilter.set(false);
        genreFilter.set("");
        return getPredicate();
    }

    public Predicate<Favourite> getPredicate() {
        Predicate<Favourite> predicate = favourite -> true;

        if (collectionNameFilter.get() != null && !collectionNameFilter.get().getName().isEmpty() &&
                !collectionNameFilter.get().getName().contains(CollectionList.COLLECTION_ALL)) {
            predicate = predicate.and(favourite -> favourite.getCollectionName().equals(collectionNameFilter.get().getName()));
        }
        if (ownFilter.get()) {
            predicate = predicate.and(favourite -> favourite.isOwn());
        }
        if (gradeFilter.get()) {
            predicate = predicate.and(favourite -> favourite.getGrade() > 0);
        }
        if (!genreFilter.get().isEmpty()) {
            predicate = predicate.and(favourite -> check(genreFilter.get(), favourite.getGenre()));
        }
        return predicate;
    }

    public ObjectProperty<CollectionData> collectionNameFilterProperty() {
        return collectionNameFilter;
    }

    public boolean isOwnFilter() {
        return ownFilter.get();
    }

    public BooleanProperty ownFilterProperty() {
        return ownFilter;
    }

    public void setOwnFilter(boolean ownFilter) {
        this.ownFilter.set(ownFilter);
    }

    public boolean isGradeFilter() {
        return gradeFilter.get();
    }

    public BooleanProperty gradeFilterProperty() {
        return gradeFilter;
    }

    public void setGradeFilter(boolean gradeFilter) {
        this.gradeFilter.set(gradeFilter);
    }

    public String getGenreFilter() {
        return genreFilter.get();
    }

    public StringProperty genreFilterProperty() {
        return genreFilter;
    }

    public void setGenreFilter(String genreFilter) {
        this.genreFilter.set(genreFilter);
    }

    private static boolean check(String filter, String im) {
        if (Filter.isPattern(filter)) {
            Pattern pattern = Filter.makePattern(filter);
            // dann ists eine RegEx
            return (pattern.matcher(im).matches());
        }
        if (im.toLowerCase().contains(filter)) {
            // wenn einer passt, dann ists gut
            return true;
        }

        // nix wars
        return false;
    }

}
