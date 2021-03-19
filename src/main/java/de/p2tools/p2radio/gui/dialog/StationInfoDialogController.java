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
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class StationInfoDialogController extends PDialogExtra {

    private final int FREE = 240;

    private final Text[] textTitle = new Text[StationXml.MAX_ELEM];
    private final Label[] lblCont = new Label[StationXml.MAX_ELEM];

    private final GridPane gridPane = new GridPane();
    private final Button btnOk = new Button("_Ok");

    private final ImageView ivHD = new ImageView();
    private final ImageView ivUT = new ImageView();
    private final ImageView ivNew = new ImageView();
    private final PToggleSwitch tglUrl = new PToggleSwitch("URL");

    private final PHyperlink pHyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

    private final PHyperlink pHyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);

    BooleanProperty urlProperty = ProgConfig.STATION_INFO_DIALOG_SHOW_URL;

    public StationInfoDialogController() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SYSTEM_SIZE_DIALOG_STATION_INFO,
                "Senderinfos", false, false);

        init(false);
    }

    public void showStationInfo() {
        showDialog();
    }

    public void setStation(Station station) {
        Platform.runLater(() -> {

            for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
                if (station == null) {
                    lblCont[i].setText("");
                    ivHD.setImage(null);
                    ivUT.setImage(null);
                    ivNew.setImage(null);
                    pHyperlinkUrl.setUrl("");
                    pHyperlinkWebsite.setUrl("");
                } else {
                    switch (i) {
                        case StationXml.STATION_NO:
                            lblCont[i].setText(station.getNo() + "");
                            break;
                        case StationXml.STATION_VOTES:
                            lblCont[i].setText(station.getBitrate() + "");
                            break;
                        case StationXml.STATION_URL:
                            pHyperlinkUrl.setUrl(station.arr[StationXml.STATION_URL]);
                            break;
                        case StationXml.STATION_WEBSITE:
                            pHyperlinkWebsite.setUrl(station.arr[StationXml.STATION_WEBSITE]);
                            break;
                        case StationXml.STATION_CODEC:
                            break;
                        case StationXml.STATION_BITRATE:
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

        tglUrl.setTooltip(new Tooltip("URL anzeigen"));
        tglUrl.selectedProperty().bindBidirectional(urlProperty);
        tglUrl.selectedProperty().addListener((observable, oldValue, newValue) -> setUrl());

        btnOk.setOnAction(a -> close());
        getvBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.SOMETIMES);

        gridPane.setHgap(10);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());


        int row = 0;
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {

            textTitle[i] = new Text(StationXml.COLUMN_NAMES[i] + ":");
//            textTitle[i].setFont(Font.font(null, FontWeight.BOLD, -1));
            lblCont[i] = new Label("");
            lblCont[i].setWrapText(true);
            lblCont[i].maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //_______

            switch (i) {
                case StationXml.STATION_PLAY:
                case StationXml.STATION_RECORD:
                case StationXml.STATION_URL_RESOLVED:
                    // bis hier nicht anzeigen
                    break;

                case StationXml.STATION_CODEC:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivHD, 1, row++);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;
                case StationXml.STATION_BITRATE:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivUT, 1, row++);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;
                case StationXml.STATION_NEW:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(ivNew, 1, row++);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;

                case StationXml.STATION_URL:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkUrl, 1, row++);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;
                case StationXml.STATION_WEBSITE:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(pHyperlinkWebsite, 1, row++);
                    gridPane.getRowConstraints().add(new RowConstraints());
                    break;
                default:
                    gridPane.add(textTitle[i], 0, row);
                    gridPane.add(lblCont[i], 1, row++);
                    gridPane.getRowConstraints().add(new RowConstraints());

                    final int ii = i;
                    lblCont[i].setOnContextMenuRequested(event ->
                            getMenu(lblCont[ii].getText()).show(lblCont[ii], event.getScreenX(), event.getScreenY()));
            }
        }

        setUrl();
    }

    private void setUrl() {
        pHyperlinkUrl.setWrapText(true);
        pHyperlinkUrl.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //------------

        pHyperlinkWebsite.setWrapText(true);
        pHyperlinkWebsite.maxWidthProperty().bind(getVBoxCompleteDialog().widthProperty().subtract(FREE)); //----------

        textTitle[StationXml.STATION_URL].setVisible(urlProperty.get());
        textTitle[StationXml.STATION_URL].setManaged(urlProperty.get());
        pHyperlinkUrl.setVisible(urlProperty.get());
        pHyperlinkUrl.setManaged(urlProperty.get());
        pHyperlinkUrl.setMinHeight(Region.USE_PREF_SIZE);

        textTitle[StationXml.STATION_WEBSITE].setVisible(urlProperty.get());
        textTitle[StationXml.STATION_WEBSITE].setManaged(urlProperty.get());
        pHyperlinkWebsite.setVisible(urlProperty.get());
        pHyperlinkWebsite.setManaged(urlProperty.get());
        pHyperlinkWebsite.setMinHeight(Region.USE_PREF_SIZE);
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
