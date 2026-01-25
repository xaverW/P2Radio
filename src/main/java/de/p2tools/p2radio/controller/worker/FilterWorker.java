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

package de.p2tools.p2radio.controller.worker;

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.tools.storedfilter.SelectedFilter;
import de.p2tools.p2radio.tools.storedfilter.SelectedFilterFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Collections;

public class FilterWorker {

    final SelectedFilter sfTemp = new SelectedFilter();
    private final ProgData progData;
    private final ObservableList<String> allCodecsList = FXCollections.observableArrayList("");
    private final ObservableList<String> allCountryList = FXCollections.observableArrayList("");
    private final ObservableList<String> allGenreList = FXCollections.observableArrayList("");

    public FilterWorker(ProgData progData) {
        this.progData = progData;
        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_START) {
            @Override
            public void pingGui(P2Event event) {
                // the station combo will be resetted, therefore save the filter
                saveFilter();
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_FINISHED) {
            @Override
            public void pingGui(P2Event event) {
                createFilterLists();
                // activate the saved filter
                resetFilter();
            }
        });
        allGenreList.addAll("70s", "80s", "90s", "classic", "classic rock", "dance",
                "deutschrock", "chillout", "disco", "electro", "electronic",
                "hard rock", "metal", "house", "jazz", "oldies", "pop", "rock",
                "swing", "techno", "trance");
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
        Collections.sort(allCodecsList);
        Collections.sort(allCountryList);

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
