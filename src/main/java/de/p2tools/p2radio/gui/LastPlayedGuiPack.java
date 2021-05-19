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

import de.p2tools.p2radio.controller.config.ProgData;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class LastPlayedGuiPack {

    ProgData progData;
    private final HBox hBox = new HBox();
    private final LastPlayedGuiController lastPlayedGuiController;

    public LastPlayedGuiPack() {
        progData = ProgData.getInstance();
        lastPlayedGuiController = new LastPlayedGuiController();
        progData.lastPlayedGuiController = lastPlayedGuiController;
    }

    public SplitPane pack() {
        final MenuController menuController = new MenuController(MenuController.StartupMode.LAST_PLAYED);
        menuController.setId("last-played-menu-pane");

        hBox.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        hBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(lastPlayedGuiController, Priority.ALWAYS);
        hBox.getChildren().addAll(lastPlayedGuiController, menuController);
        return new SplitPane(hBox);
    }
}