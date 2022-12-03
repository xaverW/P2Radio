/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2Lib.dialogs.dialog.PDialogOnly;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.stage.StageStyle;


public class SmallRadioDialog extends PDialogOnly {
    private double xOffset = 0;
    private double yOffset = 0;

    SmallRadioDialog() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE,
                "Radio", false, false, true);
        init(false);

        getStage().getScene().setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        getStage().getScene().setOnMouseDragged(event -> {
            getStage().setX(event.getScreenX() - xOffset);
            getStage().setY(event.getScreenY() - yOffset);
        });
        getStage().initStyle(StageStyle.TRANSPARENT);
        getVBoxCompleteDialog().getStyleClass().add("smallRadio");

        super.showDialog();
    }
}
