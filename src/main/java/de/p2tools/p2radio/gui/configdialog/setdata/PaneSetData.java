/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.guitools.PButton;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PStyles;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.gui.tools.HelpText;
import de.p2tools.p2radio.gui.tools.HelpTextPset;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class PaneSetData extends TitledPane {
    private final TextField txtVisibleName = new TextField("");
    private final TextArea txtDescription = new TextArea("");
    private final TextField txtProgPath = new TextField();
    private final TextField txtProgSwitch = new TextField();
    private final Button btnFile = new Button();
    private final Button btnFind = new Button("Suchen");
    private final Button btnHelpSearch;
    private final Label lblName = new Label("Name:");
    private final Label lblDescription = new Label("Beschreibung:");
    private final Label lblProgram = new Label("Programm:");
    private final Label lblSwitch = new Label("Schalter:");

    private final Stage stage;
    private final ControllerSet controllerSet;

    private ChangeListener<SetData> changeListener;
    private SetData setData = null;

    PaneSetData(ControllerSet controllerSet) {
        this.controllerSet = controllerSet;
        this.stage = controllerSet.getStage();
        this.btnHelpSearch = PButton.helpButton(stage,
                "Videoplayer", HelpText.PROG_PATHS);
        makePane();
    }

    public void close() {
        unBindProgData();
    }

    private void makePane() {
        changeListener = (observable, oldValue, newValue) -> {
            bindProgData(newValue);
        };

        controllerSet.aktSetDateProperty().addListener(changeListener);
        bindProgData(controllerSet.aktSetDateProperty().getValue());

        VBox vBox = new VBox(P2LibConst.DIST_BUTTON);
        vBox.setPadding(new Insets(P2LibConst.DIST_EDGE));
        vBox.setFillWidth(true);

        this.setText("Set Einstellungen");
        this.setContent(vBox);
        this.setCollapsible(false);
        this.setMaxHeight(Double.MAX_VALUE);

        btnFile.setOnAction(event -> PDirFileChooser.FileChooserOpenFile(ProgData.getInstance().primaryStage, txtProgPath));
        btnFile.setGraphic(ProgIconsP2Radio.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Ein Programm zum verarbeiten der URL auswählen"));

        txtProgPath.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(txtProgPath.getText());
            if (!file.exists() || !file.isFile()) {
                txtProgPath.setStyle(PStyles.PTEXTFIELD_ERROR);
            } else {
                txtProgPath.setStyle("");
            }
        });
        btnFind.setOnAction(event -> {
            txtProgPath.setText(SetFactory.getTemplatePathVlc());
        });

        txtDescription.setWrapText(true);

        // Name, Beschreibung
        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
//        gridPane.setPadding(new Insets(20));

        gridPane.add(lblName, 0, row);
        gridPane.add(txtVisibleName, 1, row, 4, 1);

        gridPane.add(lblDescription, 0, ++row);
        gridPane.add(txtDescription, 1, row, 4, 1);

        gridPane.add(lblProgram, 0, ++row);
        gridPane.add(txtProgPath, 1, row);
        gridPane.add(btnFile, 2, row);
        gridPane.add(btnFind, 3, row);
        gridPane.add(btnHelpSearch, 4, row);


        gridPane.add(lblSwitch, 0, ++row);
        gridPane.add(txtProgSwitch, 1, row, 4, 1);


        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());
        vBox.getChildren().add(gridPane);

        final Button btnHelp = PButton.helpButton(stage, "Set", HelpTextPset.HELP_PSET);
        HBox hBox = new HBox();
        VBox.setVgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(btnHelp);
        vBox.getChildren().add(hBox);
    }

    private void bindProgData(SetData setData) {
        unBindProgData();

        this.setData = setData;
        if (setData != null) {
            txtVisibleName.textProperty().bindBidirectional(setData.visibleNameProperty());
            txtProgPath.textProperty().bindBidirectional(setData.progPathProperty());
            txtProgSwitch.textProperty().bindBidirectional(setData.progSwitchProperty());
            txtDescription.textProperty().bindBidirectional(setData.descriptionProperty());
            setDis(false);

        } else {
            txtVisibleName.setText("");
            txtProgPath.setText("");
            txtProgSwitch.setText("");
            txtDescription.setText("");
            setDis(true);
        }
    }

    private void setDis(boolean set) {
        txtVisibleName.setDisable(set);
        txtProgPath.setDisable(set);
        txtProgSwitch.setDisable(set);
        txtDescription.setDisable(set);
        btnFile.setDisable(set);
        btnFind.setDisable(set);
        btnHelpSearch.setDisable(set);
        lblName.setDisable(set);
        lblDescription.setDisable(set);
        lblProgram.setDisable(set);
        lblSwitch.setDisable(set);
    }

    private void unBindProgData() {
        if (setData != null) {
            txtVisibleName.textProperty().unbindBidirectional(setData.visibleNameProperty());
            txtProgPath.textProperty().unbindBidirectional(setData.progPathProperty());
            txtProgSwitch.textProperty().unbindBidirectional(setData.progSwitchProperty());
            txtDescription.textProperty().unbindBidirectional(setData.descriptionProperty());
        }
    }
}
