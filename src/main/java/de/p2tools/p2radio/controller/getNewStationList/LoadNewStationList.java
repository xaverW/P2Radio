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

package de.p2tools.p2radio.controller.getNewStationList;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.ProgLoadFactory;
import de.p2tools.p2radio.controller.SenderLoadFactory;
import de.p2tools.p2radio.controller.SenderSaveFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventNotifyLoadRadioList;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class LoadNewStationList {

    private final ProgData progData;
    private final ReadStations readStations;
    private final HashSet<String> hashSet = new HashSet<>();
    private BooleanProperty propLoadStationList = new SimpleBooleanProperty(false);
    private static final AtomicBoolean stop = new AtomicBoolean(false); //damit kann das Laden gestoppt werden

    public LoadNewStationList(ProgData progData) {
        this.progData = progData;
        readStations = new ReadStations();
        readStations.addAdListener(new EventListenerLoadRadioList() {
            @Override
            public synchronized void start(EventLoadRadioList event) {
                //Start ans Prog melden
                progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.START, event);
            }

            @Override
            public synchronized void progress(EventLoadRadioList event) {
                progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.PROGRESS, event);
            }

            @Override
            public synchronized void finished(EventLoadRadioList event) {
                // Laden ist durch
                progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.LOADED,
                        new EventLoadRadioList("", "Sender verarbeiten",
                                EventListenerLoadRadioList.PROGRESS_INDETERMINATE, 0, false/* Fehler */));

                PLog.addSysLog("Liste der Radios geladen");
                PLog.sysLog(PLog.LILNE1);
                PLog.addSysLog("");

                PDuration.onlyPing("Sender geladen: Nachbearbeiten");
                afterImportNewStationListFromServer(event);
                PDuration.onlyPing("Sender nachbearbeiten: Ende");

                // alles fertig ans Prog melden
                progData.eventNotifyLoadRadioList.notifyEvent(EventNotifyLoadRadioList.NOTIFY.FINISHED, event);
            }
        });
    }

    public boolean getPropLoadStationList() {
        return propLoadStationList.get();
    }

    public BooleanProperty propLoadStationListProperty() {
        return propLoadStationList;
    }

    public void setPropLoadStationList(boolean propLoadStationList) {
        this.propLoadStationList.set(propLoadStationList);
    }

    public synchronized void setStop(boolean set) {
        stop.set(set);
    }

    public synchronized boolean isStop() {
        return stop.get();
    }

    public void loadNewStationFromServer() {
        if (getPropLoadStationList()) {
            // nicht doppelt starten
            return;
        }

        setPropLoadStationList(true);
        PDuration.onlyPing("Radioliste laden: start");
        final List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(PLog.LILNE1);
        logList.add("Liste der Radios laden");
        logList.add("");
        logList.add("Alte Liste erstellt  am: " + ProgData.getInstance().stationList.getGenDate());
        logList.add("           Anzahl Sender: " + progData.stationList.size());
        logList.add("           Anzahl  Neue: " + progData.stationList.countNewStations());
        logList.add(" ");

        // Hash mit URLs füllen
        fillHash(logList, progData.stationList);
        progData.maskerPane.setButtonVisible(true);
        progData.stationList.clear();
        progData.stationListBlackFiltered.clear();
        setStop(false);
        readStations.loadNewStationList(progData.stationList);

        PLog.addSysLog(logList);
    }

    /**
     * wird nach dem Import einer neuen Liste gemacht
     *
     * @param event
     */
    private void afterImportNewStationListFromServer(EventLoadRadioList event) {
//        System.out.println("===============> afterImportNewStationListFromServer");
        final List<String> logList = new ArrayList<>();
        logList.add(PLog.LILNE3);

        if (event.error) {
            // Laden war fehlerhaft
            logList.add("");
            logList.add("Senderliste laden war fehlerhaft, alte Liste wird wieder geladen");
            final boolean stopped = isStop();
            Platform.runLater(() -> PAlert.showErrorAlert("Senderliste laden",
                    stopped ? "Das Laden einer neuen Senderliste wurde abgebrochen!" :
                            "Das Laden einer neuen Senderliste hat nicht geklappt!")
            );

            // dann die alte Liste wieder laden
            progData.stationList.clear();
            setStop(false);
            SenderLoadFactory.readList();
//            readRadioList.importStationListAuto(progData.stationList); //endlosschleife!!
            logList.add("");

        } else {
            //ann war alles OK
            progData.stationList.setGenDateNow();
            findAndMarkNewStations(logList, progData.stationList);

            logList.add("Unicode-Zeichen korrigieren");
            StationListFactory.cleanFaultyCharacterStationList();

            logList.add("");
            logList.add("Sender schreiben (" + progData.stationList.size() + " Sender) :");
            logList.add("   --> Start Schreiben nach: " + ProgInfos.getStationFileJsonString());

            SenderSaveFactory.saveStationListJson();
            logList.add("   --> geschrieben!");
            logList.add("");
        }

        ProgLoadFactory.afterLoadingStationList(logList);
        setPropLoadStationList(false);
        PLog.addSysLog(logList);
    }

    private void findAndMarkNewStations(List<String> logList, StationList stationList) {
        stationList.stream() //genauso schnell wie "parallel": ~90ms
                .peek(station -> station.setNewStation(false))
                .filter(station -> !hashSet.contains(station.getStationUrl()))
                .forEach(station -> station.setNewStation(true));

        cleanHash(logList, stationList);
    }

    private void fillHash(List<String> logList, StationList stationList) {
        logList.add(PLog.LILNE3);
        logList.add("Hash füllen, Größe vorher: " + hashSet.size());

        hashSet.addAll(stationList.stream().map(Station::getStationUrl).collect(Collectors.toList()));
        logList.add("                  nachher: " + hashSet.size());
        logList.add(PLog.LILNE3);
    }

    private void cleanHash(List<String> logList, StationList stationList) {
        logList.add(PLog.LILNE3);
        logList.add("Hash bereinigen, Größe vorher: " + hashSet.size());

        stationList.stream().forEach(station -> hashSet.remove(station.getStationUrl()));
        logList.add("                      nachher: " + hashSet.size());
        logList.add(PLog.LILNE3);
    }
}