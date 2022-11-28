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

package de.p2tools.p2radio.controller.data.station;

import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.data.playable.Playable;
import de.p2tools.p2radio.controller.data.start.Start;

public class Station extends StationProps implements Playable {

    private Start start = null;

    public Station() {
    }

    public void init() {
        preserveMemory();
        setIntValues();
        setDate();
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    @Override
    public boolean getFavouriteUrl() {
        return false;
    }

    @Override
    public int getStationNo() {
        return 0;
    }

    @Override
    public String getCollectionName() {
        return null;
    }

    @Override
    public int getGrade() {
        return 0;
    }

    @Override
    public boolean getOwn() {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public PLocalDate getStationDate() {
        return null;
    }

    private void preserveMemory() {
        // Speicher sparen
        arr[STATION_DATE] = arr[STATION_DATE].intern();
        arr[STATION_LANGUAGE] = arr[STATION_LANGUAGE].intern();
    }

    private void setIntValues() {
        //STATION_BITRATE
        try {
            if (!arr[STATION_BITRATE].isEmpty()) {
                setBitrateInt(Integer.parseInt(arr[STATION_BITRATE]));
            } else {
                setBitrateInt(0);
            }
        } catch (final Exception ex) {
            setBitrateInt(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[STATION_BITRATE]);
        }

        //STATION_VOTES
        try {
            if (!arr[STATION_VOTES].isEmpty()) {
                setVotes(Integer.parseInt(arr[STATION_VOTES]));
            } else {
                setVotes(0);
            }
        } catch (final Exception ex) {
            setVotes(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[STATION_VOTES]);
        }

        //STATION_CLICK_COUNT
        try {
            if (!arr[STATION_CLICK_COUNT].isEmpty()) {
                setClickCount(Integer.parseInt(arr[STATION_CLICK_COUNT]));
            } else {
                setClickCount(0);
            }
        } catch (final Exception ex) {
            setClickCount(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[STATION_CLICK_COUNT]);
        }

        //STATION_CLICK_TREND
        try {
            if (!arr[STATION_CLICK_TREND].isEmpty()) {
                setClickTrend(Integer.parseInt(arr[STATION_CLICK_TREND]));
            } else {
                setClickTrend(0);
            }
        } catch (final Exception ex) {
            setClickTrend(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[STATION_CLICK_TREND]);
        }
    }

    private void setDate() {
        stationDate.setPLocalDateNow();
        if (!arr[STATION_DATE].isEmpty()) {
            // nur dann gibts ein Datum
            try {
                PDate pd = new PDate(sdf_date.parse(arr[STATION_DATE]));
                stationDate.setPLocalDate(pd);

            } catch (final Exception ex) {
                PLog.errorLog(854121547, ex, new String[]{"Datum: " + arr[STATION_DATE]});
                stationDate = new PLocalDate();
                arr[STATION_DATE] = "";
            }
        }
    }

    public boolean isStation() {
        return true;
    }

    public boolean isFavourite() {
        return false;
    }

    public boolean isLastPlayed() {
        return false;
    }

    public Station getCopy() {
        final Station ret = new Station();
        System.arraycopy(arr, 0, ret.arr, 0, arr.length);
        ret.no = no;
        ret.init(); //Datum und int-Werte setzen
        return ret;
    }
}
