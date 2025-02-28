/*
 * P2Tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.controller.p2event;

import javafx.application.Platform;

public class P2Listener {
    private final int eventNo;

    public P2Listener(int eventNo) {
        this.eventNo = eventNo;
    }

    public int getEventNo() {
        return eventNo;
    }

    public synchronized <T extends P2Event> void notify(T event) {
        ping(event);
        Platform.runLater(() -> pingGui(event));
    }

    public <T extends P2Event> void ping(T event) {
    }

    public <T extends P2Event> void pingGui(T event) {
    }
}
