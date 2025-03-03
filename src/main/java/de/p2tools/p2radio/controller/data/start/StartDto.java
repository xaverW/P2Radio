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

import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.beans.property.*;

public final class StartDto {
    public static final int STATE_INIT = 0; // nicht gestartet
    public static final int STATE_STARTED_RUN = 1; // läuft
    public static final int STATE_ERROR = 2; // fehlerhaft
    private final IntegerProperty state = new SimpleIntegerProperty(STATE_INIT);

    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

    private Process process = null; //Prozess des Programms (VLC)
    private final StationData stationData;
    private final P2Date startTime;
    private final SetData setData;
    private final BooleanProperty isStarting = new SimpleBooleanProperty(true); // PlayingThread meldet, wenn gestartet
    private final BooleanProperty stopFromButton = new SimpleBooleanProperty(false); // Wenn dur Button gestoppt, nicht durch Beenden VLC

    public StartDto(SetData setData, StationData stationData) {
        this.stationData = stationData;
        this.startTime = new P2Date();
        this.setData = setData;
        StartProgramFactory.makeProgParameter(this);
    }

    // STATE
    public void setStateError() {
        this.state.set(StartDto.STATE_ERROR);
    }

    public boolean isStateError() {
        return state.get() == StartDto.STATE_ERROR;
    }

    public void setStateStartedRun() {
        state.set(StartDto.STATE_STARTED_RUN);
    }

    public boolean isStateStartedRun() {
        return state.get() == StartDto.STATE_STARTED_RUN;
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

    public BooleanProperty getIsStartingProperty() {
        return isStarting;
    }

    public boolean isStopFromButton() {
        return stopFromButton.get();
    }

    public void setStopFromButton(boolean set) {
        stopFromButton.set(set);
    }

    //=================
    public String getProgramCall() {
        return programCall.get();
    }

    public void setProgramCall(String programCall) {
        this.programCall.set(programCall);
    }

    public String getProgramCallArray() {
        return programCallArray.get();
    }

    public void setProgramCallArray(String programCallArray) {
        this.programCallArray.set(programCallArray);
    }
}
