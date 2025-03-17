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

package de.p2tools.p2radio.gui.configdialog.setdata;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2radio.P2RadioFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgIcons;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneAutoStart {

    private final RadioButton rbNothing = new RadioButton("Keinen starten");
    private final RadioButton rbLastPlayed = new RadioButton("Zuletzt gespielter");
    private final RadioButton rbAuto = new RadioButton("Gewählter Autostart");
    private final RadioButton rbList = new RadioButton("Zufälliger Sender");
    private final RadioButton rbListStation = new RadioButton("Sender");
    private final RadioButton rbListFavourite = new RadioButton("Favoriten");
    private final RadioButton rbListHistory = new RadioButton("History");
    private final RadioButton rbListOwn = new RadioButton("Autostart-Liste");

    private final GridPane gridPane = new GridPane();
    private final Stage stage;
    private final Label lblLastPlayed = new Label();
    private final Label lblAuto = new Label();
    private final ProgData progData;

    public PaneAutoStart(Stage stage) {
        this.stage = stage;
        this.progData = ProgData.getInstance();
    }

    public void close() {
    }

    public void makeConfig(Collection<TitledPane> result) {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));

        TitledPane tpConfig = new TitledPane("Autostart", gridPane);
        result.add(tpConfig);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcPrefSize());

        makeAuto();
    }

    private void makeAuto() {
        ToggleGroup tg = new ToggleGroup();
        rbNothing.setToggleGroup(tg);
        rbLastPlayed.setToggleGroup(tg);
        rbAuto.setToggleGroup(tg);
        rbList.setToggleGroup(tg);

        ToggleGroup tgList = new ToggleGroup();
        rbListStation.setToggleGroup(tgList);
        rbListFavourite.setToggleGroup(tgList);
        rbListHistory.setToggleGroup(tgList);
        rbListOwn.setToggleGroup(tgList);
        rbListStation.disableProperty().bind(rbList.selectedProperty().not());
        rbListFavourite.disableProperty().bind(rbList.selectedProperty().not());
        rbListHistory.disableProperty().bind(rbList.selectedProperty().not());
        rbListOwn.disableProperty().bind(rbList.selectedProperty().not());

        setSelected();
        rbNothing.setOnAction(a -> getSelected());
        rbLastPlayed.setOnAction(a -> getSelected());
        rbAuto.setOnAction(a -> getSelected());
        rbList.setOnAction(a -> getSelected());
        rbListStation.setOnAction(a -> getSelected());
        rbListFavourite.setOnAction(a -> getSelected());
        rbListHistory.setOnAction(a -> getSelected());
        rbListOwn.setOnAction(a -> getSelected());

        final Button btnHelpAutoStart = P2Button.helpButton(stage, "Sender AutoStart",
                HelpText.AUTO_START);
        GridPane.setHalignment(btnHelpAutoStart, HPos.RIGHT);

        final Button btnPlay = new Button("");
        btnPlay.setTooltip(new Tooltip("Sender abspielen"));
        btnPlay.setGraphic(ProgIcons.ICON_BUTTON_PLAY.getImageView());
        btnPlay.setOnAction((ActionEvent event) -> {
            P2RadioFactory.loadAutoStart();
        });
        btnPlay.disableProperty().bind(rbNothing.selectedProperty());

        final Button btnStop;
        btnStop = new Button("");
        btnStop.setTooltip(new Tooltip("Sender stoppen"));
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction((ActionEvent event) -> {
            StartFactory.stopStation();
        });

        lblLastPlayed.setStyle("-fx-border-color: black;");
        lblLastPlayed.setPadding(new Insets(5));
        lblLastPlayed.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblLastPlayed, Priority.ALWAYS);
        if (ProgData.getInstance().stationLastPlayed.isAuto()) {
            lblLastPlayed.setText(ProgData.getInstance().stationLastPlayed.getStationName() +
                    "\n" +
                    ProgData.getInstance().stationLastPlayed.getStationUrl());
        }

        lblAuto.setStyle("-fx-border-color: black;");
        lblAuto.setPadding(new Insets(5));
        lblAuto.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblAuto, Priority.ALWAYS);
        if (ProgData.getInstance().stationAutoStart.isAuto()) {
            lblAuto.setText(ProgData.getInstance().stationAutoStart.getStationName() +
                    "\n" +
                    ProgData.getInstance().stationAutoStart.getStationUrl());
        }

        int row = 0;
        gridPane.add(new Label("Sender beim Programmstart starten:"), 0, row, 2, 1);
        HBox hBox = new HBox(P2LibConst.SPACING_HBOX);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnPlay, btnStop, btnHelpAutoStart);
        gridPane.add(hBox, 2, row);
        GridPane.setHalignment(hBox, HPos.RIGHT);

        gridPane.add(rbNothing, 0, ++row);

        gridPane.add(rbLastPlayed, 0, ++row);
        gridPane.add(lblLastPlayed, 1, row, 2, 1);

        gridPane.add(rbAuto, 0, ++row);
        gridPane.add(lblAuto, 1, row, 2, 1);

        gridPane.add(rbList, 0, ++row);
        HBox hBoxList = new HBox(P2LibConst.SPACING_HBOX);
        hBoxList.getChildren().addAll(rbListStation, rbListFavourite, rbListHistory, rbListOwn);
        hBoxList.setAlignment(Pos.CENTER_LEFT);
        gridPane.add(hBoxList, 1, row, 2, 1);
    }

    private void setSelected() {
        rbListStation.setSelected(true);
        switch (ProgConfig.SYSTEM_AUTO_START.get()) {
            case AutoStartFactory.AUTOSTART_LAST_PLAYED:
                rbLastPlayed.setSelected(true);
                break;
            case AutoStartFactory.AUTOSTART_AUTO:
                rbAuto.setSelected(true);
                break;
            case AutoStartFactory.AUTOSTART_LIST_STATION:
                rbList.setSelected(true);
                rbListStation.setSelected(true);
                break;
            case AutoStartFactory.AUTOSTART_LIST_FAVOURITE:
                rbList.setSelected(true);
                rbListFavourite.setSelected(true);
                break;
            case AutoStartFactory.AUTOSTART_LIST_HISTORY:
                rbList.setSelected(true);
                rbListHistory.setSelected(true);
                break;
            case AutoStartFactory.AUTOSTART_LIST_OWN:
                rbList.setSelected(true);
                rbListOwn.setSelected(true);
                break;
            default:
                rbNothing.setSelected(true);
        }
    }

    private void getSelected() {
        if (rbNothing.isSelected()) {
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_NOTHING);

        } else if (rbLastPlayed.isSelected()) {
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LAST_PLAYED);

        } else if (rbAuto.isSelected()) {
            ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_AUTO);

        } else if (rbList.isSelected()) {
            if (rbListStation.isSelected()) {
                ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LIST_STATION);
            } else if (rbListFavourite.isSelected()) {
                ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LIST_FAVOURITE);
            } else if (rbListHistory.isSelected()) {
                ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LIST_HISTORY);
            } else {
                ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LIST_OWN);
            }
        }
    }
}
