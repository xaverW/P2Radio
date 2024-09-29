/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneTray {

    private final Stage stage;
    private final P2ToggleSwitch tglTray = new P2ToggleSwitch("Programm im System Tray anzeigen");
    private final P2ToggleSwitch tglOwnIcon = new P2ToggleSwitch("Ein eigenes Icon anzeigen");
    private final Label lblDatei = new Label("Datei (png, jpg):");
    private final TextField txtPath = new TextField();

    public PaneTray(Stage stage) {
        this.stage = stage;
    }

    public void makeTray(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));

        TitledPane tpConfig = new TitledPane("System Tray", gridPane);
        result.add(tpConfig);

        tglTray.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_TRAY);
        final Button btnHelpTray = P2Button.helpButton(stage, "Programm im System Tray anzeigen",
                HelpText.TRAY);
        GridPane.setHalignment(btnHelpTray, HPos.RIGHT);

        tglOwnIcon.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_TRAY_USE_OWN_ICON);
        tglOwnIcon.disableProperty().bind(tglTray.selectedProperty().not());
        final Button btnHelpTrayOwnIcon = P2Button.helpButton(stage, "Eigenes Bild im Tray anzeigen",
                HelpText.TRAY_OWN_ICON);
        GridPane.setHalignment(btnHelpTrayOwnIcon, HPos.RIGHT);
        btnHelpTrayOwnIcon.disableProperty().bind(tglTray.selectedProperty().not());

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für das Logfile auswählen"));
        btnFile.setOnAction(event -> {
            String s = P2DirFileChooser.FileChooserSelect(ProgData.getInstance().primaryStage, "", "");
            if (!s.isEmpty()) {
                //evtl. Abbruch des FileChooser
                txtPath.setText(s);
            }
        });
        btnFile.setGraphic(ProgIcons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.disableProperty().bind(tglOwnIcon.selectedProperty().not().or(tglTray.selectedProperty().not()));

        lblDatei.disableProperty().bind(tglOwnIcon.selectedProperty().not().or(tglTray.selectedProperty().not()));
        txtPath.textProperty().bindBidirectional(ProgConfig.SYSTEM_TRAY_ICON_PATH);
        txtPath.disableProperty().bind(tglOwnIcon.selectedProperty().not().or(tglTray.selectedProperty().not()));

        int row = 0;
        gridPane.add(tglTray, 0, row, 2, 1);
        gridPane.add(btnHelpTray, 2, row);

        gridPane.add(tglOwnIcon, 0, ++row, 2, 1);
        gridPane.add(btnHelpTrayOwnIcon, 2, row);

        gridPane.add(lblDatei, 0, ++row);
        gridPane.add(txtPath, 1, row);
        gridPane.add(btnFile, 2, row);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());
    }

    public void close() {
    }
}
