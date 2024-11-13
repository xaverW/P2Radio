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
import de.p2tools.p2lib.data.P2ColorData;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2lib.tools.P2ColorFactory;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Collection;

public class PaneColor {
    private final Stage stage;
    private final P2ToggleSwitch tglDarkTheme = new P2ToggleSwitch("Dunkles Erscheinungsbild der Programmoberfläche");

    private final Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryUse
            = (final TableColumn<P2ColorData, String> param) -> new TableCell<>() {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                setText(null);
                return;
            }

            P2ColorData pColorData = getTableView().getItems().get(getIndex());
            final HBox hbox = new HBox();
            hbox.setSpacing(5);
            hbox.setAlignment(Pos.CENTER);
            hbox.setPadding(new Insets(0, 2, 0, 2));

            final CheckBox checkBox = new CheckBox("");
            checkBox.setSelected(pColorData.isUse());
            checkBox.setOnAction(a -> {
                pColorData.setUse(checkBox.isSelected());
                // ProgConfig.SYSTEM_THEME_CHANGED.set(!ProgConfig.SYSTEM_THEME_CHANGED.get()); entweder / oder direkt
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.REFRESH_TABLE));
            });

            hbox.getChildren().add(checkBox);
            setGraphic(hbox);
        }
    };

    private final Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryChange
            = (final TableColumn<P2ColorData, String> param) -> new TableCell<>() {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                setText(null);
                return;
            }

            P2ColorData p2ColorData = getTableView().getItems().get(getIndex());
            final HBox hbox = new HBox();
            hbox.setSpacing(5);
            hbox.setAlignment(Pos.CENTER);
            hbox.setPadding(new Insets(0, 2, 0, 2));

            final ColorPicker colorPicker = new ColorPicker();
            colorPicker.getStyleClass().add("split-button");

            colorPicker.setValue(p2ColorData.getColor());
            colorPicker.setOnAction(a -> {
                Color fxColor = colorPicker.getValue();
                p2ColorData.setColor(fxColor);
                // ProgConfig.SYSTEM_THEME_CHANGED.set(!ProgConfig.SYSTEM_THEME_CHANGED.get()); entweder / oder direkt
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.REFRESH_TABLE));
            });
            hbox.getChildren().addAll(colorPicker);
            setGraphic(hbox);
        }
    };

    private final Callback<TableColumn<P2ColorData, Color>, TableCell<P2ColorData, Color>> cellFactoryColor
            = (final TableColumn<P2ColorData, Color> param) -> new TableCell<>() {

        @Override
        public void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                setText(null);
                return;
            }

            P2ColorData p2ColorData = getTableView().getItems().get(getIndex());
            setStyle("-fx-background-color:" + p2ColorData.getColorSelectedToWeb());
        }
    };

    private final Callback<TableColumn<P2ColorData, Color>, TableCell<P2ColorData, Color>> cellFactoryColorReset
            = (final TableColumn<P2ColorData, Color> param) -> new TableCell<>() {

        @Override
        public void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                setText(null);
                return;
            }

            P2ColorData p2ColorData = getTableView().getItems().get(getIndex());
            setStyle("-fx-background-color:" + P2ColorFactory.getColorToWeb(p2ColorData.getResetColor()));
        }
    };

    private final Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryReset
            = (final TableColumn<P2ColorData, String> param) -> new TableCell<>() {

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setGraphic(null);
                setText(null);
                return;
            }

            P2ColorData p2ColorData = getTableView().getItems().get(getIndex());
            final HBox hbox = new HBox();
            hbox.setSpacing(5);
            hbox.setAlignment(Pos.CENTER);
            hbox.setPadding(new Insets(0, 2, 0, 2));

            final Button button = new Button("Reset");
            button.setOnAction(a -> {
                p2ColorData.resetColor();
                // ProgConfig.SYSTEM_THEME_CHANGED.set(!ProgConfig.SYSTEM_THEME_CHANGED.get()); entweder / oder direkt
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.REFRESH_TABLE));
            });

            hbox.getChildren().add(button);
            setGraphic(hbox);
        }
    };

    public PaneColor(Stage stage) {
        this.stage = stage;
    }

    public void make(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setFillWidth(true);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(P2LibConst.PADDING));

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);

        tglDarkTheme.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DARK_THEME);
        final Button btnHelpTheme = P2Button.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.DARK_THEME);

        gridPane.add(tglDarkTheme, 0, 0);
        gridPane.add(btnHelpTheme, 1, 0);
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());

        TableView<P2ColorData> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        initTableColor(tableView);
        tglDarkTheme.selectedProperty().addListener((u, o, n) -> {
            tableView.refresh();
        });

        Button button = new Button("Alle _Farben zurücksetzen");
        button.setOnAction(event -> {
            ProgColorList.resetAllColor();
            // ProgConfig.SYSTEM_THEME_CHANGED.set(!ProgConfig.SYSTEM_THEME_CHANGED.get()); entweder / oder direkt
            ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.REFRESH_TABLE));
        });
        HBox hBox = new HBox();
        hBox.getChildren().add(button);
        hBox.setPadding(new Insets(0));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().addAll(gridPane, tableView, hBox);

        TitledPane tpColor = new TitledPane("Farben", vBox);
        result.add(tpColor);
    }

    public void close() {
    }

    private void initTableColor(TableView<P2ColorData> tableView) {
        final TableColumn<P2ColorData, String> useColumn = new TableColumn<>("Verwenden");
        useColumn.setCellFactory(cellFactoryUse);
        useColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, String> textColumn = new TableColumn<>("Beschreibung");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<P2ColorData, String> changeColumn = new TableColumn<>("Farbe");
        changeColumn.setCellFactory(cellFactoryChange);
        changeColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);
        colorColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, Color> colorOrgColumn = new TableColumn<>("Original");
        colorOrgColumn.setCellValueFactory(new PropertyValueFactory<>("resetColor"));
        colorOrgColumn.setCellFactory(cellFactoryColorReset);
        colorOrgColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, String> resetColumn = new TableColumn<>("Reset");
        resetColumn.setCellFactory(cellFactoryReset);
        resetColumn.getStyleClass().add("alignCenter");

        tableView.setMinHeight(ProgConst.MIN_TABLE_HEIGHT);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableView.getColumns().addAll(useColumn, textColumn, changeColumn, colorColumn, colorOrgColumn, resetColumn);
        tableView.setItems(ProgColorList.getInstance());
    }
}
