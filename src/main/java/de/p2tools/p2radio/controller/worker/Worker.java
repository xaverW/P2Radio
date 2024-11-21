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

import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;

public class Worker {

    private final ProgData progData;

    public Worker(ProgData progData) {
        this.progData = progData;

        progData.pEventHandler.addListener(new P2Listener(Events.LOAD_RADIO_LIST) {
            public <T extends P2Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runEventRadio = (RunEventRadio) runEvent;

                    if (runEventRadio.getNotify().equals(RunEventRadio.NOTIFY.START)) {
                        //ist dann die gespeicherte Senderliste
                        PMaskerFactory.setMaskerVisible(progData, true, runEventRadio.getProgress() != RunEventRadio.PROGRESS_INDETERMINATE);
                        PMaskerFactory.setMaskerProgress(progData, runEventRadio.getProgress(), runEventRadio.getText());
                    }

                    if (runEventRadio.getNotify().equals(RunEventRadio.NOTIFY.PROGRESS)) {
                        PMaskerFactory.setMaskerProgress(progData, runEventRadio.getProgress(), runEventRadio.getText());
                    }

                    if (runEventRadio.getNotify().equals(RunEventRadio.NOTIFY.LOADED)) {
                        PMaskerFactory.setMaskerVisible(progData, true, false);
                        PMaskerFactory.setMaskerProgress(progData, RunEventRadio.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
                    }

                    if (runEventRadio.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        PMaskerFactory.setMaskerVisible(progData, false);
                        if (progData.autoStartAfterNewList) {
                            progData.autoStartAfterNewList = false;
                            P2RadioFactory.loadAutoStart();
                        }
                    }
                }
            }
        });
    }
}
