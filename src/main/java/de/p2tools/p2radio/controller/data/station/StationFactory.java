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
import de.p2tools.p2radio.gui.dialog.FavouriteEditDialogController;

import java.util.ArrayList;

public class StationFactory {

    public static void favouriteStationList() {
        final ArrayList<StationData> list = ProgData.getInstance().stationGuiController.getSelList();
        StationFactory.favouriteStation(list);
    }

    public static void favouriteStation(StationData station) {
        ArrayList<StationData> list = new ArrayList<>();
        list.add(station);
        favouriteStation(list);
    }

    private static void favouriteStation(ArrayList<StationData> list) {
        if (list.isEmpty()) {
            return;
        }

        ProgData progData = ProgData.getInstance();
        ArrayList<StationData> addList = new ArrayList<>();

        for (final StationData station : list) {
            // erst mal schauen obs den schon gibt
            StationData stationData = progData.favouriteList.getUrlStation(station.getStationUrl());
            if (stationData == null) {
                addList.add(station);
            } else {
                // dann ist der Sender schon in der Liste
                if (list.size() <= 1) {
                    PAlert.BUTTON answer = PAlert.showAlert_yes_no("Anlegen?", "Nochmal anlegen?",
                            "Sender existiert bereits:" + P2LibConst.LINE_SEPARATORx2 +
                                    station.getCountry() + P2LibConst.LINE_SEPARATORx2 +
                                    "Nochmal anlegen?");
                    switch (answer) {
                        case NO:
                            // alles Abbrechen
                            return;
                        case YES:
                            addList.add(station);
                            break;
                    }

                } else {
                    PAlert.BUTTON answer = PAlert.showAlert_yes_no_cancel("Anlegen?", "Nochmal anlegen?",
                            "Sender existiert bereits:" + P2LibConst.LINE_SEPARATORx2 +
                                    station.getCountry() + P2LibConst.LINE_SEPARATORx2 +
                                    "Nochmal anlegen (Ja / Nein)?" + P2LibConst.LINE_SEPARATOR +
                                    "Oder alles Abbrechen?");
                    switch (answer) {
                        case CANCEL:
                            // alles Abbrechen
                            return;
                        case NO:
                            continue;
                        case YES:
                            addList.add(station);
                            break;
                    }
                }
            }
        }
        if (!addList.isEmpty()) {
            ArrayList<StationData> favouriteList = new ArrayList<>();
            addList.stream().forEach(s -> {
                StationData stationData = new StationData(s, "");
                favouriteList.add(stationData);
            });

            FavouriteEditDialogController favouriteEditDialogController =
                    new FavouriteEditDialogController(progData, favouriteList);

            if (favouriteEditDialogController.isOk()) {
                favouriteList.stream().forEach(f -> {
                    progData.favouriteList.addAll((StationData) f);
                });
//                progData.favouriteList.addAll(favouriteList);
                //Favoriten markieren und Filter anstoßen
                StationListFactory.findAndMarkFavouriteStations(progData);
                progData.stationListBlackFiltered.triggerFilter();
            }
        }
    }

//    public static void playStation(Favourite station, SetData psetData) {
//        ProgData.getInstance().startFactory.playPlayable(station);
//    }
}
