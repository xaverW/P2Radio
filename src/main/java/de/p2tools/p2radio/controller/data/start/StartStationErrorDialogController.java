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

package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StartStationErrorDialogController extends PDialogExtra {

    private final HBox hBoxTitle;
    private final VBox vBoxCont;
    private final String message;
    private final Start start;
    private final Label lblHeader = new Label("Downloadfehler");
    private final Button btnOk = new Button("_Ok");
    private final Label lblStationTitle = new Label("ARD: Tatort, ..");
    private final Label lblUrl = new Label();
    private final TextArea txtCont = new TextArea();
    private final Label lblTime = new Label("");
    private final ImageView imageView = new ImageView();
    private final GridPane gridPane = new GridPane();
    private Timeline timeline = null;
    private Integer timeSeconds = ProgConfig.SYSTEM_PARAMETER_START_STATION_ERRORMSG_IN_SECOND.get();

    public StartStationErrorDialogController(Start start, String message) {
        super(ProgData.getInstance().primaryStage, ProgConfig.START_STATION_ERROR_DIALOG_SIZE,
                "Fehler", true, false);

        this.start = start;
        this.message = message;

        hBoxTitle = getHBoxTitle();
        vBoxCont = getVBoxCont();
        init(true);
    }

    @Override
    public void make() {
        initCont();

        lblStationTitle.setStyle("-fx-font-weight: bold;");
        lblStationTitle.setText(start.getPlayable().getStationName());

        lblUrl.setText(start.getUrl());

        txtCont.setEditable(false);
        txtCont.setText(message);

        btnOk.setOnAction(event -> {
            stopCounter();
            quit();
        });

        imageView.setImage(ProgIcons.Icons.IMAGE_ACHTUNG_64.getImage());

        //start the countdown...
        lblTime.setText("");
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new CountdownAction()));
        timeline.playFromStart();
    }

    private void initCont() {
        hBoxTitle.getChildren().add(lblHeader);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        vBox.getChildren().add(imageView);

        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int row = 0;
        gridPane.add(new Label("Sender:"), 0, row);
        gridPane.add(lblStationTitle, 1, row);

        gridPane.add(new Label("URL:"), 0, ++row);
        gridPane.add(lblUrl, 1, row);

        GridPane.setHgrow(txtCont, Priority.ALWAYS);
        GridPane.setVgrow(txtCont, Priority.ALWAYS);
        gridPane.add(new Label("Fehler:"), 0, ++row);
        gridPane.add(txtCont, 1, row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        HBox hBox = new HBox(10);
        VBox.setVgrow(hBox, Priority.ALWAYS);
        hBox.getChildren().addAll(vBox, gridPane);

        vBoxCont.setPadding(new Insets(5));
        vBoxCont.setSpacing(10);
        vBoxCont.getChildren().add(hBox);

        addOkButton(btnOk);
        getHboxLeft().getChildren().add(lblTime);
    }

    private void stopCounter() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void quit() {
        close();
    }

    private class CountdownAction implements EventHandler {

        @Override
        public void handle(Event event) {
            timeSeconds--;
            if (timeSeconds > 0) {
                lblTime.setText(timeSeconds + "");
            } else {
                stopCounter();
                quit();
            }
        }
    }
}
