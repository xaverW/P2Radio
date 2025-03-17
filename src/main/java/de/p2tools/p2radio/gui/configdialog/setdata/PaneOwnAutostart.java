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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.gui.tools.table.Table;
import de.p2tools.p2radio.gui.tools.table.TableStation;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneOwnAutostart {

    private final TableStation tableView;
    private final Stage stage;

    public PaneOwnAutostart(Stage stage) {
        this.stage = stage;
        tableView = new TableStation(Table.TABLE_ENUM.OWN_AUTOSTART);
    }

    public void makeConfig(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(P2LibConst.PADDING));

        initTable(vBox);

        TitledPane tpBlack = new TitledPane("Autostart-Liste", vBox);
        result.add(tpBlack);
        tpBlack.setMaxHeight(Double.MAX_VALUE);
    }

    public void close() {
    }

    private void initTable(VBox vBox) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        SortedList<StationData> sortedList;
        sortedList = new SortedList<>(ProgData.getInstance().ownAutoStartList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        vBox.getChildren().addAll(scrollPane);
    }
}