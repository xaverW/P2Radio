/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SmallRadioFactory {

    public static final String LIST_STATION = "s";
    public static final String LIST_FAVOURITE = "f";
    public static final String LIST_HISTORY = "h";

    private static final int MOVE_SIZE = 20;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private static double xSize = 0;
    private static double ySize = 0;
    private static double xPos = 0;
    private static double yPos = 0;

    private SmallRadioFactory() {
    }

    public static void addBorderListener(Stage stage) {
        stage.getScene().setOnMousePressed(event -> {
            xOffset = event.getSceneX(); //Pos im Fenster von links
            yOffset = event.getSceneY(); //Pos im Fenster von oben
            xSize = stage.getWidth(); //Fensterbreite
            ySize = stage.getHeight(); //Fensterhöhe
            xPos = event.getScreenX(); //Pos vom Bildschirmrand
            yPos = event.getScreenY(); //Pos vom Bildschirmrand

            if (isUp() && isLeft()) {
                //oben links
                stage.getScene().setCursor(Cursor.NW_RESIZE);
            } else if (isUp() && isRight()) {
                //oben rechts
                stage.getScene().setCursor(Cursor.NE_RESIZE);
            } else if (isDown() && isRight()) {
                //unten rechts
                stage.getScene().setCursor(Cursor.SE_RESIZE);
            } else if (isDown() && isLeft()) {
                //unten links
                stage.getScene().setCursor(Cursor.SW_RESIZE);
            } else if (isUp()) {
                //oben -> schieben
                stage.getScene().setCursor(Cursor.OPEN_HAND);
            } else if (isDown()) {
                //unten
                stage.getScene().setCursor(Cursor.S_RESIZE);
            } else if (isLeft()) {
                //links
                stage.getScene().setCursor(Cursor.E_RESIZE);
            } else if (isRight()) {
                //rechts
                stage.getScene().setCursor(Cursor.W_RESIZE);
            } else {
                //dann nur schieben
                stage.getScene().setCursor(Cursor.OPEN_HAND);
            }
        });
        stage.getScene().setOnMouseReleased(event -> {
            stage.getScene().setCursor(Cursor.DEFAULT);
        });
        stage.getScene().setOnMouseDragged(event -> {
            if (isUp() && isLeft()) {
                //oben links
                moveUp(stage, event);
                moveLeft(stage, event);
            } else if (isUp() && isRight()) {
                //oben rechts
                moveUp(stage, event);
                moveRight(stage, event);
            } else if (isDown() && isRight()) {
                //unten rechts
                moveDown(stage, event);
                moveRight(stage, event);
            } else if (isDown() && isLeft()) {
                //unten links
                moveDown(stage, event);
                moveLeft(stage, event);
            } else if (isUp()) {
                //oben -> schieben
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            } else if (isDown()) {
                //unten
                moveDown(stage, event);
            } else if (isLeft()) {
                //links
                moveLeft(stage, event);
            } else if (isRight()) {
                //rechts
                moveRight(stage, event);
            } else {
                //dann nur schieben
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    private static boolean isUp() {
        return yOffset < MOVE_SIZE;
    }

    private static boolean isDown() {
        return yOffset > ySize - MOVE_SIZE;
    }

    private static boolean isLeft() {
        return xOffset < MOVE_SIZE;
    }

    private static boolean isRight() {
        return xOffset > xSize - MOVE_SIZE;
    }

    private static void moveRight(Stage stage, MouseEvent event) {
        double newX = xSize + event.getScreenX() - xPos;
        stage.setWidth(newX);
    }

    private static void moveLeft(Stage stage, MouseEvent event) {
        double newX = xSize + xPos - event.getScreenX();
        double moveX = xPos - event.getScreenX();
        stage.setWidth(newX);
        stage.setX(xPos - moveX);
    }

    private static void moveUp(Stage stage, MouseEvent event) {
        double newY = ySize + yPos - event.getScreenY();
        double moveY = yPos - event.getScreenY();
        stage.setHeight(newY);
        stage.setY(yPos - moveY);
    }

    private static void moveDown(Stage stage, MouseEvent event) {
        double newY = ySize + event.getScreenY() - yPos;
        stage.setHeight(newY);
    }
}
