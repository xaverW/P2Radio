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


package de.p2tools.p2radio.controller.tips;

import java.util.ArrayList;
import java.util.List;

public class TipListSet {

    private TipListSet() {
    }

    public static List<TipData> getTips() {
        List<TipData> pToolTipList = new ArrayList<>();

        String text = "Sets sind die Einstellungen " +
                "die zum Aufzeichnen und Abspielen " +
                "der Sender gebraucht werden. " +
                "Es muss mindestens ein Set vorhanden sein." +
                "\n\n" +
                "In den Programm-Einstellungen können die Sets " +
                "angelegt und geändert werden. " +
                "Ein Set hat einen Namen und eine Beschreibung (dient nur zur eigenen Info). " +
                "Im Set wird ein Programm ausgewählt, mit dem der Sender dann " +
                "abgespielt wird. In \"Programm\" steht nur der Pfad und Dateiname des " +
                "Players." +
                "\n\n" +
                "Im Schalter stehen Parameter die das Programm braucht. " +
                "\"%f\" ist für die URL des Senders.";
        String image = "/de/p2tools/p2radio/res/tips/set/set-1.png";
        TipData pToolTip = new TipData(text, image);
        pToolTipList.add(pToolTip);


        text = "In der Tabelle mit den Sendern werden die \"Klicks\" angezeigt. " +
                "Das ist die Anzahl der Aufrufe in den letzten 24 Stunden und " +
                "\"Trend\" gibt die Änderung der Klicks in den letzten 2 Tagen an." +
                "\n\n" +
                "Damit das gezählt werden kann, schickt das Programm einen Ping " +
                "beim Start eines Senders an die Website des Radio-Projekts." +
                "\n\n" +
                "Vor dem ersten Mal, wird aber in einem Dialog gefragt, ob das gewollt wird. " +
                "Diese Einstellung kann hier dann auch wieder geändert werden.";
        image = "/de/p2tools/p2radio/res/tips/set/set-2.png";
        pToolTip = new TipData(text, image);
        pToolTipList.add(pToolTip);


        text = "Das Programm kann beim Start auch gleich einen " +
                "Sender starten.\n" +
                "Möglich ist der zuletzt gespielte " +
                "Sender oder es kann einer ausgewählt werden, der abgespielt " +
                "werden soll. Es kann auch eine Liste an Sendern vorgegeben " +
                "werden, aus der dann einer zufällig ausgewählt wird." +
                "\n\n" +
                "Das kann hier in dem Dialog (in den Einstellungen) ausgewählt " +
                "werden.";
        image = "/de/p2tools/p2radio/res/tips/set/set-3.png";
        pToolTip = new TipData(text, image);
        pToolTipList.add(pToolTip);

        return pToolTipList;
    }
}
