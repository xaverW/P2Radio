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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SmallRadioGuiPack extends SmallRadioDialog {

    ProgData progData;
    private final SmallRadioGuiController smallRadioGuiController;


    public SmallRadioGuiPack() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE, "Radiobrowser");
        progData = ProgData.getInstance();

        ProgConfig.SYSTEM_SMALL_RADIO.setValue(true);
        smallRadioGuiController = new SmallRadioGuiController(this);
        new SmallRadioBottom(this, smallRadioGuiController);

        progData.smallRadioGuiController = smallRadioGuiController;
        init();
    }

    @Override
    public void make() {
        VBox.setVgrow(smallRadioGuiController, Priority.ALWAYS);
        getVBoxCenter().getChildren().add(smallRadioGuiController);
        super.getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                changeGui();
            }
        });
    }

    public void changeGui() {
        close();
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(false);
        Platform.runLater(() -> {
                    PGuiSize.setPos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage);
                    progData.primaryStage.setWidth(PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.setHeight(PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.show();
                }
        );
    }

    public void close() {
        progData.smallRadioGuiController = null;
        smallRadioGuiController.saveTable();
        super.close();
    }
}
