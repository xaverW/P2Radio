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

package de.p2tools.p2radio.tools.storedfilter;

import de.p2tools.p2radio.controller.config.ProgData;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public final class StoredFiltersForwardBackward {

    private final ProgData progData;
    private final BooleanProperty backward = new SimpleBooleanProperty(false);
    private final BooleanProperty forward = new SimpleBooleanProperty(false);
    // ist die Liste der zuletzt verwendeten Filter
    private final ObservableList<SelectedFilter> filterListBackward =
            FXCollections.observableList(new ArrayList<>(), (SelectedFilter tp) -> new Observable[]{tp.nameProperty()});
    private final ObservableList<SelectedFilter> filterListForward =
            FXCollections.observableList(new ArrayList<>(), (SelectedFilter tp) -> new Observable[]{tp.nameProperty()});
    private boolean name = false, genre = false, url = false, somewhere = false;
    private boolean stopFilterChangeListener = false;

    public StoredFiltersForwardBackward(ProgData progData) {
        this.progData = progData;
        progData.storedFilters.getActFilterSettings().filterChangeProperty().addListener((observable, oldValue, newValue) -> {
            // wenn der User den Filter ändert
            if (stopFilterChangeListener) {
                return;
            }

            addBackward();
            filterListForward.clear();
        });
        progData.storedFilters.getActFilterSettings().blacklistChangeProperty().addListener((observable, oldValue, newValue) -> {
            // wenn der User die Blackl. ein-/ausschaltet
            if (stopFilterChangeListener) {
                return;
            }

            filterListForward.clear();
        });

        addBackward();
        filterListBackward.addListener((ListChangeListener<SelectedFilter>) c -> {
            if (filterListBackward.size() > 1) {
                backward.setValue(true);
            } else {
                backward.setValue(false);
            }
        });
        filterListForward.addListener((ListChangeListener<SelectedFilter>) c -> {
            if (filterListForward.size() > 0) {
                forward.setValue(true);
            } else {
                forward.setValue(false);
            }
        });
    }

    public BooleanProperty backwardProperty() {
        return backward;
    }

    public BooleanProperty forwardProperty() {
        return forward;
    }

    public void goBackward() {
        if (filterListBackward.size() <= 1) {
            // dann gibts noch keine oder ist nur die aktuelle Einstellung drin
            return;
        }

        SelectedFilter sf = filterListBackward.remove(filterListBackward.size() - 1); // ist die aktuelle Einstellung
        filterListForward.add(sf);
        sf = filterListBackward.remove(filterListBackward.size() - 1); // ist die davor
        setActFilterSettings(sf);
    }

    public void goForward() {
        if (filterListForward.isEmpty()) {
            // dann gibts keine
            return;
        }

        final SelectedFilter sf = filterListForward.remove(filterListForward.size() - 1);
        setActFilterSettings(sf);
    }

    private void setActFilterSettings(SelectedFilter sf) {
        stopFilterChangeListener = true;
        progData.storedFilters.setActFilterSettings(sf);
        addBackward();
        stopFilterChangeListener = false;
    }

    private void addBackward() {
        final SelectedFilter sf = new SelectedFilter();
        SelectedFilterFactory.copyFilter(progData.storedFilters.getActFilterSettings(), sf);
        if (filterListBackward.isEmpty()) {
            filterListBackward.add(sf);
            return;
        }

        SelectedFilter sfB = filterListBackward.get(filterListBackward.size() - 1);
        if (SelectedFilterFactory.compareFilterWithoutNameOfFilter(sf, sfB)) {
            // dann hat sich nichts geändert (z.B. mehrmals gelöscht)
            return;
        }

        if (checkText(sfB.stationNameProperty(), sf.stationNameProperty(), sfB, sf, name)) {
            setFalse();
            name = true;
            return;
        }

        if (checkText(sfB.genreProperty(), sf.genreProperty(), sfB, sf, genre)) {
            setFalse();
            genre = true;
            return;
        }

        if (checkText(sfB.urlProperty(), sf.urlProperty(), sfB, sf, url)) {
            setFalse();
            url = true;
            return;
        }

        if (checkText(sfB.somewhereProperty(), sf.somewhereProperty(), sfB, sf, somewhere)) {
            setFalse();
            somewhere = true;
            return;
        }

        // dann wars kein Textfilter
        filterListBackward.add(sf);
    }

    private void setFalse() {
        name = false;
        genre = false;
        url = false;
        somewhere = false;
    }

    private boolean checkText(StringProperty old, StringProperty nnew, SelectedFilter oldSf, SelectedFilter newSf,
                              boolean check) {
        if (old.get().equals(nnew.get())) {
            return false;
        }
        if (check && !old.get().isEmpty() && !nnew.get().isEmpty() &&
                (old.get().contains(nnew.get()) || nnew.get().contains(old.get()))) {
            // dann hat sich nur ein Teil geändert und wird ersetzt
            old.setValue(nnew.getValue());
        } else {
            filterListBackward.add(newSf);
        }
        return true;
    }
}
