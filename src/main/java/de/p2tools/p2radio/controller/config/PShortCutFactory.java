/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.config;

import de.p2tools.p2lib.tools.shortcut.P2ShortcutKey;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import javafx.scene.Scene;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class PShortCutFactory {

    private PShortCutFactory() {
    }

    public static void addShortCut(Scene scene) {
        P2ShortcutKey pShortcut;
        KeyCombination kc;
        Runnable rn;

        // Change GUI
        pShortcut = PShortCut.SHORTCUT_CHANGE_GUI;
        kc = KeyCodeCombination.keyCombination(pShortcut.getActShortcut());
        rn = P2RadioFactory::changeGui;
        scene.getAccelerators().put(kc, rn);

        // Center GUI
        pShortcut = PShortCut.SHORTCUT_CENTER_GUI;
        kc = KeyCodeCombination.keyCombination(pShortcut.getActShortcut());
        rn = P2RadioFactory::centerGui;
        scene.getAccelerators().put(kc, rn);

        // Minimize GUI
        pShortcut = PShortCut.SHORTCUT_MINIMIZE_GUI;
        kc = KeyCodeCombination.keyCombination(pShortcut.getActShortcut());
        rn = P2RadioFactory::minimizeGui;
        scene.getAccelerators().put(kc, rn);

        // Quit
        pShortcut = PShortCut.SHORTCUT_QUIT_PROGRAM;
        kc = KeyCodeCombination.keyCombination(pShortcut.getActShortcut());
        rn = ProgQuitFactory::quit;
        scene.getAccelerators().put(kc, rn);

        // Stop Station
        pShortcut = PShortCut.SHORTCUT_STOP_STATION;
        kc = KeyCodeCombination.keyCombination(pShortcut.getActShortcut());
        rn = StartFactory::stopStation;
        scene.getAccelerators().put(kc, rn);
    }
}
