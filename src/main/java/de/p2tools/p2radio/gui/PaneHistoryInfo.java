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

package de.p2tools.p2radio.gui;

import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PaneHistoryInfo extends VBox {
    private final GridPane gridPane = new GridPane();
    private final Label lblTitle = new Label("");
    private final CheckBox chkAutoStart = new CheckBox();
    private final P2Hyperlink hyperlinkWebsite = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL);
    private final P2Hyperlink hyperlinkUrl = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL);
    private final Label lblDescription = new Label("Beschreibung: ");
    private final TextArea taDescription = new TextArea();

    private final HistoryGuiPack historyGuiPack;
    private StationData station = null;
    private boolean stopListener = false;

    public PaneHistoryInfo(HistoryGuiPack historyGuiPack) {
        this.historyGuiPack = historyGuiPack;
        initInfo();
    }

    public void setStation(StationData station) {
        stopListener = true;
        if (this.station != null) {
            taDescription.textProperty().unbindBidirectional(this.station.descriptionProperty());
        }

        this.station = station;
        chkAutoStart.setDisable(station == null);
        taDescription.setDisable(station == null);
        if (station == null) {
            lblTitle.setText("");
            chkAutoStart.setSelected(false);
            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            taDescription.setText("");
            stopListener = false;
            return;
        }

        lblTitle.setText(station.getStationName() + "  -  " + station.getCountry());
        chkAutoStart.setSelected(ProgData.getInstance().stationAutoStart.getStationUrl().equals(station.getStationUrl()));
        hyperlinkWebsite.setUrl(station.getWebsite());
        hyperlinkUrl.setUrl(station.getStationUrl());
        taDescription.textProperty().bindBidirectional(station.descriptionProperty());
        stopListener = false;
    }

    public void initInfo() {
        ProgData.getInstance().stationAutoStart.stationUrlProperty().addListener((u, o, n) -> setStation(station));
        chkAutoStart.selectedProperty().addListener((u, o, n) -> {
            if (!stopListener) {
                AutoStartFactory.setAutoStart(station, chkAutoStart.isSelected());
            }
        });
        historyGuiPack.stationDataObjectPropertyProperty().addListener((u, o, n) -> {
            setStation(historyGuiPack.stationDataObjectPropertyProperty().getValue());
        });

        getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        lblTitle.setFont(Font.font(null, FontWeight.BOLD, -1));
        taDescription.setEditable(true);
        taDescription.setWrapText(true);
        taDescription.setPrefRowCount(2);

        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(new Label("Titel: "), 0, row);
        gridPane.add(lblTitle, 1, row);

        gridPane.add(new Label("Website: "), 0, ++row);
        gridPane.add(hyperlinkWebsite, 1, row);

        gridPane.add(new Label("Sender-URL: "), 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(new Label("AutoStart: "), 0, ++row);
        gridPane.add(chkAutoStart, 1, row);

        gridPane.add(lblDescription, 0, ++row);
        gridPane.add(taDescription, 1, row);
        GridPane.setVgrow(taDescription, Priority.ALWAYS);
    }
}
