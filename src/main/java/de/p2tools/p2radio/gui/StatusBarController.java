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
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.RunEventRadio;
import de.p2tools.p2radio.controller.data.start.PlayingTitle;
import de.p2tools.p2radio.controller.worker.InfoFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class StatusBarController extends AnchorPane {

    private final StackPane stackPane = new StackPane();
    //Sender
    private final Label lblLeftStation = new Label();
    private final Label lblNowPlayingStation = new Label();
    private final Label lblNowPlayingFavourite = new Label();
    private final Label lblRightStation = new Label();
    //Favoriten
    private final Label lblLeftFavourite = new Label();
    private final Label lblRightFavourite = new Label();
    private final Pane nonePane;
    private final Pane stationPane;
    private final Pane favouritePane;
    private final ProgData progData;
    private StatusbarIndex statusbarIndex = StatusbarIndex.NONE;
    private boolean stopTimer = false;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);
        nonePane = new HBox();
        stationPane = getHbox(lblLeftStation, lblNowPlayingStation, lblRightStation);
        favouritePane = getHbox(lblLeftFavourite, lblNowPlayingFavourite, lblRightFavourite);
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
        stackPane.getChildren().addAll(nonePane, stationPane, favouritePane);
        stackPane.setPadding(new Insets(2, 5, 2, 5));
        nonePane.toFront();

        progData.pEventHandler.addListener(new P2Listener(Events.LOAD_RADIO_LIST) {
            public <T extends P2Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEventRadio.class)) {
                    RunEventRadio runE = (RunEventRadio) runEvent;

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.START)) {
                        stopTimer = true;
                    }

                    if (runE.getNotify().equals(RunEventRadio.NOTIFY.FINISHED)) {
                        stopTimer = false;
                        setStatusbarIndex(statusbarIndex);
                    }
                }
            }
        });

        progData.pEventHandler.addListener(new P2Listener(Events.TIMER) {
            public void pingGui(P2Event event) {
                if (!progData.loadNewStationList.getPropLoadStationList()) {
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
            case STATION:
                stationPane.toFront();
                setText();
                break;
            case FAVOURITE:
                favouritePane.toFront();
                setText();
                break;
            case NONE:
            default:
                nonePane.toFront();
                break;
        }
    }

    private void setText() {
        lblLeftStation.setText(InfoFactory.getInfosStations());
        lblNowPlayingStation.setText(PlayingTitle.nowPlaying.isEmpty() ? "" : PlayingTitle.nowPlaying);

        lblLeftFavourite.setText(InfoFactory.getInfosFavourites());
        lblNowPlayingFavourite.setText(PlayingTitle.nowPlaying.isEmpty() ? "" : PlayingTitle.nowPlaying);

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

    public enum StatusbarIndex {NONE, STATION, FAVOURITE, HISTORY}
}
