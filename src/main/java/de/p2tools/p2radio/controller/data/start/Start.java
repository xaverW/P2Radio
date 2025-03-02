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
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import javafx.beans.property.*;

public final class Start {
    // Stati
    public static final int STATE_INIT = 0; //nicht gestartet
    public static final int STATE_STARTED_RUN = 1; //läuft
    public static final int STATE_STOPPED = 2; //abgebrochen
    public static final int STATE_ERROR = 3; //fehlerhaft
    private final IntegerProperty state = new SimpleIntegerProperty(STATE_INIT);

    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

    private Process process = null; //Prozess des Programms (VLC)
    private final StationData stationData;
    private final P2Date startTime;
    private final SetData setData;
    private final BooleanProperty isStarting = new SimpleBooleanProperty(true); // PlayingThread meldet, wenn gestartet
    private final BooleanProperty stopFromButton = new SimpleBooleanProperty(false); // Wenn dur Button gestoppt, nicht durch Beenden VLC

    public Start(SetData setData, StationData stationData) {
        this.stationData = stationData;
        this.startTime = new P2Date();
        this.setData = setData;
        StartProgramFactory.makeProgParameter(this);
    }

    public void initStart() {
        state.set(FavouriteConstants.STATE_INIT);

        PlayingThread playingThread = new PlayingThread(ProgData.getInstance(), this);
        playingThread.start();

        P2ToolsFactory.waitWhile(3_000, isStarting);

        StartFactory.startMsg(this);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(PEvents.REFRESH_TABLE));
        setStarting(false);
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
    public StationData getStationData() {
        return stationData;
    }

    public SetData getSetData() {
        return setData;
    }

    public P2Date getStartTime() {
        return startTime;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public boolean isStarting() {
        return isStarting.get();
    }

    public void setStarting(boolean set) {
        isStarting.set(set);
    }

    public boolean isStopFromButton() {
        return stopFromButton.get();
    }

    public void setStopFromButton(boolean set) {
        stopFromButton.set(set);
    }

    public BooleanProperty stopFromButtonProperty() {
        return stopFromButton;
    }

    //=================
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
}
