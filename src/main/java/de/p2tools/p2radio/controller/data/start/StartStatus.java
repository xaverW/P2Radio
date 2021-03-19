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


package de.p2tools.p2radio.controller.data.start;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class StartStatus {

    // Stati
    public static final int STATE_INIT = 0; //nicht gestart
    public static final int STATE_STARTED_RUN = 1; //läuft
    public static final int STATE_STOPPED = 4; //abgebrochen
    public static final int STATE_ERROR = 5; //fehlerhaft

    //Sender wird so oft gestartet, falls es beim ersten Mal nicht gelingt
    public static final int START_COUNTER_MAX = 3;

    private final IntegerProperty state = new SimpleIntegerProperty(StartStatus.STATE_INIT);
    private String errorMessage = "";

    public StartStatus() {
    }


    public int getState() {
        return state.get();
    }

    public IntegerProperty stateProperty() {
        return state;
    }

    public void setState(int state) {
        this.state.set(state);
    }

    //================================

    public void setStateInit() {
        this.state.set(StartStatus.STATE_INIT);
    }

    public boolean isStateInit() {
        return getState() == StartStatus.STATE_INIT;
    }

    public void setStateFinished() {
        this.state.set(StartStatus.STATE_INIT);
    }

    public void setStateError() {
        this.state.set(StartStatus.STATE_ERROR);
    }

    public boolean isStateError() {
        return getState() == StartStatus.STATE_ERROR;
    }

    public void setStateStopped() {
        setState(StartStatus.STATE_STOPPED);
    }

    public boolean isStateStopped() {
        return getState() == StartStatus.STATE_STOPPED;
    }

    public void setStateStartedRun() {
        setState(StartStatus.STATE_STARTED_RUN);
    }

    public boolean isStateStartedRun() {
        return getState() == StartStatus.STATE_STARTED_RUN;
    }

    public boolean isStarted() {
        return getState() >= StartStatus.STATE_STARTED_RUN;
    }

    //======================================

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        final String s = "Der Sender hatte einen Fehler:\n\n";
        this.errorMessage = s + errorMessage;
    }


}
