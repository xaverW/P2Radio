/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.stationload;

import de.p2tools.p2radio.controller.config.ProgData;

public class PMaskerFactory {

    private PMaskerFactory() {
    }

    public static void setMaskerButtonVisible(ProgData progData, boolean buttonVisible) {
        progData.maskerPane.setButtonVisible(buttonVisible);
        if (progData.smallRadioGuiController != null) {
            progData.smallRadioGuiController.getMaskerPane().setButtonVisible(buttonVisible);
        }
    }

    public static void setMaskerVisible(ProgData progData, boolean vis) {
        progData.maskerPane.setMaskerVisible(vis, vis, vis);
        if (progData.smallRadioGuiController != null) {
            progData.smallRadioGuiController.getMaskerPane().setMaskerVisible(vis, vis, vis);
        }
    }

    public static void setMaskerVisible(ProgData progData, boolean maskerVisible, boolean buttonVisible) {
        progData.maskerPane.setMaskerVisible(maskerVisible, buttonVisible, buttonVisible);
        if (progData.smallRadioGuiController != null) {
            progData.smallRadioGuiController.getMaskerPane().setMaskerVisible(maskerVisible, buttonVisible, buttonVisible);
        }
    }

    public static void setMaskerProgress(ProgData progData, double progress, String text) {
        progData.maskerPane.setMaskerProgress(progress, text);
        if (progData.smallRadioGuiController != null) {
            progData.smallRadioGuiController.getMaskerPane().setMaskerProgress(progress, text);
        }
    }
}
