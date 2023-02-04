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

package de.p2tools.p2radio.gui.configDialog;

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.configDialog.setData.SetPanePack;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConfigDialogController extends PDialogExtra {
    private static ConfigDialogController instance;
    private final ProgData progData;
    private final TabPane tabPane = new TabPane();
    private final Button btnOk = new Button("_Ok");
    private final BooleanProperty blackChanged = new SimpleBooleanProperty(false);
    IntegerProperty propSelectedTab = ProgConfig.SYSTEM_CONFIG_DIALOG_TAB;
    ConfigPaneController configPane;
    BlackListPaneController blackPane;
    SetPanePack setPane;

    private ConfigDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConfig.CONFIG_DIALOG_SIZE, "Einstellungen",
                true, false, DECO.NO_BORDER);

        this.progData = progData;
        init(false);//!!!!!!!!!!!!!!!!!!!!!
    }

    public synchronized static final ConfigDialogController getInstanceAndShow() {
        if (instance == null) {
            instance = new ConfigDialogController(ProgData.getInstance());
        }

        if (!instance.isShowing()) {
            instance.showDialog();
        }
        instance.getStage().toFront();

        return instance;
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
            progData.pEventHandler.notifyListener(new PEvent(Events.BLACKLIST_CHANGED));
        }

        configPane.close();
        blackPane.close();
        setPane.close();

        progData.pEventHandler.notifyListener(new PEvent(Events.SETDATA_CHANGED));
        super.close();
    }

    private void initPanel() {
        try {
            configPane = new ConfigPaneController(getStage());
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(configPane);
            tabPane.getTabs().add(tab);

            blackPane = new BlackListPaneController(getStage(), blackChanged);
            tab = new Tab("Blacklist");
            tab.setClosable(false);
            tab.setContent(blackPane);
            tabPane.getTabs().add(tab);

            setPane = new SetPanePack(getStage());
            tab = new Tab("Abspielen");
            tab.setClosable(false);
            tab.setContent(setPane);
            tabPane.getTabs().add(tab);

            tabPane.getSelectionModel().select(propSelectedTab.get());
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                // readOnlyBinding!!
                propSelectedTab.setValue(newValue);
            });
        } catch (final Exception ex) {
            PLog.errorLog(784459510, ex);
        }
    }
}
