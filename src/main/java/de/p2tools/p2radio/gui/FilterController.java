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

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FilterController extends AnchorPane {


    private final VBox vBoxAll = new VBox();
    private final VBox vBoxTop = new VBox(20);
    private final VBox vBoxBottom = new VBox();
    private final ProgData progData;

    public FilterController() {
        progData = ProgData.getInstance();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        Button button = new Button();
        button.getStyleClass().add("close-button");
        button.setOnAction(a -> ProgConfig.STATION_GUI_FILTER_DIVIDER_ON.setValue(false));

        HBox hBox = new HBox();
        hBox.getStyleClass().add("close-pane");
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(button);
        vBoxAll.getChildren().add(hBox);
        initVBox();

        scrollPane.setContent(vBoxAll);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);
    }

    public VBox getVBoxTop() {
        return vBoxTop;
    }

    public VBox getVBoxBottom() {
        return vBoxBottom;
    }

    private void initVBox() {
        VBox.setVgrow(vBoxTop, Priority.ALWAYS);

        vBoxBottom.getStyleClass().add("extra-pane");
        vBoxBottom.setPadding(new Insets(10));
        vBoxBottom.setSpacing(20);
        vBoxBottom.setMaxWidth(Double.MAX_VALUE);

        Separator sp = new Separator();
        sp.setVisible(false);
        sp.setMinHeight(10);

        vBoxAll.getChildren().addAll(vBoxTop, sp, vBoxBottom);
    }
}
