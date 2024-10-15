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
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.ProgIcons;
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
    private final RadioButton rbLastPlayed = new RadioButton("Letzter gespielter");
    private final RadioButton rbAuto = new RadioButton("Gewählter Autostart");

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
        switch (ProgConfig.SYSTEM_AUTO_START.get()) {
            case AutoStartFactory.AUTOSTART_LAST_PLAYED:
                rbLastPlayed.setSelected(true);
                break;
            case AutoStartFactory.AUTOSTART_AUTO:
                rbAuto.setSelected(true);
                break;
            default:
                rbNothing.setSelected(true);
        }
        rbNothing.setOnAction(a -> ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_NOTHING));
        rbLastPlayed.setOnAction(a -> ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_LAST_PLAYED));
        rbAuto.setOnAction(a -> ProgConfig.SYSTEM_AUTO_START.set(AutoStartFactory.AUTOSTART_AUTO));

        final Button btnHelpAutoStart = P2Button.helpButton(stage, "Sender AutoStart",
                HelpText.AUTO_START);
        GridPane.setHalignment(btnHelpAutoStart, HPos.RIGHT);

        lblLastPlayed.setStyle("-fx-border-color: black;");
        lblLastPlayed.setPadding(new Insets(5));
        lblLastPlayed.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblLastPlayed, Priority.ALWAYS);
        lblLastPlayed.setText(ProgData.getInstance().stationLastPlayed.getStationName() +
                "\n" +
                ProgData.getInstance().stationLastPlayed.getStationUrl());

        lblAuto.setStyle("-fx-border-color: black;");
        lblAuto.setPadding(new Insets(5));
        lblAuto.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(lblAuto, Priority.ALWAYS);
        lblAuto.setText(ProgData.getInstance().stationAutoStart.getStationName() +
                "\n" +
                ProgData.getInstance().stationAutoStart.getStationUrl());

        final Button btnClearLast = new Button("");
        btnClearLast.setTooltip(new Tooltip("Löschen"));
        btnClearLast.setGraphic(ProgIcons.ICON_BUTTON_RESET.getImageView());
        btnClearLast.setOnAction((ActionEvent event) -> {
            lblLastPlayed.setText("");
            progData.stationLastPlayed.switchOffAuto();
        });
        btnClearLast.disableProperty().bind(lblLastPlayed.textProperty().isEmpty());

        final Button btnPlayLast = new Button("");
        btnPlayLast.setTooltip(new Tooltip("Sender abspielen"));
        btnPlayLast.setGraphic(ProgIcons.ICON_BUTTON_PLAY.getImageView());
        btnPlayLast.setOnAction((ActionEvent event) -> {
            StartFactory.playPlayable(progData.stationLastPlayed);
        });
        btnPlayLast.disableProperty().bind(lblLastPlayed.textProperty().isEmpty());

        final Button btnStopLast;
        btnStopLast = new Button("");
        btnStopLast.setTooltip(new Tooltip("Sender stoppen"));
        btnStopLast.setGraphic(ProgIcons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStopLast.setOnAction((ActionEvent event) -> {
            StartFactory.stopRunningStation();
        });
        btnStopLast.disableProperty().bind(lblLastPlayed.textProperty().isEmpty());

        final Button btnClearAuto = new Button("");
        btnClearAuto.setTooltip(new Tooltip("Löschen"));
        btnClearAuto.setGraphic(ProgIcons.ICON_BUTTON_RESET.getImageView());
        btnClearAuto.setOnAction((ActionEvent event) -> {
            lblAuto.setText("");
            progData.stationAutoStart.switchOffAuto();
        });
        btnClearAuto.disableProperty().bind(lblAuto.textProperty().isEmpty());

        final Button btnPlayAuto = new Button("");
        btnPlayAuto.setTooltip(new Tooltip("Sender abspielen"));
        btnPlayAuto.setGraphic(ProgIcons.ICON_BUTTON_PLAY.getImageView());
        btnPlayAuto.setOnAction((ActionEvent event) -> {
            StartFactory.playPlayable(progData.stationAutoStart);
        });
        btnPlayAuto.disableProperty().bind(lblAuto.textProperty().isEmpty());

        final Button btnStopAuto;
        btnStopAuto = new Button("");
        btnStopAuto.setTooltip(new Tooltip("Sender stoppen"));
        btnStopAuto.setGraphic(ProgIcons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStopAuto.setOnAction((ActionEvent event) -> {
            StartFactory.stopRunningStation();
        });
        btnStopAuto.disableProperty().bind(lblAuto.textProperty().isEmpty());

        int row = 0;
        gridPane.add(new Label("Sender beim Programmstart starten:"), 0, row, 2, 1);
        gridPane.add(btnHelpAutoStart, 4, row);

        gridPane.add(rbNothing, 0, ++row);

        gridPane.add(rbLastPlayed, 0, ++row);
        gridPane.add(lblLastPlayed, 1, row);
        HBox hBoxLast = new HBox(P2LibConst.SPACING_HBOX);
        hBoxLast.getChildren().addAll(btnClearLast, btnPlayLast, btnStopLast);
        hBoxLast.setAlignment(Pos.CENTER);
        gridPane.add(hBoxLast, 2, row);

        gridPane.add(rbAuto, 0, ++row);
        gridPane.add(lblAuto, 1, row);
        HBox hBoxAuto = new HBox(P2LibConst.SPACING_HBOX);
        hBoxAuto.getChildren().addAll(btnClearAuto, btnPlayAuto, btnStopAuto);
        hBoxAuto.setAlignment(Pos.CENTER);
        gridPane.add(hBoxAuto, 2, row);
    }
}
