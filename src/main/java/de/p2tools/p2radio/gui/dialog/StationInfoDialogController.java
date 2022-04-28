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
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StationInfoDialogController extends PDialogExtra {

    private final int FREE = 220;
    private final Text[] textTitle = new Text[StationXml.MAX_ELEM];
    private final Label[] lblCont = new Label[StationXml.MAX_ELEM];

    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final Button btnOk = new Button("_Ok");
    private final ImageView ivNew = new ImageView();

    private final PHyperlink pHyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
    private final PHyperlink pHyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());

    private Station station;
    private final ProgData progData;

    public StationInfoDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO,
                "Senderinfos", false, false, DECO.SMALL);

        this.progData = progData;
        init(false);
    }

    public void toggleShowInfo() {
        if (getStage().isShowing()) {
            close();
        } else {
            showStationInfo();
        }
    }

    public void showStationInfo() {
        showDialog();
    }

    public void setStation(Station station) {
        this.station = station;
        setStation();
    }

    @Override
    public void make() {
        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> updateCss());
        getHboxLeft().getChildren().addAll(btnPrev, btnNext, new HBox(), btnStart, btnStop);
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        btnPrev.setTooltip(new Tooltip("weniger Informationen zum Sender anzeigen"));
        btnPrev.setGraphic(ProgIcons.Icons.ICON_BUTTON_PREV.getImageView());
        btnPrev.setOnAction(event -> {
            switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                case 0:
                    progData.stationGuiController.setPreviousStation();
                    break;
                case 1:
                    progData.favouriteGuiController.setPreviousStation();
                    break;
                case 2:
                default:
                    progData.lastPlayedGuiController.setPreviousStation();
            }
        });

        btnNext.setTooltip(new Tooltip("weniger Informationen zum Sender anzeigen"));
        btnNext.setGraphic(ProgIcons.Icons.ICON_BUTTON_NEXT.getImageView());
        btnNext.setOnAction(event -> {
            switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                case 0:
                    progData.stationGuiController.setNextStation();
                    break;
                case 1:
                    progData.favouriteGuiController.setNextStation();
                    break;
                case 2:
                default:
                    progData.lastPlayedGuiController.setNextStation();
            }
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.setGraphic(ProgIcons.Icons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> {
            switch (ProgConfig.SYSTEM_LAST_TAB_STATION.get()) {
                case 0:
                    progData.stationGuiController.playStation();
                    break;
                case 1:
                    progData.favouriteGuiController.playStation();
                    break;
                case 2:
                default:
                    progData.lastPlayedGuiController.playStation();
            }
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP_PLAY.getImageView());
        btnStop.setOnAction(event -> progData.startFactory.stopAll());

        initUrl();
        makeGridPane();
    }

    private void setStation() {
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            if (station == null) {
                lblCont[i].setText("");
                ivNew.setImage(ProgIcons.Icons.ICON_DIALOG_AUS.getImage());
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
                        pHyperlinkUrl.setUrl(station.getStationUrl());
                        break;
                    case StationXml.STATION_WEBSITE:
                        pHyperlinkWebsite.setUrl(station.arr[StationXml.STATION_WEBSITE]);
                        break;
                    case StationXml.STATION_NEW:
                        if (station.isNewStation()) {
                            ivNew.setImage(ProgIcons.Icons.ICON_DIALOG_EIN.getImage());
                        } else {
                            ivNew.setImage(ProgIcons.Icons.ICON_DIALOG_AUS.getImage());
                        }
                        break;

                    default:
                        lblCont[i].setText(station.arr[i]);
                }
            }
        }
    }

    private void makeGridPane() {
        final GridPane gridPane = new GridPane();
        getvBoxCont().getChildren().clear();
        getvBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            textTitle[i] = new Text(StationXml.COLUMN_NAMES[i] + ":");
            lblCont[i] = new Label("");
            lblCont[i].setWrapText(false);
            lblCont[i].maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //_______
        }

        makeGridPaneBig(gridPane);

        setStation();
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
                case StationXml.STATION_STATE:
                case StationXml.STATION_CLICK_COUNT:
                case StationXml.STATION_DATE:
                    // bis hier nicht anzeigen
                    break;

                case StationXml.STATION_NEW:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivNew, 1, row++, 3, 1);
                    break;
                case StationXml.STATION_CODEC:
                    gridPane.add(textTitle[StationXml.STATION_CODEC], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_CODEC], 1, row);
                    lblCont[StationXml.STATION_CODEC].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_CODEC].getText()).show(lblCont[StationXml.STATION_CODEC], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_BITRATE], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_BITRATE], 3, row++);
                    lblCont[StationXml.STATION_BITRATE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_BITRATE].getText()).show(lblCont[StationXml.STATION_BITRATE], event.getScreenX(), event.getScreenY()));
                    break;
                case StationXml.STATION_LANGUAGE:
                    gridPane.add(textTitle[StationXml.STATION_LANGUAGE], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_LANGUAGE], 1, row);
                    lblCont[StationXml.STATION_LANGUAGE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_LANGUAGE].getText()).show(lblCont[StationXml.STATION_LANGUAGE], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_COUNTRY], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_COUNTRY], 3, row++);
                    lblCont[StationXml.STATION_COUNTRY].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_COUNTRY].getText()).show(lblCont[StationXml.STATION_COUNTRY], event.getScreenX(), event.getScreenY()));
                    break;
                case StationXml.STATION_VOTES:
                    gridPane.add(textTitle[StationXml.STATION_VOTES], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_VOTES], 1, row);
                    lblCont[StationXml.STATION_VOTES].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_VOTES].getText()).show(lblCont[StationXml.STATION_VOTES], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_CLICK_COUNT], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_CLICK_COUNT], 3, row++);
                    lblCont[StationXml.STATION_CLICK_COUNT].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_CLICK_COUNT].getText()).show(lblCont[StationXml.STATION_CLICK_COUNT], event.getScreenX(), event.getScreenY()));
                    break;
                case StationXml.STATION_CLICK_TREND:
                    gridPane.add(textTitle[StationXml.STATION_CLICK_TREND], 0, row);
                    gridPane.add(lblCont[StationXml.STATION_CLICK_TREND], 1, row);
                    lblCont[StationXml.STATION_CLICK_TREND].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_CLICK_TREND].getText()).show(lblCont[StationXml.STATION_CLICK_TREND], event.getScreenX(), event.getScreenY()));

                    gridPane.add(textTitle[StationXml.STATION_DATE], 2, row);
                    gridPane.add(lblCont[StationXml.STATION_DATE], 3, row++);
                    lblCont[StationXml.STATION_DATE].setOnContextMenuRequested(event ->
                            getMenu(lblCont[StationXml.STATION_DATE].getText()).show(lblCont[StationXml.STATION_DATE], event.getScreenX(), event.getScreenY()));
                    break;
                case StationXml.STATION_URL:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkUrl, 1, row++, 3, 1);
                    break;
                case StationXml.STATION_WEBSITE:
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
