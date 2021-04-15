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
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StationInfoDialogController extends PDialogExtra {

    private final int FREE = 220;

    private final Text[] textTitle = new Text[StationXml.MAX_ELEM];
    private final Label[] lblCont = new Label[StationXml.MAX_ELEM];

    private final Button btnOk = new Button("_Ok");
    private final ImageView ivNew = new ImageView();
    private final PToggleSwitch tglUrl = new PToggleSwitch("URL");

    private final PHyperlink pHyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

    private final PHyperlink pHyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

    BooleanProperty urlProperty = ProgConfig.STATION_INFO_DIALOG_SHOW_URL;
    private Station station;

    public StationInfoDialogController() {
        super(ProgData.getInstance().primaryStage,
                ProgConfig.STATION_INFO_DIALOG_SHOW_URL.get() ? ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL,
                "Senderinfos", false, false, DECO.SMALL);

        init(false);
    }

    public void showStationInfo() {
        showDialog();
    }

    public void setStation(Station station) {
        this.station = station;
        setStation();
    }

    private void setStation() {
        Platform.runLater(() -> {
            for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
                if (station == null) {
                    lblCont[i].setText("");
                    ivNew.setImage(null);
                    pHyperlinkUrl.setUrl("");
                    pHyperlinkWebsite.setUrl("");
                } else {
                    switch (i) {
                        case StationXml.STATION_NO:
                            lblCont[i].setText(station.getNo() + "");
                            break;
                        case StationXml.STATION_COUNTRY:
                            lblCont[i].setText(station.getCountry() + " - " + station.getCountryCode());
                            break;
                        case StationXml.STATION_BITRATE:
                            lblCont[i].setText(station.getBitrateInt() + "");
                            break;
                        case StationXml.STATION_VOTES:
                            lblCont[i].setText(station.getVotes() + "");
                            break;
                        case StationXml.STATION_CLICK_COUNT:
                            lblCont[i].setText(station.getClickCount() + "");
                            break;
                        case StationXml.STATION_CLICK_TREND:
                            lblCont[i].setText(station.getClickTrend() + "");
                            break;
                        case StationXml.STATION_URL:
                            pHyperlinkUrl.setUrl(station.arr[StationXml.STATION_URL]);
                            break;
                        case StationXml.STATION_WEBSITE:
                            pHyperlinkWebsite.setUrl(station.arr[StationXml.STATION_WEBSITE]);
                            break;
                        case StationXml.STATION_NEW:
                            if (station.isNewStation()) {
                                ivNew.setImage(new ProgIcons().ICON_DIALOG_EIN_SW);
                            } else {
                                ivNew.setImage(null);
                            }
                            break;

                        default:
                            lblCont[i].setText(station.arr[i]);
                    }
                }
            }
        });
    }

    @Override
    public void make() {
        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> updateCss());
        getHboxLeft().getChildren().add(tglUrl);
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        tglUrl.setTooltip(new Tooltip("URL anzeigen"));
        tglUrl.selectedProperty().bindBidirectional(urlProperty);
        tglUrl.selectedProperty().addListener((observable, oldValue, newValue) -> {
            makeGridPane();
        });
        initUrl();
        makeGridPane();
    }

    private void makeGridPane() {
        final GridPane gridPane = new GridPane();
        getvBoxCont().getChildren().clear();
        getvBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.SOMETIMES);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5));
//        gridPane.setGridLinesVisible(true);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            textTitle[i] = new Text(StationXml.COLUMN_NAMES[i] + ":");
            lblCont[i] = new Label("");
            lblCont[i].setWrapText(true);
            lblCont[i].maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //_______
        }

        if (tglUrl.isSelected()) {
            makeGridPaneBig(gridPane);
        } else {
            makeGridPaneSmall(gridPane);
        }

        setStation();
        if (tglUrl.isSelected()) {
            setSizeConfiguration(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO);
            int w = PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO);
            int h = PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO);
            if (w > 0 && h > 0) {
                this.getStage().getScene().getWindow().setHeight(h);
                this.getStage().getScene().getWindow().setWidth(w);
            }

        } else {
            setSizeConfiguration(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL);
            int w = PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL);
            int h = PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL);
            if (w > 0 && h > 0) {
                this.getStage().getScene().getWindow().setHeight(h);
                this.getStage().getScene().getWindow().setWidth(w);
            }
        }
    }

    private void makeGridPaneBig(GridPane gridPane) {
        int row = 0;
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            switch (i) {
                case StationXml.STATION_PLAY:
                case StationXml.STATION_RECORD:
                case StationXml.STATION_URL_RESOLVED:
                case StationXml.STATION_BITRATE:
                case StationXml.STATION_COUNTRY:
                case StationXml.STATION_COUNTRY_CODE:
                    // bis hier nicht anzeigen
                    break;

                case StationXml.STATION_CODEC:
                    gridPane.add(textTitle[StationXml.STATION_CODEC], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_CODEC], 1, row);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    lblCont[StationXml.STATION_CODEC].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_CODEC].getText()).show(lblCont[StationXml.STATION_CODEC], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_BITRATE], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_BITRATE], 3, row++);
                    lblCont[StationXml.STATION_BITRATE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_BITRATE].getText()).show(lblCont[StationXml.STATION_BITRATE], event.getScreenX(), event.getScreenY()));
                    break;
                case StationXml.STATION_STATE:
                    gridPane.add(textTitle[StationXml.STATION_STATE], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_STATE], 1, row);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    lblCont[StationXml.STATION_STATE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_STATE].getText()).show(lblCont[StationXml.STATION_STATE], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_COUNTRY], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_COUNTRY], 3, row++);
                    lblCont[StationXml.STATION_COUNTRY].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_COUNTRY].getText()).show(lblCont[StationXml.STATION_COUNTRY], event.getScreenX(), event.getScreenY()));
                    break;
                case StationXml.STATION_NEW:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivNew, 1, row++, 3, 1);
