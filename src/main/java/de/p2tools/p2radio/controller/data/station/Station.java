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
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.data.playable.Playable;
import de.p2tools.p2radio.controller.data.playable.PlayableXml;
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
    public void setStationNo(int no) {

    }

    @Override
    public String getCollectionName() {
        return null;
    }

    @Override
    public int getBitrateInt() {
        return 0;
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

    private void preserveMemory() {
        // Speicher sparen
        arr[PlayableXml.STATION_PROP_DATE_INT] = arr[PlayableXml.STATION_PROP_DATE_INT].intern();
        arr[PlayableXml.STATION_PROP_LANGUAGE_INT] = arr[PlayableXml.STATION_PROP_LANGUAGE_INT].intern();
    }

    private void setIntValues() {
        //STATION_BITRATE
        try {
            if (!arr[PlayableXml.STATION_PROP_BITRATE_INT].isEmpty()) {
                setBitrateInt(Integer.parseInt(arr[PlayableXml.STATION_PROP_BITRATE_INT]));
            } else {
                setBitrateInt(0);
            }
        } catch (final Exception ex) {
            setBitrateInt(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[PlayableXml.STATION_PROP_BITRATE_INT]);
        }

        //STATION_VOTES
        try {
            if (!arr[PlayableXml.STATION_PROP_VOTES_INT].isEmpty()) {
                setVotes(Integer.parseInt(arr[PlayableXml.STATION_PROP_VOTES_INT]));
            } else {
                setVotes(0);
            }
        } catch (final Exception ex) {
            setVotes(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[PlayableXml.STATION_PROP_VOTES_INT]);
        }

        //STATION_CLICK_COUNT
        try {
            if (!arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT].isEmpty()) {
                setClickCount(Integer.parseInt(arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT]));
            } else {
                setClickCount(0);
            }
        } catch (final Exception ex) {
            setClickCount(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT]);
        }

        //STATION_CLICK_TREND
        try {
            if (!arr[PlayableXml.STATION_PROP_CLICK_TREND_INT].isEmpty()) {
                setClickTrend(Integer.parseInt(arr[PlayableXml.STATION_PROP_CLICK_TREND_INT]));
            } else {
                setClickTrend(0);
            }
        } catch (final Exception ex) {
            setClickTrend(0);
            PLog.errorLog(468912049, "Bitrate: " + arr[PlayableXml.STATION_PROP_CLICK_TREND_INT]);
        }
    }

    private void setDate() {
        getStationDate().setPLocalDateNow();
        if (!arr[PlayableXml.STATION_PROP_DATE_INT].isEmpty()) {
            // nur dann gibts ein Datum
            try {
                PDate pd = new PDate(sdf_date.parse(arr[PlayableXml.STATION_PROP_DATE_INT]));
                setStationDate(pd.getPlocalDate());

            } catch (final Exception ex) {
                PLog.errorLog(854121547, ex, new String[]{"Datum: " + arr[PlayableXml.STATION_PROP_DATE_INT]});
                getStationDate().setPLocalDateNow();
                arr[PlayableXml.STATION_PROP_DATE_INT] = "";
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

}
