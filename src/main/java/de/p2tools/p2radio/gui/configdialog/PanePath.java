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

package de.p2tools.p2radio.gui.configdialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.guitools.P2Styles;
import de.p2tools.p2lib.tools.ProgramToolsFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PanePath {
    private final Stage stage;
    private final GridPane gridPane = new GridPane();
    private final List<UnBind> unbindList = new ArrayList<>();
    private int row = 0;

    public PanePath(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        unbindList.stream().forEach(unBind -> unBind.unbind());
    }

    public TitledPane makePath() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcComputedSizeAndHgrow());

        Button btnEmpty = new Button(" "); // ist nur für die Zeilenhöhe
        btnEmpty.setVisible(false);
        gridPane.add(btnEmpty, 2, row);

        switch (ProgramToolsFactory.getOs()) {
            case WIN32:
            case WIN64:
                // da wird nur der VLC gebraucht, der Rest wird mitgeliefert
                addPlayer();
                break;
            default:
                // da brauchts alles
                addPlayer();
        }

        TitledPane tpConfig = new TitledPane("Programmpfade", gridPane);
        return tpConfig;
    }

    private void addPlayer() {
        Text text;
        P2Hyperlink hyperlink;
        TextField txtPlayer = new TextField();
        final Button btnFind = new Button("suchen");

        text = new Text("Pfad zum VLC-Player auswählen");
        btnFind.setOnAction(event -> {
            ProgConfig.SYSTEM_PATH_VLC.setValue("");
            txtPlayer.setText(SetFactory.getTemplatePathVlc());
        });
        hyperlink = new P2Hyperlink(stage,
                ProgConst.URL_WEBSITE_VLC,
                ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());

        text.setStyle("-fx-font-weight: bold");

        txtPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(txtPlayer.getText());
            if (!file.exists() || !file.isFile()) {
                txtPlayer.setStyle(P2Styles.PTEXTFIELD_ERROR);
            } else {
                txtPlayer.setStyle("");
            }
        });
        txtPlayer.textProperty().bindBidirectional(ProgConfig.SYSTEM_PATH_VLC);
        unbindList.add(new UnBind(txtPlayer, ProgConfig.SYSTEM_PATH_VLC));

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            PDirFileChooser.FileChooserOpenFile(stage, txtPlayer);
        });
        btnFile.setGraphic(ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Programmdatei auswählen"));
        final Button btnHelp = P2Button.helpButton(stage,
                "Videoplayer", HelpText.PROG_PATHS);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(new Label("Website"), hyperlink);

        gridPane.add(text, 0, row);
        gridPane.add(txtPlayer, 0, ++row);
        gridPane.add(btnFile, 1, row);
        gridPane.add(btnFind, 2, row);
        gridPane.add(btnHelp, 3, row);
        gridPane.add(hBox, 0, ++row, 3, 1);
    }

    private class UnBind {
        private final TextField txt;
        private final StringProperty property;

        UnBind(TextField txt, StringProperty property) {
            this.txt = txt;
            this.property = property;
        }

        void unbind() {
            txt.textProperty().unbindBidirectional(property);
        }
    }
}
