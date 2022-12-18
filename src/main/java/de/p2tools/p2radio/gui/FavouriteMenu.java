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

import de.p2tools.p2Lib.tools.shortcut.PShortcutWorker;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.P2RadioShortCuts;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class FavouriteMenu {
    final private VBox vBox;
    final private ProgData progData;
//    BooleanProperty boolInfoOn = ProgConfig.FAVOURITE_GUI_DIVIDER_ON;
//    BooleanProperty boolFilterOn = ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON;

    public FavouriteMenu(VBox vBox) {
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
                "Markierten Sender abspielen", "Markierten Sender abspielen", ProgIcons.Icons.ICON_TOOLBAR_STATION_START.getImageView());
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "Alle laufenden Sender stoppen", "Alle laufenden Sender stoppen", ProgIcons.Icons.ICON_TOOLBAR_STATION_STOP.getImageView());
        final ToolBarButton btNew = new ToolBarButton(vBox,
                "Eigenen Sender als Favoriten anlegen", "Eigenen Sender als Favoriten anlegen", ProgIcons.Icons.ICON_TOOLBAR_FAVOURITE_NEW.getImageView());
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "Markierte Favoriten löschen", "Markierte Favoriten löschen", ProgIcons.Icons.ICON_TOOLBAR_FAVOURITE_DEL.getImageView());
        final ToolBarButton btChange = new ToolBarButton(vBox,
                "Markierte Favoriten ändern", "Markierte Favoriten ändern", ProgIcons.Icons.ICON_TOOLBAR_FAVOURITE_CHANGE.getImageView());
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Senderinfo-Dialog anzeigen", "Senderinfo-Dialog anzeigen", ProgIcons.Icons.ICON_TOOLBAR_STATION_INFO.getImageView());

        btStart.setOnAction(a -> progData.favouriteGuiController.playStation());
        btStop.setOnAction(a -> progData.startFactory.stopAll());
        btChange.setOnAction(a -> FavouriteFactory.changeFavourite(true));
        btNew.setOnAction(a -> FavouriteFactory.addFavourite(true));
        btDel.setOnAction(a -> FavouriteFactory.deleteFavourite(true));
        btInfo.setOnAction(a -> progData.stationInfoDialogController.toggleShowInfo());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Favoriten-Menü anzeigen"));
        mb.setGraphic(ProgIcons.Icons.ICON_TOOLBAR_MENU.getImageView());
        mb.getStyleClass().add("btnFunctionWide");

        final boolean moreSets = progData.setDataList.size() > 1;
        if (moreSets) {
            Menu miStartWithSet = new Menu("Sender abspielen, Programm auswählen");
            for (SetData set : progData.setDataList) {
                MenuItem miStart = new MenuItem(set.getVisibleName());
                miStart.setOnAction(a -> {
                    final Optional<StationData> favourite = ProgData.getInstance().favouriteGuiController.getSel();
                    if (favourite.isPresent()) {
                        progData.startFactory.playPlayable(favourite.get(), set);
                    }
                });
                miStartWithSet.getItems().add(miStart);
            }
            mb.getItems().addAll(miStartWithSet);

        } else {
            final MenuItem miPlay = new MenuItem("Sender abspielen");
            miPlay.setOnAction(a -> progData.favouriteGuiController.playStation());
            PShortcutWorker.addShortCut(miPlay, P2RadioShortCuts.SHORTCUT_PLAY_STATION);
            mb.getItems().addAll(miPlay);
        }

//        final MenuItem miFavouriteStart = new MenuItem("Sender abspielen");
//        miFavouriteStart.setOnAction(a -> progData.favouriteGuiController.playStation());
//        PShortcutWorker.addShortCut(miFavouriteStart, P2RadioShortCuts.SHORTCUT_FAVOURITE_START);

        final MenuItem miFavouriteStop = new MenuItem("Sender stoppen");
        miFavouriteStop.setOnAction(a -> progData.favouriteGuiController.stopStation(false));

        final MenuItem miStopAll = new MenuItem("Alle laufenden Sender stoppen");
        miStopAll.setOnAction(a -> ProgData.getInstance().startFactory.stopAll());
        PShortcutWorker.addShortCut(miStopAll, P2RadioShortCuts.SHORTCUT_FAVOURITE_STOP);

        MenuItem miCopyUrl = new MenuItem("Sender-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.favouriteGuiController.copyUrl());

        final MenuItem miFavouriteOwn = new MenuItem("Eigenen Sender als Favoriten anlegen");
        miFavouriteOwn.setOnAction(a -> FavouriteFactory.addFavourite(true));

        mb.getItems().addAll(miFavouriteStop, miStopAll, miCopyUrl, miFavouriteOwn);

        // Submenü "Favoriten"
        final MenuItem miFavouriteChange = new MenuItem("Favoriten ändern");
        miFavouriteChange.setOnAction(a -> FavouriteFactory.changeFavourite(true));
        PShortcutWorker.addShortCut(miFavouriteChange, P2RadioShortCuts.SHORTCUT_FAVOURITE_CHANGE);

        final MenuItem miFavouriteDel = new MenuItem("Favoriten löschen");
        miFavouriteDel.setOnAction(a -> FavouriteFactory.deleteFavourite(false));

        mb.getItems().add(new SeparatorMenuItem());
        Menu submenuFavourite = new Menu("Favoriten");
        mb.getItems().addAll(submenuFavourite);
        submenuFavourite.getItems().addAll(miFavouriteChange, miFavouriteDel);

        mb.getItems().add(new SeparatorMenuItem());
        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.selectedProperty().bindBidirectional(ProgConfig.FAVOURITE_GUI_FILTER_DIVIDER_ON);
        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.selectedProperty().bindBidirectional(ProgConfig.FAVOURITE_GUI_DIVIDER_ON);
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
