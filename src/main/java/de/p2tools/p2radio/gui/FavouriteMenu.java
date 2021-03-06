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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
                "markierten Sender abspielen", "markierten Sender abspielen", new ProgIcons().ICON_TOOLBAR_STATION_START);
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "alle laufenden Sender stoppen", "alle laufenden Sender stoppen", new ProgIcons().ICON_TOOLBAR_STATION_STOP);
        final ToolBarButton btChange = new ToolBarButton(vBox,
                "markierte Favoriten ??ndern", "markierte Favoriten ??ndern", new ProgIcons().ICON_TOOLBAR_FAVOURITE_CHANGE);
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "markierte Favoriten l??schen", "markierte Favoriten l??schen", new ProgIcons().ICON_TOOLBAR_FAVOURITE_DEL);
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Senderinfo-Dialog anzeigen", "Senderinfo-Dialog anzeigen", new ProgIcons().ICON_TOOLBAR_STATION_INFO);

        btStart.setOnAction(a -> progData.favouriteGuiController.playStation());
        btStop.setOnAction(a -> ProgData.getInstance().startFactory.stopAll());
        btChange.setOnAction(a -> progData.favouriteGuiController.changeFavourite(true));
        btDel.setOnAction(a -> progData.favouriteGuiController.deleteFavourite(true));
        btInfo.setOnAction(a -> progData.stationInfoDialogController.toggleShowInfo());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Favoriten-Men?? anzeigen"));
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunctionWide");

        final MenuItem miFavouriteStart = new MenuItem("Sender abspielen");
        miFavouriteStart.setOnAction(a -> progData.favouriteGuiController.playStation());
        PShortcutWorker.addShortCut(miFavouriteStart, P2RadioShortCuts.SHORTCUT_FAVOURITE_START);

        final MenuItem miFavouriteStop = new MenuItem("Sender stoppen");
        miFavouriteStop.setOnAction(a -> progData.favouriteGuiController.stopStation(false));

        final MenuItem miStopAll = new MenuItem("alle laufenden Sender stoppen");
        miStopAll.setOnAction(a -> ProgData.getInstance().startFactory.stopAll());
        PShortcutWorker.addShortCut(miStopAll, P2RadioShortCuts.SHORTCUT_FAVOURITE_STOP);

        MenuItem miCopyUrl = new MenuItem("Sender-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.favouriteGuiController.copyUrl());

        final MenuItem miFavouriteOwn = new MenuItem("eigenen Sender als Favoriten anlegen");
        miFavouriteOwn.setOnAction(a -> progData.favouriteList.addFavourite(true));

        mb.getItems().addAll(miFavouriteStart, miFavouriteStop, miStopAll, miCopyUrl, miFavouriteOwn);

        // Submen?? "Favoriten"
        final MenuItem miFavouriteChange = new MenuItem("Favoriten ??ndern");
        miFavouriteChange.setOnAction(a -> progData.favouriteGuiController.changeFavourite(true));
        PShortcutWorker.addShortCut(miFavouriteChange, P2RadioShortCuts.SHORTCUT_FAVOURITE_CHANGE);

        final MenuItem miFavouriteDel = new MenuItem("Favoriten l??schen");
        miFavouriteDel.setOnAction(a -> progData.favouriteGuiController.deleteFavourite(false));

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
