package de.p2tools.p2radio.controller.stationweb.load;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.controller.station.LoadStationFactory;
import de.p2tools.p2radio.controller.stationlocal.LocalReadFactory;
import de.p2tools.p2radio.controller.stationlocal.LocalSaveFactory;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WebAfterLoadFactory {

    private static final HashSet<String> hashSet = new HashSet<>();
    private static int count = 0;

    private WebAfterLoadFactory() {
    }

    public static void fillHash() {
        // Hash mit den "alten" Sendern füllen
        hashSet.addAll(ProgData.getInstance().stationList.stream().map(StationData::getStationUrl).toList());
    }

    public static void afterWebLoad(boolean ok) {
        // Laden ist durch
        P2Duration.onlyPing("Sender geladen: Nachbearbeiten");
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_LOADED,
                "Sender verarbeiten",
                WebLoadFactory.PROGRESS_INDETERMINATE));


        final List<String> logList = new ArrayList<>();
        logList.add(P2Log.LILNE3);
        logList.add("Liste der Radios geladen");
        logList.add(P2Log.LILNE1);
        logList.add("");

        if (!ok) {
            // Laden war fehlerhaft
            logList.add("");
            logList.add("Senderliste laden war fehlerhaft, alte Liste wird wieder geladen");
            final boolean stopped = ProgData.getInstance().webWorker.isStop();

            Platform.runLater(() -> P2Alert.showErrorAlert(P2LibConst.actStage,
                    "Senderliste laden",
                    stopped ? "Das Laden einer neuen Senderliste wurde abgebrochen!" :
                            "Das Laden einer neuen Senderliste hat nicht geklappt!"));

            // dann die alte Liste wieder laden
            ProgData.getInstance().stationList.clear();
            ProgData.getInstance().webWorker.setStop(false);
            LocalReadFactory.readList(); // meldet nix
            logList.add("");

        } else {
            //dann war alles OK
            ProgData.getInstance().stationList.setGenDateNow();
            findAndMarkNewStations(logList, ProgData.getInstance().stationList);

            logList.add("Unicode-Zeichen korrigieren");
            StationListFactory.cleanFaultyCharacterStationList();

            logList.add("");
            logList.add("Sender schreiben (" + ProgData.getInstance().stationList.size() + " Sender) :");
            logList.add("   --> Start Schreiben nach: " + ProgInfos.getStationFileJsonString());

            LocalSaveFactory.saveStationListJson();
            logList.add("   --> geschrieben!");
            logList.add("");
        }

        LoadStationFactory.afterLoadingStationList(logList);
        ProgData.getInstance().webWorker.setPropLoadWeb(false);

        P2Log.sysLog(logList);
        P2Duration.onlyPing("Sender nachbearbeiten: Ende");
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_FINISHED,
                "Senderliste geladen", 0, 0, 0, !ok));
    }

    private static void findAndMarkNewStations(List<String> logList, StationList stationList) {
        stationList.stream()
                .peek(station -> station.setNewStation(false))
                .filter(station -> !hashSet.contains(station.getStationUrl()))
                .forEach(station -> {
                    station.setNewStation(true);
                    ++count;
                });
        logList.add("");
        logList.add("Neue Sender: " + count);

        hashSet.clear();
    }
}
