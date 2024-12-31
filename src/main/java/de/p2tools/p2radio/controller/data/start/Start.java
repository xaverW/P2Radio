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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.station.StationData;

public final class Start extends StartProps {

    private int startCounter = 0;
    private int restartCounter = 0; //zählt die Anzahl der Neustarts bei einem Startfehler -> Summe Starts = erster Start + Restarts
    private Process process = null; //Prozess des Programms (VLC)
    private P2Date startTime = null;

    private StartStatus startStatus = new StartStatus();
    private StationData stationData = null;
    private SetData setData = null;

    public Start() {
    }

    public Start(SetData setData, StationData stationData) {
        this.stationData = stationData;
        setStationNo(stationData.getStationNo());
        setStationName(stationData.getStationName());
        setUrl(stationData.getStationUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public StartStatus getStartStatus() {
        return startStatus;
    }

    public void initStart() {
        setStartTime();
        setRestartCounter(0);
        getStartStatus().setState(FavouriteConstants.STATE_INIT);
        getStartStatus().setErrorMessage("");
    }

    public void stopStart() {
        if (!getStartStatus().isStateError()) {
            getStartStatus().setStateStopped();
        }

        setNo(P2LibConst.NUMBER_NOT_STARTED);
    }

    //==============================================
    // Get/Set
    //==============================================

    public String getStationUrl() {
        return stationData.getStationUrl();
    }

    public StationData getStationData() {
        return stationData;
    }

    public SetData getSetData() {
        return setData;
    }

    public void setSetData(SetData setData) {
        this.setData = setData;
        setSetDataId(setData.getId());
    }

    public void setStartTime() {
        setStartTime(new P2Date());
    }

    public P2Date getStartTime() {
        return startTime;
    }

    public void setStartTime(P2Date startTime) {
        this.startTime = startTime;
    }

    public int getStartCounter() {
        return startCounter;
    }

    public void setStartCounter(int startCounter) {
        this.startCounter = startCounter;
    }

    public void incStartCounter() {
        ++this.startCounter;
    }

    public int getRestartCounter() {
        return restartCounter;
    }

    public void setRestartCounter(int restartCounter) {
        this.restartCounter = restartCounter;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
}
