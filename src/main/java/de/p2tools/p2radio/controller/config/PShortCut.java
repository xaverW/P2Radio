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


package de.p2tools.p2radio.controller.config;

import de.p2tools.p2lib.tools.shortcut.P2ShortcutKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PShortCut {

    public static final P2ShortcutKey SHORTCUT_CHANGE_GUI =
            new P2ShortcutKey(ProgConfig.SHORTCUT_CHANGE_GUI, ProgConfig.SHORTCUT_CHANGE_GUI_INIT,
                    "Programm-GUI umschalten",
                    "Das Programmfenster (groß/klein) umschalten.");

    public static final P2ShortcutKey SHORTCUT_CENTER_GUI =
            new P2ShortcutKey(ProgConfig.SHORTCUT_CENTER_GUI, ProgConfig.SHORTCUT_CENTER_INIT,
                    "Center Programm",
                    "Das Programmfenster wird auf dem Bildschirm zentriert.");

    public static final P2ShortcutKey SHORTCUT_MINIMIZE_GUI =
            new P2ShortcutKey(ProgConfig.SHORTCUT_MINIMIZE_GUI, ProgConfig.SHORTCUT_MINIMIZE_INIT,
                    "Programm-GUI minimieren",
                    "Das Programmfenster wird minimiert.");

    // Menü
    public static final P2ShortcutKey SHORTCUT_QUIT_PROGRAM =
            new P2ShortcutKey(ProgConfig.SHORTCUT_QUIT_PROGRAM, ProgConfig.SHORTCUT_QUIT_PROGRAM_INIT,
                    "Programm beenden",
                    "Das Programm wird beendet.");

    public static final P2ShortcutKey SHORTCUT_STOP_STATION =
            new P2ShortcutKey(ProgConfig.SHORTCUT_STOP_STATION, ProgConfig.SHORTCUT_STOP_STATION_INIT,
                    "Sendung stoppen",
                    "Die laufende Sendung wird gestoppt.");

    // Tabelle Sender
    public static final P2ShortcutKey SHORTCUT_PLAY_STATION =
            new P2ShortcutKey(ProgConfig.SHORTCUT_PLAY_STATION, ProgConfig.SHORTCUT_PLAY_STATION_INIT,
                    "Sender abspielen",
                    "Der markierte Sender in der Tabelle \"Sender\" wird abgespielt.");

    public static final P2ShortcutKey SHORTCUT_SAVE_STATION =
            new P2ShortcutKey(ProgConfig.SHORTCUT_SAVE_STATION, ProgConfig.SHORTCUT_SAVE_STATIION_INIT,
                    "Sender als Favorit speichern",
                    "Der markierte Sender in der Tabelle \"Sender\" wird als Favorite gespeichert.");

    // Tabelle Favoriten
    public static final P2ShortcutKey SHORTCUT_FAVOURITE_START =
            new P2ShortcutKey(ProgConfig.SHORTCUT_FAVOURITE_START, ProgConfig.SHORTCUT_FAVOURITE_START_INIT,
                    "Favoriten abspielen",
                    "Der markierte Favorite in der Tabelle \"Favoriten\" wird gestartet.");

    public static final P2ShortcutKey SHORTCUT_FAVOURITE_CHANGE =
            new P2ShortcutKey(ProgConfig.SHORTCUT_FAVOURITE_CHANGE, ProgConfig.SHORTCUT_FAVOURITE_CHANGE_INIT,
                    "Favoriten ändern",
                    "Der markierte Favorite in der Tabelle \"Favoriten\" kann geändert werden.");

    private static ObservableList<P2ShortcutKey> shortcutList = FXCollections.observableArrayList();

    public PShortCut() {
        shortcutList.add(SHORTCUT_CHANGE_GUI);
        shortcutList.add(SHORTCUT_CENTER_GUI);
        shortcutList.add(SHORTCUT_MINIMIZE_GUI);

        shortcutList.add(SHORTCUT_QUIT_PROGRAM);
        shortcutList.add(SHORTCUT_STOP_STATION);

        shortcutList.add(SHORTCUT_PLAY_STATION);
        shortcutList.add(SHORTCUT_SAVE_STATION);

        shortcutList.add(SHORTCUT_FAVOURITE_START);
        shortcutList.add(SHORTCUT_FAVOURITE_CHANGE);
    }

    public static synchronized ObservableList<P2ShortcutKey> getShortcutList() {
        return shortcutList;
    }
}
