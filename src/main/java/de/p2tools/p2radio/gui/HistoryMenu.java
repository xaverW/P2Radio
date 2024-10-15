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

package de.p2tools.p2radio.gui;

import de.p2tools.p2lib.tools.shortcut.P2ShortcutWorker;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.P2RadioShortCuts;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.history.HistoryFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class HistoryMenu {
    final private VBox vBox;
    final private ProgData progData;
    BooleanProperty boolFilterOn = ProgConfig.HISTORY__FILTER_IS_SHOWING;
    BooleanProperty boolInfoOn = ProgConfig.HISTORY__INFO_IS_SHOWING;

    public HistoryMenu(VBox vBox) {
        this.vBox = vBox;
        progData = ProgData.getInstance();
    }

    public void init() {
        vBox.getChildren().clear();
        initMenu();
        initButton();
    }

    private void initButton() {
        VBox vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(0);
        vBoxSpace.setMinHeight(0);
        vBox.getChildren().add(vBoxSpace);

        final ToolBarButton btStart = new ToolBarButton(vBox,
                "markierten Sender abspielen", "markierten Sender abspielen", ProgIcons.ICON_TOOLBAR_STATION_START.getImageView());
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "alle laufenden Sender stoppen", "alle laufenden Sender stoppen", ProgIcons.ICON_TOOLBAR_STATION_STOP.getImageView());
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "markierte Sender aus der History löschen", "markierte Sender aus der History löschen", ProgIcons.ICON_TOOLBAR_FAVOURITE_DEL.getImageView());
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Senderinfo-Dialog anzeigen", "Senderinfo-Dialog anzeigen", ProgIcons.ICON_TOOLBAR_STATION_INFO.getImageView());

        btStart.setOnAction(a -> progData.historyGuiPack.getHistoryGuiController().playStation());
        btStop.setOnAction(a -> StartFactory.stopAll());
        btDel.setOnAction(a -> HistoryFactory.deleteHistory(true));
        btInfo.setOnAction(a -> progData.stationInfoDialogController.toggleShowInfo());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("History-Menü anzeigen"));
        mb.setGraphic(ProgIcons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().addAll("btnFunction", "btnFunc-0");

        final boolean moreSets = progData.setDataList.size() > 1;
        if (moreSets) {
            Menu miStartWithSet = new Menu("Sender abspielen, Programm auswählen");
            for (SetData set : progData.setDataList) {
                MenuItem miStart = new MenuItem(set.getVisibleName());
                miStart.setOnAction(a -> {
                    final Optional<StationData> stationData = ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSel();
                    if (stationData.isPresent()) {
                        StartFactory.playPlayable(stationData.get(), set);
                    }
                });
                miStartWithSet.getItems().add(miStart);
            }
            mb.getItems().addAll(miStartWithSet);

        } else {
            final MenuItem miPlay = new MenuItem("Sender abspielen");
            miPlay.setOnAction(a -> progData.historyGuiPack.getHistoryGuiController().playStation());
            P2ShortcutWorker.addShortCut(miPlay, P2RadioShortCuts.SHORTCUT_PLAY_STATION);
            mb.getItems().addAll(miPlay);
        }

        final MenuItem miFavouriteStop = new MenuItem("Sender stoppen");
        miFavouriteStop.setOnAction(a -> progData.historyGuiPack.getHistoryGuiController().stopStation(false));

        final MenuItem miStopAll = new MenuItem("Alle laufenden Sender stoppen");
        miStopAll.setOnAction(a -> StartFactory.stopAll());
        P2ShortcutWorker.addShortCut(miStopAll, P2RadioShortCuts.SHORTCUT_FAVOURITE_STOP);

        MenuItem miCopyUrl = new MenuItem("Sender-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.historyGuiPack.getHistoryGuiController().copyUrl());

        mb.getItems().addAll(miFavouriteStop, miStopAll, miCopyUrl);

        // Submenü
        final MenuItem miHistoryDel = new MenuItem("Sender aus History löschen");
        miHistoryDel.setOnAction(a -> HistoryFactory.deleteHistory(false));

        final MenuItem miHistoryDelSel = new MenuItem("Ale markierten Sender aus History löschen");
        miHistoryDelSel.setOnAction(a -> HistoryFactory.deleteHistory(true));

        final MenuItem miHistoryDelAll = new MenuItem("Gesamte History löschen");
        miHistoryDelAll.setOnAction(a -> HistoryFactory.deleteCompleteHistory());

        MenuItem miAddFavourite = new MenuItem("Sender als Favoriten speichern");
        miAddFavourite.setOnAction(a -> {
            final Optional<StationData> data = ProgData.getInstance().historyGuiPack.getHistoryGuiController().getSel();
            if (data.isPresent()) {
                String stationUrl = data.get().getStationUrl();
                StationData stationData = progData.stationList.getSenderByUrl(stationUrl);
                if (stationData != null) {
                    FavouriteFactory.favouriteStation(stationData);
                }
            }
        });

        final MenuItem miAutoStart = new MenuItem("Sender als AutoStart auswählen");
        miAutoStart.setOnAction(e -> AutoStartFactory.setHistoryAutoStart());

        mb.getItems().addAll(miAddFavourite, miAutoStart);


        mb.getItems().add(new SeparatorMenuItem());
        Menu submenu = new Menu("History");
        mb.getItems().addAll(submenu);
        submenu.getItems().addAll(miHistoryDel, miHistoryDelSel, miHistoryDelAll);

        mb.getItems().add(new SeparatorMenuItem());
        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.disableProperty().bind(ProgConfig.HISTORY__FILTER_IS_RIP);
        miShowFilter.selectedProperty().bindBidirectional(boolFilterOn);

        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.disableProperty().bind(ProgConfig.HISTORY__INFO_PANE_IS_RIP);
        miShowInfo.selectedProperty().bindBidirectional(boolInfoOn);
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
