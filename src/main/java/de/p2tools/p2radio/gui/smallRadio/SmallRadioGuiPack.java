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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.application.Platform;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SmallRadioGuiPack extends SmallRadioDialog {

    ProgData progData;
    private final SmallRadioGuiController smallRadioGuiController;
    private final SmallRadioBottom smallRadioBottom;


    public SmallRadioGuiPack() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE, "Radiobrowser");

        progData = ProgData.getInstance();
        smallRadioGuiController = new SmallRadioGuiController();
        progData.smallRadioGuiController = smallRadioGuiController;
        smallRadioBottom = new SmallRadioBottom(this, smallRadioGuiController);
        init();
    }

    @Override
    public void make() {
        pack();
    }

    public void close() {
        progData.smallRadioGuiController = null;
        smallRadioGuiController.saveTable();
        Platform.runLater(() -> {
                    progData.primaryStage.show();
                    progData.primaryStage.getScene().getWindow().setWidth(PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.getScene().getWindow().setHeight(PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
                }
        );
        super.close();
    }

    private void pack() {
        VBox.setVgrow(smallRadioGuiController, Priority.ALWAYS);
        getVBoxCenter().getChildren().add(smallRadioGuiController);
    }
}
