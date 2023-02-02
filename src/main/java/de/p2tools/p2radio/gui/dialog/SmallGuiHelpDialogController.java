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

package de.p2tools.p2radio.gui.dialog;


import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SmallGuiHelpDialogController extends PDialogExtra {
    private final Button btnOk = new Button("_Ok");

    public SmallGuiHelpDialogController(Stage stage) {
        super(stage, null, "Kleines Radio", true, false);
        init(true);
    }

    @Override
    public void make() {
        btnOk.setOnAction(event -> {
            close();
        });
        addOkButton(btnOk);
        makeInfos();
    }

    private void makeInfos() {
        HBox hBox = new HBox();
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(0));

        ImageView iv = new ImageView();
        Image im = getHelpScreen();
        iv.setSmooth(true);
        iv.setImage(im);

        hBox.getChildren().addAll(iv);

        Label text = new Label("1) Mit einem Klick auf den" + P2LibConst.LINE_SEPARATOR +
                "oberen Rand, kann das Fenster" + P2LibConst.LINE_SEPARATOR +
                "verschoben werden." +
                P2LibConst.LINE_SEPARATORx2 +

                "2) Ein Klick auf den" + P2LibConst.LINE_SEPARATOR +
                "restlichen Rand, passt die" + P2LibConst.LINE_SEPARATOR +
                "Fenstergröße an.");
        hBox.getChildren().add(text);
        getVBoxCont().getChildren().addAll(hBox);
    }

    private javafx.scene.image.Image getHelpScreen() {
        final String path = "/de/p2tools/p2radio/res/p2Radio-startpage-small.png";
        return new Image(path, 400, 400, true, true);
    }

}