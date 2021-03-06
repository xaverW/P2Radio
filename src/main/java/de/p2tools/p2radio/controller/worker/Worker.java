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

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;

public class Worker {

    private final ProgData progData;

    public Worker(ProgData progData) {
        this.progData = progData;

        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
            @Override
            public void start(EventLoadRadioList event) {
//                System.out.println("EventListenerLoadRadioList--start");
                if (event.progress == EventListenerLoadRadioList.PROGRESS_INDETERMINATE) {
                    //ist dann die gespeicherte Senderliste
//                    progData.maskerPane.setMaskerVisible(true, false);
                    PMaskerFactory.setMaskerVisible(progData, true, false);

                } else {
//                    progData.maskerPane.setMaskerVisible(true, true);
                    PMaskerFactory.setMaskerVisible(progData, true, true);
                }
//                progData.maskerPane.setMaskerProgress(event.progress, event.text);
                PMaskerFactory.setMaskerProgress(progData, event.progress, event.text);
            }

            @Override
            public void progress(EventLoadRadioList event) {
//                System.out.println("--progress");
//                progData.maskerPane.setMaskerProgress(event.progress, event.text);
                PMaskerFactory.setMaskerProgress(progData, event.progress, event.text);
            }

            @Override
            public void loaded(EventLoadRadioList event) {
//                System.out.println("EventListenerLoadRadioList--loaded");
//                progData.maskerPane.setMaskerVisible(true, false);
                PMaskerFactory.setMaskerVisible(progData, true, false);
//                progData.maskerPane.setMaskerProgress(EventListenerLoadRadioList.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
                PMaskerFactory.setMaskerProgress(progData, EventListenerLoadRadioList.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
            }

            @Override
            public void finished(EventLoadRadioList event) {
//                System.out.println("EventListenerLoadRadioList--finished");
//                progData.maskerPane.setMaskerVisible(false);
                PMaskerFactory.setMaskerVisible(progData, false);
            }
        });
    }
}
