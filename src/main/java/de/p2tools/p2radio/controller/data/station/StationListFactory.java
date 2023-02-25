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

import de.p2tools.p2lib.tools.duration.PDuration;
import de.p2tools.p2lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.beans.property.SimpleListProperty;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class StationListFactory {
    final static String regEx1 = "[\\n\\r]";
    final static String regEx2 = "[\\p{Cc}&&[^\\t\\n\\r]]";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final Map<Character, Integer> counterMap = new HashMap<>(25);


    private StationListFactory() {
    }

    public static void cleanFaultyCharacterStationList() {
        // damit werden Unicode-Zeichen korrigiert
        // gibt da einen Java-Bug
        // https://github.com/javafxports/openjdk-jfx/issues/287
        PDuration.counterStart("cleanFaultyCharacter");
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
            PLog.sysLog("Key: " + (int) key + "  Key: " + key + "  Anz: " + value);

        }
        PDuration.counterStop("cleanFaultyCharacter");
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
        PDuration.counterStart("FavouriteList.addSenderInList");
        int counter = 50;
        for (StationData stationData : list) {
            --counter;
            if (counter < 0) {
                break;
            }
            stationData.setStation(ProgData.getInstance().stationList.getSenderByUrl(stationData.getStationUrl()));
        }
        PDuration.counterStop("FavouriteList.addSenderInList");
    }

    public static void findAndMarkFavouriteStations(ProgData progData) {
        PDuration.counterStart("findAndMarkFavouriteStations");
        final HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(progData.favouriteList.stream().map(StationData::getStationUrl).collect(Collectors.toList()));

        progData.stationList.stream().forEach(station -> station.setFavourite(false));
        progData.stationList.stream()
                .filter(station -> hashSet.contains(station.getStationUrl()))
                .forEach(station -> station.setFavourite(true));

        progData.historyList.stream().forEach(station -> station.setFavourite(false));
        progData.historyList.stream()
                .filter(station -> hashSet.contains(station.getStationUrl()))
                .forEach(stationData -> stationData.setFavourite(true));

        hashSet.clear();
        PDuration.counterStop("findAndMarkFavouriteStations");
    }
}
