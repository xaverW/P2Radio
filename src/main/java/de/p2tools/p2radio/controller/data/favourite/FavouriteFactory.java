/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.data.favourite;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.gui.dialog.FavouriteAddOwnDialogController;
import de.p2tools.p2radio.gui.favouriteadd.FavouriteAddDialogController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class FavouriteFactory {
    private FavouriteFactory() {
    }

    public static void favouriteStationList() {
        final ArrayList<StationData> list = ProgData.getInstance().stationGuiPack.getStationGuiController().getSelList();
        favouriteStation(list);
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
            StationData stationData = StationListFactory.getStationByUrl(progData.favouriteList, station.getStationUrl());
            if (stationData == null) {
                addList.add(station);
            } else {
                // dann ist der Sender schon in der Liste
                if (list.size() <= 1) {
                    P2Alert.BUTTON answer = P2Alert.showAlert_yes_no("Anlegen?", "Nochmal anlegen?",
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
                    P2Alert.BUTTON answer = P2Alert.showAlert_yes_no_cancel("Anlegen?", "Nochmal anlegen?",
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
            ArrayList<StationData> newFavourites = new ArrayList<>();
            addList.forEach(stationData -> {
                StationData newStationData = new StationData();
                newStationData.copyToMe(stationData);
                newFavourites.add(newStationData);
            });

            new FavouriteAddDialogController(ProgData.getInstance(), addList, true);

            //Favoriten markieren und Filter anstoßen
            addList.forEach(stationData -> stationData.setFavourite(true));
            progData.stationListBlackFiltered.triggerFilter();
        }
    }

    public static void addOwnStationAsFavourite() {
        StationData stationData = new StationData();
        stationData.setOwn(true);
        stationData.setStationDateLastChange(P2LDateFactory.toString(LocalDate.now()));

        FavouriteAddOwnDialogController favouriteEditDialogController =
                new FavouriteAddOwnDialogController(ProgData.getInstance(), stationData);

        if (favouriteEditDialogController.isOk()) {
            ProgData.getInstance().stationList.add(stationData);
            ProgData.getInstance().favouriteList.add(stationData);
            ProgData.getInstance().collectionList.updateNames(); // könnte ja geändert sein
            ProgQuitFactory.saveProgConfig();
        }
    }

    public static void changeFavourite(StationData stationData) {
        ArrayList<StationData> list = new ArrayList<>();
        list.add(stationData);
        changeFavourite(list);
    }

    public static void changeFavourite(boolean allSel) {
        ArrayList<StationData> list = new ArrayList<>();
        if (allSel) {
            list.addAll(ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSelList());
        } else {
            final Optional<StationData> favourite = ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSel();
            favourite.ifPresent(list::add);
        }
        changeFavourite(list);
    }

    private static void changeFavourite(ArrayList<StationData> list) {
        if (list.isEmpty()) {
            return;
        }

        new FavouriteAddDialogController(ProgData.getInstance(), list, false);
        ProgData.getInstance().collectionList.updateNames();//könnte ja geändert sein
    }

    public static void deleteFavourite(StationData stationData) {
        if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?", "Favoriten löschen?",
                "Soll der Favorite gelöscht werden?").equals(P2Alert.BUTTON.YES)) {
            stationData.setFavourite(false);
            ProgData.getInstance().favouriteList.remove(stationData);
            ProgQuitFactory.saveProgConfig();
        }
    }

    public static void deleteFavourite(boolean all) {
        if (all) {
            final ArrayList<StationData> list = ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Sender aus den Favoriten gelöscht werden?";
            } else {
                text = "Sollen die " + list.size() + " markierten Sender aus den Favoriten gelöscht werden?";
            }
            if (P2Alert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?", "Favoriten löschen?", text)
                    .equals(P2Alert.BUTTON.YES)) {
                list.forEach(s -> s.setFavourite(false));
                ProgData.getInstance().favouriteList.removeAll(list);
                ProgQuitFactory.saveProgConfig();
            }

        } else {
            final Optional<StationData> favourite = ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().getSel();
            favourite.ifPresent(FavouriteFactory::deleteFavourite);
        }
    }
}
