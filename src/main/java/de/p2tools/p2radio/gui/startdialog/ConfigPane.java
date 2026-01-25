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
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.guitools.grid.P2GridConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.controller.picon.PIconFactory;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class ConfigPane extends VBox {
    private final P2ToggleSwitch tglSearch = new P2ToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
    private final GridPane gridPane = new GridPane();
    private final TextField txtPlayer = new TextField();
    private int row = 0;
    private final Stage stage;

    public ConfigPane(Stage stage) {
        this.stage = stage;
    }

    public void make() {
        makeUpdate();
        makePath();

        HBox hBox = new HBox();
        hBox.getStyleClass().add("startInfo_2");
        hBox.setPadding(new Insets(P2LibConst.PADDING));
        Label lbl = new Label("Hier kann die Update-Suche und " +
                "das Programm zum Abspielen der Radiosender, eingestellt werden.");
        lbl.setWrapText(true);
        lbl.setPrefWidth(500);
        hBox.getChildren().add(lbl);
        getChildren().addAll(StartFactory.getTitle("Programmeinstellungen"), hBox, P2GuiTools.getHDistance(20));
        getChildren().add(gridPane);
    }

    public void close() {
        tglSearch.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_UPDATE_SEARCH_ACT);
        txtPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_PATH_VLC);
    }

    private void makeUpdate() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));
        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcComputedSizeAndHgrow());

        //einmal am Tag Update suchen
        tglSearch.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_UPDATE_SEARCH_ACT);

        Text text = new Text("Suche nach einem Programmupdate");
        text.setStyle("-fx-font-weight: bold");

        final Button btnHelp = P2Button.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. Wenn es " +
                        "eine neue Version gibt, wird das mit einer Nachricht mitgeteilt. Es wird nicht " +
                        "automatisch das Programm verändert.");

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, row);
        gridPane.add(tglSearch, 0, ++row, 3, 1);
        gridPane.add(btnHelp, 3, row);
    }

    private void makePath() {
        Text text = new Text("Pfad zum Media-Player auswählen");
        text.setStyle("-fx-font-weight: bold");

        P2Hyperlink hyperlink = new P2Hyperlink(stage,
                ProgConst.URL_WEBSITE_VLC,
                ProgConfig.SYSTEM_PROG_OPEN_URL);

        final Button btnFind = new Button("suchen");
        btnFind.setOnAction(event -> {
            ProgConfig.SYSTEM_PATH_VLC.setValue("");
            txtPlayer.setText(SetFactory.getTemplatePathVlc());
        });

        txtPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(txtPlayer.getText());
            if (!file.exists() || !file.isFile()) {
                txtPlayer.setStyle(ProgColorList.STATION_ERROR_BG.getCssBackground());
            } else {
                txtPlayer.setStyle("");
            }
        });
        txtPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_PATH_VLC);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            P2DirFileChooser.FileChooserOpenFile(stage, txtPlayer);
        });
        btnFile.setGraphic(PIconFactory.PICON.BTN_DIR_OPEN.getFontIcon());
        btnFile.setTooltip(new Tooltip("Programmdatei auswählen"));

        final Button btnHelp = P2Button.helpButton(stage,
                "Videoplayer", HelpText.PROG_PATHS);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Website"), hyperlink);

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, row);
        gridPane.add(txtPlayer, 0, ++row);
        gridPane.add(btnFile, 1, row);
        gridPane.add(btnFind, 2, row);
        gridPane.add(btnHelp, 3, row);
        gridPane.add(hBox, 0, ++row, 3, 1);
    }
}
