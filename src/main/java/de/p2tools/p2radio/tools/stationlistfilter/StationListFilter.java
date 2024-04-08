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


package de.p2tools.p2radio.tools.stationlistfilter;

import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.atomic.AtomicBoolean;

public class StationListFilter {
    private static final AtomicBoolean search = new AtomicBoolean(false);
    private static final AtomicBoolean research = new AtomicBoolean(false);
    private final ProgData progData;

    /**
     * hier wird das Filtern der Senderliste "angestoßen"
     *
     * @param progData
     */
    public StationListFilter(ProgData progData) {
        this.progData = progData;
    }

    public void init() {
        progData.storedFilters.filterChangeProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                filter();
            }
        }); // Senderfilter (User) haben sich geändert
        progData.pEventHandler.addListener(new P2Listener(Events.LOAD_RADIO_LIST) {
            public <T extends P2Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) runEvent;

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        filterList();
                    }
                }
            }
        });
        progData.pEventHandler.addListener(new P2Listener(Events.BLACKLIST_CHANGED) {
            public void pingGui(P2Event event) {
                if (!progData.loadNewStationList.getPropLoadStationList()) {
                    //wird sonst eh gemacht
                    filterList();
                }
            }
        });
    }

    private void filter() {
        Platform.runLater(() -> filterList());
    }

    private void filterList() {
        // ist etwas "umständlich", scheint aber am flüssigsten zu laufen
        if (!search.getAndSet(true)) {

            research.set(false);
            Thread th = new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        P2Duration.counterStart("StationListFilter.filterList");
                        progData.stationListBlackFiltered.filteredListSetPred(progData.storedFilters.getActFilterSettings().getPredicate());
                        P2Duration.counterStop("StationListFilter.filterList");
                        search.set(false);
                        if (research.get()) {
                            filterList();
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            th.setName("filterList");
            th.start();
        } else {
            research.set(true);
        }
    }
}
