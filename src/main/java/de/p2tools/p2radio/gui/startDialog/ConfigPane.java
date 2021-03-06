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

package de.p2tools.p2radio.gui.startDialog;


import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class ConfigPane {
    private final Stage stage;
    private final PToggleSwitch tglSearch = new PToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
    BooleanProperty updateProp = ProgConfig.SYSTEM_UPDATE_SEARCH_ACT;
    StringProperty vlcProp = ProgConfig.SYSTEM_PATH_VLC;
    private final GridPane gridPane = new GridPane();
    private final TextField txtPlayer = new TextField();
    private int row = 0;

    public ConfigPane(Stage stage) {
        this.stage = stage;
    }

    public TitledPane makeStart() {
        makeUpdate();
        makePath();

        TitledPane tpConfig = new TitledPane("Programmeinstellungen", gridPane);
        return tpConfig;
    }

    public void close() {
        tglSearch.selectedProperty().unbindBidirectional(updateProp);
        txtPlayer.textProperty().bindBidirectional(vlcProp);
    }

    private void makeUpdate() {
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());

        //einmal am Tag Update suchen
        tglSearch.selectedProperty().bindBidirectional(updateProp);

        Text text = new Text("Suche nach einem Programmupdate");
        text.setStyle("-fx-font-weight: bold");

        final Button btnHelp = PButton.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird gepr??ft, ob es eine neue Version des Programms gibt. Wenn es " +
                        "eine neue Version gibt, wird das mit einer Nachricht mitgeteilt. Es wird nicht " +
                        "automatisch das Programm ver??ndert.");

        gridPane.add(new Label(" "), 2, ++row);
        gridPane.add(text, 0, row);
        gridPane.add(tglSearch, 0, ++row, 3, 1);
        gridPane.add(btnHelp, 3, row);
    }

    private void makePath() {
        Text text = new Text("Pfad zum Media-Player ausw??hlen");
        text.setStyle("-fx-font-weight: bold");

        PHyperlink hyperlink = new PHyperlink(stage,
                ProgConst.URL_WEBSITE_VLC,
                ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

        final Button btnFind = new Button("suchen");
        btnFind.setOnAction(event -> {
            ProgConfig.SYSTEM_PATH_VLC.setValue("");
            txtPlayer.setText(SetFactory.getTemplatePathVlc());
        });

        txtPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(txtPlayer.getText());
            if (!file.exists() || !file.isFile()) {
                txtPlayer.setStyle(ProgColorList.STATION_ERROR.getCssBackground());
            } else {
                txtPlayer.setStyle("");
            }
        });
        txtPlayer.textProperty().bindBidirectional(vlcProp);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            PDirFileChooser.FileChooserOpenFile(stage, txtPlayer);
        });
        btnFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnFile.setTooltip(new Tooltip("Programmdatei ausw??hlen"));

        final Button btnHelp = PButton.helpButton(stage,
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
