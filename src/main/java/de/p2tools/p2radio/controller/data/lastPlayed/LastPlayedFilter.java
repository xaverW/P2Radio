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


package de.p2tools.p2radio.controller.data.lastPlayed;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2radio.tools.storedFilter.Filter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class LastPlayedFilter extends LastPlayedFilterXml {

    private BooleanProperty gradeFilter = new SimpleBooleanProperty();
    private StringProperty genreFilter = new SimpleStringProperty();

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigBoolPropExtra("Grade", COLUMN_NAMES[LAST_PLAYED_FILTER_GRADE], gradeFilter));
        list.add(new ConfigStringPropExtra("Genre", COLUMN_NAMES[LAST_PLAYED_FILTER_GENRE], genreFilter));
        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public Predicate<LastPlayed> clearFilter() {
        gradeFilter.set(false);
        genreFilter.set("");
        return getPredicate();
    }

    public Predicate<LastPlayed> getPredicate() {
        Predicate<LastPlayed> predicate = favourite -> true;

        if (gradeFilter.get()) {
            predicate = predicate.and(favourite -> favourite.getGrade() > 0);
        }
        if (!genreFilter.get().isEmpty()) {
            predicate = predicate.and(favourite -> check(genreFilter.get(), favourite.getGenre()));
        }
        return predicate;
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
