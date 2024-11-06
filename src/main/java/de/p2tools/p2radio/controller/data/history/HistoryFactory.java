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


package de.p2tools.p2radio.controller.data.history;

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;

import java.util.ArrayList;
import java.util.Optional;

public class HistoryFactory {
    private HistoryFactory() {
    }

    public static void deleteHistory(boolean all) {
        if (all) {
            final ArrayList<StationData> list = ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Sender aus der History gelöscht werden?";
            } else {
                text = "Sollen die " + list.size() + " markierten Sender aus der History gelöscht werden?";
            }
            if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage,
                    "History löschen?", "History löschen?", text).equals(P2Alert.BUTTON.YES)) {
                list.forEach(h -> h.setHistory(false));
                ProgData.getInstance().historyList.removeAll(list);
            }

        } else {
            final Optional<StationData> data = ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSel();
            data.ifPresent(HistoryFactory::deleteHistory);
        }
    }

    public static void deleteHistory(StationData stationData) {
        if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage, "History löschen?", "History löschen?",
                "Soll der Sender aus der History gelöscht werden?").equals(P2Alert.BUTTON.YES)) {
            stationData.setHistory(false);
            ProgData.getInstance().historyList.remove(stationData);
        }
    }

    public static void deleteCompleteHistory() {
        final String text;
        text = "Soll die gesamte History gelöscht werden?";
        if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage,
                "History löschen?", "History löschen?", text).equals(P2Alert.BUTTON.YES)) {
            ProgData.getInstance().historyList.forEach(h -> h.setHistory(false));
            ProgData.getInstance().historyList.clear();
        }
    }
}
