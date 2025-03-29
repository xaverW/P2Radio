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

package de.p2tools.p2radio.controller.stationweb;

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.controller.stationweb.load.WebLoadFactory;
import de.p2tools.p2radio.controller.stationweb.load.WebLoadThread;
import de.p2tools.p2radio.controller.worker.PMaskerFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebLoad {

    private static final AtomicBoolean stop = new AtomicBoolean(false); //damit kann das Laden gestoppt werden
    private final ProgData progData;
    private final HashSet<String> hashSet = new HashSet<>();
    private final BooleanProperty propLoadWeb = new SimpleBooleanProperty(false);

    public WebLoad(ProgData progData) {
        this.progData = progData;

        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_START) {
            @Override
            public void pingGui(P2Event event) {
                //ist dann die gespeicherte Senderliste
                PMaskerFactory.setMaskerVisible(progData, true,
                        event.getAct() != WebLoadFactory.PROGRESS_INDETERMINATE);
                PMaskerFactory.setMaskerProgress(progData, event.getAct(), event.getText());
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_PROGRESS) {
            @Override
            public void pingGui(P2Event event) {
                PMaskerFactory.setMaskerProgress(progData, event.getAct(), event.getText());
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_LOADED) {
            @Override
            public void pingGui(P2Event event) {
                PMaskerFactory.setMaskerVisible(progData, true, false);
                PMaskerFactory.setMaskerProgress(progData, WebLoadFactory.PROGRESS_INDETERMINATE, "Senderliste verarbeiten");
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_FINISHED) {
            @Override
            public void pingGui(P2Event event) {
                PMaskerFactory.setMaskerVisible(progData, false);
                if (progData.autoStartAfterNewList) {
                    progData.autoStartAfterNewList = false;
                    P2RadioFactory.loadAutoStart();
                }
            }
        });
    }

    public void loadFromWeb() {
        //das Laden wird gestartet, wenns noch nicht läuft
        if (getPropLoadWeb()) {
            // nicht doppelt starten
            return;
        }

        setPropLoadWeb(true);
        PMaskerFactory.setMaskerButtonVisible(progData, true);
        P2Duration.onlyPing("Radioliste laden: start");

        final List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(P2Log.LILNE1);
        logList.add("Liste der Radios laden");
        logList.add("");
        logList.add("Alte Liste erstellt  am: " + ProgData.getInstance().stationList.getGenDate());
        logList.add("           Anzahl Sender: " + progData.stationList.size());
        logList.add("           Anzahl  Neue: " + progData.stationList.countNewStations());
        logList.add(" ");

        // Hash mit URLs füllen
        WebFactory.fillHash();

        progData.stationList.clear();
        progData.stationListBlackFiltered.clear();
        setStop(false);
        WebLoadThread.loadStationList(progData.stationList);
        P2Log.sysLog(logList);
    }

    public boolean getPropLoadWeb() {
        return propLoadWeb.get();
    }

    public void setPropLoadWeb(boolean propLoadWeb) {
        this.propLoadWeb.set(propLoadWeb);
    }

    public BooleanProperty propLoadWebProperty() {
        return propLoadWeb;
    }

    public synchronized boolean isStop() {
        return stop.get();
    }

    public synchronized void setStop(boolean set) {
        stop.set(set);
    }
}