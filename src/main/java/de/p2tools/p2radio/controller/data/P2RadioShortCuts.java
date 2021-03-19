/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.data;

import de.p2tools.p2Lib.tools.shortcut.PShortcut;
import de.p2tools.p2radio.controller.config.ProgConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class P2RadioShortCuts {
    // Menü
    public static final PShortcut SHORTCUT_QUIT_PROGRAM =
            new PShortcut(ProgConfig.SHORTCUT_QUIT_PROGRAM, ProgConfig.SHORTCUT_QUIT_PROGRAM_INIT,
                    "Programm beenden",
                    "Das Programm wird beendet.");

    // Tabelle Sender
    public static final PShortcut SHORTCUT_PLAY_STATION =
            new PShortcut(ProgConfig.SHORTCUT_PLAY_STATION, ProgConfig.SHORTCUT_PLAY_STATION_INIT,
                    "Sender abspielen",
                    "Der markierte Sender in der Tabelle \"Sender\" wird abgespielt.");
    public static final PShortcut SHORTCUT_SAVE_STATION =
            new PShortcut(ProgConfig.SHORTCUT_SAVE_STATION, ProgConfig.SHORTCUT_SAVE_STATIION_INIT,
                    "Sender als Favorit speichern",
                    "Der markierte Sender in der Tabelle \"Sender\" wird als Favorite gespeichert.");

    // Tabelle Favoriten
    public static final PShortcut SHORTCUT_FAVOURITE_START =
            new PShortcut(ProgConfig.SHORTCUT_FAVOURITE_START, ProgConfig.SHORTCUT_FAVOURITE_START_INIT,
                    "Favoriten abspielen",
                    "Der markierte Favorite in der Tabelle \"Favoriten\" wird gestartet.");
    public static final PShortcut SHORTCUT_FAVOURITE_STOP =
            new PShortcut(ProgConfig.SHORTCUT_FAVOURITE_STOP, ProgConfig.SHORTCUT_FAVOURITE_STOP_INIT,
                    "Favoriten stoppen",
                    "Der markierte Favorite in der Tabelle \"Favoriten\" wird gestoppt.");
    public static final PShortcut SHORTCUT_FAVOURITE_CHANGE =
            new PShortcut(ProgConfig.SHORTCUT_FAVOURITE_CHANGE, ProgConfig.SHORTCUT_FAVOURITE_CHANGE_INIT,
                    "Favoriten ändern",
                    "Der markierte Favorite in der Tabelle \"Favoriten\" kann geändert werden.");

    private static ObservableList<PShortcut> shortcutList = FXCollections.observableArrayList();

    public P2RadioShortCuts() {
        shortcutList.add(SHORTCUT_QUIT_PROGRAM);

        shortcutList.add(SHORTCUT_PLAY_STATION);
        shortcutList.add(SHORTCUT_SAVE_STATION);

        shortcutList.add(SHORTCUT_FAVOURITE_START);
        shortcutList.add(SHORTCUT_FAVOURITE_STOP);
        shortcutList.add(SHORTCUT_FAVOURITE_CHANGE);
    }

    public static synchronized ObservableList<PShortcut> getShortcutList() {
        return shortcutList;
    }
}
