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

package de.p2tools.p2radio.controller.config.pEvent;

import java.util.EventObject;

public class EventLoadRadioList extends EventObject {

    public String senderUrl = "";
    public String text;
    public double max;
    public double progress;
    public boolean error;
    public int countFoundStations;

    public EventLoadRadioList(Object source,
                              String senderUrl, String text, double max, double progress, int countFoundStations, boolean error) {
        super(source);
        this.senderUrl = senderUrl;
        this.text = text;
        this.max = max;
        this.progress = progress;
        this.countFoundStations = countFoundStations;
        this.error = error;
    }

    public EventLoadRadioList(Object source,
                              String senderUrl, String text, double progress, int countFoundStations, boolean error) {
        super(source);
        this.senderUrl = senderUrl;
        this.text = text;
        max = 0;
        this.progress = progress;
        this.countFoundStations = countFoundStations;
        this.error = error;
    }

    public static EventLoadRadioList getEmptyEvent(Object source) {
        return new EventLoadRadioList(source,
                "", "", 0, 0, false);
    }


}
