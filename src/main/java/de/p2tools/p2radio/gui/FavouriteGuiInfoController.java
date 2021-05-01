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

import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PHyperlink;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FavouriteGuiInfoController {
    private final GridPane gridPane = new GridPane();
    private final Label lblTitle = new Label("");
    private final Label lblWebsite = new Label("Website: ");
    private final Label lblUrl = new Label("Sender-URL: ");
    private final PHyperlink hyperlinkWebsite = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
    private final PHyperlink hyperlinkUrl = new PHyperlink("",
            ProgConfig.SYSTEM_PROG_OPEN_URL, new ProgIcons().ICON_BUTTON_FILE_OPEN);
    private final Label lblDescription = new Label("Beschreibung: ");
    private final TextArea taDescription = new TextArea();

    private Favourite favourite = null;

    public FavouriteGuiInfoController(AnchorPane anchorPane) {
        initInfo(anchorPane);
    }

    public void initInfo(AnchorPane anchorPane) {
        Button button = new Button();
        button.getStyleClass().add("close-button");
        button.setOnAction(a -> ProgConfig.FAVOURITE_GUI_DIVIDER_ON.setValue(false));

        VBox vBox = new VBox(0);
        vBox.getStyleClass().add("close-pane");
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(button);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(gridPane);

        HBox hBoxCont = new HBox();
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        hBoxCont.getChildren().addAll(scrollPane, vBox);
        AnchorPane.setLeftAnchor(hBoxCont, 0.0);
        AnchorPane.setBottomAnchor(hBoxCont, 0.0);
        AnchorPane.setRightAnchor(hBoxCont, 0.0);
        AnchorPane.setTopAnchor(hBoxCont, 0.0);
        anchorPane.getChildren().add(hBoxCont);
        anchorPane.setMinHeight(0);

        lblTitle.setFont(Font.font(null, FontWeight.BOLD, -1));
        lblWebsite.setMinWidth(Region.USE_PREF_SIZE);
        lblUrl.setMinWidth(Region.USE_PREF_SIZE);

        taDescription.setEditable(true);
        taDescription.setWrapText(true);
        taDescription.setPrefRowCount(2);

        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());

        int row = 0;
        gridPane.add(lblTitle, 0, row, 2, 1);

        gridPane.add(lblWebsite, 0, ++row);
        gridPane.add(hyperlinkWebsite, 1, row);

        gridPane.add(lblUrl, 0, ++row);
        gridPane.add(hyperlinkUrl, 1, row);

        gridPane.add(lblDescription, 0, ++row);
        gridPane.add(taDescription, 1, row);
    }

    public void setFavourite(Favourite favourite) {
        if (this.favourite != null) {
            taDescription.textProperty().unbindBidirectional(this.favourite.descriptionProperty());
        }

        this.favourite = favourite;
        if (favourite == null) {
            lblTitle.setText("");
            hyperlinkWebsite.setUrl("");
            hyperlinkUrl.setUrl("");
            taDescription.setText("");
            return;
        }

        lblTitle.setText(favourite.getStationName() + "  -  " + favourite.getCountry());
        hyperlinkWebsite.setUrl(favourite.getWebsite());
        hyperlinkUrl.setUrl(favourite.getUrl());
        taDescription.textProperty().bindBidirectional(favourite.descriptionProperty());
    }
}
