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

package de.p2tools.p2radio.gui.startdialog;


import de.p2tools.p2lib.P2LibConst;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartPane extends VBox {
    final int picSize = 450;

    public StartPane() {
    }

    public void close() {
    }

    public void makeStart1() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20, 10, 20, 10));

        ImageView iv = new ImageView();
        Image im = getHelpScreen1();
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1)  Hier kann eine verkleinerte Ansicht" + P2LibConst.LINE_SEPARATOR +
                "des Radios eingestellt werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) Hier kann die Liste der" + P2LibConst.LINE_SEPARATOR +
                "Sender gefiltert werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) Die Blacklist kann man" + P2LibConst.LINE_SEPARATOR +
                "hier ein- und ausschalten" +

                P2LibConst.LINE_SEPARATORx2 +
                "4) Die Ansicht der Sender, Favoriten und" + P2LibConst.LINE_SEPARATOR +
                "der History wird hier umgeschaltet." +

                P2LibConst.LINE_SEPARATORx2 +
                "5) Damit können Sender gestartet" + P2LibConst.LINE_SEPARATOR +
                "und gestoppt werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "6) In dem Menü befinden sich" + P2LibConst.LINE_SEPARATOR +
                "die Programmeinstellungen.");

        hBox.getChildren().addAll(iv, text);
        getChildren().addAll(StartFactory.getTitle("Infos zur Programmoberfläche"), hBox);
    }

    public void makeStart2() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20, 10, 0, 10));

        ImageView iv = new ImageView();
        Image im = getHelpScreen2();
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1) Damit können die Favoriten" + P2LibConst.LINE_SEPARATOR +
                "gefiltert werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) In dem Menü können die Favoriten" + P2LibConst.LINE_SEPARATOR +
                "bearbeitet werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) Damit können die Favoriten" + P2LibConst.LINE_SEPARATOR +
                "gestartet, gestoppt und" + P2LibConst.LINE_SEPARATOR +
                "gelöscht werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "4) Hier kann ein eigener Sender zu" + P2LibConst.LINE_SEPARATOR +
                "den Favoriten hinzugefügt werden.");

        hBox.getChildren().addAll(iv, text);
        getChildren().addAll(StartFactory.getTitle("Infos zu den Favoriten"), hBox);
    }

    public void makeStart3() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20, 10, 0, 10));

        ImageView iv = new ImageView();
        Image im = getHelpScreen3();
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1) Damit können die Sender in" + P2LibConst.LINE_SEPARATOR +
                "der History gefiltert werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) In dem Menü kann die History" + P2LibConst.LINE_SEPARATOR +
                "bearbeitet werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) Hiermit werden die Sender" + P2LibConst.LINE_SEPARATOR +
                "gestartet, gestoppt und" + P2LibConst.LINE_SEPARATOR +
                "aus der History gelöscht");

        hBox.getChildren().addAll(iv, text);
        getChildren().addAll(StartFactory.getTitle("Infos zur History"), hBox);
    }

    private Image getHelpScreen1() {
        final String path = "/de/p2tools/p2radio/res/startdialog/p2Radio-startdialog-1.png";
        return new Image(path, picSize, picSize, true, true);
    }

    private Image getHelpScreen2() {
        final String path = "/de/p2tools/p2radio/res/startdialog/p2Radio-startdialog-2.png";
        return new javafx.scene.image.Image(path, picSize, picSize, true, true);
    }

    private Image getHelpScreen3() {
        final String path = "/de/p2tools/p2radio/res/startdialog/p2Radio-startdialog-3.png";
        return new Image(path, picSize, picSize, true, true);
    }
}
