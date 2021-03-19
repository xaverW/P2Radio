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
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class FilterWorker {

    private ObservableList<String> allCodecsList = FXCollections.observableArrayList("");
    private ObservableList<String> allCountryList = FXCollections.observableArrayList("");
    private ObservableList<String> allGenreList = FXCollections.observableArrayList("");

    final SelectedFilter sfTemp = new SelectedFilter();
    private final ProgData progData;

    public FilterWorker(ProgData progData) {
        this.progData = progData;

        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
            @Override
            public void start(EventLoadRadioList event) {
                // the station combo will be resetted, therefore save the filter
                saveFilter();
            }

            @Override
            public void progress(EventLoadRadioList event) {
            }

            @Override
            public void loaded(EventLoadRadioList event) {
            }

            @Override
            public void finished(EventLoadRadioList event) {
                createFilterLists();
                // activate the saved filter
                resetFilter();
            }
        });

        allGenreList.addAll("classic", "disco", "dance", "90s", "80s", "70s",
                "rock", "hard rock", "classic rock", "deutschrock", "pop", "disco", "oldies",
                "electro", "electronic", "techno", "house", "trance",
                "chillout");
    }

    private void saveFilter() {
        SelectedFilterFactory.copyFilter(progData.storedFilters.getActFilterSettings(), sfTemp);
    }

    private void resetFilter() {
        SelectedFilterFactory.copyFilter(sfTemp, progData.storedFilters.getActFilterSettings());
    }

    private void createFilterLists() {
        // alle Sender laden
        allCodecsList.setAll(Arrays.asList(progData.stationList.codecs));
        allCountryList.setAll(Arrays.asList(progData.stationList.countries));
    }

    public ObservableList<String> getAllCodecsList() {
        return allCodecsList;
    }

    public ObservableList<String> getAllGenreList() {
        return allGenreList;
    }

    public ObservableList<String> getAllCountryLList() {
        return allCountryList;
    }
}
