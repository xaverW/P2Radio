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
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2radio.controller.data.history.HistoryFilterXml;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.tools.storedfilter.Filter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class HistoryFilter extends HistoryFilterXml {

    private final BooleanProperty gradeFilter = new SimpleBooleanProperty();
    private final StringProperty genreFilter = new SimpleStringProperty();

    private static boolean check(String filter, String im) {
        if (Filter.isPattern(filter)) {
            Pattern pattern = Filter.makePattern(filter);
            // dann ists eine RegEx
            return (pattern.matcher(im).matches());
        }
        // wenn einer passt, dann ists gut
        return im.toLowerCase().contains(filter);
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_boolProp("Grade", COLUMN_NAMES[HISTORY_FILTER_GRADE], gradeFilter));
        list.add(new Config_stringProp("Genre", COLUMN_NAMES[HISTORY_FILTER_GENRE], genreFilter));
        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public Predicate<StationData> clearFilter() {
        gradeFilter.set(false);
        genreFilter.set("");
        return getPredicate();
    }

    public Predicate<StationData> getPredicate() {
        Predicate<StationData> predicate = favourite -> true;

        if (gradeFilter.get()) {
            predicate = predicate.and(favourite -> favourite.getOwnGrade() > 0);
        }
        if (!genreFilter.get().isEmpty()) {
            predicate = predicate.and(favourite -> check(genreFilter.get(), favourite.getGenre()));
        }
        return predicate;
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
