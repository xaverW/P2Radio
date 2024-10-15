package de.p2tools.p2radio.controller.data;

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;

import java.util.Optional;

public class AutoStartFactory {
    public static final String TAG_AUTOSTART = "StationAutoStart";
    public static final String TAG_LAST_PLAYED = "StationLastPlayed";

    public static final int AUTOSTART_NOTHING = 0;
    public static final int AUTOSTART_AUTO = 1;
    public static final int AUTOSTART_LAST_PLAYED = 2;

    private AutoStartFactory() {
    }

    public static void setStationAutoStart() {
        final Optional<StationData> optionalStation =
                ProgData.getInstance().stationGuiPack.getStationGuiController().getSel(true);
        set(optionalStation);
    }

    public static void setFavouriteAutoStart() {
        final Optional<StationData> optionalStation =
                ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSel(true);
        set(optionalStation);
    }

    public static void setHistoryAutoStart() {
        final Optional<StationData> optionalStation =
                ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSel(true);
        set(optionalStation);
    }

    private static void set(Optional<StationData> optionalStation) {
        StationData station;
        if (optionalStation.isPresent()) {
            station = optionalStation.get();
            ProgData.getInstance().stationAutoStart.switchOffAuto();
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_AUTO);
        }
    }

    public static void setAutoStart(boolean set) {
        setAutoStart(null, set);
    }

    public static void setAutoStart(StationData station, boolean set) {
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

    public static void setLastPlayed(boolean set) {
        setLastPlayed(null, set);
    }

    public static void setLastPlayed(StationData station, boolean set) {
        if (set) {
            if (station != null) {
                ProgData.getInstance().stationLastPlayed.copyToMe(station);
            }
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LAST_PLAYED);

        } else {
            ProgData.getInstance().stationLastPlayed.switchOffAuto();
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_NOTHING);
        }
    }
}