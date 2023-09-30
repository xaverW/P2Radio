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
import de.p2tools.p2lib.guitools.pclosepane.P2ClosePaneH;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StationGuiInfoController extends P2ClosePaneH {
    private final GridPane gridPane = new GridPane();
    private final Label lblTitle = new Label("");
    private final P2Hyperlink hyperlinkWebsite = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());
    private final P2Hyperlink hyperlinkUrl = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());

    private final StationGuiPack stationGuiPack;
    private StationData station = null;

    public StationGuiInfoController(StationGuiPack stationGuiPack) {
        super(ProgConfig.STATION_GUI_DIVIDER_ON, true);
        this.stationGuiPack = stationGuiPack;

        initInfo();
    }

    public void initInfo() {
        stationGuiPack.stationDataObjectPropertyProperty().addListener((u, o, n) -> {
            setStation(stationGuiPack.stationDataObjectPropertyProperty().getValue());
        });
        getVBoxAll().getChildren().addAll(gridPane);

        lblTitle.setFont(Font.font(null, FontWeight.BOLD, -1));
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
    }

    public void setStation(StationData station) {
        this.station = station;
        if (station == null) {
            lblTitle.setText("");
            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            return;
        }

        lblTitle.setText(station.getStationName() + "  -  " + station.getCountry());
        hyperlinkWebsite.setUrl(station.getWebsite());
        hyperlinkUrl.setUrl(station.getStationUrl());
    }
}
