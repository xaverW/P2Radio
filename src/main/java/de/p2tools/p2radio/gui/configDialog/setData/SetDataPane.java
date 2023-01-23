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


package de.p2tools.p2radio.gui.configDialog.setData;

import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PStyles;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
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

public class SetDataPane extends TitledPane {
    private final TextField txtVisibleName = new TextField("");
    private final TextArea txtDescription = new TextArea("");
    private final TextField txtProgPath = new TextField();
    private final TextField txtProgSwitch = new TextField();
    private final Stage stage;
    private final SetPanePack setPanePack;

    private ChangeListener<SetData> changeListener;
    private SetData setData = null;

    SetDataPane(SetPanePack setPanePack) {
        this.setPanePack = setPanePack;
        this.stage = setPanePack.getStage();
    }

    public void close() {
        unBindProgData();
    }

    public void makePane() {
        changeListener = (observable, oldValue, newValue) -> {
            bindProgData(newValue);
        };

        setPanePack.aktSetDateProperty().addListener(changeListener);
        bindProgData(setPanePack.aktSetDateProperty().getValue());

        VBox vBox = new VBox(10);
        vBox.setFillWidth(true);
        vBox.setPadding(new Insets(10));

        this.setText("Set Einstellungen");
        this.setContent(vBox);
        this.setCollapsible(false);
        this.setMaxHeight(Double.MAX_VALUE);

        final Button btnFile = new Button();
        btnFile.setOnAction(event -> PDirFileChooser.FileChooserOpenFile(ProgData.getInstance().primaryStage, txtProgPath));
        btnFile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnFile.setTooltip(new Tooltip("Ein Programm zum verarbeiten der URL auswählen"));

        txtProgPath.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(txtProgPath.getText());
            if (!file.exists() || !file.isFile()) {
                txtProgPath.setStyle(PStyles.PTEXTFIELD_ERROR);
            } else {
                txtProgPath.setStyle("");
            }
        });
        final Button btnFind = new Button("suchen");
        btnFind.setOnAction(event -> {
            ProgConfig.SYSTEM_PATH_VLC.setValue("");
            txtProgPath.setText(SetFactory.getTemplatePathVlc());
        });
        final Button btnHelpSearch = PButton.helpButton(stage,
                "Videoplayer", HelpText.PROG_PATHS);

        txtDescription.setWrapText(true);
        
        // Name, Beschreibung
        int row = 0;
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        gridPane.add(new Label("Name:"), 0, row);
        gridPane.add(txtVisibleName, 1, row, 4, 1);

        gridPane.add(new Label("Beschreibung:"), 0, ++row);
        gridPane.add(txtDescription, 1, row, 4, 1);

        gridPane.add(new Label("Programm:"), 0, ++row);
        gridPane.add(txtProgPath, 1, row);
        gridPane.add(btnFile, 2, row);
        gridPane.add(btnFind, 3, row);
        gridPane.add(btnHelpSearch, 4, row);


        gridPane.add(new Label("Schalter:"), 0, ++row);
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

    public void bindProgData(SetData setData) {
        unBindProgData();

        this.setData = setData;
        if (setData != null) {
            txtVisibleName.textProperty().bindBidirectional(setData.visibleNameProperty());
            txtProgPath.textProperty().bindBidirectional(setData.progPathProperty());
            txtProgSwitch.textProperty().bindBidirectional(setData.progSwitchProperty());
            txtDescription.textProperty().bindBidirectional(setData.descriptionProperty());
        }
    }

    void unBindProgData() {
        if (setData != null) {
            txtVisibleName.textProperty().unbindBidirectional(setData.visibleNameProperty());
            txtProgPath.textProperty().unbindBidirectional(setData.progPathProperty());
            txtProgSwitch.textProperty().unbindBidirectional(setData.progSwitchProperty());
            txtDescription.textProperty().unbindBidirectional(setData.descriptionProperty());
        }
    }
}
