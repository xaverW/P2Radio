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
package de.p2tools.p2radio;

import de.p2tools.p2lib.css.P2CssFactory;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2lib.tools.P2Lock;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.ProgStartAfterGui;
import de.p2tools.p2radio.controller.ProgStartBeforeGui;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.gui.dialog.StationInfoDialogController;
import de.p2tools.p2radio.gui.smallradio.SmallRadioGuiController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class P2Radio extends Application {

    private static final String LOG_TEXT_PROGRAM_START = "Dauer Programmstart";
    private ProgData progData;
    private boolean smallInitDone = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        P2Duration.counterStart(LOG_TEXT_PROGRAM_START);

        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        ProgStartBeforeGui.workBeforeGui(progData);

        //wenn gewünscht, Lock-File prüfen
        final String xmlFilePath = ProgInfos.getLockFileStr();
        if (ProgConfig.SYSTEM_ONLY_ONE_INSTANCE.getValue() && !P2Lock.getLockInstance(xmlFilePath)) {
            //dann kann man sich den Rest sparen
            return;
        }

        initRootLayout();
        ProgStartAfterGui.workAfterGui(progData);

        P2Duration.onlyPing("Gui steht!");
        P2Duration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initRootLayout() {
        try {
            progData.stationInfoDialogController = new StationInfoDialogController(progData);
            ProgConfig.SYSTEM_SMALL_RADIO.addListener((u, o, n) -> selectGui());

            initBigLayout();
            selectGui();
            if (ProgData.startMinimized) {
                progData.primaryStage.setIconified(true);
                P2DialogExtra.getDialogList().forEach(d -> d.getStage().setIconified(true));
            }

            if (ProgData.firstProgramStart) {
                // dann gabs den Startdialog
                ProgConfig.SYSTEM_DARK_THEME.set(ProgConfig.SYSTEM_DARK_START.get());
                ProgConfig.SYSTEM_GUI_THEME_1.set(ProgConfig.SYSTEM_GUI_THEME_1_START.get());
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void initBigLayout() {
        try {
            progData.p2RadioController = new P2RadioController(); // bigGui
            Scene sceneBig = new Scene(progData.p2RadioController,
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_SIZE_GUI, true),
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_SIZE_GUI, false));

            progData.primaryStage.setScene(sceneBig);
            progData.primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuitFactory.quit();
            });

            progData.primaryStage.setOnShowing(e -> P2GuiSize.setSizePos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage));
            progData.primaryStage.setOnShown(e -> P2GuiSize.setSizePos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage));

            P2CssFactory.addP2CssToScene(progData.primaryStage.getScene()); // und jetzt noch CSS einstellen
            PShortCutFactory.addShortCut(progData.primaryStage.getScene());
            setTitle();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void setTitle() {
        // muss nur für das große GUI gesetzt werden
        Stage stage = ProgData.getInstance().primaryStage;
        if (ProgData.debug) {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion() + " / DEBUG");
        } else {
            stage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion());
        }
    }

    private void initSmallLayout() {
        try {
            progData.smallRadioGuiController = new SmallRadioGuiController();
            progData.primaryStageSmall = progData.smallRadioGuiController.getStage();
            progData.primaryStageSmall.setOnCloseRequest(e -> {
                //beim Beenden
                e.consume();
                ProgQuitFactory.quit();
            });
            PShortCutFactory.addShortCut(progData.primaryStageSmall.getScene());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void selectGui() {
        if (ProgConfig.SYSTEM_SMALL_RADIO.getValue()) {
            // dann SMALL anzeigen
            if (!smallInitDone) {
                smallInitDone = true;
                initSmallLayout();
            }
            ProgData.STATION_TAB_ON.setValue(Boolean.FALSE);
            ProgData.FAVOURITE_TAB_ON.setValue(Boolean.FALSE);
            ProgData.HISTORY_TAB_ON.setValue(Boolean.FALSE);
            progData.primaryStageSmall.show();
            if (progData.primaryStage.isShowing()) {
                // nur wenn zu sehen, nicht beim Start in small!!
                P2GuiSize.getSize(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage);
                Platform.runLater(() -> progData.primaryStage.close()); // kann erst geschlossen werden, wenn SMAL steht!!
            }

        } else {
            // BIG anzeigen
            if (progData.smallRadioGuiController != null &&
                    ProgData.getInstance().primaryStageSmall.isShowing()) {
                P2GuiSize.getSize(ProgConfig.SMALL_RADIO_SIZE, progData.primaryStageSmall);
                progData.primaryStageSmall.hide();
            }
            progData.p2RadioController.initPanel();
            progData.primaryStage.show();
        }
        P2RadioFactory.setLastHistoryUrl();
    }
}
