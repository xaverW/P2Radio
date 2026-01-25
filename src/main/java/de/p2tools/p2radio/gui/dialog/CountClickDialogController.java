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

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CountClickDialogController extends P2DialogExtra {

    private final ProgData progData;
    private final Button btnYes = new Button("_Ja");
    private final Button btnNo = new Button("_Nein");
    private final CheckBox chkAsk = new CheckBox("Vorher immer fragen");

    public CountClickDialogController(ProgData progData) {
        super(progData.primaryStage, null,
                "Klicks zählen", true, false, false, DECO.BORDER_SMALL);

        this.progData = progData;
        init(true);
    }

    @Override
    public void close() {
        chkAsk.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_ASK_COUNT_CLICKS);
        super.close();
    }

    @Override
    public void make() {
        chkAsk.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_ASK_COUNT_CLICKS);
        btnNo.setOnAction(a -> {
            // dann nicht zählen
            ProgConfig.SYSTEM_COUNT_CLICKS.set(false);
            close();
        });
        btnYes.setOnAction(a -> {
            // dann zählen
            ProgConfig.SYSTEM_COUNT_CLICKS.set(true);
            close();
        });

        final Button btnHelp = P2Button.helpButton(getStage(), "Klicks zählen",
                HelpText.CLICK_COUNT);

        Text textHeaderPlay = new Text("Sollen die Klicks gezählt werden?");
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
        textArea.setText("Die Klicks eines Radiosenders (Spalte Klicks in der Tabelle) " +
                "ist die Anzahl der Starts aller User dieses Senders. Damit kann man " +
                "sehen, wie beliebt ein Sender ist. " +
                "Dazu wird ein Ping zum Anbieter dieser Radioliste geschickt." +
                "\n\n" +
                "In den Einstellungen kann das auch wieder geändert werden.");

        addOkCancelButtons(btnYes, btnNo);
        addHlpButton(btnHelp);
        getHBoxOverButtons().getChildren().add(chkAsk);
    }
}
