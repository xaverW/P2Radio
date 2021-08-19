/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2radio.controller.config;

import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.gui.configDialog.ConfigDialogController;
import de.p2tools.p2radio.gui.dialog.AboutDialogController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Arrays;

public class ProgTray {
    private final ProgData progData;
    private BooleanProperty propTray = ProgConfig.SYSTEM_TRAY;
    private SystemTray systemTray = null;

    public ProgTray(ProgData progData) {
        this.progData = progData;
        propTray.addListener((observableValue, aBoolean, t1) -> {
            if (propTray.get()) {
                setTray();
            } else {
                removeTray();
            }
        });
        if (propTray.get()) {
            setTray();
//        } else {
//            removeTray();
        }
    }

    public void removeTray() {
        if (systemTray != null) {
            Arrays.stream(systemTray.getTrayIcons()).sequential().forEach(e -> systemTray.remove(e));
            systemTray = null;
        }
    }

    private void setTray() {
        if (!SystemTray.isSupported()) {
            return;
        }

        systemTray = SystemTray.getSystemTray();
        String resource = "/de/p2tools/p2radio/res/P2_24.png";
        URL res = getClass().getResource(resource);
        Image image = Toolkit.getDefaultToolkit().getImage(res);

        TrayIcon trayicon = new TrayIcon(image, "P2Radio");
        addMenu(trayicon);
        trayicon.setImageAutoSize(true);
        trayicon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    maxMin();
                }
            }
        });

        try {
            systemTray.add(trayicon);
        } catch (AWTException exception) {
            PLog.errorLog(945120364, exception.getMessage());
        }
    }

    private void addMenu(TrayIcon trayicon) {
        java.awt.MenuItem miMaxMin = new java.awt.MenuItem("Programm maximieren/minimieren");
        java.awt.MenuItem miStop = new java.awt.MenuItem("alle laufenden Sender stoppen");
        java.awt.MenuItem miInfo = new java.awt.MenuItem("Sender-Info-Dialog öffnen");
        java.awt.MenuItem miConfig = new java.awt.MenuItem("Einstellungen öffnen");
        java.awt.MenuItem miTray = new java.awt.MenuItem("Tray-Icon ausblenden");

        java.awt.MenuItem miAbout = new java.awt.MenuItem("über dieses Programm");
        java.awt.MenuItem miQuit = new java.awt.MenuItem("Programm Beenden");

        miMaxMin.addActionListener(e -> Platform.runLater(() -> maxMin()));
        miStop.addActionListener(e -> {
            progData.stationList.stream().forEach(f -> progData.startFactory.stopStation(f));
            progData.favouriteList.stream().forEach(f -> progData.startFactory.stopFavourite(f));
            progData.lastPlayedList.stream().forEach(f -> progData.startFactory.stopLastPlayed(f));
        });
        miInfo.addActionListener(e -> Platform.runLater(() -> {
            progData.stationInfoDialogController.toggleShowInfo();
        }));
        miConfig.addActionListener(e -> Platform.runLater(() -> {
            new ConfigDialogController();
        }));
        miTray.addActionListener(e -> Platform.runLater(() -> {
            //vor dem Ausschalten des Tray GUI anzeigen!!
            closeTray();
        }));

        miAbout.addActionListener(e -> Platform.runLater(() -> new AboutDialogController(progData)));
        miQuit.addActionListener(e -> Platform.runLater(() -> {
            Stage stage = null;
            if (progData.smallRadioGuiController != null) {
                stage = progData.smallRadioGuiController.getSmallRadioGuiPack().getStage();
            } else if (progData.primaryStage.isShowing()) {
                stage = progData.primaryStage;
            }
            ProgQuitFactory.quit(stage, true);
        }));

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(miMaxMin);
        popupMenu.add(miStop);
        popupMenu.add(miInfo);
        popupMenu.add(miConfig);
        popupMenu.add(miTray);

        popupMenu.addSeparator();
        popupMenu.add(miAbout);
        popupMenu.addSeparator();
        popupMenu.add(miQuit);

        trayicon.setPopupMenu(popupMenu);
    }

    private void closeTray() {
        if (progData.smallRadioGuiController != null) {
            PGuiSize.showSave(progData.smallRadioGuiController.getSmallRadioGuiPack().getStage());
        } else {
            PGuiSize.showSave(progData.primaryStage);
        }

//        if (progData.smallRadioGuiController != null &&
//                !progData.smallRadioGuiController.getSmallRadioGuiPack().getStage().isShowing()) {
//            Platform.runLater(() -> progData.smallRadioGuiController.getSmallRadioGuiPack().getStage().show());
//
//        } else if (!progData.primaryStage.isShowing()) {
//            Platform.runLater(() -> progData.primaryStage.show());
//        }
        ProgConfig.SYSTEM_TRAY.setValue(false);
    }

    private void maxMin() {
        if (progData.smallRadioGuiController != null) {
            //dann den SmallRadio
            if (progData.smallRadioGuiController.getSmallRadioGuiPack().getStage().isShowing()) {
                Platform.runLater(() -> progData.smallRadioGuiController.getSmallRadioGuiPack().getStage().close());

            } else {
                PGuiSize.showSave(progData.smallRadioGuiController.getSmallRadioGuiPack().getStage());
//                Platform.runLater(() -> progData.smallRadioGuiController.getSmallRadioGuiPack().getStage().show());
            }

        } else {
            if (progData.primaryStage.isShowing()) {
                Platform.runLater(() -> progData.primaryStage.close());

            } else {
                PGuiSize.showSave(progData.primaryStage);
//                Platform.runLater(() -> progData.primaryStage.show());
            }
        }
    }
}
