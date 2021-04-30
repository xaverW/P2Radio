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

package de.p2tools.p2radio.gui;

import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.pEvent.EventListenerLoadRadioList;
import de.p2tools.p2radio.controller.config.pEvent.EventLoadRadioList;
import de.p2tools.p2radio.controller.worker.InfoFactory;
import de.p2tools.p2radio.gui.tools.Listener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class StatusBarController extends AnchorPane {

    public enum StatusbarIndex {NONE, STATION, FAVOURITE}

    private final StackPane stackPane = new StackPane();
    //Sender
    private final Label lblLeftStation = new Label();
    private final Label lblRightStation = new Label();
    //Favoriten
    private final Label lblLeftFavourite = new Label();

    private final Label lblRightFavourite = new Label();
    private final Pane nonePane;
    private final Pane stationPane;
    private final Pane favouritePane;
    private StatusbarIndex statusbarIndex = StatusbarIndex.NONE;
    private boolean loadList = false;

    private final ProgData progData;
    private boolean stopTimer = false;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);
        nonePane = new HBox();
        stationPane = getHbox(lblLeftStation, lblRightStation);
        favouritePane = getHbox(lblLeftFavourite, lblRightFavourite);
        make();
    }

    private HBox getHbox(Label lblLeft, Label lblRight) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        lblLeft.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeft, Priority.ALWAYS);

        hBox.getChildren().addAll(lblLeft, lblRight);
        hBox.setStyle("-fx-background-color: -fx-background ;");
        return hBox;
    }


    private void make() {
        stackPane.getChildren().addAll(nonePane, stationPane, favouritePane);
        stackPane.setPadding(new Insets(2, 5, 2, 5));
        nonePane.toFront();

        progData.eventNotifyLoadRadioList.addListenerLoadStationList(new EventListenerLoadRadioList() {
            @Override
            public void start(EventLoadRadioList event) {
                stopTimer = true;
            }

            @Override
            public void finished(EventLoadRadioList event) {
                stopTimer = false;
                setStatusbarIndex(statusbarIndex);
            }
        });

        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, StatusBarController.class.getSimpleName()) {
            @Override
            public void pingFx() {
                try {
                    if (!stopTimer) {
                        setStatusbarIndex(statusbarIndex);
                    }
                } catch (final Exception ex) {
                    PLog.errorLog(936251087, ex);
                }
            }
        });
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;

        switch (statusbarIndex) {
            case STATION:
                stationPane.toFront();
                setInfoStation();
                setRightText();
                break;
            case FAVOURITE:
                favouritePane.toFront();
                setInfoFavourite();
                setRightText();
                break;
            case NONE:
            default:
                nonePane.toFront();
                break;
        }
    }


    private void setInfoStation() {
        lblLeftStation.setText(InfoFactory.getInfosStations());
    }

    private void setInfoFavourite() {
        lblLeftFavourite.setText(InfoFactory.getInfosFavourites());
    }

    private void setRightText() {
        // Text rechts: alter/neuladenIn anzeigen
        String strText = "Senderliste geladen: ";
        strText += progData.stationList.getGenDate();
        strText += "  ";

        final int day = progData.stationList.getAge();
        if (day != 0) {
            strText += "||  Alter: ";
            String strDay = String.valueOf(day);
            strText += strDay;
            strText += " Tage";
        }

        // Infopanel setzen
        lblRightStation.setText(strText);
        lblRightFavourite.setText(strText);
    }
}
