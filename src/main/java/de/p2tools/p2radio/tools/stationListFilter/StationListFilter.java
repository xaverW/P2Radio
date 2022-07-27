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


package de.p2tools.p2radio.tools.stationListFilter;

import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.events.Event;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;
import javafx.application.Platform;

import java.util.concurrent.atomic.AtomicBoolean;

public class StationListFilter {
    private final ProgData progData;

    /**
     * hier wird das Filtern der Senderliste "angestoßen"
     *
     * @param progData
     */
    public StationListFilter(ProgData progData) {
        this.progData = progData;

        progData.storedFilters.filterChangeProperty().addListener((observable, oldValue, newValue) -> filter()); // Senderfilter (User) haben sich geändert
        progData.pEventHandler.addListener(new PListener(Events.LOAD_RADIO_LIST) {
            public <T extends Event> void ping(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) runEvent;

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        filterList();
                    }
                }
            }
        });

//        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
//            @Override
//            public void finished(EventLoadRadioList event) {
//                filterList();
//            }
//        });

        progData.pEventHandler.addListener(new PListener(Events.BLACKLIST_CHANGED) {
            public void ping(Event event) {
                if (!progData.loadNewStationList.getPropLoadStationList()) {
                    //wird sonst eh gemacht
                    filterList();
                }
            }
        });

//        Listener.addListener(new Listener(Listener.EVENT_BLACKLIST_CHANGED, StationListFilter.class.getSimpleName()) {
//            @Override
//            public void pingFx() {
//                if (!progData.loadNewStationList.getPropLoadStationList()) {
//                    //wird sonst eh gemacht
//                    filterList();
//                }
//            }
//        });
    }

    private void filter() {
        Platform.runLater(() -> filterList());
    }

    private static final AtomicBoolean search = new AtomicBoolean(false);
    private static final AtomicBoolean research = new AtomicBoolean(false);

    private void filterList() {
        // ist etwas "umständlich", scheint aber am flüssigsten zu laufen
        if (!search.getAndSet(true)) {

            research.set(false);
            Thread th = new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        PDuration.counterStart("StationListFilter.filterList");
                        progData.stationListBlackFiltered.filteredListSetPred(progData.storedFilters.getActFilterSettings().getPredicate());
                        PDuration.counterStop("StationListFilter.filterList");
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
