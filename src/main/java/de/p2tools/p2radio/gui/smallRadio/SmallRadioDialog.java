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
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;


public class SmallRadioDialog extends PDialogOnly {
    private double xOffset = 0;
    private double yOffset = 0;
    private double xSize = 0;
    private double ySize = 0;
    private double xPos = 0;
    private double yPos = 0;

    SmallRadioDialog() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE,
                "Radio", false, false, true);
        init(false);

        getStage().getScene().setOnMousePressed(event -> {
            xOffset = event.getSceneX(); //Pos im Fenster von links
            yOffset = event.getSceneY(); //Pos im Fenster von oben
            xSize = getStage().getWidth(); //Fensterbreite
            ySize = getStage().getHeight(); //Fensterhöhe
            xPos = event.getScreenX(); //Pos vom Bildschirmrand
            yPos = event.getScreenY(); //Pos vom Bildschirmrand

            if (yOffset < 20 && xOffset < 20) {
                //oben links
                getStage().getScene().setCursor(Cursor.NW_RESIZE);
            } else if (yOffset < 20 && xOffset > xSize - 20) {
                //oben rechts
                getStage().getScene().setCursor(Cursor.NE_RESIZE);
            } else if (yOffset > ySize - 20 && xOffset > xSize - 20) {
                //unten rechts
                getStage().getScene().setCursor(Cursor.SE_RESIZE);
            } else if (yOffset > ySize - 20 && xOffset < 20) {
                //unten links
                getStage().getScene().setCursor(Cursor.SW_RESIZE);
            } else if (yOffset < 20) {
                //oben
                getStage().getScene().setCursor(Cursor.N_RESIZE);
            } else if (yOffset > ySize - 20) {
                //unten
                getStage().getScene().setCursor(Cursor.S_RESIZE);
            } else if (xOffset < 20) {
                //links
                getStage().getScene().setCursor(Cursor.E_RESIZE);
            } else if (xOffset > xSize - 20) {
                //rechts
                getStage().getScene().setCursor(Cursor.W_RESIZE);
            } else {
                //dann nur schieben
                getStage().getScene().setCursor(Cursor.OPEN_HAND);
            }
        });
        getStage().getScene().setOnMouseReleased(event -> {
            getStage().getScene().setCursor(Cursor.DEFAULT);
        });
        getStage().getScene().setOnMouseDragged(event -> {
            if (yOffset < 20 && xOffset < 20) {
                //oben links
                moveUp(event);
                moveLeft(event);
            } else if (yOffset < 20 && xOffset > xSize - 20) {
                //oben rechts
                moveUp(event);
                moveRight(event);
            } else if (yOffset > ySize - 20 && xOffset > xSize - 20) {
                //unten rechts
                moveDown(event);
                moveRight(event);
            } else if (yOffset > ySize - 20 && xOffset < 20) {
                //unten links
                moveDown(event);
                moveLeft(event);
            } else if (yOffset < 20) {
                //oben
                moveUp(event);
            } else if (yOffset > ySize - 20) {
                //unten
                moveDown(event);
            } else if (xOffset < 20) {
                //links
                moveLeft(event);
            } else if (xOffset > xSize - 20) {
                //rechts
                moveRight(event);
            } else {
                //dann nur schieben
                getStage().setX(event.getScreenX() - xOffset);
                getStage().setY(event.getScreenY() - yOffset);
            }
        });
        getStage().initStyle(StageStyle.TRANSPARENT);
        getVBoxCompleteDialog().getStyleClass().add("smallRadio");

        super.showDialog();
    }

    private void moveRight(MouseEvent event) {
        double newX = xSize + event.getScreenX() - xPos;
        getStage().setWidth(newX);
    }

    private void moveLeft(MouseEvent event) {
        double newX = xSize + xPos - event.getScreenX();
        double moveX = xPos - event.getScreenX();
        System.out.println(moveX);
        getStage().setWidth(newX);
        getStage().setX(xPos - moveX);
    }

    private void moveUp(MouseEvent event) {
        double newY = ySize + yPos - event.getScreenY();
        double moveY = yPos - event.getScreenY();
        System.out.println(moveY);
        getStage().setHeight(newY);
        getStage().setY(yPos - moveY);
    }

    private void moveDown(MouseEvent event) {
        double newY = ySize + event.getScreenY() - yPos;
        getStage().setHeight(newY);
    }
}
