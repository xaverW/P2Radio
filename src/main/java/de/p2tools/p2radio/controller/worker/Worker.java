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

import de.p2tools.p2Lib.tools.events.Event;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;

public class Worker {

    private final ProgData progData;

    public Worker(ProgData progData) {
        this.progData = progData;

        progData.pEventHandler.addListener(new PListener(Events.LOAD_RADIO_LIST) {
            public <T extends Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) runEvent;

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.START)) {
                        //ist dann die gespeicherte Senderliste
                        PMaskerFactory.setMaskerVisible(progData, true, runE.getProgress() != RunEventRadio.PROGRESS_INDETERMINATE);
                        PMaskerFactory.setMaskerProgress(progData, runE.getProgress(), runE.getText());
                    }

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.PROGRESS)) {
                        PMaskerFactory.setMaskerProgress(progData, runE.getProgress(), runE.getText());
                    }

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.LOADED)) {
                        PMaskerFactory.setMaskerVisible(progData, true, false);
                        PMaskerFactory.setMaskerProgress(progData, RunEventRadio.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
                    }

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        PMaskerFactory.setMaskerVisible(progData, false);
                    }
                }
            }
        });


//        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
//            @Override
//            public void start(EventLoadRadioList event) {
//                if (event.progress == EventListenerLoadRadioList.PROGRESS_INDETERMINATE) {
//                    //ist dann die gespeicherte Senderliste
////                    progData.maskerPane.setMaskerVisible(true, false);
//                    PMaskerFactory.setMaskerVisible(progData, true, false);
//
//                } else {
////                    progData.maskerPane.setMaskerVisible(true, true);
//                    PMaskerFactory.setMaskerVisible(progData, true, true);
//                }
////                progData.maskerPane.setMaskerProgress(event.progress, event.text);
//                PMaskerFactory.setMaskerProgress(progData, event.progress, event.text);
//            }
//
//            @Override
//            public void progress(EventLoadRadioList event) {
////                progData.maskerPane.setMaskerProgress(event.progress, event.text);
//                PMaskerFactory.setMaskerProgress(progData, event.progress, event.text);
//            }
//
//        @Override
//        public void loaded (EventLoadRadioList event){
////                progData.maskerPane.setMaskerVisible(true, false);
//            PMaskerFactory.setMaskerVisible(progData, true, false);
////                progData.maskerPane.setMaskerProgress(EventListenerLoadRadioList.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
//            PMaskerFactory.setMaskerProgress(progData, EventListenerLoadRadioList.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
//        }
//
//        @Override
//        public void finished (EventLoadRadioList event){
////                progData.maskerPane.setMaskerVisible(false);
//            PMaskerFactory.setMaskerVisible(progData, false);
//        }
//    });
    }
}
