/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteXml;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class FavouriteEditDialogController extends PDialogExtra {

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
    private final Button btnPrev = new Button("<");
    private final Button btnNext = new Button(">");
    private final Label lblSum = new Label("");
    private final VBox vBoxAllFavourites = new VBox();
    private ArrayList<Favourite> favouriteList;

    private final HBox hBoxTop = new HBox();
    private Favourite actFavourite;
    private int actSender = 0;
    private final ProgData progData;

    public FavouriteEditDialogController(ProgData progData, ArrayList<Favourite> favouriteList) {
        super(progData.primaryStage, ProgConfig.FAVOURITE_DIALOG_EDIT_SIZE,
                "Favoriten ändern", true, false);

        this.progData = progData;
        this.favouriteList = favouriteList;

        if (favouriteList.size() == 0) {
            // Satz mit x, war wohl nix
            ok = false;
            close();
            return;
        }

        actFavourite = favouriteList.get(actSender).getCopy();
        init(true);
    }


    public void make() {
        cboCollection.setMaxWidth(Double.MAX_VALUE);
        cboCollection.setEditable(true);
        cboCollection.setItems(progData.collectionList.getNames());
        cboCollection.getSelectionModel().select(actFavourite.getCollectionName());

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
        if (favouriteList.size() > 1) {
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);
            hBoxTop.setPadding(new Insets(10));
            hBoxTop.getChildren().addAll(btnPrev, lblSum, btnNext);

            vBoxAllFavourites.getStyleClass().add("downloadDialog");
            vBoxAllFavourites.getChildren().addAll(hBoxTop);
            getvBoxCont().getChildren().add(vBoxAllFavourites);
        }
        getvBoxCont().getChildren().add(gridPane);
    }

    private void initButton() {
        btnPrev.setOnAction(event -> {
            saveAct();
            --actSender;
            changeAct(actSender);
            setStationNo();
        });
        btnNext.setOnAction(event -> {
            saveAct();
            ++actSender;
            changeAct(actSender);
            setStationNo();
        });
        setStationNo();

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

    private void changeAct(int newPos) {
        Favourite fNew = favouriteList.get(newPos);
        actFavourite.copyToMe(fNew);
        cboCollection.setValue(actFavourite.getCollectionName());
    }

    private void saveAct() {
        actFavourite.setCollectionName(cboCollection.getValue());
        Favourite f = favouriteList.get(actSender);
        f.copyToMe(actFavourite);
    }

    private void setStationNo() {
        final int nr = actSender + 1;
        lblSum.setText("Sender " + nr + " von " + favouriteList.size() + " Sendern");

        if (actSender == 0) {
            btnPrev.setDisable(true);
            btnNext.setDisable(false);
        } else if (actSender == favouriteList.size() - 1) {
            btnPrev.setDisable(false);
            btnNext.setDisable(true);
        } else {
            btnPrev.setDisable(false);
            btnNext.setDisable(false);
        }
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
            txt[i].setEditable(false);
            txt[i].setMaxWidth(Double.MAX_VALUE);
            txt[i].setPrefWidth(Control.USE_COMPUTED_SIZE);

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
                // bis hier nicht anzeigen
                break;
            case FavouriteXml.FAVOURITE_STATION_NO:
                lblCont[i].textProperty().bind(actFavourite.stationNoProperty().asString());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_STATION:
                lblCont[i].textProperty().bind(actFavourite.stationNameProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_COLLECTION:
                lblCont[i].textProperty().bind(actFavourite.collectionNameProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(cboCollection, 1, row, 3, 1);
                if (favouriteList.size() > 1) {
                    gridPane.add(addAllButton(i), 5, row);
                }
                ++row;
                break;
            case FavouriteXml.FAVOURITE_GENRE:
                lblCont[i].textProperty().bind(actFavourite.genreProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_CODEC:
                lblCont[i].textProperty().bind(actFavourite.codecProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);

                lblCont[FavouriteXml.FAVOURITE_BITRATE].textProperty().bind(actFavourite.bitrateProperty().asString());
                gridPane.add(lbl[FavouriteXml.FAVOURITE_BITRATE], 2, row);
                gridPane.add(lblCont[FavouriteXml.FAVOURITE_BITRATE], 3, row);

                ++row;
                break;
//            case FavouriteXml.FAVOURITE_BITRATE:
//                lblCont[i].textProperty().bind(favourite.bitrateProperty().asString());
//                gridPane.add(lbl[i], 0, row);
//                gridPane.add(lblCont[i], 1, row);
//                ++row;
//                break;

            case FavouriteXml.FAVOURITE_BUTTON1:
            case FavouriteXml.FAVOURITE_BUTTON2:
                break;

            case FavouriteXml.FAVOURITE_COUNTRY:
                final String text = actFavourite.getCountry() + ", " + actFavourite.getCountryCode();
                lblCont[i].textProperty().bind(actFavourite.countryProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);

                lblCont[FavouriteXml.FAVOURITE_LANGUAGE].textProperty().bind(actFavourite.languageProperty());
                gridPane.add(lbl[FavouriteXml.FAVOURITE_LANGUAGE], 2, row);
                gridPane.add(lblCont[FavouriteXml.FAVOURITE_LANGUAGE], 3, row);
                ++row;
                break;

//            case FavouriteXml.FAVOURITE_COUNTRY:
//                lblCont[i].setText(favourite.getCountry());
//                gridPane.add(lbl[i], 0, row);
//                gridPane.add(lblCont[i], 1, row);
//                ++row;
//                break;
//            case FavouriteXml.FAVOURITE_COUNTRY_CODE:
//                lblCont[i].textProperty().bind(favourite.countryCodeProperty());
//                gridPane.add(lbl[i], 0, row);
//                gridPane.add(lblCont[i], 1, row);
//                ++row;
//                break;
//            case FavouriteXml.FAVOURITE_LANGUAGE:
//                lblCont[i].textProperty().bind(favourite.languageProperty());
//                gridPane.add(lbl[i], 0, row);
//                gridPane.add(lblCont[i], 1, row);
//                ++row;
//                break;
            case FavouriteXml.FAVOURITE_DESCRIPTION:
                lbl[i].setTextFill(Color.BLUE);
                taDescription.textProperty().bindBidirectional(actFavourite.descriptionProperty());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(taDescription, 1, row, 3, 1);
                if (favouriteList.size() > 1) {
                    gridPane.add(addAllButton(i), 5, row);
                }
                ++row;
                break;
            case FavouriteXml.FAVOURITE_VOTES:
                lblCont[i].textProperty().bind(actFavourite.votesProperty().asString());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_CLICK_COUNT:
                lblCont[i].textProperty().bind(actFavourite.clickCountProperty().asString());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_CLICK_TREND:
                lblCont[i].textProperty().bind(actFavourite.clickTrendProperty().asString());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_URL:
                hyperlink = new PHyperlink(this.getStage(), actFavourite.urlProperty().getValueSafe(),
                        ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
                hyperlink.setChangeable();
                hyperlink.textProperty().bindBidirectional(actFavourite.urlProperty());
//                hyperlink.textProperty().addListener((ob, ol, ne) -> actFavourite.setUrl(hyperlink.getText()));
//                actFavourite.urlProperty().addListener((ob, ol, ne) -> hyperlink.setUrl(actFavourite.getUrl()));
                gridPane.add(lbl[i], 0, row);
                gridPane.add(hyperlink, 1, row, 3, 1);
                ++row;
                break;
//            case FavouriteXml.FAVOURITE_URL_RESOLVED:
//                hyperlink = new PHyperlink(actFavourite.urlResolvedProperty().getValueSafe(),
//                        ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
//                actFavourite.urlProperty().addListener((ob, ol, ne) -> hyperlink.setUrl(actFavourite.getUrlResolved()));
//                gridPane.add(lbl[FavouriteXml.FAVOURITE_URL], 0, row);
//                gridPane.add(hyperlink, 1, row, 3, 1);
//
//                ++row;
//                break;

//                lblCont[i].textProperty().bind(actFavourite.urlResolvedProperty());
//                gridPane.add(lbl[FavouriteXml.FAVOURITE_URL], 0, row);
//                gridPane.add(lblCont[i], 1, row);
//                ++row;
//                break;
            case FavouriteXml.FAVOURITE_WEBSITE:
                hyperlink = new PHyperlink(this.getStage(), actFavourite.websiteProperty().getValueSafe(),
                        ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
                hyperlink.setChangeable();
                hyperlink.textProperty().bindBidirectional(actFavourite.websiteProperty());
//                hyperlink.textProperty().addListener((ob, ol, ne) -> actFavourite.setWebsite(hyperlink.getText()));
//                actFavourite.websiteProperty().addListener((ob, ol, ne) -> hyperlink.setUrl(actFavourite.getWebsite()));
                gridPane.add(lbl[i], 0, row);
                gridPane.add(hyperlink, 1, row, 3, 1);
                ++row;
                break;
            case FavouriteXml.FAVOURITE_DATE:
                lblCont[i].textProperty().bind(actFavourite.stationDateProperty().asString());
                gridPane.add(lbl[i], 0, row);
                gridPane.add(lblCont[i], 1, row);
                ++row;
                break;
        }

        if (i == FavouriteXml.FAVOURITE_COLLECTION || txt[i].isEditable() || !cbx[i].isDisabled()) {
            lbl[i].setTextFill(Color.BLUE);
        }
        return row;
    }

    private Button addAllButton(int i) {
        Button btn = new Button("alle");
        btn.setOnAction(e -> {
            saveAct();
            favouriteList.stream().forEach(f -> {
                switch (i) {
                    case FavouriteXml.FAVOURITE_COLLECTION:
                        f.setCollectionName(actFavourite.getCollectionName());
                        break;
                    case FavouriteXml.FAVOURITE_DESCRIPTION:
                        f.setDescription(actFavourite.getDescription());
                        break;
                }
            });
        });
        return btn;
    }
}
