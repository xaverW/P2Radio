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


package de.p2tools.p2radio.controller.config.pEvent;

import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.event.Event;

import javax.swing.event.EventListenerList;
import java.util.EventListener;

public class PEventFactory {

    public enum NOTIFY {START, PROGRESS, LOADED, FINISHED}

    public final EventListenerList listeners = new EventListenerList();

    public void addListener(EventListener listener) {
        listeners.add(EventListener.class, listener);
    }

    public void removeListenerLoadStationList(EventListener listener) {
        listeners.remove(EventListener.class, listener);
    }

    public void notifyEvent(NOTIFY notify, Event event) {
        try {
            Platform.runLater(() -> {
                for (final PListener l : listeners.getListeners(PListener.class)) {
                    l.notify(notify, event);
                }
            });
        } catch (final Exception ex) {
            PLog.errorLog(825987894, ex);
        }
    }
}
