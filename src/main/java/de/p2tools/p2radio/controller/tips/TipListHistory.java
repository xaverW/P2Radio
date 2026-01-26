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

public class TipListHistory {

    private TipListHistory() {
    }

    public static List<TipData> getTips() {
        List<TipData> pToolTipList = new ArrayList<>();

        String text = "Im Tab History werden alle Sender die schon " +
                "abgespielt wurden, angezeigt. Sender können wieder " +
                "gestartet und aus der History gelöscht werden." +
                "\n\n" +
                "In dem Infobereich unter der Tabelle " +
                "werden Infos zum Sender angezeigt. " +
                "Links neben der Tabelle kann dieselbe " +
                "gefiltert werden.";
        String image = "/de/p2tools/p2radio/res/tips/history/history-1.png";
        TipData pToolTip = new TipData(text, image);
        pToolTipList.add(pToolTip);

        text = "Zuerst kann damit der Sender gestartet und " +
                "darunter wieder gestoppt werden." +
                "\n\n" +
                "Das \"X\" löscht alle in der Tabelle " +
                "ausgewählte Sender aus der History. Das \"i\" zeigt " +
                "Infos zum Sender an.";
        image = "/de/p2tools/p2radio/res/tips/history/history-2.png";
        pToolTip = new TipData(text, image);
        pToolTipList.add(pToolTip);

        return pToolTipList;
    }
}
