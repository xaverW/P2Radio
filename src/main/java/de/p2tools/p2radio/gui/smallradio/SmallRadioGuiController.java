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
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgIcons;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class SmallRadioGuiController extends P2DialogOnly {

    final SmallRadioGuiTop smallRadioGuiTop;
    final SmallRadioGuiCenter smallRadioGuiCenter;
    final SmallRadioGuiBottom smallRadioGuiBottom;
    private final ProgData progData;

    public SmallRadioGuiController() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE,
                "Radio", false, false, true);

        progData = ProgData.getInstance();

        smallRadioGuiTop = new SmallRadioGuiTop(this);
        smallRadioGuiCenter = new SmallRadioGuiCenter(this);
        smallRadioGuiBottom = new SmallRadioGuiBottom(this);

        init(false);

        Button btnStop = getMaskerPane().getButton();
        getMaskerPane().setButtonText("");
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_CLEAR.getImageView());
        btnStop.setOnAction(a -> progData.webWorker.setStop(true));

        if (!ProgConfig.SYSTEM_SMALL_RADIO_SHOW_START_HELP.getValue()) {
            new SmallGuiHelpDialogController(ProgData.getInstance().primaryStage);
            ProgConfig.SYSTEM_SMALL_RADIO_SHOW_START_HELP.setValue(true);
        }
    }

    @Override
    public void make() {
        getVBoxCompleteDialog().getChildren().addAll(smallRadioGuiTop, smallRadioGuiCenter, smallRadioGuiBottom);
        VBox.setVgrow(smallRadioGuiCenter, Priority.ALWAYS);
        VBox.setVgrow(super.getVBoxCompleteDialog(), Priority.ALWAYS);

        getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                close();
            }
        });
        getStage().setOnCloseRequest(e -> {
            e.consume();
            close();
        });
    }

    @Override
    public void close() {
        saveTable();
        progData.smallRadioGuiController = null;
        ProgConfig.SYSTEM_SMALL_RADIO.set(false);
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

    public void saveTable() {
        smallRadioGuiCenter.saveTable();
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> favourite = getSel();
        favourite.ifPresent(StartFactory::startStation);
    }

    public Optional<StationData> getSel() {
        return smallRadioGuiCenter.getSel();
    }

    public void selLastHistory() {
        smallRadioGuiCenter.selLastHistory();
    }

    public void playRandomStation() {
        smallRadioGuiCenter.playRandomStation();
    }
}