//                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;

                case StationXml.STATION_URL:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkUrl, 1, row++, 3, 1);
//                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;
                case StationXml.STATION_WEBSITE:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkWebsite, 1, row++, 3, 1);
//                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;
                default:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(lblCont[i], 1, row++, 3, 1);
//                    gridPane.getRowConstraints().add(new RowConstraints());

                    final int ii = i;
                    lblCont[i].setOnContextMenuRequested(event ->
                            getMenu(lblCont[ii].getText()).show(lblCont[ii], event.getScreenX(), event.getScreenY()));
            }
        }
    }

    private void makeGridPaneSmall(GridPane gridPane) {
        int row = 0;
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            switch (i) {
                case StationXml.STATION_PLAY:
                case StationXml.STATION_RECORD:
                case StationXml.STATION_URL_RESOLVED:
                case StationXml.STATION_URL:
                case StationXml.STATION_WEBSITE:
                case StationXml.STATION_GENRE:
                case StationXml.STATION_CODEC:
                case StationXml.STATION_BITRATE:
                case StationXml.STATION_STATE:
                case StationXml.STATION_COUNTRY:
                case StationXml.STATION_CLICK_COUNT:
                case StationXml.STATION_CLICK_TREND:
                case StationXml.STATION_DATE:
                case StationXml.STATION_NEW:
                case StationXml.STATION_LANGUAGE:
                    // bis hier nicht anzeigen
                    break;

                case StationXml.STATION_COUNTRY_CODE:
                    gridPane.add(textTitle[StationXml.STATION_COUNTRY_CODE], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_COUNTRY_CODE], 1, row);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    lblCont[StationXml.STATION_COUNTRY_CODE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_COUNTRY_CODE].getText()).show(lblCont[StationXml.STATION_COUNTRY_CODE], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_LANGUAGE], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_LANGUAGE], 3, row++);
                    lblCont[StationXml.STATION_LANGUAGE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_LANGUAGE].getText()).show(lblCont[StationXml.STATION_LANGUAGE], event.getScreenX(), event.getScreenY()));
                    break;

                case StationXml.STATION_VOTES:
                    gridPane.add(textTitle[StationXml.STATION_VOTES], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_VOTES], 1, row);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    lblCont[StationXml.STATION_VOTES].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_VOTES].getText()).show(lblCont[StationXml.STATION_VOTES], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_CLICK_COUNT], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_CLICK_COUNT], 3, row++);
                    lblCont[StationXml.STATION_CLICK_COUNT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_CLICK_COUNT].getText()).show(lblCont[StationXml.STATION_CLICK_COUNT], event.getScreenX(), event.getScreenY()));
                    break;

                default:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(lblCont[i], 1, row++, 3, 1);
                    gridPane.getRowConstraints().add(new RowConstraints());

                    final int ii = i;
                    lblCont[i].setOnContextMenuRequested(event ->
                            getMenu(lblCont[ii].getText()).show(lblCont[ii], event.getScreenX(), event.getScreenY()));
            }
        }
    }

    private void initUrl() {
        pHyperlinkUrl.setWrapText(true);
        pHyperlinkUrl.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //------------

        pHyperlinkWebsite.setWrapText(true);
        pHyperlinkWebsite.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //----------

//        textTitle[StationXml.STATION_URL].setVisible(urlProperty.get());
//        textTitle[StationXml.STATION_URL].setManaged(urlProperty.get());
//        pHyperlinkUrl.setVisible(urlProperty.get());
//        pHyperlinkUrl.setManaged(urlProperty.get());
//        pHyperlinkUrl.setMinHeight(Region.USE_PREF_SIZE);
//
//        textTitle[StationXml.STATION_WEBSITE].setVisible(urlProperty.get());
//        textTitle[StationXml.STATION_WEBSITE].setManaged(urlProperty.get());
//        pHyperlinkWebsite.setVisible(urlProperty.get());
//        pHyperlinkWebsite.setManaged(urlProperty.get());
//        pHyperlinkWebsite.setMinHeight(Region.USE_PREF_SIZE);
    }

    private ContextMenu getMenu(String url) {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("kopieren");
        resetTable.setOnAction(a -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(url);
            clipboard.setContent(content);
        });
        contextMenu.getItems().addAll(resetTable);
        return contextMenu;
    }
}
