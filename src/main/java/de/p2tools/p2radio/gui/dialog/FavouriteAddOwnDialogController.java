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

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteXml;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

public class FavouriteAddOwnDialogController extends PDialogExtra {

    private Button btnOk = new Button("_Ok");
    private Button btnCancel = new Button("_Abbrechen");

    private boolean ok = false;
    private final GridPane gridPane = new GridPane();
    private final Label[] lbl = new Label[FavouriteXml.MAX_ELEM];
    private final Label[] lblCont = new Label[FavouriteXml.MAX_ELEM];
    private final CheckBox[] cbx = new CheckBox[FavouriteXml.MAX_ELEM];
    private final TextField[] txt = new TextField[FavouriteXml.MAX_ELEM];
    private final ComboBox<String> cboCollection = new ComboBox<>();
    private final TextArea taDescription = new TextArea();

    private Favourite favourite;
    private final ProgData progData;

    public FavouriteAddOwnDialogController(ProgData progData, Favourite favourite) {
        super(progData.primaryStage, ProgConfig.FAVOURITE_DIALOG_ADD_SIZE,
                "Favoriten hinzufügen", true, false);

        this.progData = progData;
        this.favourite = favourite;

        init(true);
    }

    public void make() {
        cboCollection.setMaxWidth(Double.MAX_VALUE);
        cboCollection.setEditable(true);
        cboCollection.setItems(progData.collectionList.getNames());
        cboCollection.getSelectionModel().select(favourite.getCollectionName());

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
        btnOk.disableProperty().bind(favourite.stationUrlProperty().isEmpty().or(favourite.stationNameProperty().isEmpty()));
        btnOk.setOnAction(event -> {
//            if (favourite.getStationUrl().isEmpty()) {
//                PAlert.showErrorAlert("Sender-URL", "Es muss wenigstens eine URL für den " +
//                        "Sender angeben werden.");
//                return;
//            }

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
        favourite.setCollectionName(cboCollection.getValue());
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
        for (int i = 0; i < FavouriteXml.MAX_ELEM; ++i) {
            lbl[i] = new Label(FavouriteXml.COLUMN_NAMES[i] + ":");
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

        for (int i = 0; i < FavouriteXml.MAX_ELEM; ++i) {
            row = setGrid(i, row);
        }
    }

    private int setGrid(int i, int row) {
        PHyperlink hyperlink;
        switch (i) {
            case FavouriteXml.FAVOURITE_NO:
            case FavouriteXml.FAVOURITE_STATION_NO:
            case FavouriteXml.FAVOURITE_BUTTON1:
            case FavouriteXml.FAVOURITE_BUTTON2:
            case FavouriteXml.FAVOURITE_CLICK_COUNT:
            case FavouriteXml.FAVOURITE_DATE:
                // bis hier nicht anzeigen
                break;
            case FavouriteXml.FAVOURITE_STATION:
                addCheck(txt[i]);
                txt[i].textProperty().bindBidirectional(favourite.stationNameProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_COLLECTION:
                lblCont[i].textProperty().bindBidirectional(favourite.collectionNameProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(cboCollection, 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_GENRE:
                txt[i].textProperty().bindBidirectional(favourite.genreProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_CODEC:
                txt[i].textProperty().bindBidirectional(favourite.codecProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row);

                txt[FavouriteXml.FAVOURITE_BITRATE].textProperty().bindBidirectional(favourite.bitrateProperty(), new NumberStringConverter());
                gridPane.add(lbl[FavouriteXml.FAVOURITE_BITRATE], 2, row);
                gridPane.add(txt[FavouriteXml.FAVOURITE_BITRATE], 3, row);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_COUNTRY:
                txt[i].textProperty().bindBidirectional(favourite.countryProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row);

                txt[FavouriteXml.FAVOURITE_LANGUAGE].textProperty().bindBidirectional(favourite.languageProperty());
                gridPane.add(lbl[FavouriteXml.FAVOURITE_LANGUAGE], 2, row);
                gridPane.add(txt[FavouriteXml.FAVOURITE_LANGUAGE], 3, row);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_DESCRIPTION:
                lbl[i].setTextFill(Color.BLUE);
                taDescription.textProperty().bindBidirectional(favourite.descriptionProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(taDescription, 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_URL:
                addCheck(txt[i]);
                txt[i].textProperty().bindBidirectional(favourite.stationUrlProperty());

                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_WEBSITE:
                txt[i].textProperty().bindBidirectional(favourite.websiteProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(txt[i], 1, row, 3, 1);
                ++row;
                break;
        }

        if (i == FavouriteXml.FAVOURITE_COLLECTION || txt[i].isEditable() || !cbx[i].isDisabled()) {
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
                txtF.setStyle(ProgColorList.FAVOURITE_ADD_DIALOG_NAME_URL_ERROR.getCssBackground());
            } else {
                txtF.setStyle("");
            }
        });
        if (txtF.getText().isEmpty()) {
            txtF.setStyle(ProgColorList.FAVOURITE_ADD_DIALOG_NAME_URL_ERROR.getCssBackground());
        } else {
            txtF.setStyle("");
        }
    }
}
