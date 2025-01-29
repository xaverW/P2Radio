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


package de.p2tools.p2radio.controller.data.station;

import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.beans.property.SimpleListProperty;

import java.text.SimpleDateFormat;
import java.util.*;

public class StationListFactory {
    final static String regEx1 = "[\\n\\r]";
    final static String regEx2 = "[\\p{Cc}&&[^\\t\\n\\r]]";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final Map<Character, Integer> counterMap = new HashMap<>(25);


    private StationListFactory() {
    }

    public static synchronized StationData getStationByUrl(List<StationData> list, final String url) {
        final Optional<StationData> opt =
                list.parallelStream().filter(station -> station.getStationUrl().equalsIgnoreCase(url)).findAny();
        return opt.orElse(null);
    }

    public static synchronized StationData getStationByHash(List<StationData> list, StationData stationData) {
        final String searchHash = getHash(stationData);
        final Optional<StationData> opt =
                list.parallelStream().filter(station -> getHash(station).equalsIgnoreCase(searchHash)).findAny();
        return opt.orElse(null);
    }

    public static void cleanFaultyCharacterStationList() {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da einen Java-Bug
        // https://github.com/javafxports/openjdk-jfx/issues/287
        P2Duration.counterStart("cleanFaultyCharacter");
        StationList stationList = ProgData.getInstance().stationList;
        stationList.stream().forEach(favourite -> {


            favourite.setCountry(clean_1(favourite.getCountry(), true));
            favourite.setGenre(clean_1(favourite.getGenre(), true));

            favourite.setCountry(clean_2(favourite.getCountry()));
            favourite.setGenre(clean_2(favourite.getGenre()));

            // U+3000 (12288)	　	Trenn- (Leer-) Zeichen	Whitespace	IDEOGRAPHIC SPACE	Ideographisches Leerzeichen
            // das hat die Probleme gemacht, Sender: Weltbilder
        });

        for (Map.Entry<Character, Integer> entry : counterMap.entrySet()) {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            P2Log.sysLog("Key: " + (int) key + "  Key: " + key + "  Anz: " + value);

        }
        P2Duration.counterStop("cleanFaultyCharacter");
    }

    public static String cleanUnicode(String ret) {
        return clean_1(ret, true);
    }

    private static String clean_1(String ret, boolean alsoNewLine) {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da eine Java-Bug
        // https://github.com/javafxports/openjdk-jfx/issues/287
        if (alsoNewLine) {
            ret = ret.replaceAll(regEx1, " ").replaceAll(regEx2, "");
        } else {
            ret = ret.replaceAll(regEx2, "");
        }
        return ret;
    }

    private static String clean_2(String test) {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da eine Java-Bug, auch Probleme bei Linux mit fehlenden Zeichen in den code tablen
        // https://github.com/javafxports/openjdk-jfx/issues/287
        char[] c = test.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            if ((int) c[i] > 11263) { // der Wert ist jetzt einfach mal geschätzt und kommt ~ 20x vor
                counterMap.merge(c[i], 1, Integer::sum);
                c[i] = ' ';
                test = String.valueOf(c);
            }
        }
        return test;
    }

    public static synchronized void addStationInList(SimpleListProperty<StationData> list) {
        // bei Favoriten nach einem Programmstart/Neuladen der Senderliste
        // den Sender wieder eintragen
        P2Duration.counterStart("FavouriteList.addSenderInList");
        int counter = 50;
        for (StationData stationData : list) {
            --counter;
            if (counter < 0) {
                break;
            }
            stationData.setStation(ProgData.getInstance().stationList.getStationByUrl(stationData.getStationUrl()));
        }
        P2Duration.counterStop("FavouriteList.addSenderInList");
    }

    public static void findAndMarkStations(ProgData progData) {
        // nach Programmstart
        P2Duration.counterStart("findAndMarkFavouriteStations");

        final HashSet<String> hashSet = new HashSet<>(100_000);

        // Stationen putzen
        Iterator<StationData> it = progData.stationList.iterator();
        while (it.hasNext()) {
            StationData stationData = it.next();
            if (hashSet.contains(getHash(stationData))) {
                it.remove();
            } else {
                hashSet.add(getHash(stationData));
            }
        }

        progData.stationList.forEach(station -> {
            station.setFavourite(false);
            station.setHistory(false);
        });

        // favourite markieren
        hashSet.clear();
        hashSet.addAll(progData.favouriteList.stream().map(StationListFactory::getHash).toList());
        progData.stationList.stream()
                .filter(station -> hashSet.contains(getHash(station))) // nur station die im Hash (Favoriten) sind
                .forEach(station -> {
                    StationData fav = getStationByHash(progData.favouriteList, station);
                    copyFav(station, fav);
                    station.setFavourite(true);
                });

        // und noch die eigenen eintragen
        progData.favouriteList.stream().filter(StationDataProperty::isOwn)
                .forEach(stationData -> progData.stationList.add(stationData));

        // history
        hashSet.clear();
        hashSet.addAll(progData.historyList.stream().map(StationListFactory::getHash).toList());
        progData.stationList.stream()
                .filter(station -> hashSet.contains(getHash(station)))
                .forEach(station -> {
                    StationData history = getStationByHash(progData.historyList, station);
                    copyFav(station, history);
                    station.setHistory(true);
                });

        hashSet.clear();
        progData.favouriteList.clear();
        progData.historyList.clear();
        progData.stationList.stream()
                .filter(StationDataProperty::isFavourite)
                .forEach(station -> progData.favouriteList.add(station));
        progData.stationList.stream()
                .filter(StationDataProperty::isHistory)
                .forEach(station -> progData.historyList.add(station));

        P2Duration.counterStop("findAndMarkFavouriteStations");
    }

    public static String getHash(StationData stationData) {
        return stationData.getStationName() + stationData.getStationUrl();
    }

    private static void copyFav(StationData station, StationData fav) {
        // nach dem Neuladen einer Radioliste, für Favourite/History
        if (station == null || fav == null) {
            return;
        }

        station.setCollectionName(fav.getCollectionName());
        station.setDescription(fav.getDescription());
        station.setOwnGrade(fav.getOwnGrade());
        station.setStarts(fav.getStarts());
        station.setStationDateLastStart(fav.getStationDateLastStart());
    }
}
