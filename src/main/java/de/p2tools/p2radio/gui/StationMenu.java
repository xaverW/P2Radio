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
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


public class StationMenu {
    final private VBox vBox;
    final private ProgData progData;

    BooleanProperty boolFilterOn = ProgConfig.STATION_GUI_FILTER_DIVIDER_ON;
    BooleanProperty boolInfoOn = ProgConfig.STATION_GUI_DIVIDER_ON;

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
                "markierten Sender abspielen", "markierten Sender abspielen", new ProgIcons().ICON_TOOLBAR_STATION_START);
        final ToolBarButton btStop = new ToolBarButton(vBox,
                "alle laufenden Sender stoppen", "alle laufenden Sender stoppen", new ProgIcons().ICON_TOOLBAR_STATION_STOP);
        final ToolBarButton btSave = new ToolBarButton(vBox,
                "markierte Sender als Favoriten speichern", "markierte Sender als Favoriten speichern", new ProgIcons().ICON_TOOLBAR_STATION_REC);
        final ToolBarButton btInfo = new ToolBarButton(vBox,
                "Senderinfo-Dialog anzeigen", "Senderinfo-Dialog anzeigen", new ProgIcons().ICON_TOOLBAR_STATION_INFO);

        vBoxSpace = new VBox();
        vBoxSpace.setMaxHeight(10);
        vBoxSpace.setMinHeight(10);
        vBox.getChildren().add(vBoxSpace);

        btPlay.setOnAction(a -> progData.stationGuiController.playStation());
        btStop.setOnAction(a -> progData.startFactory.stopAll());
        btSave.setOnAction(a -> progData.stationGuiController.saveStation());
        btInfo.setOnAction(a -> progData.stationInfoDialogController.toggleShowInfo());
    }

    private void initStationMenu() {
        final MenuButton mb = new MenuButton("");
        mb.setTooltip(new Tooltip("Sendermen?? anzeigen"));
        mb.setGraphic(new ProgIcons().ICON_TOOLBAR_MENU);
        mb.getStyleClass().add("btnFunctionWide");

        final MenuItem miPlay = new MenuItem("Sender abspielen");
        miPlay.setOnAction(a -> progData.stationGuiController.playStation());
        PShortcutWorker.addShortCut(miPlay, P2RadioShortCuts.SHORTCUT_PLAY_STATION);

        final MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> progData.stationGuiController.stopStation(false));

        final MenuItem miStopAll = new MenuItem("alle laufenden Sender stoppen");
        miStopAll.setOnAction(a -> ProgData.getInstance().startFactory.stopAll());
        PShortcutWorker.addShortCut(miStopAll, P2RadioShortCuts.SHORTCUT_FAVOURITE_STOP);

        final MenuItem miSave = new MenuItem("Sender als Favoriten speichern");
        miSave.setOnAction(e -> progData.stationGuiController.saveStation());
        PShortcutWorker.addShortCut(miSave, P2RadioShortCuts.SHORTCUT_SAVE_STATION);

        mb.getItems().addAll(miPlay, miStop, miStopAll, miSave);

        final CheckMenuItem miShowFilter = new CheckMenuItem("Filter anzeigen");
        miShowFilter.selectedProperty().bindBidirectional(boolFilterOn);
        final CheckMenuItem miShowInfo = new CheckMenuItem("Infos anzeigen");
        miShowInfo.selectedProperty().bindBidirectional(boolInfoOn);

        mb.getItems().add(new SeparatorMenuItem());
        mb.getItems().addAll(miShowFilter, miShowInfo);

        vBox.getChildren().add(mb);
    }
}
