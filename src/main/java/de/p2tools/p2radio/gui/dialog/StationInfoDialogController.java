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
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class StationInfoDialogController extends PDialogExtra {

    private final int FREE = 220;
    private final Text[] textTitle = new Text[StationXml.MAX_ELEM];
    private final Label[] lblCont = new Label[StationXml.MAX_ELEM];

    private final Button btnUpDown = new Button("");
    private final Button btnNext = new Button("");
    private final Button btnPrev = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final Button btnOk = new Button("_Ok");
    private final ImageView ivNew = new ImageView();

    private final PHyperlink pHyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
    private final PHyperlink pHyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

    BooleanProperty urlProperty = ProgConfig.STATION_INFO_DIALOG_SHOW_URL;
    private Station station;
    private final ProgData progData;

    public StationInfoDialogController(ProgData progData) {
        super(progData.primaryStage, null,
                "Senderinfos", false, false, DECO.SMALL);

        this.progData = progData;
        init(false);
    }

    public void close() {
//        System.out.println("close");
        final StringProperty sp = ProgConfig.STATION_INFO_DIALOG_SHOW_URL.get() ?
                ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL;
        PGuiSize.getSizeWindow(sp, getStage());
        super.close();
    }

    public void showDialog() {
        PGuiSize.setPos(ProgConfig.STATION_INFO_DIALOG_SHOW_URL.get() ?
                ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO : ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL, getStage());
        getStage().show();
        setSize();
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
        getHboxLeft().getChildren().addAll(btnUpDown, new HBox(), btnPrev, btnNext, new HBox(), btnStart, btnStop);
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        btnUpDown.setTooltip(urlProperty.getValue() ? new Tooltip("weniger Informationen zum Sender anzeigen") :
                new Tooltip("mehr Informationen zum Sender anzeigen"));
        btnUpDown.setGraphic(urlProperty.getValue() ? new ProgIcons().ICON_BUTTON_UP : new ProgIcons().ICON_BUTTON_DOWN);
        btnUpDown.setOnAction(event -> {
            urlProperty.setValue(!urlProperty.getValue());
            makeGridPane(true);
            btnUpDown.setTooltip(urlProperty.getValue() ? new Tooltip("weniger Informationen zum Sender anzeigen") :
                    new Tooltip("mehr Informationen zum Sender anzeigen"));
            btnUpDown.setGraphic(urlProperty.getValue() ? new ProgIcons().ICON_BUTTON_UP : new ProgIcons().ICON_BUTTON_DOWN);
        });

        btnPrev.setTooltip(new Tooltip("weniger Informationen zum Sender anzeigen"));
        btnPrev.setGraphic(new ProgIcons().ICON_BUTTON_PREV);
        btnPrev.setOnAction(event -> {
            boolean panelStation = ProgConfig.SYSTEM_LAST_TAB_STATION.get();
            if (panelStation) {
                progData.stationGuiController.setPreviousStation();
            } else {
                progData.favouriteGuiController.setPreviousStation();
            }
        });

        btnNext.setTooltip(new Tooltip("weniger Informationen zum Sender anzeigen"));
        btnNext.setGraphic(new ProgIcons().ICON_BUTTON_NEXT);
        btnNext.setOnAction(event -> {
            boolean panelStation = ProgConfig.SYSTEM_LAST_TAB_STATION.get();
            if (panelStation) {
                progData.stationGuiController.setNextStation();
            } else {
                progData.favouriteGuiController.setNextStation();
            }
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.setGraphic(new ProgIcons().ICON_BUTTON_PLAY);
        btnStart.setOnAction(event -> {
            boolean panelStation = ProgConfig.SYSTEM_LAST_TAB_STATION.get();
            if (panelStation) {
                progData.stationGuiController.playStation();
            } else {
                progData.favouriteGuiController.playStation();
            }
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.setGraphic(new ProgIcons().ICON_BUTTON_STOP_PLAY);
        btnStop.setOnAction(event -> progData.startFactory.stopAll());

        initUrl();
        makeGridPane(false);
    }

    private void setStation() {
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
                        pHyperlinkUrl.setUrl(station.getUrl());
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
    }

    private void makeGridPane(boolean setSize) {
        final GridPane gridPane = new GridPane();
        getvBoxCont().getChildren().clear();
        getvBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);
//        gridPane.getRowConstraints().add(new RowConstraints());

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

        if (urlProperty.getValue()) {
            makeGridPaneBig(gridPane);
        } else {
            makeGridPaneSmall(gridPane);
        }

        setStation();
        if (setSize) {
            setSize();
        }
    }

    private void setSize() {
        if (urlProperty.getValue()) {
            int w = PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO);
            int h = PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO);
            if (w > 0 && h > 0) {
                this.getStage().getScene().getWindow().setHeight(h);
                this.getStage().getScene().getWindow().setWidth(w);
            }

        } else {
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
//                    gridPane.getRowConstraints().add(new RowConstraints());
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
//                    gridPane.getRowConstraints().add(new RowConstraints());
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
