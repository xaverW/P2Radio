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
import de.p2tools.p2radio.controller.config.PShortCut;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


public class StationMenu {
    final private VBox vBox;
    final private ProgData progData;

    public StationMenu(VBox vBox) {
        this.vBox = vBox;
        progData = ProgData.getInstance();
    }

    public void init() {
        vBox.getChildren().clear();
        initStationMenu();
        initButton();
    }

    private void initButton() {
        VBox vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(0);
        vBoxSpace.setMinHeight(0);
        vBox.getChildren().add(vBoxSpace);

        final ToolBarButton btPlay = new ToolBarButton(vBox,
                "Markierten Sender abspielen", "Markierten Sender abspielen", ProgIcons.ICON_TOOLBAR_STATION_START.getImageView());
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "Alle laufenden Sender stoppen", "Alle laufenden Sender stoppen", ProgIcons.ICON_TOOLBAR_STATION_STOP.getImageView());
        final ToolBarButton btFavourite = new ToolBarButton(vBox,
                "Markierte Sender als Favoriten speichern", "Markierte Sender als Favoriten speichern", ProgIcons.ICON_TOOLBAR_STATION_REC.getImageView());
        final ToolBarButton btRandom = new ToolBarButton(vBox,
                "Einen Sender per Zufall starten", "Einen Sender per Zufall starten", ProgIcons.ICON_TOOLBAR_STATION_RANDOM.getImageView());
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Senderinfo-Dialog anzeigen", "Senderinfo-Dialog anzeigen", ProgIcons.ICON_TOOLBAR_STATION_INFO.getImageView());

        vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(10);
        vBoxSpace.setMinHeight(10);
        vBox.getChildren().add(vBoxSpace);

        btPlay.setOnAction(a -> progData.stationGuiPack.getStationGuiController().playStation());
        btStop.setOnAction(a -> StartFactory.stopRunningStation());
        btFavourite.setOnAction(a -> FavouriteFactory.favouriteStationList());
        btRandom.setOnAction(a -> progData.stationGuiPack.getStationGuiController().playRandomStation());
        btInfo.setOnAction(a -> progData.stationInfoDialogController.toggleShowInfo());
    }

    private void initStationMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Sendermenü anzeigen"));
        mb.setGraphic(ProgIcons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().addAll("btnFunction", "btnFunc-0");

        final boolean moreSets = progData.setDataList.size() > 1;
        if (moreSets) {
            Menu miStartWithSet = new Menu("Sender abspielen, Programm auswählen");
            for (SetData set : progData.setDataList) {
                MenuItem miStart = new MenuItem(set.getVisibleName());
                miStart.setOnAction(a -> progData.stationGuiPack.getStationGuiController().playStationWithSet(set));
                miStartWithSet.getItems().add(miStart);
            }
            mb.getItems().addAll(miStartWithSet);

        } else {
            final MenuItem miPlay = new MenuItem("Sender abspielen");
            miPlay.setOnAction(a -> progData.stationGuiPack.getStationGuiController().playStation());
            P2ShortcutWorker.addShortCut(miPlay, PShortCut.SHORTCUT_PLAY_STATION);
            mb.getItems().addAll(miPlay);
        }


        final MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> StartFactory.stopRunningStation());
        P2ShortcutWorker.addShortCut(miStop, PShortCut.SHORTCUT_STOP_STATION);

        final MenuItem miSave = new MenuItem("Sender als Favoriten speichern");
        miSave.setOnAction(e -> FavouriteFactory.favouriteStationList());
        P2ShortcutWorker.addShortCut(miSave, PShortCut.SHORTCUT_SAVE_STATION);

        final MenuItem miAutoStart = new MenuItem("Sender als AutoStart auswählen");
        miAutoStart.setOnAction(e -> AutoStartFactory.setStationAutoStart());

        mb.getItems().addAll(miStop, miSave, miAutoStart);

        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.disableProperty().bind(ProgConfig.STATION__FILTER_IS_RIP);
        miShowFilter.selectedProperty().bindBidirectional(ProgConfig.STATION__FILTER_IS_SHOWING);

        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.disableProperty().bind(ProgConfig.STATION__INFO_PANE_IS_RIP);
        miShowInfo.selectedProperty().bindBidirectional(ProgConfig.STATION__INFO_IS_SHOWING);

        mb.getItems().add(new SeparatorMenuItem());
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
