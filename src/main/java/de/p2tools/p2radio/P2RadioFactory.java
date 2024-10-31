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


package de.p2tools.p2radio;

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;

public class P2RadioFactory {
    private P2RadioFactory() {
    }

    public static void changeGui() {
        if (ProgConfig.SYSTEM_SMALL_RADIO.getValue() && ProgData.getInstance().smallRadioGuiController != null) {
            ProgData.getInstance().smallRadioGuiController.close();
        } else {
            ProgData.getInstance().p2RadioController.selPanelSmallRadio();
        }
    }

    public static void centerGui() {
        ProgData.getInstance().primaryStage.centerOnScreen();
    }

    public static void setLastHistoryUrl() {
        ProgData.getInstance().stationGuiPack.getStationGuiController().selLastHistoryUrl();
        ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().selLastHistoryUrl();
        ProgData.getInstance().historyGuiPack.getHistoryGuiController().selLastHistoryUrl();

        if (ProgConfig.SYSTEM_SMALL_RADIO.getValue() && ProgData.getInstance().smallRadioGuiController != null) {
            ProgData.getInstance().smallRadioGuiController.selLastHistoryUrl();
        }

//        switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
//            case 0:
//            default:
//                ProgData.getInstance().stationGuiPack.getStationGuiController().selLastHistoryUrl();
//                break;
//            case 1:
//                ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().selLastHistoryUrl();
//                break;
//            case 2:
//                ProgData.getInstance().historyGuiPack.getHistoryGuiController().selLastHistoryUrl();
//        }
    }
}
