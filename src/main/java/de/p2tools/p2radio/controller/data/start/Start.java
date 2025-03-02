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

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.atomic.AtomicBoolean;

public final class Start extends StartProps {

    // Stati
    public static final int STATE_INIT = 0; //nicht gestart
    public static final int STATE_STARTED_RUN = 1; //läuft
    public static final int STATE_STOPPED = 2; //abgebrochen
    public static final int STATE_ERROR = 3; //fehlerhaft

    //Sender wird so oft gestartet, falls es beim ersten Mal nicht gelingt
    public static final int START_COUNTER_MAX = 3;
    private final IntegerProperty state = new SimpleIntegerProperty(STATE_INIT);

    private int startCounter = 0; // Anzahl der Startversuche
    private Process process = null; //Prozess des Programms (VLC)
    private P2Date startTime = null;
    private StationData stationData = null;
    private SetData setData = null;
    private PlayingThread playingThread = null;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public Start(SetData setData, StationData stationData) {
        this.stationData = stationData;
        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public void initStart() {
        setStartTime(new P2Date());
        state.set(FavouriteConstants.STATE_INIT);

        playingThread = new PlayingThread(ProgData.getInstance(), this);
        playingThread.start();

        StartFactory.startMsg(this);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
    }

    // STATE
    public void setState(int state) {
        this.state.set(state);
    }

    public void setStateError() {
        this.state.set(Start.STATE_ERROR);
    }

    public boolean isStateError() {
        return state.get() == Start.STATE_ERROR;
    }

    public boolean isStateStopped() {
        return state.get() == Start.STATE_STOPPED;
    }

    public void setStateStartedRun() {
        state.set(Start.STATE_STARTED_RUN);
    }

    public boolean isStateStartedRun() {
        return state.get() == Start.STATE_STARTED_RUN;
    }

    //=============
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

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public boolean getRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }
}
