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

import de.p2tools.p2lib.tools.duration.P2Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

public class P2EventHandler {

    private final ArrayList<P2Listener> listeners = new ArrayList<>();
    public static long countRunningTimeSeconds = 0; // Gesamtzeit die das Programm läuft
    private boolean oneSecond = false;

    public P2EventHandler() {
        startTimer();
    }

    public void addListener(P2Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(P2Listener listener) {
        listeners.remove(listener);
    }

    public <T extends P2Event> void notifyListener(T event) {
        listeners.stream()
                .filter(p2Listener -> p2Listener.getEventNo() == event.getEventNo())
                .forEach(p2Listener -> p2Listener.notify(event));
    }

    public void startTimer() {
        // extra starten, damit er im Einrichtungsdialog nicht dazwischen funkt
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(500), ae -> {

            oneSecond = !oneSecond;
            if (oneSecond) {
                doTimerWorkOneSecond();
            }
            doTimerWorkHalfSecond();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
        P2Duration.onlyPing("Timer gestartet");
    }

    private void doTimerWorkOneSecond() {
        ++countRunningTimeSeconds;
        notifyListener(new P2Event(P2Events.EVENT_TIMER_SECOND));
    }

    private void doTimerWorkHalfSecond() {
        notifyListener(new P2Event(P2Events.EVENT_TIMER_HALF_SECOND));
    }

}
