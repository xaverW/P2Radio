/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.p2radio.controller.pevent;

import de.p2tools.p2lib.p2event.P2Event;

public class RunEventRadio extends P2Event {

    public static final double PROGRESS_MIN = 0.0;
    public static final double PROGRESS_MAX = 1.0;
    public static final double PROGRESS_INDETERMINATE = -1.0;

    // meldet eine Änderung
    private final double progress;
    private String url = "";
    private boolean error = false;
    private final NOTIFY notify;

    public RunEventRadio(int eventNo, NOTIFY notify,
                         String url, String text,
                         double progress, boolean error) {
        super(eventNo, text);
        setEventNo(eventNo);
        this.notify = notify;
        this.progress = progress;
        this.url = url;
        this.error = error;
    }

    public double getProgress() {
        return progress;
    }

    public String getUrl() {
        return url;
    }

    public NOTIFY getNotify() {
        return notify;
    }

    public boolean isError() {
        return error;
    }

    public enum NOTIFY {START, PROGRESS, LOADED, FINISHED}
}
