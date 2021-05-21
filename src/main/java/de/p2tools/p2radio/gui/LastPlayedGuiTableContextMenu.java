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

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFactory;
import de.p2tools.p2radio.gui.tools.table.Table;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;

public class LastPlayedGuiTableContextMenu {

    private final ProgData progData;
    private final LastPlayedGuiController lastPlayedGuiController;
    private final TableView tableView;

    public LastPlayedGuiTableContextMenu(ProgData progData, LastPlayedGuiController lastPlayedGuiController, TableView tableView) {
        this.progData = progData;
        this.lastPlayedGuiController = lastPlayedGuiController;
        this.tableView = tableView;
    }

    public ContextMenu getContextMenu(LastPlayed lastPlayed) {
        final ContextMenu contextMenu = new ContextMenu();
        getMenu(contextMenu, lastPlayed);
        return contextMenu;
    }

    private void getMenu(ContextMenu contextMenu, LastPlayed lastPlayed) {
        MenuItem miStart = new MenuItem("Sender starten");
        miStart.setOnAction(a -> lastPlayedGuiController.playStation());
        MenuItem miStop = new MenuItem("Sender stoppen");
        miStop.setOnAction(a -> lastPlayedGuiController.stopStation(false));
        MenuItem miStopAll = new MenuItem("alle Sender stoppen");
        miStopAll.setOnAction(a -> lastPlayedGuiController.stopStation(true /* alle */));
        MenuItem miCopyUrl = new MenuItem("Sender (URL) kopieren");
        miCopyUrl.setOnAction(a -> lastPlayedGuiController.copyUrl());

        MenuItem miRemove = new MenuItem("Sender aus History löschen");
        miRemove.setOnAction(a -> LastPlayedFactory.deleteHistory(false));

        miStart.setDisable(lastPlayed == null);
        miStop.setDisable(lastPlayed == null);
        miStopAll.setDisable(lastPlayed == null);
        miCopyUrl.setDisable(lastPlayed == null);
        miRemove.setDisable(lastPlayed == null);

        contextMenu.getItems().addAll(miStart, miStop, miStopAll, miCopyUrl, miRemove);

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.LAST_PLAYED));

        contextMenu.getItems().add(new SeparatorMenuItem());
        contextMenu.getItems().addAll(resetTable);
    }
}
