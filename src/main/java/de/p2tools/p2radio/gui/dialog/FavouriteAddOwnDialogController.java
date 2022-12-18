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

package de.p2tools.p2radio.gui.dialog;

import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class FavouriteAddOwnDialogController extends PDialogExtra {

    private static final PColorData FAVOURITE_ADD_DIALOG_NAME_URL_ERROR =
            new PColorData("", Color.rgb(255, 233, 233),
                    Color.rgb(200, 183, 183));
    private final GridPane gridPane = new GridPane();
    private final Label[] lbl = new Label[StationDataXml.MAX_ELEM];
    private final Label[] lblCont = new Label[StationDataXml.MAX_ELEM];
    private final CheckBox[] cbx = new CheckBox[StationDataXml.MAX_ELEM];
    private final TextField[] txt = new TextField[StationDataXml.MAX_ELEM];
    private final ComboBox<String> cboCollection = new ComboBox<>();
    private final TextArea taDescription = new TextArea();
    private final ProgData progData;
    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");
    private final StationData stationData;
    private boolean ok = false;

    public FavouriteAddOwnDialogController(ProgData progData, StationData stationData) {
        super(progData.primaryStage, ProgConfig.FAVOURITE_DIALOG_ADD_SIZE,
                "Favoriten hinzufügen", true, false);

        this.progData = progData;
        this.stationData = stationData;

        init(true);
    }

    public void make() {
        cboCollection.setMaxWidth(Double.MAX_VALUE);
        cboCollection.setEditable(true);
        cboCollection.setItems(progData.collectionList.getNames());
        cboCollection.getSelectionModel().select(stationData.getCollectionName());

        taDescription.setEditable(true);

        addOkCancelButtons(btnOk, btnCancel);
        initCont();
        initButton();
        initGridPane();
    }

    public boolean isOk() {
        return ok;
    }

    private void initCont() {
        getvBoxCont().getChildren().add(gridPane);
    }

    private void initButton() {
        btnOk.disableProperty().bind(stationData.stationUrlProperty().isEmpty().or(stationData.stationNameProperty().isEmpty()));
        btnOk.setOnAction(event -> {
            saveAct();
            ok = true;
            close();
        });
        btnCancel.setOnAction(event -> {
            ok = false;
            close();
        });
    }

    private void saveAct() {
        stationData.setCollectionName(cboCollection.getValue());
    }

    private void initGridPane() {
        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        for (int i = 0; i < StationDataXml.MAX_ELEM; ++i) {
            lbl[i] = new Label(StationDataXml.COLUMN_NAMES[i] + ":");
            lbl[i].setPadding(new Insets(2, 0, 2, 0));
            lblCont[i] = new Label("");

            txt[i] = new TextField("");
            txt[i].setEditable(true);
            txt[i].setMaxWidth(Double.MAX_VALUE);
            txt[i].setPrefWidth(Control.USE_COMPUTED_SIZE);
            final int ii = i;
            txt[i].setOnContextMenuRequested(event ->
                    getMenu(txt[ii].getText()).show(txt[ii], event.getScreenX(), event.getScreenY()));


            cbx[i] = new CheckBox();
            cbx[i].setDisable(true);
        }

        for (int i = 0; i < StationDataXml.MAX_ELEM; ++i) {
            row = setGrid(i, row);
        }
    }

    private int setGrid(int i, int row) {
        PHyperlink hyperlink;
        switch (i) {
            case StationDataXml.STATION_PROP_NO_INT:
            case StationDataXml.STATION_PROP_STATION_NO_INT:
            case StationDataXml.STATION_PROP_BUTTON1_INT:
            case StationDataXml.STATION_PROP_BUTTON2_INT:
            case StationDataXml.STATION_PROP_CLICK_COUNT_INT:
            case StationDataXml.STATION_PROP_DATE_INT:
                // bis hier nicht anzeigen
                break;
            case StationDataXml.STATION_PROP_STATION_NAME_INT:
                addCheck(txt[i]);
                txt[i].textProperty().bindBidirectional(stationData.stationNameProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
            case StationDataXml.STATION_PROP_COLLECTION_INT:
                lblCont[i].textProperty().bindBidirectional(stationData.collectionNameProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(cboCollection, 1, row, 3, 1);
                ++row;
                break;
            case StationDataXml.STATION_PROP_GENRE_INT:
                txt[i].textProperty().bindBidirectional(stationData.genreProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
            case StationDataXml.STATION_PROP_CODEC_INT:
                txt[i].textProperty().bindBidirectional(stationData.codecProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row);

                txt[StationDataXml.STATION_PROP_BITRATE_INT].textProperty().bindBidirectional(stationData.bitrateProperty());
                gridPane.add(lbl[StationDataXml.STATION_PROP_BITRATE_INT], 2, row);
                gridPane.add(txt[StationDataXml.STATION_PROP_BITRATE_INT], 3, row);
                ++row;
                break;
            case StationDataXml.STATION_PROP_COUNTRY_INT:
                txt[i].textProperty().bindBidirectional(stationData.countryProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row);

                txt[StationDataXml.STATION_PROP_LANGUAGE_INT].textProperty().bindBidirectional(stationData.languageProperty());
                gridPane.add(lbl[StationDataXml.STATION_PROP_LANGUAGE_INT], 2, row);
                gridPane.add(txt[StationDataXml.STATION_PROP_LANGUAGE_INT], 3, row);
                ++row;
                break;
            case StationDataXml.STATION_PROP_DESCRIPTION_INT:
                lbl[i].setTextFill(Color.BLUE);
                taDescription.textProperty().bindBidirectional(stationData.descriptionProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(taDescription, 1, row, 3, 1);
                ++row;
                break;
            case StationDataXml.STATION_PROP_URL_INT:
                addCheck(txt[i]);
                txt[i].textProperty().bindBidirectional(stationData.stationUrlProperty());

                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
            case StationDataXml.STATION_PROP_WEBSITE_INT:
                txt[i].textProperty().bindBidirectional(stationData.websiteProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
        }

        if (i == StationDataXml.STATION_PROP_COLLECTION_INT || txt[i].isEditable() || !cbx[i].isDisabled()) {
            lbl[i].setTextFill(Color.BLUE);
        }
        return row;
    }

    private ContextMenu getMenu(String str) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItem = new MenuItem("kopieren");
        menuItem.setOnAction(a -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(str);
            clipboard.setContent(content);
        });
        contextMenu.getItems().addAll(menuItem);
        return contextMenu;
    }

    private void addCheck(TextField txtF) {
        txtF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txtF.getText().isEmpty()) {
                txtF.setStyle(FAVOURITE_ADD_DIALOG_NAME_URL_ERROR.getCssBackground());
            } else {
                txtF.setStyle("");
            }
        });
        if (txtF.getText().isEmpty()) {
            txtF.setStyle(FAVOURITE_ADD_DIALOG_NAME_URL_ERROR.getCssBackground());
        } else {
            txtF.setStyle("");
        }
    }
}
