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
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.PToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneProg {

    private final PToggleSwitch tglSmallStation = new PToggleSwitch("In den Tabellen nur kleine Button anzeigen:");
    private final PToggleSwitch tglLoadStationList = new PToggleSwitch("Die Senderliste automatisch alle " +
            ProgConst.LOAD_STATION_LIST_EVERY_DAYS + " Tage aktualisieren");
    private final PToggleSwitch tglEnableLog = new PToggleSwitch("Ein Logfile anlegen:");
    private final Stage stage;
    private TextField txtFileManagerWeb;

    public PaneProg(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        tglSmallStation.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_SMALL_ROW_TABLE);
        tglLoadStationList.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS);
        tglEnableLog.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_LOG_ON);
        txtFileManagerWeb.textProperty().unbindBidirectional(ProgConfig.SYSTEM_PROG_OPEN_URL);
    }

    public void makeProg(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));

        TitledPane tpConfig = new TitledPane("Programme", gridPane);
        result.add(tpConfig);

        addWebbrowser(gridPane, 0);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());
    }

    private void addWebbrowser(GridPane gridPane, int row) {
        gridPane.add(new Label("Webbrowser zum Öffnen von URLs"), 0, row);
        txtFileManagerWeb = new TextField();
        txtFileManagerWeb.textProperty().bindBidirectional(ProgConfig.SYSTEM_PROG_OPEN_URL);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            PDirFileChooser.FileChooserOpenFile(ProgData.getInstance().primaryStage, txtFileManagerWeb);
        });
        btnFile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Einen Webbrowser zum Öffnen von URLs auswählen"));

        final Button btnHelp = PButton.helpButton(stage, "Webbrowser", HelpText.WEBBROWSER);

        gridPane.add(txtFileManagerWeb, 0, row + 1);
        gridPane.add(btnFile, 1, row + 1);
        gridPane.add(btnHelp, 2, row + 1);
    }
}
