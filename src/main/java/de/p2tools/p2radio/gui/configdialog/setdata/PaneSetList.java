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
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ImportSetDataFactory;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.SetFactory;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PaneSetList extends TitledPane {

    static int newCounter = 1;
    private final ProgData progData;
    private final TableView<SetData> tableView = new TableView<>();
    private final ControllerSet controllerSet;

    public PaneSetList(ControllerSet controllerSet) {
        this.controllerSet = controllerSet;
        this.progData = ProgData.getInstance();

        make();
    }

    public void close() {
    }

    private void make() {
        final VBox vBox = new VBox(P2LibConst.DIST_BUTTON);
        initTable(vBox);

        this.setText("Sets");
        this.setContent(vBox);
        this.setCollapsible(false);
        this.setMaxHeight(Double.MAX_VALUE);
    }

    private void initTable(VBox vBox) {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            controllerSet.aktSetDateProperty().setValue(newValue);
        });

        final TableColumn<SetData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("visibleName"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(nameColumn);
        tableView.setItems(progData.setDataList);
        if (tableView.getItems().size() > 0) {
            tableView.getSelectionModel().select(0);
        }
        tableView.getSelectionModel().selectFirst();

        VBox.setVgrow(tableView, Priority.ALWAYS);
        vBox.getChildren().addAll(tableView);

        Button btnDel = new Button("");
        btnDel.setTooltip(new Tooltip("Markiertes Set löschen"));
        btnDel.setGraphic(ProgIconsP2Radio.ICON_BUTTON_REMOVE.getImageView());
        btnDel.setOnAction(event -> {
            SetData setData = getSelectedSelData();
            if (setData != null) {
                progData.setDataList.removeSetData(setData);
            }
        });

        Button btnNew = new Button("");
        btnNew.setTooltip(new Tooltip("Ein neues Set anlegen"));
        btnNew.setGraphic(ProgIconsP2Radio.ICON_BUTTON_ADD.getImageView());
        btnNew.setOnAction(event -> {
            SetData setData = new SetData("Neu-" + ++newCounter);
            progData.setDataList.addSetData(setData);
        });

        Button btnUp = new Button("");
        btnUp.setTooltip(new Tooltip("Markiertes Set nach oben schieben"));
        btnUp.setGraphic(ProgIconsP2Radio.ICON_BUTTON_MOVE_UP.getImageView());
        btnUp.setOnAction(event -> {
            int sel = getSelectedLine();
            if (sel >= 0) {
                int newSel = progData.setDataList.up(sel, true);
                tableView.getSelectionModel().select(newSel);
            }
        });

        Button btnDown = new Button("");
        btnDown.setTooltip(new Tooltip("Markiertes Set nach unten schieben"));
        btnDown.setGraphic(ProgIconsP2Radio.ICON_BUTTON_MOVE_DOWN.getImageView());
        btnDown.setOnAction(event -> {
            int sel = getSelectedLine();
            if (sel >= 0) {
                int newSel = progData.setDataList.up(sel, false);
                tableView.getSelectionModel().select(newSel);
            }
        });

        Button btnStandard = new Button("Als _Standardset setzen");
        btnStandard.setTooltip(new Tooltip("Das Set als Standardset festlegen"));
        btnStandard.setOnAction(event -> {
            SetData setData = getSelectedSelData();
            if (setData != null) {
                progData.setDataList.removeSetData(setData);
                progData.setDataList.add(0, setData);
                tableView.getSelectionModel().select(0);
            }
        });
        HBox.setHgrow(btnStandard, Priority.ALWAYS);
        btnStandard.setMaxWidth(Double.MAX_VALUE);

        Button btnDup = new Button("_Duplizieren");
        btnDup.setTooltip(new Tooltip("Eine Kopie des markierten Sets erstellen"));
        btnDup.setOnAction(event -> {
            SetData setData = getSelectedSelData();
            if (setData != null) {
                progData.setDataList.addSetData(setData.copy());
            }
        });
        HBox.setHgrow(btnDup, Priority.ALWAYS);
        btnDup.setMaxWidth(Double.MAX_VALUE);

        Button btnNewSet = new Button("Standardsets _anfügen");
        btnNewSet.setTooltip(new Tooltip("Standardsets erstellen und der Liste anfügen"));
        btnNewSet.setOnAction(event -> {
            if (!SetFactory.addSetTemplate(ImportSetDataFactory.getStandarset())) {
                P2Alert.showErrorAlert("Set importieren", "Set konnten nicht importiert werden!");
            }
        });
        HBox.setHgrow(btnNewSet, Priority.ALWAYS);
        btnNewSet.setMaxWidth(Double.MAX_VALUE);

        Button btnCheck = new Button("_Prüfen");
        btnCheck.setTooltip(new Tooltip("Die angelegten Sets überprüfen"));
        btnCheck.setOnAction(event -> SetFactory.checkPrograms(progData));
        HBox.setHgrow(btnCheck, Priority.ALWAYS);
        btnCheck.setMaxWidth(Double.MAX_VALUE);

        HBox hBox = new HBox(P2LibConst.DIST_BUTTON);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.getChildren().addAll(btnNew, btnDel, P2GuiTools.getHBoxGrower(), btnUp, btnDown);
        vBox.getChildren().addAll(hBox, btnStandard, btnDup, btnNewSet, btnCheck);
    }

    private SetData getSelectedSelData() {
        final SetData sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            P2Alert.showInfoNoSelection();
        }
        return sel;
    }

    private int getSelectedLine() {
        final int sel = tableView.getSelectionModel().getSelectedIndex();
        if (sel < 0) {
            P2Alert.showInfoNoSelection();
        }
        return sel;
    }
}