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
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2lib.tools.log.P2Logger;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneLogFile {

    private final P2ToggleSwitch tglSmallStation = new P2ToggleSwitch("In den Tabellen nur kleine Button anzeigen:");
    private final P2ToggleSwitch tglLoadStationList = new P2ToggleSwitch("Die Senderliste automatisch alle " +
            ProgConst.LOAD_STATION_LIST_EVERY_DAYS + " Tage aktualisieren");
    private final P2ToggleSwitch tglEnableLog = new P2ToggleSwitch("Ein Logfile anlegen:");
    private final BooleanProperty logfileChanged = new SimpleBooleanProperty(false);
    private final Stage stage;
    private TextField txtLogFile;

    public PaneLogFile(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        tglSmallStation.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_SMALL_ROW_TABLE);
        tglLoadStationList.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS);
        tglEnableLog.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_LOG_ON);
        txtLogFile.textProperty().unbindBidirectional(ProgConfig.SYSTEM_LOG_DIR);
    }

    public void makeLogfile(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));

        TitledPane tpConfig = new TitledPane("Logfile", gridPane);
        result.add(tpConfig);

        tglEnableLog.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_LOG_ON);
        tglEnableLog.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (newValue) {
                P2Logger.setFileHandler(ProgInfos.getLogDirectoryString());
            } else {
                P2Logger.removeFileHandler();
            }
        }));

        final Button btnHelp = P2Button.helpButton(stage, "Logfile", HelpText.LOGFILE);

        txtLogFile = new TextField();
        txtLogFile.textProperty().bindBidirectional(ProgConfig.SYSTEM_LOG_DIR);
        if (txtLogFile.getText().isEmpty()) {
            txtLogFile.setText(ProgInfos.getLogDirectoryString());
        }

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für das Logfile auswählen"));
        btnFile.setOnAction(event -> {
            P2DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtLogFile);
        });
        btnFile.setGraphic(ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());

        final Button btnReset = new Button();
        btnReset.setGraphic(ProgIconsP2Radio.ICON_BUTTON_RESET.getImageView());
        btnReset.setTooltip(new Tooltip("Standardpfad für das Logfile wieder herstellen"));
        btnReset.setOnAction(event -> {
            txtLogFile.setText(ProgInfos.getStandardLogDirectoryString());
        });

        final Button btnChange = new Button("_Pfad zum Logfile jetzt schon ändern");
        btnChange.setTooltip(new Tooltip("Den geänderten Pfad für das Logfile\n" +
                "jetzt schon verwenden, \n\n" +
                "ansonsten wird er erst beim nächsten\n" +
                "Programmstart verwendet"));
        btnChange.setOnAction(event -> {
            P2Logger.setFileHandler(ProgInfos.getLogDirectoryString());
            logfileChanged.setValue(false);
        });
        Label lblDir = new Label("Ordner:");

        int row = 0;
        gridPane.add(tglEnableLog, 0, row, 3, 1);
        gridPane.add(btnHelp, 3, row);

        ++row;
        gridPane.add(lblDir, 0, ++row);
        gridPane.add(txtLogFile, 1, row);
        gridPane.add(btnFile, 2, row);
        gridPane.add(btnReset, 3, row);

        gridPane.add(btnChange, 0, ++row, 2, 1);
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize());

        lblDir.disableProperty().bind(tglEnableLog.selectedProperty().not());
        txtLogFile.disableProperty().bind(tglEnableLog.selectedProperty().not());
        btnFile.disableProperty().bind(tglEnableLog.selectedProperty().not());
        btnReset.disableProperty().bind(tglEnableLog.selectedProperty().not());
        btnChange.disableProperty().bind(tglEnableLog.selectedProperty().not().or(logfileChanged.not()));

        txtLogFile.textProperty().addListener((observable, oldValue, newValue) -> logfileChanged.setValue(true));
    }
}
