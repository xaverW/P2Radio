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

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.gui.dialog.FavouriteAddOwnDialogController;
import de.p2tools.p2radio.gui.dialog.FavouriteEditDialogController;

import java.util.ArrayList;
import java.util.Optional;

public class FavouriteFactory {
    private FavouriteFactory() {
    }

    public static void addFavourite(boolean own) {
        StationData stationData = new StationData();
        stationData.setOwn(own);
        stationData.setStationDate(new PLocalDate().getDateTime(PLocalDate.FORMAT_dd_MM_yyyy));

        FavouriteAddOwnDialogController favouriteEditDialogController =
                new FavouriteAddOwnDialogController(ProgData.getInstance(), stationData);

        if (favouriteEditDialogController.isOk()) {
            ProgData.getInstance().favouriteList.add(stationData);
            ProgData.getInstance().collectionList.updateNames();//könnte ja geändert sein
        }
    }

    public static void changeFavourite(boolean allSel) {
        ArrayList<StationData> list = new ArrayList<>();
        ArrayList<StationData> listCopy = new ArrayList<>();
        if (allSel) {
            list.addAll(ProgData.getInstance().favouriteGuiController.getSelList());
        } else {
            final Optional<StationData> favourite = ProgData.getInstance().favouriteGuiController.getSel();
            if (favourite.isPresent()) {
                list.add(favourite.get());
            }
        }

        if (list.isEmpty()) {
            return;
        }
        list.stream().forEach(f -> {
            StationData favouriteCopy = f.getCopy();
            listCopy.add(favouriteCopy);
        });

        FavouriteEditDialogController favouriteEditDialogController =
                new FavouriteEditDialogController(ProgData.getInstance(), listCopy);

        if (favouriteEditDialogController.isOk()) {
            for (int i = 0; i < listCopy.size(); ++i) {
                final StationData f, fCopy;
                f = list.get(i);
                fCopy = listCopy.get(i);
                f.copyToMe(fCopy);
            }
            ProgData.getInstance().collectionList.updateNames();//könnte ja geändert sein
        }
    }

    public static void deletePlayable(StationData favourite) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?", "Favoriten löschen?",
                "Soll der Favorite gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            ProgData.getInstance().favouriteList.remove(favourite);
            StationListFactory.findAndMarkFavouriteStations(ProgData.getInstance());
        }
    }

    public static void deleteFavourite(StationData stationData) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?", "Favoriten löschen?",
                "Soll der Favorite gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            ProgData.getInstance().favouriteList.remove(stationData);
            StationListFactory.findAndMarkFavouriteStations(ProgData.getInstance());
        }
    }

    public static void deleteFavourite(boolean all) {
        if (all) {
            final ArrayList<StationData> list = ProgData.getInstance().favouriteGuiController.getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Favorit gelöscht werden?";
            } else {
                text = "Sollen die Favoriten gelöscht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?", "Favoriten löschen?", text)
                    .equals(PAlert.BUTTON.YES)) {
                ProgData.getInstance().favouriteList.removeAll(list);
                StationListFactory.findAndMarkFavouriteStations(ProgData.getInstance());
            }

        } else {
            final Optional<StationData> favourite = ProgData.getInstance().favouriteGuiController.getSel();
            if (favourite.isPresent()) {
                FavouriteFactory.deleteFavourite(favourite.get());
            }
        }
    }

    public static synchronized int countStartedAndRunningFavourites() {
        //es wird nach gestarteten und laufenden Favoriten gesucht
        int ret = 0;
        for (final StationData stationData : ProgData.getInstance().favouriteList) {
            if (stationData.getStart() != null &&
                    (stationData.getStart().getStartStatus().isStarted() || stationData.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }
}
