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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.gui.tools.table.Table;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

public class SmallRadioGuiTableContextMenu {

    private final ProgData progData;
    private final SmallRadioGuiController smallRadioGuiController;
    private final TableView tableView;

    public SmallRadioGuiTableContextMenu(ProgData progData, SmallRadioGuiController smallRadioGuiController, TableView tableView) {
        this.progData = progData;
        this.smallRadioGuiController = smallRadioGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(Favourite favourite) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, favourite);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, Favourite favourite) {
        MenuItem miStart = new MenuItem("Sender starten");
        miStart.setOnAction(a -> smallRadioGuiController.playStation());
        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> smallRadioGuiController.stopStation(false));
        MenuItem miStopAll = new MenuItem("alle Sender stoppen");
        miStopAll.setOnAction(a -> smallRadioGuiController.stopStation(true /* alle */));
        MenuItem miCopyUrl = new MenuItem("Sender (URL) kopieren");
        miCopyUrl.setOnAction(a -> smallRadioGuiController.copyUrl());

        MenuItem miChange = new MenuItem("Favorit ändern");
        miChange.setOnAction(a -> smallRadioGuiController.changeFavourite(false));
        MenuItem miRemove = new MenuItem("Favoriten löschen");
        miRemove.setOnAction(a -> progData.favouriteGuiController.deleteFavourite(false));

        miStart.setDisable(favourite == null);
        miStop.setDisable(favourite == null);
        miStopAll.setDisable(favourite == null);
        miCopyUrl.setDisable(favourite == null);
        miChange.setDisable(favourite == null);
        miRemove.setDisable(favourite == null);

        contextMenu.getItems().addAll(miStart, miStop, miStopAll, miCopyUrl, miChange, miRemove);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.FAVOURITE));

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
