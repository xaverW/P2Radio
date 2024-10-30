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


package de.p2tools.p2radio.gui.tools;

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.ProgQuit;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.gui.configdialog.ConfigDialogController;
import de.p2tools.p2radio.gui.dialog.AboutDialogController;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Arrays;

public class ProgTray {
    private final ProgData progData;
    private SystemTray systemTray = null;

    public ProgTray(ProgData progData) {
        this.progData = progData;
    }

    public void initProgTray() {
        ProgConfig.SYSTEM_TRAY.addListener((observableValue, aBoolean, t1) -> {
            Platform.runLater(() -> setTray());
        });
        ProgConfig.SYSTEM_TRAY_USE_OWN_ICON.addListener((observableValue, aBoolean, t1) -> {
            Platform.runLater(() -> setTray());
        });
        ProgConfig.SYSTEM_TRAY_ICON_PATH.addListener((observableValue, aBoolean, t1) -> {
            Platform.runLater(() -> setTray());
        });
        setTray();
    }

    private void removeTray() {
        if (systemTray != null) {
            Arrays.stream(systemTray.getTrayIcons()).sequential().forEach(e -> systemTray.remove(e));
            systemTray = null;
        }
    }

    private void setTray() {
        if (!SystemTray.isSupported()) {
            return;
        }
        if (!ProgConfig.SYSTEM_TRAY.get()) {
            removeTray();
            return;
        }

        if (systemTray == null) {
            systemTray = SystemTray.getSystemTray();
        }
        setIcon();
    }

    private void setIcon() {
        if (systemTray == null) {
            return;
        }

        for (TrayIcon tr : systemTray.getTrayIcons()) {
            //vorhandene Icons erst mal entfernen
            systemTray.remove(tr);
        }

        Image image;
        if (ProgConfig.SYSTEM_TRAY_USE_OWN_ICON.getValue() && !ProgConfig.SYSTEM_TRAY_ICON_PATH.getValueSafe().isEmpty()) {
            String resource = ProgConfig.SYSTEM_TRAY_ICON_PATH.getValueSafe();
            image = Toolkit.getDefaultToolkit().getImage(resource);
        } else {
            String resource = "de/p2tools/p2radio/res/P2_24.png";
            URL res = ClassLoader.getSystemResource(resource);
            image = Toolkit.getDefaultToolkit().getImage(res);
        }

        TrayIcon trayicon = new TrayIcon(image, "MTPlayer");
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
            P2Log.errorLog(912547030, exception.getMessage());
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
            StartFactory.stopRunningStation();
//            progData.stationList.forEach(StartFactory::stopPlayable);
//            progData.favouriteList.forEach(StartFactory::stopPlayable);
//            progData.historyList.forEach(StartFactory::stopPlayable);
        });
        miInfo.addActionListener(e -> Platform.runLater(() -> {
            progData.stationInfoDialogController.toggleShowInfo();
        }));
        miConfig.addActionListener(e -> Platform.runLater(() -> new ConfigDialogController(progData)));
        miTray.addActionListener(e -> Platform.runLater(() -> {
            //vor dem Ausschalten des Tray GUI anzeigen!!
            closeTray();
        }));

        miAbout.addActionListener(e -> Platform.runLater(() -> new AboutDialogController(ProgData.getInstance()).showDialog()));
        miQuit.addActionListener(e -> Platform.runLater(() -> {
            ProgQuit.quit();
        }));

        PopupMenu popupMenu = new PopupMenu();
        popupMenu.add(miMaxMin);
        popupMenu.add(miStop);
        popupMenu.add(miInfo);
        popupMenu.add(miConfig);
        if (!P2ToolsFactory.getOs().equals(P2ToolsFactory.OperatingSystemType.MAC)) {
            //machen unter MAC Probleme
            popupMenu.add(miTray);
        }

        popupMenu.addSeparator();
        popupMenu.add(miAbout);
        if (!P2ToolsFactory.getOs().equals(P2ToolsFactory.OperatingSystemType.MAC)) {
            //machen unter MAC Probleme
            popupMenu.addSeparator();
            popupMenu.add(miQuit);
        }

        trayicon.setPopupMenu(popupMenu);
    }


    private void closeTray() {
        //dann die Dialoge wieder anzeigen
        showDialog();
        ProgConfig.SYSTEM_TRAY.setValue(false);
    }

    private synchronized void maxMin() {
        if (progData.primaryStage.isShowing()) {
            closeDialog();
        } else {
            showDialog();
        }
    }

    private void closeDialog() {
        Platform.runLater(() -> {
            progData.primaryStage.close();
        });
        P2DialogExtra.closeAllDialog();
    }

    private void showDialog() {
        Platform.runLater(() -> {
            progData.primaryStage.show();
        });
        P2DialogExtra.showAllDialog();
    }
}
