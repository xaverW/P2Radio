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

import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.start.PlayingTitle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class StatusBarController extends AnchorPane {

    private final StackPane stackPane = new StackPane();

    private final Label lblNowPlayingStation = new Label();
    private final Label lblNowPlayingFavourite = new Label();
    private final Label lblNowPlayingHistory = new Label();

    // Sender
    private final Label lblLeftStation = new Label();
    private final Label lblRightStation = new Label();
    // Favoriten
    private final Label lblLeftFavourite = new Label();
    private final Label lblRightFavourite = new Label();
    // History
    private final Label lblLeftHistory = new Label();
    private final Label lblRightHistory = new Label();

    private final Pane stationPane;
    private final Pane favouritePane;
    private final Pane historyPane;
    private final ProgData progData;
    private StatusbarIndex statusbarIndex = StatusbarIndex.STATION;
    private boolean stopTimer = false;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);
        stationPane = getHbox(lblLeftStation, lblNowPlayingStation, lblRightStation);
        favouritePane = getHbox(lblLeftFavourite, lblNowPlayingFavourite, lblRightFavourite);
        historyPane = getHbox(lblLeftHistory, lblNowPlayingHistory, lblRightHistory);
        make();
    }

    private HBox getHbox(Label lblLeft, Label lblNowPlaying, Label lblRight) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        lblNowPlaying.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblNowPlaying, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeft, P2GuiTools.getVDistance(20), lblNowPlaying, lblRight);
        hBox.setStyle("-fx-background-color: -fx-background;");
        return hBox;
    }

    private void make() {
        stackPane.getChildren().addAll(stationPane, favouritePane, historyPane);
        stackPane.setPadding(new Insets(2, 5, 2, 5));
        stackPane.toFront();

        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_START) {
            @Override
            public void pingGui(P2Event event) {
                stopTimer = true;
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.LOAD_RADIO_LIST_FINISHED) {
            @Override
            public void pingGui(P2Event event) {
                stopTimer = false;
                setStatusbarIndex(statusbarIndex);
            }
        });
        progData.pEventHandler.addListener(new P2Listener(PEvents.EVENT_TIMER_SECOND) {
            public void pingGui(P2Event event) {
                if (!progData.worker.getPropLoadWeb()) {
                    try {
                        if (!stopTimer) {
                            setStatusbarIndex(statusbarIndex);
                        }
                    } catch (final Exception ex) {
                        P2Log.errorLog(936251087, ex);
                    }
                }
            }
        });
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;

        switch (statusbarIndex) {
            case STATION -> stationPane.toFront();
            case FAVOURITE -> favouritePane.toFront();
            case HISTORY -> historyPane.toFront();
            default -> stackPane.toFront();
        }
        setText();
    }

    private void setText() {
        lblLeftStation.setText(StatusbarInfoFactory.getInfosStations());
        lblNowPlayingStation.setText(PlayingTitle.nowPlaying);

        lblLeftFavourite.setText(StatusbarInfoFactory.getInfosFavourites());
        lblNowPlayingFavourite.setText(PlayingTitle.nowPlaying);

        lblLeftHistory.setText(StatusbarInfoFactory.getInfosHistory());
        lblNowPlayingHistory.setText(PlayingTitle.nowPlaying);

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
        lblRightHistory.setText(strText);
    }

    public enum StatusbarIndex {STATION, FAVOURITE, HISTORY}
}
