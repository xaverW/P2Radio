package de.p2tools.p2radio.controller.stationload;

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.controller.stationweb.load.WebLoadFactory;

import java.util.List;

public class LoadStationFactory {
    private LoadStationFactory() {
    }

    /**
     * alles was nach einem Neuladen oder Einlesen einer gespeicherten Senderliste ansteht
     */
    public static void afterLoadingStationList(List<String> logList) {
        final ProgData progData = ProgData.getInstance();

        logList.add("");
        logList.add("Jetzige Liste erstellt am: " + progData.stationList.getGenDate());
        logList.add("  Anzahl Sender: " + progData.stationList.size());
        logList.add("  Anzahl Neue:  " + progData.stationList.countNewStations());
        logList.add("");
        logList.add(P2Log.LILNE2);
        logList.add("");

        progData.pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_LOADED,
                "Sender markieren", WebLoadFactory.PROGRESS_INDETERMINATE));

        logList.add("Sender markieren");
        final int count = progData.stationList.markDoubleStations();
        logList.add("Anzahl doppelte Sender: " + count);
        logList.add("Tags suchen");
        progData.stationList.loadFilterLists();

        progData.pEventHandler.notifyListener(new P2Event(PEvents.LOAD_RADIO_LIST_LOADED,
                "Blacklist filtern", WebLoadFactory.PROGRESS_INDETERMINATE));

        logList.add(P2Log.LILNE3);
        logList.add("Favoriten/History markieren");
        StationListFactory.findAndMarkStations(progData);

        logList.add(P2Log.LILNE3);
        logList.add("Blacklist filtern");
        progData.stationList.filterListWithBlacklist(true);
    }
}
