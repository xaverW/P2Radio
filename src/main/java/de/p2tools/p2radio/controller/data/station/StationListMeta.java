/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigLocalDatePropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataListMeta;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;

import java.util.ArrayList;

public class StationListMeta extends PDataSample<StationListMeta> {
    final PLocalDateProperty stationDate = new PLocalDateProperty();

    public StationListMeta() {
        stationDate.getValue().setPLocalDateNow();
    }

    @Override
    public String getTag() {
        return PDataListMeta.META_KEY;
    }

    @Override
    public String getComment() {
        return "comment";
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigLocalDatePropExtra("stationDate", StationDataXml.STATION_PROP_DATE, stationDate));
        return list.toArray(new Config[]{});
    }

    public PLocalDate getStationDate() {
        return stationDate.get();
    }

    public void setStationDate(PLocalDate stationDate) {
        this.stationDate.set(stationDate);
    }

    public PLocalDateProperty stationDateProperty() {
        return stationDate;
    }
}
