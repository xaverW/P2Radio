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

package de.p2tools.p2radio.controller;

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.controller.stationload.LoadStationFactory;
import de.p2tools.p2radio.controller.stationlocal.LocalReadFactory;
import de.p2tools.p2radio.controller.stationweb.load.WebLoadFactory;
import de.p2tools.p2radio.tools.update.SearchProgramUpdate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgStartAfterGui {

    private ProgStartAfterGui() {
    }

    /**
     * alles was nach der GUI gemacht werden soll z.B.
     * Senderliste beim Programmstart!! laden
     */
    public static void workAfterGui(ProgData progData) {
        startMsg();
        setTitle();

        progData.initProgData();
        checkProgUpdate();
        loadStationProgStart();
    }

    private static void startMsg() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfos.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectoryString());
        list.add(P2Log.LILNE2);
        list.add("");
        list.add("Programmsets:");
        list.addAll(ProgData.getInstance().setDataList.getStringListSetData());
        ProgConfig.getInstance().writeConfigs();
        P2LogMessage.startMsg(ProgConst.PROGRAM_NAME, list);
    }

    private static void setTitle() {
        // muss nur für das große GUI gesetzt werden
        Stage stage = ProgData.getInstance().primaryStageBig;
        if (ProgData.debug) {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion());
        }
    }

    private static void checkProgUpdate() {
        // Prüfen obs ein Programmupdate gibt
        P2Duration.onlyPing("checkProgUpdate");
        if (ProgConfig.SYSTEM_SEARCH_UPDATE.getValue() &&
                !isUpdateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck();

        } else {
            // will der User nicht --oder-- wurde heute schon gemacht
            List<String> list = new ArrayList<>(5);
            list.add("Kein Update-Check:");
            if (!ProgConfig.SYSTEM_SEARCH_UPDATE.getValue()) {
                list.add("  der User will nicht");
            }
            if (isUpdateCheckTodayDone()) {
                list.add("  heute schon gemacht");
            }
            P2Log.sysLog(list);
        }
    }

    private static boolean isUpdateCheckTodayDone() {
        return ProgConfig.SYSTEM_SEARCH_UPDATE_TODAY_DONE.get().equals(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
    }

    private static void runUpdateCheck() {
        ProgConfig.SYSTEM_SEARCH_UPDATE_TODAY_DONE.setValue(P2LDateFactory.getNowStringR());
        new SearchProgramUpdate(ProgData.getInstance()).searchNewProgramVersion(false);
    }

    private static void loadStationProgStart() {
        P2Log.sysLog("START: loadStationProgStart");
        final ProgData progData = ProgData.getInstance();
        P2Duration.onlyPing("Programmstart Senderliste laden: start");

        progData.pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_START,
                "gespeicherte Senderliste laden",
                WebLoadFactory.PROGRESS_INDETERMINATE));

        final List<String> logList = new ArrayList<>();
        logList.add("");
        logList.add(P2Log.LILNE1);

        if (ProgData.firstProgramStart) {
            //dann wird immer geladen
            logList.add("erster Programmstart: Neue Senderliste laden");
            progData.webWorker.loadFromWeb();
            ProgQuitFactory.saveProgConfig(); // und schon mal speichern

        } else {
            // gespeicherte Senderliste laden, gibt keine Fortschrittsanzeige und kein Abbrechen
            logList.add("Programmstart, gespeicherte Senderliste laden");
            boolean loadOk = LocalReadFactory.readList(); // meldet nix

            if (!loadOk || progData.stationList.isTooOld() && ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS.get()) {
                //wenn die gespeicherte zu alt ist
                progData.pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_PROGRESS,
                        "Senderliste zu alt, neue Senderliste laden",
                        WebLoadFactory.PROGRESS_INDETERMINATE));

                logList.add("Senderliste zu alt, neue Senderliste laden");
                logList.add(P2Log.LILNE3);
                progData.webWorker.loadFromWeb();

                progData.autoStartAfterNewList = true; // dann erst nach dem Neuladen der Liste starten

            } else {
                progData.pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_LOADED,
                        "Senderliste verarbeiten",
                        WebLoadFactory.PROGRESS_INDETERMINATE));

                logList.add("Liste der Radios geladen");
                LoadStationFactory.afterLoadingStationList(logList);
                progData.pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_FINISHED));
                P2RadioFactory.loadAutoStart();
            }
        }

        logList.add(P2Log.LILNE1);
        logList.add("");
        P2Log.sysLog(logList);
    }
}
