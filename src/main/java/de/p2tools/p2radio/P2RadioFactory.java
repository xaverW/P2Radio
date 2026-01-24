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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2WindowIcon;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.scene.control.TableView;

import java.util.Optional;

public class P2RadioFactory {
    private P2RadioFactory() {
    }

    public static String getOwnIconPath() {
        if (ProgConfig.SYSTEM_USE_OWN_PROGRAM_ICON.getValue()) {
            return ProgConfig.SYSTEM_PROGRAM_ICON_PATH.getValueSafe();
        } else {
            return "";
        }
    }

    public static void setProgramIcon() {
        if (ProgConfig.SYSTEM_USE_OWN_PROGRAM_ICON.getValue()) {
            P2WindowIcon.setStageIcon(ProgConfig.SYSTEM_PROGRAM_ICON_PATH.getValueSafe());
        } else {
            P2WindowIcon.setStageIcon("");
        }
    }

    public static void changeGui() {
        if (ProgConfig.SYSTEM_SMALL_RADIO.getValue() && ProgData.getInstance().smallRadioGuiController != null) {
            ProgData.getInstance().smallRadioGuiController.hide();
        } else {
            selPanelSmallRadio();
        }
    }

    public static void selPanelSmallRadio() {
        if (ProgData.getInstance().maskerPane.isVisible()) {
            return;
        }

        ProgData.STATION_TAB_ON.setValue(Boolean.FALSE);
        ProgData.FAVOURITE_TAB_ON.setValue(Boolean.FALSE);
        ProgData.HISTORY_TAB_ON.setValue(Boolean.FALSE);

        ProgConfig.SYSTEM_SMALL_RADIO.set(true);
    }


    public static void centerGui() {
        ProgData.getInstance().primaryStage.centerOnScreen();
    }

    public static void minimizeGui() {
        ProgData.getInstance().primaryStage.setIconified(true);
        P2DialogExtra.getDialogList().forEach(p2Dialog -> p2Dialog.getStage().setIconified(true));
    }

    public static void setLastHistoryUrl() {
        if (ProgData.getInstance().p2RadioController != null) {
            ProgData.getInstance().stationGuiPack.getStationGuiController().selLastHistory();
            ProgData.getInstance().favouriteGuiPack.getFavouriteGuiController().selLastHistory();
            ProgData.getInstance().historyGuiPack.getHistoryGuiController().selLastHistory();
        }

        if (ProgData.getInstance().smallRadioGuiController != null) {
            ProgData.getInstance().smallRadioGuiController.selLastHistory();
        }
    }

    public static void selLastHistory(TableView<StationData> tableView) {
        final String url = ProgConfig.SYSTEM_HISTORY.getValue();
        Optional<StationData> optional = tableView.getItems().stream().
                filter(station -> station.getStationUrl().equals(url)).findFirst();
        if (optional.isPresent()) {
            tableView.getSelectionModel().clearSelection();
            tableView.getSelectionModel().select(optional.get());
            int sel = tableView.getSelectionModel().getSelectedIndex();
            tableView.scrollTo(sel);
        }
    }

    public static void loadAutoStart() {
        final ProgData progData = ProgData.getInstance();

        switch (ProgConfig.SYSTEM_AUTO_START.get()) {
            case AutoStartFactory.AUTOSTART_LAST_PLAYED:
                if (progData.stationLastPlayed.isAuto()) {
                    StationData stationData = progData.stationList.getStationByUrl(progData.stationLastPlayed.getStationUrl());
                    if (stationData != null) {
                        StartFactory.startStation(stationData);
                    } else {
                        P2Alert.showErrorAlert(P2LibConst.primaryStage,
                                "Autostart",
                                "Der Sender für den Autostart ist nicht mehr in der Senderliste");
                    }
                }
                break;

            case AutoStartFactory.AUTOSTART_AUTO:
                if (progData.stationAutoStart.isAuto()) {
                    StationData stationData = progData.stationList.getStationByUrl(progData.stationAutoStart.getStationUrl());
                    if (stationData != null) {
                        StartFactory.startStation(stationData);
                    } else {
                        P2Alert.showErrorAlert(P2LibConst.primaryStage,
                                "Autostart",
                                "Der Sender für den Autostart ist nicht mehr in der Senderliste");
                    }
                }
                break;

            case AutoStartFactory.AUTOSTART_LIST_STATION:
            case AutoStartFactory.AUTOSTART_LIST_FAVOURITE:
            case AutoStartFactory.AUTOSTART_LIST_HISTORY:
            case AutoStartFactory.AUTOSTART_LIST_OWN:
                StationData stationAuto = AutoStartFactory.getRandomStation();
                if (stationAuto == null) {
                    P2Alert.showErrorAlert(P2LibConst.primaryStage,
                            "Autostart",
                            "Der Sender für den Autostart ist nicht mehr in der Senderliste");
                    break;
                }

                StationData stationData = progData.stationList.getStationByUrl(stationAuto.getStationUrl());
                if (stationData == null) {
                    P2Alert.showErrorAlert(P2LibConst.primaryStage,
                            "Autostart",
                            "Der Sender für den Autostart ist nicht mehr in der Senderliste");
                    break;
                }

                StartFactory.startStation(stationData);
                break;

            default:
        }

        setLastHistoryUrl();
    }
}
