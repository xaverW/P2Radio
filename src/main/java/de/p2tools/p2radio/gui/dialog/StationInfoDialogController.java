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

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2Hyperlink;
import de.p2tools.p2lib.guitools.grid.P2GridConstraints;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.date.P2LDateTimeFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import de.p2tools.p2radio.controller.picon.PIconFactory;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StationInfoDialogController extends P2DialogExtra {

    private final int FREE = 220;
    private final Text[] textTitle = new Text[StationDataXml.MAX_ELEM];
    private final Label[] lblCont = new Label[StationDataXml.MAX_ELEM];

    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final Button btnOk = new Button("_Ok");
    private final Label ivNew = new Label();
    private final Label ivOwn = new Label();
    private final Label ivDouble = new Label();
    private final Label ivFavourite = new Label();
    private final Label ivHistory = new Label();
    private final Label ivBlack = new Label();

    private final P2Hyperlink pHyperlinkUrl = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL);
    private final P2Hyperlink pHyperlinkWebsite = new P2Hyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL);
    private final ProgData progData;
    private StationData station;

    public StationInfoDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO,
                "Senderinfos", false, true, true, DECO.BORDER_SMALL);

        this.progData = progData;
        init(false);
    }

    public void toggleShowInfo() {
        if (getStage().isShowing()) {
            hide();
        } else {
            showStationInfo();
        }
    }

    public void showStationInfo() {
        showDialog();
    }

    public void setStation(StationData station) {
        this.station = station;
        setStation();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void hide() {
        super.saveDialog();
        super.hide();
    }

    @Override
    public void make() {
        getHboxLeft().getChildren().addAll(btnPrev, btnNext, new HBox(), btnStart, btnStop);
        addOkButton(btnOk);
        btnOk.setOnAction(a -> hide());

        btnPrev.setTooltip(new Tooltip("Vorherigen Sender in der Tabelle anzeigen"));
        btnPrev.setGraphic(PIconFactory.PICON.BTN_PREV.getFontIcon());
        btnPrev.setOnAction(event -> {
            if (ProgConfig.SYSTEM_SMALL_RADIO.getValue()) {
                //dann ist das kleine Fenster offen
                progData.smallRadioGuiController.setPreviousStation();

            } else {
                switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                    case 0:
                        progData.stationGuiPack.getStationGuiController().setPreviousStation();
                        break;
                    case 1:
                        progData.favouriteGuiPack.getFavouriteGuiController().setPreviousStation();
                        break;
                    case 2:
                    default:
                        progData.historyGuiPack.getHistoryGuiController().setPreviousStation();
                }
            }
        });

        btnNext.setTooltip(new Tooltip("Nächsten Sender in der Tabelle anzeigen"));
        btnNext.setGraphic(PIconFactory.PICON.BTN_NEXT.getFontIcon());
        btnNext.setOnAction(event -> {
            if (ProgConfig.SYSTEM_SMALL_RADIO.getValue()) {
                //dann ist das kleine Fenster offen
                progData.smallRadioGuiController.setNextStation();

            } else {
                switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                    case 0:
                        progData.stationGuiPack.getStationGuiController().setNextStation();
                        break;
                    case 1:
                        progData.favouriteGuiPack.getFavouriteGuiController().setNextStation();
                        break;
                    case 2:
                    default:
                        progData.historyGuiPack.getHistoryGuiController().setNextStation();
                }
            }
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.setGraphic(PIconFactory.PICON.BTN_PLAY.getFontIcon());
        btnStart.setOnAction(event -> {
            switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                case 0:
                    progData.stationGuiPack.getStationGuiController().playStation();
                    break;
                case 1:
                    progData.favouriteGuiPack.getFavouriteGuiController().playStation();
                    break;
                case 2:
                default:
                    progData.historyGuiPack.getHistoryGuiController().playStation();
            }
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.setGraphic(PIconFactory.PICON.BTN_STOP_PLAY.getFontIcon());
        btnStop.setOnAction(event -> StartFactory.stopStation());

        initUrl();
        makeGridPane();
    }

    private void setStation() {
        for (int i = 0; i < StationDataXml.MAX_ELEM; ++i) {
            if (station == null) {
                lblCont[i].setText("");
                ivNew.setGraphic(null);
                ivOwn.setGraphic(null);
                ivDouble.setGraphic(null);
                ivFavourite.setGraphic(null);
                ivHistory.setGraphic(null);
                ivBlack.setGraphic(null);
                pHyperlinkUrl.setUrl("");
                pHyperlinkWebsite.setUrl("");
            } else {
                switch (i) {
                    case StationDataXml.STATION_PROP_STATION_NO_INT:
                        lblCont[i].setText(station.getStationNo() + "");
                        break;
                    case StationDataXml.STATION_PROP_STATION_NEW_INT:
                        if (station.isNewStation()) {
                            ivNew.setGraphic(PIconFactory.PICON.ICON_BOOLEAN_ON.getFontIcon());
                        } else {
                            ivNew.setGraphic(null);
                        }
                        break;
                    case StationDataXml.STATION_PROP_STATION_NAME_INT:
                        lblCont[i].setText(station.getStationName());
                        break;
                    case StationDataXml.STATION_PROP_COLLECTION_INT:
                        lblCont[i].setText(station.getCollectionName());
                        break;
                    case StationDataXml.STATION_PROP_OWN_GRADE_INT:
                        lblCont[i].setText(station.getOwnGrade() + "");
                        break;
                    case StationDataXml.STATION_PROP_STARTS_INT:
                        lblCont[i].setText(station.getStarts() + "");
                        break;
                    case StationDataXml.STATION_PROP_CLICK_COUNT_INT:
                        lblCont[i].setText(station.getClickCount() + "");
                        break;
                    case StationDataXml.STATION_PROP_CLICK_TREND_INT:
                        lblCont[i].setText(station.getClickTrend() + "");
                        break;
                    case StationDataXml.STATION_PROP_GENRE_INT:
                        lblCont[i].setText(station.getGenre());
                        break;
                    case StationDataXml.STATION_PROP_CODEC_INT:
                        lblCont[i].setText(station.getCodec());
                        break;

                    case StationDataXml.STATION_PROP_BITRATE_INT:
                        lblCont[i].setText(station.getBitrateStr());
                        break;

                    case StationDataXml.STATION_PROP_OWN_INT:
                        if (station.isOwn()) {
                            ivOwn.setGraphic(PIconFactory.PICON.ICON_BOOLEAN_ON.getFontIcon());
                        } else {
                            ivOwn.setGraphic(null);
                        }
                        break;

                    case StationDataXml.STATION_PROP_COUNTRY_INT:
                        lblCont[i].setText(station.getCountry() + " - " + station.getCountryCode());
                        break;
                    case StationDataXml.STATION_PROP_STATE_INT:
                        lblCont[i].setText(station.getState());
                        break;
                    case StationDataXml.STATION_PROP_COUNTRY_CODE_INT:
                        lblCont[i].setText(station.getCountryCode());
                        break;
                    case StationDataXml.STATION_PROP_LANGUAGE_INT:
                        lblCont[i].setText(station.getLanguage());
                        break;

                    case StationDataXml.STATION_PROP_VOTES_INT:
                        lblCont[i].setText(station.getVotes() + "");
                        break;
                    case StationDataXml.STATION_PROP_DESCRIPTION_INT:
                        lblCont[i].setText(station.getDescription());
                        break;
                    case StationDataXml.STATION_PROP_DATE_INT:
                        lblCont[i].setText(P2LDateFactory.toString(station.getStationDateLastChange()));
                        break;
                    case StationDataXml.STATION_PROP_DATE_LONG_INT:
                        break;

                    case StationDataXml.STATION_PROP_DATE_LAST_START_INT:
                        lblCont[i].setText(P2LDateTimeFactory.toString(station.getStationDateLastStart()));
                        break;

                    case StationDataXml.STATION_PROP_URL_INT:
                        pHyperlinkUrl.setUrl(station.getStationUrl());
                        break;
                    case StationDataXml.STATION_PROP_DOUBLE_URL_INT:
                        if (station.isDoubleUrl()) {
                            ivDouble.setGraphic(PIconFactory.PICON.ICON_BOOLEAN_ON.getFontIcon());
                        } else {
                            ivDouble.setGraphic(null);
                        }
                        break;
                    case StationDataXml.STATION_PROP_IS_FAVOURITE_INT:
                        if (station.isFavourite()) {
                            ivFavourite.setGraphic(PIconFactory.PICON.ICON_BOOLEAN_ON.getFontIcon());
                        } else {
                            ivFavourite.setGraphic(null);
                        }
                        break;
                    case StationDataXml.STATION_PROP_IS_HISTORY_INT:
                        if (station.isHistory()) {
                            ivHistory.setGraphic(PIconFactory.PICON.ICON_BOOLEAN_ON.getFontIcon());
                        } else {
                            ivHistory.setGraphic(null);
                        }
                        break;
                    case StationDataXml.STATION_PROP_BLACK_BLOCKED_URL_INT:
                        if (station.isBlackBlocked()) {
                            ivBlack.setGraphic(PIconFactory.PICON.ICON_BOOLEAN_ON.getFontIcon());
                        } else {
                            ivBlack.setGraphic(null);
                        }
                        break;
                    case StationDataXml.STATION_PROP_URL_RESOLVED_INT:
                        lblCont[i].setText(station.getStationUrlResolved());
                        break;

                    case StationDataXml.STATION_PROP_WEBSITE_INT:
                        pHyperlinkWebsite.setUrl(station.getWebsite());
                        break;
                }
            }
        }
    }

    private void makeGridPane() {
        final GridPane gridPane = new GridPane();
        getVBoxCont().getChildren().clear();
        getVBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5));
        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcPrefSize(),
                P2GridConstraints.getCcComputedSizeAndHgrow(), P2GridConstraints.getCcPrefSize(),
                P2GridConstraints.getCcComputedSizeAndHgrow());

        for (int i = 0; i < StationDataXml.MAX_ELEM; ++i) {
            textTitle[i] = new Text(StationDataXml.COLUMN_NAMES[i] + ":");
            lblCont[i] = new Label("");
            lblCont[i].setWrapText(false);
            lblCont[i].maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //_______
        }

        makeGridPaneBig(gridPane);

        setStation();
    }

    private void makeGridPaneBig(GridPane gridPane) {
        int row = 0;
        for (int i = 0; i < StationDataXml.MAX_ELEM; ++i) {
            switch (i) {
                case StationDataXml.STATION_PROP_BUTTON1_INT:
                case StationDataXml.STATION_PROP_BUTTON2_INT:
                case StationDataXml.STATION_PROP_URL_RESOLVED_INT:
                case StationDataXml.STATION_PROP_BITRATE_INT:
                case StationDataXml.STATION_PROP_COUNTRY_INT:
                case StationDataXml.STATION_PROP_COUNTRY_CODE_INT:
                case StationDataXml.STATION_PROP_STATE_INT:
                case StationDataXml.STATION_PROP_CLICK_TREND_INT:
                case StationDataXml.STATION_PROP_CLICK_COUNT_INT:
                case StationDataXml.STATION_PROP_DATE_LAST_START_INT:
                case StationDataXml.STATION_PROP_IS_HISTORY_INT:
                case StationDataXml.STATION_PROP_DATE_LONG_INT:
                    // bis hier nicht anzeigen
                    break;

                case StationDataXml.STATION_PROP_STATION_NEW_INT:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivNew, 1, row++, 3, 1);
                    break;
                case StationDataXml.STATION_PROP_OWN_INT:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivOwn, 1, row++, 3, 1);
                    break;
                case StationDataXml.STATION_PROP_DOUBLE_URL_INT:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivDouble, 1, row++, 3, 1);
                    break;
                case StationDataXml.STATION_PROP_IS_FAVOURITE_INT:
                    gridPane.add(textTitle[StationDataXml.STATION_PROP_IS_FAVOURITE_INT], 0, row);
                    gridPane.add(ivFavourite, 1, row);

                    gridPane.add(textTitle[StationDataXml.STATION_PROP_IS_HISTORY_INT], 2, row);
                    gridPane.add(ivHistory, 3, row++);
                    break;
                case StationDataXml.STATION_PROP_BLACK_BLOCKED_URL_INT:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivBlack, 1, row++, 3, 1);
                    break;


                case StationDataXml.STATION_PROP_CODEC_INT:
                    gridPane.add(textTitle[StationDataXml.STATION_PROP_CODEC_INT], 0, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_CODEC_INT], 1, row);
                    lblCont[StationDataXml.STATION_PROP_CODEC_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_CODEC_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_CODEC_INT], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationDataXml.STATION_PROP_BITRATE_INT], 2, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_BITRATE_INT], 3, row++);
                    lblCont[StationDataXml.STATION_PROP_BITRATE_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_BITRATE_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_BITRATE_INT], event.getScreenX(), event.getScreenY()));
                    break;
                case StationDataXml.STATION_PROP_LANGUAGE_INT:
                    gridPane.add(textTitle[StationDataXml.STATION_PROP_LANGUAGE_INT], 0, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_LANGUAGE_INT], 1, row);
                    lblCont[StationDataXml.STATION_PROP_LANGUAGE_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_LANGUAGE_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_LANGUAGE_INT], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationDataXml.STATION_PROP_COUNTRY_INT], 2, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_COUNTRY_INT], 3, row++);
                    lblCont[StationDataXml.STATION_PROP_COUNTRY_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_COUNTRY_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_COUNTRY_INT], event.getScreenX(), event.getScreenY()));
                    break;
                case StationDataXml.STATION_PROP_VOTES_INT:
                    gridPane.add(textTitle[StationDataXml.STATION_PROP_VOTES_INT], 0, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_VOTES_INT], 1, row);
                    lblCont[StationDataXml.STATION_PROP_VOTES_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_VOTES_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_VOTES_INT], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationDataXml.STATION_PROP_CLICK_COUNT_INT], 2, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_CLICK_COUNT_INT], 3, row++);
                    lblCont[StationDataXml.STATION_PROP_CLICK_COUNT_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_CLICK_COUNT_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_CLICK_COUNT_INT], event.getScreenX(), event.getScreenY()));
                    break;


                case StationDataXml.STATION_PROP_STARTS_INT:
                    gridPane.add(textTitle[StationDataXml.STATION_PROP_STARTS_INT], 0, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_STARTS_INT], 1, row);
                    lblCont[StationDataXml.STATION_PROP_STARTS_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_STARTS_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_STARTS_INT], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationDataXml.STATION_PROP_CLICK_TREND_INT], 2, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_CLICK_TREND_INT], 3, row++);
                    lblCont[StationDataXml.STATION_PROP_CLICK_TREND_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_CLICK_TREND_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_CLICK_TREND_INT], event.getScreenX(), event.getScreenY()));
                    break;

                case StationDataXml.STATION_PROP_DATE_INT:
                    gridPane.add(textTitle[StationDataXml.STATION_PROP_DATE_INT], 0, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_DATE_INT], 1, row);
                    lblCont[StationDataXml.STATION_PROP_DATE_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_DATE_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_DATE_INT], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationDataXml.STATION_PROP_DATE_LAST_START_INT], 2, row);
                    gridPane.add(lblCont[StationDataXml.STATION_PROP_DATE_LAST_START_INT], 3, row++);
                    lblCont[StationDataXml.STATION_PROP_DATE_LAST_START_INT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationDataXml.STATION_PROP_DATE_LAST_START_INT].getText()).show(lblCont[StationDataXml.STATION_PROP_DATE_LAST_START_INT], event.getScreenX(), event.getScreenY()));
                    break;


                case StationDataXml.STATION_PROP_URL_INT:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkUrl, 1, row++, 3, 1);
                    break;
                case StationDataXml.STATION_PROP_WEBSITE_INT:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkWebsite, 1, row++, 3, 1);
                    break;
                default:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(lblCont[i], 1, row++, 3, 1);
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
