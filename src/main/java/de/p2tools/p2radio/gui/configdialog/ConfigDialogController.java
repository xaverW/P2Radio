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

package de.p2tools.p2radio.gui.configdialog;

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.p2event.P2Event;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.gui.configdialog.setdata.ControllerSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConfigDialogController extends P2DialogExtra {
    private final ProgData progData;
    private final TabPane tabPane = new TabPane();
    private final Button btnOk = new Button("_Ok");
    private final BooleanProperty blackChanged = new SimpleBooleanProperty(false);

    private ControllerConfig controllerConfig;
    private ControllerBlackList controllerBlackList;
    private ControllerSet controllerSet;

    public ConfigDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConfig.CONFIG_DIALOG_SIZE, "Einstellungen",
                true, false, DECO.NO_BORDER);

        this.progData = progData;
        init(true);
    }

    @Override
    public void make() {
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(tabPane);
        getVBoxCont().setPadding(new Insets(0));

        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());

        ProgConfig.SYSTEM_THEME_CHANGED.addListener((u, o, n) -> updateCss());
        initPanel();
    }

    public void close() {
        if (blackChanged.get()) {
            //sonst hat sich nichts geändert
            progData.stationList.filterListWithBlacklist(true);
            progData.pEventHandler.notifyListener(new de.p2tools.p2radio.controller.p2event.P2Event(PEvents.BLACKLIST_CHANGED));
        }

        controllerConfig.close();
        controllerBlackList.close();
        controllerSet.close();

        progData.pEventHandler.notifyListener(new P2Event(PEvents.SETDATA_CHANGED));
        super.close();
    }

    private void initPanel() {
        try {
            controllerConfig = new ControllerConfig(getStage());
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(controllerConfig);
            tabPane.getTabs().add(tab);

            controllerBlackList = new ControllerBlackList(getStage(), blackChanged);
            tab = new Tab("Blacklist");
            tab.setClosable(false);
            tab.setContent(controllerBlackList);
            tabPane.getTabs().add(tab);

            controllerSet = new ControllerSet(getStage());
            tab = new Tab("Abspielen");
            tab.setClosable(false);
            tab.setContent(controllerSet);
            tabPane.getTabs().add(tab);

            tabPane.getSelectionModel().select(ProgConfig.SYSTEM_CONFIG_DIALOG_TAB.get());
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                // readOnlyBinding!!
                ProgConfig.SYSTEM_CONFIG_DIALOG_TAB.setValue(newValue);
            });
        } catch (final Exception ex) {
            P2Log.errorLog(784459510, ex);
        }
    }
}
