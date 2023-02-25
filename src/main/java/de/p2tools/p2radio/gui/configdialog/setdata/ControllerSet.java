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

package de.p2tools.p2radio.gui.configdialog.setdata;

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.SetData;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerSet extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBox = new VBox();
    private final Stage stage;
    private final PaneSetList paneSetList;
    private final PaneSetData paneSetData;
    private final ObjectProperty<SetData> aktSetDate = new SimpleObjectProperty<>();

    public ControllerSet(Stage stage) {
        this.stage = stage;

        paneSetList = new PaneSetList(this);
        paneSetData = new PaneSetData(this);

        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        VBox.setVgrow(paneSetList, Priority.ALWAYS);
        vBox.getChildren().addAll(paneSetList);

        splitPane.getItems().addAll(vBox, scrollPane);
        SplitPane.setResizableWithParent(vBox, Boolean.FALSE);
        getChildren().addAll(splitPane);

        scrollPane.setContent(paneSetData);
        splitPane.getDividers().get(0).positionProperty().bindBidirectional(ProgConfig.CONFIG_DIALOG_SET_DIVIDER);
    }

    public void close() {
        splitPane.getDividers().get(0).positionProperty().unbindBidirectional(ProgConfig.CONFIG_DIALOG_SET_DIVIDER);
        paneSetData.close();
        paneSetList.close();
    }

    public Stage getStage() {
        return stage;
    }

    public SetData getAktSetDate() {
        return aktSetDate.get();
    }

    public void setAktSetDate(SetData aktSetDate) {
        this.aktSetDate.set(aktSetDate);
    }

    public ObjectProperty<SetData> aktSetDateProperty() {
        return aktSetDate;
    }
}