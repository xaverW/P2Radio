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

package de.p2tools.p2radio.controller.data.station;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.gui.dialog.FavouriteEditDialogController;

import java.util.ArrayList;

public class StationTools {

    public static void playStation(Station station, SetData psetData) {
        ProgData.getInstance().startFactory.playStation(station);
    }

    public static void saveStation(Station station) {
        ArrayList<Station> list = new ArrayList<>();
        list.add(station);
        saveStation(list);
    }

    public static void saveStation(ArrayList<Station> list) {
        if (list.isEmpty()) {
            return;
        }

        ProgData progData = ProgData.getInstance();
        ArrayList<Station> addList = new ArrayList<>();

        for (final Station dateStation : list) {
            // erst mal schauen obs den schon gibt
            Favourite favourite = progData.favouriteList.getUrlStation(dateStation.arr[Station.STATION_URL]);
            if (favourite == null) {
                addList.add(dateStation);
            } else {
                // dann ist der Sender schon in der Liste
                if (list.size() <= 1) {
                    PAlert.BUTTON answer = PAlert.showAlert_yes_no("Anlegen?", "Nochmal anlegen?",
                            "Sender existiert bereits:" + P2LibConst.LINE_SEPARATORx2 +
                                    dateStation.getCountry() + P2LibConst.LINE_SEPARATORx2 +
                                    "Nochmal anlegen?");
                    switch (answer) {
                        case NO:
                            // alles Abbrechen
                            return;
                        case YES:
                            addList.add(dateStation);
                            break;
                    }

                } else {
                    PAlert.BUTTON answer = PAlert.showAlert_yes_no_cancel("Anlegen?", "Nochmal anlegen?",
                            "Sender existiert bereits:" + P2LibConst.LINE_SEPARATORx2 +
                                    dateStation.getCountry() + P2LibConst.LINE_SEPARATORx2 +
                                    "Nochmal anlegen (Ja / Nein)?" + P2LibConst.LINE_SEPARATOR +
                                    "Oder alles Abbrechen?");
                    switch (answer) {
                        case CANCEL:
                            // alles Abbrechen
                            return;
                        case NO:
                            continue;
                        case YES:
                            addList.add(dateStation);
                            break;
                    }
                }
            }
        }
        if (!addList.isEmpty()) {
            ArrayList<Favourite> favouriteList = new ArrayList<>();
            addList.stream().forEach(s -> {
                Favourite favourite = new Favourite(s, "");
                favouriteList.add(favourite);
            });

            FavouriteEditDialogController favouriteEditDialogController =
                    new FavouriteEditDialogController(progData, favouriteList);

            if (favouriteEditDialogController.isOk()) {
                progData.favouriteList.addAll(favouriteList);
                //Favoriten markieren und Filter anstoßen
                StationListFactory.findAndMarkFavouriteStations(progData);
                progData.stationListBlackFiltered.triggerFilter();
            }
        }
    }
}
