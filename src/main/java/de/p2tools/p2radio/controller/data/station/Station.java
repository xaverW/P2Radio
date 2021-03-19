/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.p2radio.controller.data.start.Start;

public class Station extends StationProps {

    private Start start = null;

    public Station() {
    }

    public void init() {
        preserveMemory();
        setBitrateInt();
        setDate();
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    private void preserveMemory() {
        // Speicher sparen
        arr[STATION_DATE] = arr[STATION_DATE].intern();
        arr[STATION_LANGUAGE] = arr[STATION_LANGUAGE].intern();
    }

    private void setBitrateInt() {
        int durSecond;
        try {
            if (!arr[STATION_BITRATE].isEmpty()) {
                durSecond = Integer.parseInt(arr[STATION_BITRATE]);
            } else {
                durSecond = 0;
            }
        } catch (final Exception ex) {
            durSecond = 0;
            PLog.errorLog(468912049, "Bitrate: " + arr[STATION_BITRATE]);
        }

        setBitrateInt(durSecond);
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

    public Station getCopy() {
        final Station ret = new Station();
        System.arraycopy(arr, 0, ret.arr, 0, arr.length);
        ret.stationDate = stationDate;
        ret.no = no;
        ret.setBitrateInt(getBitrateInt());
        return ret;
    }
}
