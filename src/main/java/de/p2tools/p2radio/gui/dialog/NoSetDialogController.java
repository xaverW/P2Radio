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
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class NoSetDialogController extends PDialogExtra {

    final ProgData progData;
    Button btnCancel = new Button("_Abbrechen");
    Button btnImport = new Button("_Standarsets wieder herstellen");

    public NoSetDialogController(ProgData progData) {
        super(progData.primaryStage, null,
                "Kein Videoplayer!", true, false, DECO.SMALL);

        this.progData = progData;
        init(true);
    }


    @Override
    public void make() {
        btnCancel.setOnAction(a -> close());

        btnImport.setOnAction(event -> {
            importSet();
            close();
        });

        Text textHeaderPlay = new Text("Kein Mediaplayer zum Abspielen!");
        textHeaderPlay.setFont(Font.font(null, FontWeight.BOLD, -1));

        final int prefRowCount = 14;
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setPrefRowCount(prefRowCount);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        getVBoxCont().getChildren().addAll(textHeaderPlay, textArea);
        getVBoxCont().setSpacing(20);
        textArea.setText("Es ist kein Mediaplayer zum Abspielen der Radiosender " +
                "angelegt." + P2LibConst.LINE_SEPARATORx2 +
                "Im Menü Einstellungen unter \"Abspielen\" " +
                "kann ein Programm zum Abspielen von Radiosendern " +
                "eingerichtet werden. Bitte dort die Einstellungen " +
                "korrigieren. Oder die Einstellungen zurücksetzen " +
                "und das Standardset wieder herstellen.");

        addOkButton(btnImport);
        addCancelButton(btnCancel);
    }

    private void importSet() {
        Platform.runLater(() -> {
            ImportSetDialogController importSetDialogController = new ImportSetDialogController(progData);
            importSetDialogController.close();
        });
    }
}
