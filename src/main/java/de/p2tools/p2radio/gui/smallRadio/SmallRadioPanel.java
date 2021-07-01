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


import de.p2tools.p2Lib.P2LibConst;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SmallRadioPanel extends TitledPane {
    private final Stage stage;
    private final HBox hBox = new HBox();

    public SmallRadioPanel(Stage stage) {
        super();
        this.stage = stage;
        this.setText("Infos zur Programmoberfläche");
        this.getChildren().add(hBox);
        make();
    }

    public void close() {
    }

    private void make() {
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));

        ImageView iv = new ImageView();
        Image im = getHelpScreen1();
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1) Hier kann die Liste der" + P2LibConst.LINE_SEPARATOR +
                "Sender gefiltert werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) Die Ansicht der Sender, Favoriten und" + P2LibConst.LINE_SEPARATOR +
                "der History wird hier umgeschaltet." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) In dem Menü befinden sich" + P2LibConst.LINE_SEPARATOR +
                "die Programmeinstellungen." +

                P2LibConst.LINE_SEPARATORx2 +
                "4) Mit dem Pluszeichen können" + P2LibConst.LINE_SEPARATOR +
                "Spalten in der Tabelle" + P2LibConst.LINE_SEPARATOR +
                "ein- und ausgeblendet werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "5) In dem Menü können Sender gestartet" + P2LibConst.LINE_SEPARATOR +
                "und verarbeitet werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "6) Damit können Sender gestartet" + P2LibConst.LINE_SEPARATOR +
                "und gestoppt werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "7) Damit können Sender zu" + P2LibConst.LINE_SEPARATOR +
                "den Favoriten hinzugefügt werden.");

        hBox.getChildren().addAll(iv, text);
    }

    private Image getHelpScreen1() {
        final String path = "/de/p2tools/p2radio/res/p2Radio-startpage-1.png";
        return new Image(path, 600, 600, true, true);
    }

}
