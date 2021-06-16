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
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFactory;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationTools;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class LastPlayedMenu {
    final private VBox vBox;
    final private ProgData progData;
    BooleanProperty boolFilterOn = ProgConfig.LAST_PLAYED_GUI_FILTER_DIVIDER_ON;
    BooleanProperty boolInfoOn = ProgConfig.LAST_PLAYED_GUI_DIVIDER_ON;

    public LastPlayedMenu(VBox vBox) {
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
        final ToolBarButton btDel = new ToolBarButton(vBox,
                "markierte Sender aus der History löschen", "markierte Sender aus der History löschen", new ProgIcons().ICON_TOOLBAR_FAVOURITE_DEL);
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Senderinfo-Dialog anzeigen", "Senderinfo-Dialog anzeigen", new ProgIcons().ICON_TOOLBAR_STATION_INFO);

        btStart.setOnAction(a -> progData.lastPlayedGuiController.playStation());
        btStop.setOnAction(a -> ProgData.getInstance().startFactory.stopAll());
        btDel.setOnAction(a -> LastPlayedFactory.deleteHistory(true));
        btInfo.setOnAction(a -> progData.stationInfoDialogController.toggleShowInfo());
    }

    private void initMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("History-Menü anzeigen"));
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunctionWide");

        final MenuItem miFavouriteStart = new MenuItem("Sender abspielen");
        miFavouriteStart.setOnAction(a -> progData.lastPlayedGuiController.playStation());
        PShortcutWorker.addShortCut(miFavouriteStart, P2RadioShortCuts.SHORTCUT_FAVOURITE_START);

        final MenuItem miFavouriteStop = new MenuItem("Sender stoppen");
        miFavouriteStop.setOnAction(a -> progData.lastPlayedGuiController.stopStation(false));

        final MenuItem miStopAll = new MenuItem("alle laufenden Sender stoppen");
        miStopAll.setOnAction(a -> ProgData.getInstance().startFactory.stopAll());
        PShortcutWorker.addShortCut(miStopAll, P2RadioShortCuts.SHORTCUT_FAVOURITE_STOP);

        MenuItem miCopyUrl = new MenuItem("Sender-URL kopieren");
        miCopyUrl.setOnAction(a -> progData.lastPlayedGuiController.copyUrl());

        mb.getItems().addAll(miFavouriteStart, miFavouriteStop, miStopAll, miCopyUrl);

        // Submenü
        final MenuItem miLastPlayedDel = new MenuItem("Sender aus History löschen");
        miLastPlayedDel.setOnAction(a -> LastPlayedFactory.deleteHistory(false));

        final MenuItem miLastPlayedDelSel = new MenuItem("alle markierten Sender aus History löschen");
        miLastPlayedDelSel.setOnAction(a -> LastPlayedFactory.deleteHistory(true));

        final MenuItem miLastPlayedDelAll = new MenuItem("gesamte History löschen");
        miLastPlayedDelAll.setOnAction(a -> LastPlayedFactory.deleteCompleteHistory());

        MenuItem miAddFavourite = new MenuItem("Sender als Favoriten speichern");
        miAddFavourite.setOnAction(a -> {
            final Optional<LastPlayed> lastPlayed = ProgData.getInstance().lastPlayedGuiController.getSel();
            if (lastPlayed.isPresent()) {
                String stationUrl = lastPlayed.get().getUrl();
                Station station = progData.stationList.getSenderByUrl(stationUrl);
                if (station != null) {
                    StationTools.saveStation(station);
                }
            }
        });
        mb.getItems().addAll(miAddFavourite);

        mb.getItems().add(new SeparatorMenuItem());
        Menu submenu = new Menu("History");
        mb.getItems().addAll(submenu);
        submenu.getItems().addAll(miLastPlayedDel, miLastPlayedDelSel, miLastPlayedDelAll);

        mb.getItems().add(new SeparatorMenuItem());
        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.selectedProperty().bindBidirectional(boolFilterOn);
        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.selectedProperty().bindBidirectional(boolInfoOn);
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
