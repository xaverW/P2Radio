package de.p2tools.p2radio.controller.data;

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;

import java.util.List;
import java.util.Optional;

public class AutoStartFactory {
    public static final String TAG_AUTOSTART = "StationAutoStart";
    public static final String TAG_LAST_PLAYED = "StationLastPlayed";

    public static final int AUTOSTART_NOTHING = 0;
    public static final int AUTOSTART_AUTO = 1;
    public static final int AUTOSTART_LAST_PLAYED = 2;
    public static final int AUTOSTART_LIST_STATION = 3;
    public static final int AUTOSTART_LIST_FAVOURITE = 4;
    public static final int AUTOSTART_LIST_HISTORY = 5;
    public static final int AUTOSTART_LIST_OWN = 6;

    private AutoStartFactory() {
    }

    public static void setStationAutoStartOwnList() {
        // aus dem Menü
        List<StationData> list;
        if (ProgData.STATION_TAB_ON.get()) {
            list = ProgData.getInstance().stationGuiPack.getStationGuiController().getSelList();
        } else if (ProgData.FAVOURITE_TAB_ON.get()) {
            list = ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSelList();
        } else {
            list = ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSelList();
        }
        addOwnList(list);
    }

    public static void setAutoStartOwnList(StationData station) {
        // Sender als Autostart setzen
        addOwnList(station);
    }

    public static void setStationAutoStart() {
        // aus dem Menü
        final Optional<StationData> optionalStation;

        if (ProgData.STATION_TAB_ON.get()) {
            optionalStation = ProgData.getInstance().stationGuiPack.getStationGuiController().getSel(true);

        } else if (ProgData.FAVOURITE_TAB_ON.get()) {
            optionalStation = ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSel(true);

        } else {
            optionalStation = ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSel(true);
        }

        optionalStation.ifPresent(AutoStartFactory::setAutoStart);
    }

    public static void setAutoStart(StationData station) {
        // Sender als Autostart setzen
        setAutoStart(station, true);
    }

    public static void setAutoStart(StationData station, boolean set) {
        // Sender als Autostart setzen
        if (set) {
            if (station != null) {
                ProgData.getInstance().stationAutoStart.copyToMe(station);
            }
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_AUTO);

        } else {
            ProgData.getInstance().stationAutoStart.switchOffAuto();
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_NOTHING);
        }
    }

    public static StationData getRandomStation() {
        StationData stationData = null;
        switch (ProgConfig.SYSTEM_AUTO_START.get()) {
            case AutoStartFactory.AUTOSTART_LIST_STATION:
                stationData = getListPos(ProgData.getInstance().stationList);
                break;
            case AutoStartFactory.AUTOSTART_LIST_FAVOURITE:
                stationData = getListPos(ProgData.getInstance().favouriteList);
                break;
            case AutoStartFactory.AUTOSTART_LIST_HISTORY:
                stationData = getListPos(ProgData.getInstance().historyList);
                break;
            case AutoStartFactory.AUTOSTART_LIST_OWN:
                stationData = getListPos(ProgData.getInstance().ownAutoStartList);
                break;
            default:
        }

        if (stationData == null) {
            return getListPos(ProgData.getInstance().stationList);
        } else {
            return stationData;
        }
    }

    private static void addOwnList(List<StationData> list) {
        list.forEach(AutoStartFactory::addOwnList);
    }

    private static void addOwnList(StationData stationData) {
        ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LIST_OWN);
        if (ProgData.getInstance().ownAutoStartList.stream().filter(s -> s.getStationUrl().equals(stationData.getStationUrl()))
                .findAny().isEmpty()) {
            // nur wenn die URL noch nicht drin
            ProgData.getInstance().ownAutoStartList.add(stationData);
        }
    }

    private static StationData getListPos(List<StationData> list) {
        StationData stationData = null;
        if (!list.isEmpty()) {
            int i = (int) (Math.random() * list.size());
            stationData = list.get(i);
//            Random rand = new Random();
//            i = rand.nextInt(list.size());
//            stationData = list.get(i);
        }
        return stationData;
    }
}