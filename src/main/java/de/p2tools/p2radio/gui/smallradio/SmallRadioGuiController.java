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

package de.p2tools.p2radio.gui.smallradio;

import de.p2tools.p2lib.dialogs.dialog.P2DialogOnly;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.tools.P2SystemUtils;
import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.dialog.SmallGuiHelpDialogController;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.Optional;

public class SmallRadioGuiController extends P2DialogOnly {

    final SmallRadioGuiCenter smallRadioGuiCenter;
    final SmallRadioGuiBottom smallRadioGuiBottom;
    private final ProgData progData;
    private final P2Listener listener = new P2Listener(Events.REFRESH_TABLE) {
        public void pingGui(P2Event event) {
            tableRefresh();
        }
    };

    public SmallRadioGuiController() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE,
                "Radio", false, false, true);

        progData = ProgData.getInstance();
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(true);

        progData.smallRadioGuiController = this;
        smallRadioGuiCenter = new SmallRadioGuiCenter(this);
        smallRadioGuiBottom = new SmallRadioGuiBottom(this);

        init(true);

        if (!ProgConfig.SYSTEM_SMALL_RADIO_SHOW_START_HELP.getValue()) {
            new SmallGuiHelpDialogController(ProgData.getInstance().primaryStage);
            ProgConfig.SYSTEM_SMALL_RADIO_SHOW_START_HELP.setValue(true);
        }
    }

    @Override
    public void make() {
        SmallRadioFactory.addBorderListener(getStage());
        getStage().initStyle(StageStyle.TRANSPARENT);
        getVBoxCompleteDialog().getStyleClass().add("smallGui");

        getVBoxCompleteDialog().getChildren().addAll(smallRadioGuiCenter, smallRadioGuiBottom);
        VBox.setVgrow(smallRadioGuiCenter, Priority.ALWAYS);
        VBox.setVgrow(super.getVBoxCompleteDialog(), Priority.ALWAYS);

        getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                changeGui();
            }
        });
        getStage().setOnCloseRequest(e -> {
            e.consume();
            close();
        });
        progData.pEventHandler.addListener(listener);
    }

    @Override
    public void close() {
        saveMe();
        ProgData.getInstance().pEventHandler.removeListener(listener);
        super.close();
    }

    public void setNextStation() {
        smallRadioGuiCenter.setNextStation();
    }

    public void setPreviousStation() {
        smallRadioGuiCenter.setPreviousStation();
    }

    public P2MaskerPane getMaskerPane() {
        return super.getMaskerPane();
    }

    public void changeGui() {
        close();
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(false);
        progData.smallRadioGuiController = null;
        progData.p2RadioController.quittSmallRadio();
    }

    private void saveMe() {
        progData.smallRadioGuiController.saveTable();
        P2GuiSize.getSizeStage(ProgConfig.SMALL_RADIO_SIZE, getStage());
    }

    public void saveTable() {
        smallRadioGuiCenter.saveTable();
    }

    public void tableRefresh() {
        smallRadioGuiCenter.tableRefresh();
    }

    public void copyUrl() {
        final Optional<StationData> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        P2SystemUtils.copyToClipboard(favourite.get().getStationUrl());
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> favourite = getSel();
        if (favourite.isPresent()) {
            progData.startFactory.playPlayable(favourite.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.favouriteList.stream().forEach(f -> progData.startFactory.stopPlayable(f));

        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                progData.startFactory.stopPlayable(favourite.get());
            }
        }
    }

    public Optional<StationData> getSel() {
        return smallRadioGuiCenter.getSel();
    }

    public void playRandomStation() {
        smallRadioGuiCenter.playRandomStation();
    }
}
