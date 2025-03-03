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

        String sumStationListStr = numberFormat.format(sumStationsShown);
        String sumStationsShownStr = numberFormat.format(sumStationList);

        // Anzahl der Sender
        textLinks = sumStationListStr + " Sender";
        if (sumStationList != sumStationsShown) {
            textLinks += " (Insgesamt: " + sumStationsShownStr + " )";
        }

        // auch die Favoriten anzeigen
        if (!progData.favouriteList.isEmpty()) {
            textLinks += SEPARATOR;

            // Anzahl der Favoriten
            if (progData.favouriteList.size() == 1) {
                textLinks += "1 Favorit";
            } else {
                textLinks += progData.favouriteList.size() + " Favoriten";
            }
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
        return textLinks;
    }

    public static String getInfosHistory() {
        String textLinks;
        String sumFavouriteListStr = numberFormat.format(progData.historyList.size());
        String sumFavouritesShownStr = numberFormat.format(progData.historyGuiPack.getHistoryGuiController().getHistoryShown());

        // Anzahl der History
        if (progData.historyGuiPack.getHistoryGuiController().getHistoryShown() == 1) {
            textLinks = "1 History";
        } else {
            textLinks = sumFavouritesShownStr + " Historys";
        }

        // weitere Infos anzeigen
        if (progData.historyList.size() != progData.historyGuiPack.getHistoryGuiController().getHistoryShown()) {
            textLinks += " (Insgesamt: " + sumFavouriteListStr;
            textLinks += ")";
        }
        return textLinks;
    }
}
