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


package de.p2tools.p2radio.controller.data.lastPlayed;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationListFactory;

import java.util.ArrayList;
import java.util.Optional;

public class LastPlayedFactory {
    private LastPlayedFactory() {

    }

    public static void deleteHistory(boolean all) {
        if (all) {
            final ArrayList<LastPlayed> list = ProgData.getInstance().lastPlayedGuiController.getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Sender aus der History gelöscht werden?";
            } else {
                text = "Sollen die " + list.size() + " markierten Sender aus der History gelöscht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage,
                    "History löschen?", "History löschen?", text).equals(PAlert.BUTTON.YES)) {
                ProgData.getInstance().lastPlayedList.removeAll(list);
            }

        } else {
            final Optional<LastPlayed> favourite = ProgData.getInstance().lastPlayedGuiController.getSel();
            if (favourite.isPresent()) {
                deleteHistory(favourite.get());
            }
        }
    }

    public static void deleteHistory(LastPlayed lastPlayed) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "History löschen?", "History löschen?",
                "Soll der Sender aus der History gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            ProgData.getInstance().lastPlayedList.remove(lastPlayed);
            StationListFactory.findAndMarkFavouriteStations(ProgData.getInstance());
        }
    }

    public static void deleteCompleteHistory() {
        final String text;
        text = "Soll die gesamte History gelöscht werden?";
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage,
                "History löschen?", "History löschen?", text).equals(PAlert.BUTTON.YES)) {
            ProgData.getInstance().lastPlayedList.clear();
        }
    }
}
