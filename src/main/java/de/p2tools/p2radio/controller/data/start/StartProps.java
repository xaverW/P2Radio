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

package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.Config_intProp;
import de.p2tools.p2Lib.configFile.config.Config_stringProp;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import de.p2tools.p2radio.tools.Data;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class StartProps extends PDataSample<Start> {

    private final IntegerProperty no = new SimpleIntegerProperty(ProgConst.NUMBER_DONT_USED);
    private final IntegerProperty stationNo = new SimpleIntegerProperty(ProgConst.NUMBER_DONT_USED);
    private final StringProperty stationName = new SimpleStringProperty("");
    private final StringProperty url = new SimpleStringProperty("");
    private final StringProperty setDataId = new SimpleStringProperty("");
    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_intProp("stationNo", StationDataXml.STATION_PROP_STATION_NO, stationNo));
        list.add(new Config_stringProp("station", StationDataXml.STATION_PROP_STATION_NAME, stationName));
        list.add(new Config_stringProp("url", StationDataXml.STATION_PROP_URL, url));

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }


    public int getNo() {
        return no.get();
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public IntegerProperty noProperty() {
        return no;
    }

    public int getStationNo() {
        return stationNo.get();
    }

    public void setStationNo(int stationNo) {
        this.stationNo.set(stationNo);
    }

    public IntegerProperty stationNoProperty() {
        return stationNo;
    }

    public String getStationName() {
        return stationName.get();
    }

    public void setStationName(String stationName) {
        this.stationName.set(stationName);
    }

    public StringProperty stationNameProperty() {
        return stationName;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public StringProperty urlProperty() {
        return url;
    }

    public String getSetDataId() {
        return setDataId.get();
    }

    public void setSetDataId(String setDataId) {
        this.setDataId.set(setDataId);
    }

    public StringProperty setDataIdProperty() {
        return setDataId;
    }

    public String getProgramCall() {
        return programCall.get();
    }

    public void setProgramCall(String programCall) {
        this.programCall.set(programCall);
    }

    public StringProperty programCallProperty() {
        return programCall;
    }

    public String getProgramCallArray() {
        return programCallArray.get();
    }

    public void setProgramCallArray(String programCallArray) {
        this.programCallArray.set(programCallArray);
    }

    public StringProperty programCallArrayProperty() {
        return programCallArray;
    }

    public int compareTo(StartProps arg0) {
        return Data.sorter.compare(getStationName(), arg0.getStationName());
    }
}
