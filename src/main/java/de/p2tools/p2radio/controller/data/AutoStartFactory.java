package de.p2tools.p2radio.controller.data;

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
        final Optional<StationData> optionalStation = ProgData.getInstance().stationGuiPack.getStationGuiController().getSel(true);
        StationData station;
        if (optionalStation.isPresent()) {
            station = optionalStation.get();
            ProgData.getInstance().stationAutoStart.copyToMe(station);
        }
    }

    public static void setAuto(StationData station, boolean set) {
        if (station != null) {
            if (set) {
                ProgData.getInstance().stationAutoStart.copyToMe(station);
            } else {
                ProgData.getInstance().stationAutoStart.copyToMe(new StationData(AutoStartFactory.TAG_AUTOSTART));
            }
        }
    }

}
