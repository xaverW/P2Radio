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
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2lib.tools.P2StringUtils;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneConfig {

    private final P2ToggleSwitch tglSmallStation = new P2ToggleSwitch("In den Tabellen nur kleine Button anzeigen:");
    private final P2ToggleSwitch tglLoadStationList = new P2ToggleSwitch("Die Senderliste automatisch alle " +
            ProgConst.LOAD_STATION_LIST_EVERY_DAYS + " Tage aktualisieren");
    private final P2ToggleSwitch tglEnableLog = new P2ToggleSwitch("Ein Logfile anlegen:");

    final GridPane gridPane = new GridPane();

    private final Stage stage;
    private TextField txtUserAgent;
    private final ProgData progData;

    public PaneConfig(Stage stage) {
        this.stage = stage;
        this.progData = ProgData.getInstance();
    }

    public void close() {
        tglSmallStation.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_SMALL_ROW_TABLE);
        tglLoadStationList.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS);
        txtUserAgent.textProperty().unbindBidirectional(ProgConfig.SYSTEM_USERAGENT);
        tglEnableLog.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_LOG_ON);
    }

    public void makeConfig(Collection<TitledPane> result) {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));

        TitledPane tpConfig = new TitledPane("Allgemein", gridPane);
        result.add(tpConfig);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize());

        makeInfos();
    }

    private void makeInfos() {
        tglSmallStation.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_SMALL_ROW_TABLE);
        tglLoadStationList.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_LOAD_STATION_LIST_EVERY_DAYS);
        final Button btnHelpLoadStationList = P2Button.helpButton(stage, "Liste der Sender aktualisieren",
                HelpText.LOAD_STATION_LIST_EVERY_DAYS);
        final Button btnHelpSize = P2Button.helpButton(stage, "Nur kleine Button anzeigen",
                HelpText.SMALL_BUTTON);
        GridPane.setHalignment(btnHelpLoadStationList, HPos.RIGHT);
        GridPane.setHalignment(btnHelpSize, HPos.RIGHT);

        // UserAgent
        final Button btnHelpUserAgent = P2Button.helpButton(stage, "User Agent festlegen",
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
                String str = P2StringUtils.convertToASCIIEncoding(text);
                final int size = getText().length() + text.length();

                return text.isEmpty() || (size < ProgConst.MAX_USER_AGENT_SIZE) && text.equals(str);
            }
        };
        txtUserAgent.textProperty().bindBidirectional(ProgConfig.SYSTEM_USERAGENT);

        int row = 0;
        gridPane.add(tglLoadStationList, 0, row, 2, 1);
        gridPane.add(btnHelpLoadStationList, 4, row);

        ++row;
        gridPane.add(tglSmallStation, 0, ++row, 2, 1);
        gridPane.add(btnHelpSize, 4, row);

        ++row;
        gridPane.add(new Label("User Agent:"), 0, ++row);
        gridPane.add(txtUserAgent, 1, row);
        gridPane.add(btnHelpUserAgent, 4, row);
    }
}
