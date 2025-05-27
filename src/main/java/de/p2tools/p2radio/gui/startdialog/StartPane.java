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
import de.p2tools.p2lib.guitools.P2GuiTools;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartPane {
    private final Stage stage;
    final int dist = 10;

    public StartPane(Stage stage) {
        this.stage = stage;
    }

    public void close() {
    }

    public TitledPane makeStart1() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getHelpScreen1();
        iv.setSmooth(true);
        iv.setImage(im);

        VBox vBox = new VBox();
        vBox.getChildren().add(new Label("1)  Hier kann eine verkleinerte Ansicht" + P2LibConst.LINE_SEPARATOR +
                "des Radios eingestellt werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("2) Hier kann die Liste der" + P2LibConst.LINE_SEPARATOR +
                "Sender gefiltert werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("3) Die Blacklist kann man" + P2LibConst.LINE_SEPARATOR +
                "hier ein- und ausschalten"));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("4) Die Ansicht der Sender, Favoriten und" + P2LibConst.LINE_SEPARATOR +
                "der History wird hier umgeschaltet."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("5) In dem Menü befinden sich" + P2LibConst.LINE_SEPARATOR +
                "die Programmeinstellungen."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("6) Mit dem Pluszeichen können" + P2LibConst.LINE_SEPARATOR +
                "Spalten in der Tabelle" + P2LibConst.LINE_SEPARATOR +
                "ein- und ausgeblendet werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("7) Damit können Sender gestartet" + P2LibConst.LINE_SEPARATOR +
                "und gestoppt werden."));

        hBox.getChildren().addAll(iv, vBox);
        return new TitledPane("Infos zur Programmoberfläche", hBox);
    }

    public TitledPane makeStart2() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getHelpScreen2();
        iv.setSmooth(true);
        iv.setImage(im);
        hBox.getChildren().addAll(iv);

        VBox vBox = new VBox();
        vBox.getChildren().add(new Label("1) Damit können die Favoriten" + P2LibConst.LINE_SEPARATOR +
                "gefiltert werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("2) In dem Menü können die Favoriten" + P2LibConst.LINE_SEPARATOR +
                "bearbeitet werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("3) Damit werden die Favoriten" + P2LibConst.LINE_SEPARATOR +
                "gestartet und gestoppt"));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("4) Damit kann ein eigener Sender zu" + P2LibConst.LINE_SEPARATOR +
                "den Favoriten hinzugefügt werden."));

        hBox.getChildren().add(vBox);

        return new TitledPane("Infos zu den Favoriten", hBox);
    }

    public TitledPane makeStart3() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getHelpScreen3();
        iv.setSmooth(true);
        iv.setImage(im);
        hBox.getChildren().addAll(iv);

        VBox vBox = new VBox();
        vBox.getChildren().add(new Label("1) Damit können die Sender in" + P2LibConst.LINE_SEPARATOR +
                "der History gefiltert werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("2) In dem Menü kann die History" + P2LibConst.LINE_SEPARATOR +
                "bearbeitet werden."));

        vBox.getChildren().add(P2GuiTools.getHDistance(dist));
        vBox.getChildren().add(new Label("3) Damit werden die Sender" + P2LibConst.LINE_SEPARATOR +
                "gestartet, gestoppt und\n" +
                "aus der History gelöscht"));

        hBox.getChildren().add(vBox);

        return new TitledPane("Infos zur History", hBox);
    }

    private javafx.scene.image.Image getHelpScreen1() {
        final String path = "/de/p2tools/p2radio/res/startdialog/p2Radio-startdialog-1.png";
        return new Image(path, 600, 600, true, true);
    }

    private javafx.scene.image.Image getHelpScreen2() {
        final String path = "/de/p2tools/p2radio/res/startdialog/p2Radio-startdialog-2.png";
        return new Image(path, 600, 600, true, true);
    }

    private javafx.scene.image.Image getHelpScreen3() {
        final String path = "/de/p2tools/p2radio/res/startdialog/p2Radio-startdialog-3.png";
        return new Image(path, 600, 600, true, true);
    }
}
