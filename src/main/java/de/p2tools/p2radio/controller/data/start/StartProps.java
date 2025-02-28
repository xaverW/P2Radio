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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StartProps {

    private final StringProperty programCall = new SimpleStringProperty("");
    private final StringProperty programCallArray = new SimpleStringProperty("");

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
