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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgIcons;
import de.p2tools.p2radio.controller.data.filter.FilterFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SmallRadioGuiBottom extends HBox {

    private final Button btnRandom = new Button("");
    private final Button btnStart = new Button("");
    private final Button btnStop = new Button("");
    private final RadioButton rbSender = new RadioButton("Sender");
    private final RadioButton rbFavourite = new RadioButton("Favoriten");
    private final RadioButton rbHistory = new RadioButton("History");
    private final SmallRadioGuiController smallRadioGuiController;
    private final ProgData progData;

    public SmallRadioGuiBottom(SmallRadioGuiController smallRadioGuiController) {
        progData = ProgData.getInstance();
        this.smallRadioGuiController = smallRadioGuiController;
        initBottom();
        initStartButton();
    }

    private void initBottom() {
        setPadding(new Insets(0, 10, 10, 10));

        //Collection
        ToggleGroup tg = new ToggleGroup();
        rbSender.setToggleGroup(tg);
        rbFavourite.setToggleGroup(tg);
        rbHistory.setToggleGroup(tg);
        if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_STATION)) {
            rbSender.setSelected(true);
        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_FAVOURITE)) {
            rbFavourite.setSelected(true);
        } else if (ProgConfig.SMALL_RADIO_SELECTED_LIST.getValueSafe().equals(FilterFactory.LIST_HISTORY)) {
            rbHistory.setSelected(true);
        }
        rbSender.setOnAction(a -> {
            setList();
        });
        rbFavourite.setOnAction(a -> {
            setList();
        });
        rbHistory.setOnAction(a -> {
            setList();
        });

        HBox hBoxRb = new HBox(P2LibConst.DIST_BUTTON);
        hBoxRb.setAlignment(Pos.CENTER);
        hBoxRb.getChildren().addAll(rbSender, rbFavourite, rbHistory);
        VBox vbColl = new VBox(P2LibConst.DIST_BUTTON);

        vbColl.getChildren().addAll(hBoxRb);

        HBox hBoxButton = new HBox(P2LibConst.DIST_BUTTON);
        hBoxButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxButton.getChildren().addAll(btnStart, btnStop, P2GuiTools.getVDistance(P2LibConst.DIST_BUTTON_BLOCK), btnRandom);

        setAlignment(Pos.BOTTOM_CENTER);
        getChildren().addAll(P2GuiTools.getHBoxGrower(), vbColl, P2GuiTools.getHBoxGrower(), hBoxButton);
    }

    private void setList() {
        if (rbSender.isSelected()) {
            ProgConfig.SMALL_RADIO_SELECTED_LIST.setValue(FilterFactory.LIST_STATION);
        } else if (rbFavourite.isSelected()) {
            ProgConfig.SMALL_RADIO_SELECTED_LIST.setValue(FilterFactory.LIST_FAVOURITE);
        } else {
            ProgConfig.SMALL_RADIO_SELECTED_LIST.setValue(FilterFactory.LIST_HISTORY);
        }
    }

    private void initStartButton() {
        btnRandom.setTooltip(new Tooltip("Einen Sender per Zufall starten"));
        btnRandom.getStyleClass().add("btnSmallGui");
        btnRandom.setGraphic(ProgIcons.ICON_BUTTON_RANDOM.getImageView());
        btnRandom.setOnAction(event -> {
            smallRadioGuiController.playRandomStation();
        });

        btnStart.setTooltip(new Tooltip("Sender abspielen"));
        btnStart.getStyleClass().add("btnSmallGui");
        btnStart.setGraphic(ProgIcons.ICON_BUTTON_PLAY.getImageView());
        btnStart.setOnAction(event -> {
            smallRadioGuiController.playStation();
        });

        btnStop.setTooltip(new Tooltip("alle laufenden Sender stoppen"));
        btnStop.getStyleClass().add("btnSmallGui");
        btnStop.setGraphic(ProgIcons.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction(event -> StartFactory.stopStation());
    }
}
