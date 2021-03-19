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

package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2radio.controller.data.SetData;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Starter {

    private int startCounter = 0;
    private int restartCounter = 0; //zählt die Anzahl der Neustarts bei einem Startfehler -> Summe Starts = erster Start + Restarts
    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

    private Process process = null; //Prozess des Programms (VLC)
    private PDate startTime = null;

    private SetData setData = null;
    private StartPlayingStation startPlayingStation = null;

    private Start start = null;

    public Starter(Start start) {
        this.start = start;
    }

    public StartPlayingStation getSt_externalProgramDownload() {
        return startPlayingStation;
    }

    public void setSt_externalProgramDownload(StartPlayingStation startPlayingStation) {
        this.startPlayingStation = startPlayingStation;
    }

    public SetData getSetData() {
        return setData;
    }

    public void setSetData(SetData setData) {
        this.setData = setData;
    }

    public void setStartTime() {
        setStartTime(new PDate());
    }

    public PDate getStartTime() {
        return startTime;
    }

    public void setStartTime(PDate startTime) {
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

    public String getProgramCall() {
        return programCall.get();
    }

    public StringProperty programCallProperty() {
        return programCall;
    }

    public void setProgramCall(String programCall) {
        this.programCall.set(programCall);
    }

    public String getProgramCallArray() {
        return programCallArray.get();
    }

    public StringProperty programCallArrayProperty() {
        return programCallArray;
    }

    public void setProgramCallArray(String programCallArray) {
        this.programCallArray.set(programCallArray);
    }

}
