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
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.Station;
import de.p2tools.p2radio.controller.data.station.StationXml;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StationDialogController extends PDialogExtra {

    private final int FREE = 220;
    private final Label[] lblName = new Label[StationXml.MAX_ELEM];
    private final TextField[] txtCont = new TextField[StationXml.MAX_ELEM];

    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");
    private final ProgData progData;
    private final Station station;
    private boolean ok = false;

    public StationDialogController(ProgData progData, Station station) {
        super(progData.primaryStage, null,
                "Sender hinzufügen", true, false, DECO.SMALL);

        this.progData = progData;
        this.station = station;

        init(false);
    }

    public void closeCheck() {
        fillStation();
        ok = true;
        super.close();
    }

    public void close() {
        super.close();
    }

    public void showDialog() {
        super.showDialog();
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public void make() {
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            lblName[i] = new Label(StationXml.COLUMN_NAMES[i] + ":");
            txtCont[i] = new TextField("");
            txtCont[i].setText(station.arr[i]);
        }

        addOkButton(btnOk);
        btnOk.setOnAction(a -> closeCheck());
        btnOk.disableProperty().bind(txtCont[StationXml.STATION_URL].textProperty().isEmpty()
                .or(txtCont[StationXml.STATION_NAME].textProperty().isEmpty()));

        addCancelButton(btnCancel);
        btnCancel.setOnAction(a -> close());

        makeGridPane();
    }

    private void makeGridPane() {
        final GridPane gridPane = new GridPane();
        getvBoxCont().getChildren().clear();
        getvBoxCont().getChildren().add(gridPane);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(5));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        makeGrid(gridPane);
    }

    private void makeGrid(GridPane gridPane) {
        int row = 0;
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            switch (i) {
                case StationXml.STATION_PLAY:
                case StationXml.STATION_RECORD:
                case StationXml.STATION_URL_RESOLVED:
                case StationXml.STATION_NEW:
                    //die nicht anzeigen
                    break;
                case StationXml.STATION_BITRATE:
                case StationXml.STATION_COUNTRY_CODE:
                    //und die sind in der 2. Spalte
                    break;

                case StationXml.STATION_COUNTRY:
                    gridPane.add(lblName[StationXml.STATION_COUNTRY], 0, row);
                    gridPane.add(txtCont[StationXml.STATION_COUNTRY], 1, row);
                    txtCont[StationXml.STATION_COUNTRY].setOnContextMenuRequested(event ->
                            getMenu(txtCont[StationXml.STATION_COUNTRY].getText()).show(txtCont[StationXml.STATION_COUNTRY], event.getScreenX(), event.getScreenY()));

                    gridPane.add(lblName[StationXml.STATION_COUNTRY_CODE], 2, row);
                    gridPane.add(txtCont[StationXml.STATION_COUNTRY_CODE], 3, row++);
                    txtCont[StationXml.STATION_COUNTRY_CODE].setOnContextMenuRequested(event ->
                            getMenu(txtCont[StationXml.STATION_COUNTRY_CODE].getText()).show(txtCont[StationXml.STATION_COUNTRY_CODE], event.getScreenX(), event.getScreenY()));
                    break;

                case StationXml.STATION_CODEC:
                    gridPane.add(lblName[StationXml.STATION_CODEC], 0, row);
                    gridPane.add(txtCont[StationXml.STATION_CODEC], 1, row);
                    txtCont[StationXml.STATION_CODEC].setOnContextMenuRequested(event ->
                            getMenu(txtCont[StationXml.STATION_CODEC].getText()).show(txtCont[StationXml.STATION_CODEC], event.getScreenX(), event.getScreenY()));

                    gridPane.add(lblName[StationXml.STATION_BITRATE], 2, row);
                    gridPane.add(txtCont[StationXml.STATION_BITRATE], 3, row++);
                    txtCont[StationXml.STATION_BITRATE].setOnContextMenuRequested(event ->
                            getMenu(txtCont[StationXml.STATION_BITRATE].getText()).show(txtCont[StationXml.STATION_BITRATE], event.getScreenX(), event.getScreenY()));
                    break;

                default:
                    gridPane.add(lblName[i], 0, row);
                    gridPane.add(txtCont[i], 1, row++, 3, 1);
                    final int ii = i;
                    txtCont[i].setOnContextMenuRequested(event ->
                            getMenu(txtCont[ii].getText()).show(txtCont[ii], event.getScreenX(), event.getScreenY()));
            }
        }
    }

    private void fillStation() {
        for (int i = 0; i < StationXml.MAX_ELEM; ++i) {
            switch (i) {
                case StationXml.STATION_PLAY:
                case StationXml.STATION_RECORD:
                case StationXml.STATION_URL_RESOLVED:
                case StationXml.STATION_NEW:
                    //die nicht anzeigen
                    break;

                default:
                    station.arr[i] = txtCont[i].getText();
            }
        }
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
