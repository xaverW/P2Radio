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

package de.p2tools.p2radio.controller.radiosloadfromweb;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.ProgStartAfterGui;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.controller.p2event.P2Event;
import de.p2tools.p2radio.controller.p2event.P2Listener;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.controller.pevent.RunEventRadio;
import de.p2tools.p2radio.controller.radiosreadwritefile.StationLoadFactory;
import de.p2tools.p2radio.controller.radiosreadwritefile.StationSaveFactory;
import de.p2tools.p2radio.controller.worker.PMaskerFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LoadNewStationList {

    private static final AtomicBoolean stop = new AtomicBoolean(false); //damit kann das Laden gestoppt werden
    private final ProgData progData;
    private final HashSet<String> hashSet = new HashSet<>();
    private final BooleanProperty propLoadStationList = new SimpleBooleanProperty(false);

    public LoadNewStationList(ProgData progData) {
        this.progData = progData;

        progData.pEventHandler.addListener(new P2Listener(PEvents.READ_STATIONS) {
            public <T extends P2Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) runEvent;

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.START)) {
                        //Start ans Prog melden
                        ProgData.getInstance().pEventHandler.notifyListener(
                                new RunEventRadio(PEvents.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.START,
                                        runE.getUrl(), runE.getText(), runE.getProgress(), runE.isError()));
                    }

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.PROGRESS)) {
                        ProgData.getInstance().pEventHandler.notifyListener(
                                new RunEventRadio(PEvents.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.PROGRESS,
                                        runE.getUrl(), runE.getText(), runE.getProgress(), runE.isError()));
                    }

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        // Laden ist durch
                        ProgData.getInstance().pEventHandler.notifyListener(
                                new RunEventRadio(PEvents.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.LOADED,
                                        "", "Sender verarbeiten",
                                        RunEventRadio.PROGRESS_INDETERMINATE, false/* Fehler */));

                        P2Log.sysLog("Liste der Radios geladen");
                        P2Log.sysLog(P2Log.LILNE1);
                        P2Log.sysLog("");

                        P2Duration.onlyPing("Sender geladen: Nachbearbeiten");
                        afterImportNewStationListFromServer(runE);
                        P2Duration.onlyPing("Sender nachbearbeiten: Ende");

                        // alles fertig ans Prog melden
                        ProgData.getInstance().pEventHandler.notifyListener(
                                new RunEventRadio(PEvents.LOAD_RADIO_LIST, RunEventRadio.NOTIFY.FINISHED,
                                        runE.getUrl(), runE.getText(), runE.getProgress(), runE.isError()));
                    }
                }
            }
        });
    }

    public boolean getPropLoadStationList() {
        return propLoadStationList.get();
    }

    public void setPropLoadStationList(boolean propLoadStationList) {
        this.propLoadStationList.set(propLoadStationList);
    }

    public BooleanProperty propLoadStationListProperty() {
        return propLoadStationList;
    }

    public synchronized boolean isStop() {
        return stop.get();
    }

    public synchronized void setStop(boolean set) {
        stop.set(set);
    }

    public void loadNewStationFromServer() {
        //das Laden wird gestartet, wenns noch nicht läuft
        if (getPropLoadStationList()) {
            // nicht doppelt starten
            return;
        }

        setPropLoadStationList(true);
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
        fillHash(logList, progData.stationList);
        PMaskerFactory.setMaskerButtonVisible(progData, true);

        progData.stationList.clear();
        progData.stationListBlackFiltered.clear();
        setStop(false);
        new ReadRadiosFromWebThread().loadNewStationList(progData.stationList);
        P2Log.sysLog(logList);
    }

    /**
     * wird nach dem Import einer neuen Liste gemacht
     *
     * @param event
     */
    private void afterImportNewStationListFromServer(RunEventRadio event) {
        final List<String> logList = new ArrayList<>();
        logList.add(P2Log.LILNE3);

        if (event.isError()) {
            // Laden war fehlerhaft
            logList.add("");
            logList.add("Senderliste laden war fehlerhaft, alte Liste wird wieder geladen");
            final boolean stopped = isStop();

            Platform.runLater(() ->
                    P2Alert.showErrorAlert(P2LibConst.actStage,
                            "Senderliste laden",
                            stopped ? "Das Laden einer neuen Senderliste wurde abgebrochen!" :
                                    "Das Laden einer neuen Senderliste hat nicht geklappt!"));

            // dann die alte Liste wieder laden
            progData.stationList.clear();
            setStop(false);
            StationLoadFactory.readList();
            logList.add("");

        } else {
            //dann war alles OK
            progData.stationList.setGenDateNow();
            findAndMarkNewStations(logList, progData.stationList);

            logList.add("Unicode-Zeichen korrigieren");
            StationListFactory.cleanFaultyCharacterStationList();

            logList.add("");
            logList.add("Sender schreiben (" + progData.stationList.size() + " Sender) :");
            logList.add("   --> Start Schreiben nach: " + ProgInfos.getStationFileJsonString());

            StationSaveFactory.saveStationListJson();
            logList.add("   --> geschrieben!");
            logList.add("");
        }

        ProgStartAfterGui.afterLoadingStationList(logList);
        setPropLoadStationList(false);
        P2Log.sysLog(logList);
    }

    private void findAndMarkNewStations(List<String> logList, StationList stationList) {
        stationList.stream() //genauso schnell wie "parallel": ~90ms
                .peek(station -> station.setNewStation(false))
                .filter(station -> !hashSet.contains(station.getStationUrl()))
                .forEach(station -> station.setNewStation(true));

        cleanHash(logList, stationList);
    }

    private void fillHash(List<String> logList, StationList stationList) {
        logList.add(P2Log.LILNE3);
        logList.add("Hash füllen, Größe vorher: " + hashSet.size());

        hashSet.addAll(stationList.stream().map(StationData::getStationUrl).collect(Collectors.toList()));
        logList.add("                  nachher: " + hashSet.size());
        logList.add(P2Log.LILNE3);
    }

    private void cleanHash(List<String> logList, StationList stationList) {
        logList.add(P2Log.LILNE3);
        logList.add("Hash bereinigen, Größe vorher: " + hashSet.size());

        stationList.stream().forEach(station -> hashSet.remove(station.getStationUrl()));
        logList.add("                      nachher: " + hashSet.size());
        logList.add(P2Log.LILNE3);
    }
}