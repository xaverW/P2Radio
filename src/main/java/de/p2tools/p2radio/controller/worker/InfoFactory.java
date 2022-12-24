/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.worker;

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;

import java.text.NumberFormat;
import java.util.Locale;

public class InfoFactory {

    private static final String SEPARATOR = "  ||  ";
    private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
    private static final ProgData progData = ProgData.getInstance();

    private InfoFactory() {
    }

    public static synchronized String getInfosStations() {
        String textLinks;
        final int sumStationList = progData.stationList.size();
        final int sumStationsShown = progData.stationGuiPack.getStationGuiController().getStationCount();
        final int runs = progData.favouriteList.getListOfStartsNotFinished(FavouriteConstants.SRC_BUTTON).size();

        String sumStationListStr = numberFormat.format(sumStationsShown);
        String sumStationsShownStr = numberFormat.format(sumStationList);

        // Anzahl der Sender
        textLinks = sumStationListStr + " Sender";
        if (sumStationList != sumStationsShown) {
            textLinks += " (Insgesamt: " + sumStationsShownStr + " )";
        }
        // laufende Programme
        if (runs == 1) {
            textLinks += SEPARATOR;
            textLinks += (runs + " laufender Sender");
        } else if (runs > 1) {
            textLinks += SEPARATOR;
            textLinks += (runs + " laufende Sender");
        }

        // auch die Favoriten anzeigen
        if (progData.favouriteInfos.getAmount() > 0) {
            textLinks += SEPARATOR;

            // Anzahl der Favoriten
            if (progData.favouriteInfos.getAmount() == 1) {
                textLinks += "1 Favorit";
            } else {
                textLinks += progData.favouriteInfos.getAmount() + " Favoriten";
            }
            textLinks += getRunningInfos();
        }

        return textLinks;
    }

    public static String getInfosFavourites() {
        String textLinks;
        String sumFavouriteListStr = numberFormat.format(progData.favouriteList.size());
        String sumFavouritesShownStr = numberFormat.format(progData.favouriteGuiPack.getFavouriteGuiController().getFavouritesShown());

        // Anzahl der Favoriten
        if (progData.favouriteGuiPack.getFavouriteGuiController().getFavouritesShown() == 1) {
            textLinks = "1 Favorite";
        } else {
            textLinks = sumFavouritesShownStr + " Favoriten";
        }

        // weitere Infos anzeigen
        if (progData.favouriteList.size() != progData.favouriteGuiPack.getFavouriteGuiController().getFavouritesShown()) {
            textLinks += " (Insgesamt: " + sumFavouriteListStr;
            textLinks += ")";
        }

        if (progData.favouriteInfos.getAmount() > 0) {
            // nur wenn ein Favorite läuft, wartet, ..
            textLinks += getRunningInfos();
        }

        return textLinks;
    }

    private static synchronized String getRunningInfos() {
        String textLinks = " || ";
        int running = 0;
        running = progData.favouriteInfos.getStarted();
        running += progData.stationInfos.getStarted();
        if (running == 0) {
            textLinks = "";

        } else if (running == 1) {
            textLinks += "1 Sender läuft";

        } else {
            textLinks += running + " Sender laufen";
        }

        return textLinks;
    }
}
