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

package de.p2tools.p2radio.tools.storedFilter;

import de.p2tools.p2radio.controller.config.ProgData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

public final class StoredFilters {

    private final ProgData progData;
    private StoredFiltersForwardBackward storedFiltersForwardBackward = null; // gespeicherte Filterprofile

    private final BooleanProperty filterChange = new SimpleBooleanProperty(true);
    private final ChangeListener<Boolean> filterChangeListener;
    private final ChangeListener<Boolean> blacklistChangeListener;

    // ist der aktuell angezeigte Filter
    private SelectedFilter actFilterSettings = new SelectedFilter("aktuelle Einstellung"); //ist der "aktuelle" Filter, Name dient nur der Info im Config-File

    // ist die Liste der gespeicherten Filter
    private final SelectedFilterList filterList = new SelectedFilterList();

    public StoredFilters(ProgData progData) {
        this.progData = progData;

        filterChangeListener = (observable, oldValue, newValue) -> {
            setFilterChange();
        };
        blacklistChangeListener = (observable, oldValue, newValue) -> {
            setBlacklistChange();
        };
        actFilterSettings.filterChangeProperty().addListener(filterChangeListener); // wenn der User den Filter ändert
        actFilterSettings.blacklistChangeProperty().addListener(blacklistChangeListener); // wenn der User die Blackl. ein-/ausschaltet
    }

    public void init() {
        storedFiltersForwardBackward = new StoredFiltersForwardBackward(progData);
    }

    public StoredFiltersForwardBackward getStoredFiltersForwardBackward() {
        return storedFiltersForwardBackward;
    }

    public BooleanProperty filterChangeProperty() {
        return filterChange;
    }

    /**
     * liefert den aktuell angezeigten Filter
     *
     * @return
     */
    public SelectedFilter getActFilterSettings() {
        return actFilterSettings;
    }

    /**
     * setzt die aktuellen Filtereinstellungen aus einen Filter (gespeicherten Filter)
     *
     * @param sf
     */
    public synchronized void setActFilterSettings(SelectedFilter sf) {
        if (sf == null) {
            return;
        }

        actFilterSettings.filterChangeProperty().removeListener(filterChangeListener);
        actFilterSettings.blacklistChangeProperty().removeListener(blacklistChangeListener);

        boolean black = actFilterSettings.isBlacklistOn();
        boolean blackOnly = actFilterSettings.isBlacklistOnly();
        SelectedFilterFactory.copyFilter(sf, actFilterSettings);
        if (actFilterSettings.isBlacklistOn() == black &&
                actFilterSettings.isBlacklistOnly() == blackOnly) {
            // Black hat sich nicht geändert
            setFilterChange();

        } else {
            setBlacklistChange();
        }

        actFilterSettings.filterChangeProperty().addListener(filterChangeListener);
        actFilterSettings.blacklistChangeProperty().addListener(blacklistChangeListener);
    }

    /**
     * sind die gesicherten Filterprofile
     *
     * @return
     */
    public SelectedFilterList getStoredFilterList() {
        return filterList;
    }

    /**
     * einen neuen Filter zu den gespeicherten hinzufügen
     *
     * @return
     */
    public String addNewStoredFilter(String name) {
        final SelectedFilter sf = new SelectedFilter();
        SelectedFilterFactory.copyFilter(actFilterSettings, sf);

        sf.setName(name.isEmpty() ? getNextName() : name);
        filterList.add(sf);

        return sf.getName();
    }

    /**
     * delete filter
     *
     * @param sf
     */
    public void removeStoredFilter(SelectedFilter sf) {
        if (sf == null) {
            return;
        }
        filterList.remove(sf);
    }

    /**
     * delete all Filter
     */
    public void removeAllStoredFilter() {
        filterList.clear();
    }


    /**
     * gesicherten Filter mit den aktuellen Einstellungen überschreiben
     *
     * @param sf
     */
    public void saveStoredFilter(SelectedFilter sf) {
        if (sf == null) {
            return;
        }
        final String name = sf.getName();
        SelectedFilterFactory.copyFilter(actFilterSettings, sf);
        sf.setName(name);
    }

    public synchronized void clearFilter() {
        actFilterSettings.filterChangeProperty().removeListener(filterChangeListener);
        actFilterSettings.blacklistChangeProperty().removeListener(blacklistChangeListener);

        if (actFilterSettings.isTextFilterEmpty()) {
            actFilterSettings.clearFilter(); // Button Black wird nicht verändert
        } else {
            actFilterSettings.clearTxtFilter();
        }

        setFilterChange();
        actFilterSettings.filterChangeProperty().addListener(filterChangeListener);
        actFilterSettings.blacklistChangeProperty().addListener(blacklistChangeListener);
    }

    public String getNextName() {
        String ret = "";
        int id = 1;
        boolean found = false;
        while (!found) {
            final String name = "Filter " + id;
            if (!filterList.stream().filter(f -> name.equalsIgnoreCase(f.getName())).findAny().isPresent()) {
                ret = name;
                found = true;
            }
            ++id;
        }
        return ret;
    }

    private void setFilterChange() {
        this.filterChange.set(!filterChange.get());
    }

    private void setBlacklistChange() {
        // dann hat sich auch Blacklist-ein/aus geändert
        progData.stationList.filterListWithBlacklist(false);
        setFilterChange();
    }
}
