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

package de.p2tools.p2radio.gui.configDialog;

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.dialogs.accordion.PAccordionPane;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2Lib.tools.PStringUtils;
import de.p2tools.p2Lib.tools.log.PLogger;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.gui.tools.HelpText;
import de.p2tools.p2radio.tools.update.SearchProgramUpdate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class ConfigPaneController extends PAccordionPane {

    private final ProgData progData;

    private final PToggleSwitch tglSearch = new PToggleSwitch("einmal am Tag nach einer neuen Programmversion suchen");
    private final PToggleSwitch tglSearchBeta = new PToggleSwitch("auch nach neuen Vorabversionen suchen");
    private final Button btnNow = new Button("_Jetzt suchen");
    private Button btnHelpBeta;

    BooleanProperty logfileChanged = new SimpleBooleanProperty(false);

    BooleanProperty propUpdateSearch = ProgConfig.SYSTEM_UPDATE_SEARCH;
    BooleanProperty propUpdateBetaSearch = ProgConfig.SYSTEM_UPDATE_BETA_SEARCH;
    StringProperty propUrl = ProgConfig.SYSTEM_PROG_OPEN_URL;
    BooleanProperty propLog = ProgConfig.SYSTEM_LOG_ON;
    StringProperty propLogDir = ProgConfig.SYSTEM_LOG_DIR;
    BooleanProperty propSizeSender = ProgConfig.SYSTEM_SMALL_ROW_TABLE;
    BooleanProperty propLoadStationList = ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS;
    BooleanProperty propTray = ProgConfig.SYSTEM_TRAY;

    private final PToggleSwitch tglSmallStation = new PToggleSwitch("In den Tabellen nur kleine Button anzeigen:");
    private final PToggleSwitch tglLoadStationList = new PToggleSwitch("Die Senderliste automatisch alle " +
            ProgConst.LOAD_STATION_LIST_EVERY_DAYS + " Tage aktualisieren");
    private final PToggleSwitch tglTray = new PToggleSwitch("Programm im System Tray anzeigen");

    private TextField txtUserAgent;
    private final PToggleSwitch tglEnableLog = new PToggleSwitch("Ein Logfile anlegen:");
    private TextField txtLogFile;
    private TextField txtFileManagerWeb;

    private final Stage stage;
    private ColorPane colorPane;
    private ShortcutPane shortcutPane;
    private StylePane stylePane;

    public ConfigPaneController(Stage stage) {
        super(stage, ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_CONFIG);
        this.stage = stage;
        progData = ProgData.getInstance();

        init();
    }

    public void close() {
        super.close();
        colorPane.close();
        shortcutPane.close();
        stylePane.close();
        tglSmallStation.selectedProperty().unbindBidirectional(propSizeSender);
        tglTray.selectedProperty().unbindBidirectional(propTray);
        tglLoadStationList.selectedProperty().unbindBidirectional(propLoadStationList);
        txtUserAgent.textProperty().unbindBidirectional(ProgConfig.SYSTEM_USERAGENT);
        tglEnableLog.selectedProperty().unbindBidirectional(propLog);
        txtLogFile.textProperty().unbindBidirectional(propLogDir);
        txtFileManagerWeb.textProperty().unbindBidirectional(propUrl);
        tglSearch.selectedProperty().unbindBidirectional(propUpdateSearch);
        tglSearchBeta.selectedProperty().unbindBidirectional(propUpdateBetaSearch);
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<TitledPane>();
        makeConfig(result);
        makeLogfile(result);
        colorPane = new ColorPane(stage);
        colorPane.makeColor(result);
        shortcutPane = new ShortcutPane(stage);
        shortcutPane.makeShortcut(result);
        stylePane = new StylePane(stage, progData);
        stylePane.makeStyle(result);

        makeProg(result);
        makeUpdate(result);
        return result;
    }

    private void makeConfig(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Allgemein", gridPane);
        result.add(tpConfig);

        tglSmallStation.selectedProperty().bindBidirectional(propSizeSender);
        tglLoadStationList.selectedProperty().bindBidirectional(propLoadStationList);
        final Button btnHelpLoadStationList = PButton.helpButton(stage, "Liste der Sender aktualisieren",
                HelpText.LOAD_STATION_LIST_EVERY_DAYS);
        final Button btnHelpSize = PButton.helpButton(stage, "Nur kleine Button anzeigen",
                HelpText.SMALL_BUTTON);
        GridPane.setHalignment(btnHelpLoadStationList, HPos.RIGHT);
        GridPane.setHalignment(btnHelpSize, HPos.RIGHT);


        final Button btnHelpUserAgent = PButton.helpButton(stage, "User Agent festlegen",
                HelpText.USER_AGENT);
        GridPane.setHalignment(btnHelpUserAgent, HPos.RIGHT);
        txtUserAgent = new TextField() {

            @Override
            public void replaceText(int start, int end, String text) {
                if (check(text)) {
                    super.replaceText(start, end, text);
                }
            }

            @Override
            public void replaceSelection(String text) {
                if (check(text)) {
                    super.replaceSelection(text);
                }
            }

            private boolean check(String text) {
                String str = PStringUtils.convertToASCIIEncoding(text);
                final int size = getText().length() + text.length();

                if (text.isEmpty() || (size < ProgConst.MAX_USER_AGENT_SIZE) && text.equals(str)) {
                    return true;
                }
                return false;
            }
        };
        txtUserAgent.textProperty().bindBidirectional(ProgConfig.SYSTEM_USERAGENT);


        int row = 0;
        gridPane.add(tglLoadStationList, 0, ++row, 2, 1);
        gridPane.add(btnHelpLoadStationList, 2, row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(tglSmallStation, 0, ++row, 2, 1);
        gridPane.add(btnHelpSize, 2, row);

        tglTray.selectedProperty().bindBidirectional(propTray);
        final Button btnHelpTray = PButton.helpButton(stage, "Programm im System Tray anzeigen",
                HelpText.TRAY);
        GridPane.setHalignment(btnHelpTray, HPos.RIGHT);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(tglTray, 0, ++row, 2, 1);
        gridPane.add(btnHelpTray, 2, row);


        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(new Label("User Agent:"), 0, ++row);
        gridPane.add(txtUserAgent, 1, row);
        gridPane.add(btnHelpUserAgent, 2, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());
    }

    private void makeLogfile(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Logfile", gridPane);
        result.add(tpConfig);

        tglEnableLog.selectedProperty().bindBidirectional(propLog);
        tglEnableLog.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (newValue) {
                PLogger.setFileHandler(ProgInfos.getLogDirectoryString());
            } else {
                PLogger.removeFileHandler();
            }
        }));

        final Button btnHelp = PButton.helpButton(stage, "Logfile", HelpText.LOGFILE);

        txtLogFile = new TextField();
        txtLogFile.textProperty().bindBidirectional(propLogDir);
        if (txtLogFile.getText().isEmpty()) {
            txtLogFile.setText(ProgInfos.getLogDirectoryString());
        }

        final Button btnFile = new Button();
        btnFile.setTooltip(new Tooltip("Einen Ordner für das Logfile auswählen"));
        btnFile.setOnAction(event -> {
            PDirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtLogFile);
        });
        btnFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);

        final Button btnReset = new Button();
        btnReset.setGraphic(new ProgIcons().ICON_BUTTON_RESET);
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
            PLogger.setFileHandler(ProgInfos.getLogDirectoryString());
            logfileChanged.setValue(false);
        });

        int row = 0;
        gridPane.add(tglEnableLog, 0, row, 3, 1);
        gridPane.add(btnHelp, 3, row);

        gridPane.add(new Label(""), 0, ++row);

        gridPane.add(new Label("Ordner:"), 0, ++row);
        gridPane.add(txtLogFile, 1, row);
        gridPane.add(btnFile, 2, row);
        gridPane.add(btnReset, 3, row);

        gridPane.add(btnChange, 0, ++row, 2, 1);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcPrefSize());

        txtLogFile.disableProperty().bind(tglEnableLog.selectedProperty().not());
        btnFile.disableProperty().bind(tglEnableLog.selectedProperty().not());
        btnReset.disableProperty().bind(tglEnableLog.selectedProperty().not());
        btnChange.disableProperty().bind(tglEnableLog.selectedProperty().not().or(logfileChanged.not()));

        txtLogFile.textProperty().addListener((observable, oldValue, newValue) -> logfileChanged.setValue(true));
    }

    private void makeProg(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Programme", gridPane);
        result.add(tpConfig);

        addWebbrowser(gridPane, 0);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());
    }

    private void addWebbrowser(GridPane gridPane, int row) {
        gridPane.add(new Label("Webbrowser zum Öffnen von URLs"), 0, row);
        txtFileManagerWeb = new TextField();
        txtFileManagerWeb.textProperty().bindBidirectional(propUrl);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> {
            PDirFileChooser.FileChooserOpenFile(ProgData.getInstance().primaryStage, txtFileManagerWeb);
        });
        btnFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnFile.setTooltip(new Tooltip("Einen Webbrowser zum Öffnen von URLs auswählen"));

        final Button btnHelp = PButton.helpButton(stage, "Webbrowser", HelpText.WEBBROWSER);

        gridPane.add(txtFileManagerWeb, 0, row + 1);
        gridPane.add(btnFile, 1, row + 1);
        gridPane.add(btnHelp, 2, row + 1);
    }

    private void makeUpdate(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        TitledPane tpConfig = new TitledPane("Programmupdate", gridPane);
        result.add(tpConfig);

        //einmal am Tag Update suchen
        tglSearch.selectedProperty().bindBidirectional(propUpdateSearch);
        final Button btnHelp = PButton.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. " +
                        "Ist eine aktualisierte Version vorhanden, dann wird das gemeldet."
                        + P2LibConst.LINE_SEPARATOR +
                        "Das Programm wird aber nicht ungefragt ersetzt.");

        tglSearchBeta.selectedProperty().bindBidirectional(propUpdateBetaSearch);
        btnHelpBeta = PButton.helpButton(stage, "Vorabversionen suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Vorabversion des Programms gibt. " +
                        P2LibConst.LINE_SEPARATORx2 +
                        "Das sind \"Zwischenschritte\" auf dem Weg zur nächsten Version. Hier ist die " +
                        "Entwicklung noch nicht abgeschlossen und das Programm kann noch Fehler enthalten. Wer Lust hat " +
                        "einen Blick auf die nächste Version zu werfen, ist eingeladen, die Vorabversionen zu testen." +
                        P2LibConst.LINE_SEPARATORx2 +
                        "Ist eine aktualisierte Vorabversion vorhanden, dann wird das gemeldet."
                        + P2LibConst.LINE_SEPARATOR +
                        "Das Programm wird aber nicht ungefragt ersetzt.");

        //jetzt suchen
        btnNow.setOnAction(event -> new SearchProgramUpdate(stage, progData).searchNewVersionInfos());
        checkBeta();
        tglSearch.selectedProperty().addListener((ob, ol, ne) -> checkBeta());

        PHyperlink hyperlink = new PHyperlink(ProgConst.ADRESSE_WEBSITE,
                ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
        HBox hBoxHyper = new HBox();
        hBoxHyper.setAlignment(Pos.CENTER_LEFT);
        hBoxHyper.setPadding(new Insets(10, 0, 0, 0));
        hBoxHyper.setSpacing(10);
        hBoxHyper.getChildren().addAll(new Label("Infos auch auf der Website:"), hyperlink);

        int row = 0;
        gridPane.add(tglSearch, 0, row);
        gridPane.add(btnHelp, 1, row);

        gridPane.add(tglSearchBeta, 0, ++row);
        gridPane.add(btnHelpBeta, 1, row);

        gridPane.add(btnNow, 0, ++row);

        gridPane.add(new Label(" "), 0, ++row);
        gridPane.add(hBoxHyper, 0, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());
        gridPane.getRowConstraints().addAll(PColumnConstraints.getRcPrefSize(), PColumnConstraints.getRcPrefSize(),
                PColumnConstraints.getRcPrefSize(), PColumnConstraints.getRcVgrow(), PColumnConstraints.getRcPrefSize());
    }

    private void checkBeta() {
        if (tglSearch.isSelected()) {
            tglSearchBeta.setDisable(false);
            btnHelpBeta.setDisable(false);
        } else {
            tglSearchBeta.setDisable(true);
            tglSearchBeta.setSelected(false);
            btnHelpBeta.setDisable(true);
        }
    }
}
