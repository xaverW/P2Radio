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

import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.ProgStartAfterGui;
import de.p2tools.p2radio.controller.ProgStartBeforeGui;
import de.p2tools.p2radio.controller.config.PShortCutFactory;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.dialog.StationInfoDialogController;
import de.p2tools.p2radio.gui.smallradio.SmallRadioGuiController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class P2Radio extends Application {

    private static final String LOG_TEXT_PROGRAM_START = "Dauer Programmstart";
    private ProgData progData;

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
        progData.primaryStageBig = primaryStage;

        ProgStartBeforeGui.workBeforeGui(progData);
        initRootLayout();
        ProgStartAfterGui.workAfterGui(progData);

        P2Duration.onlyPing("Gui steht!");
        P2Duration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initRootLayout() {
        try {
            progData.stationInfoDialogController = new StationInfoDialogController(progData);

            // bigGui
            progData.p2RadioController = new P2RadioController();
            Scene sceneBig = new Scene(progData.p2RadioController,
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_SIZE_GUI, true),
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_SIZE_GUI, false));
            progData.primaryStageBig.setScene(sceneBig);

            progData.primaryStageBig.setOnCloseRequest(e -> {
                e.consume();
                ProgQuitFactory.quit();
            });

            progData.primaryStageBig.setOnShowing(e -> P2GuiSize.setSizePos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig));
            progData.primaryStageBig.setOnShown(e -> P2GuiSize.setSizePos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig));
            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> ProgColorList.setColorTheme());
            ProgConfig.SYSTEM_SMALL_RADIO.addListener((u, o, n) -> selectGui());

            PShortCutFactory.addShortCut(progData.primaryStageBig.getScene());
            P2LibInit.addP2CssToScene(progData.primaryStageBig.getScene()); // und jetzt noch CSS einstellen

            selectGui();
            progData.primaryStage.setIconified(ProgData.startMinimized);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void selectGui() {
        if (ProgConfig.SYSTEM_SMALL_RADIO.getValue()) {
            progData.smallRadioGuiController = new SmallRadioGuiController();
            progData.primaryStageSmall = progData.smallRadioGuiController.getStage();
            progData.primaryStage = progData.primaryStageSmall;
            P2LibInit.setActStage(progData.primaryStageSmall);
            PShortCutFactory.addShortCut(progData.primaryStageSmall.getScene());

            ProgData.STATION_TAB_ON.setValue(Boolean.FALSE);
            ProgData.FAVOURITE_TAB_ON.setValue(Boolean.FALSE);
            ProgData.HISTORY_TAB_ON.setValue(Boolean.FALSE);

            if (ProgData.getInstance().primaryStageBig.isShowing()) {
                // nur wenn zu sehen, nicht beim Start in small!!
                P2GuiSize.getSize(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig);
            }
            progData.primaryStageBig.close();
            progData.primaryStageSmall.show();

        } else {
            progData.primaryStage = progData.primaryStageBig;
            P2LibInit.setActStage(progData.primaryStageBig);
            progData.p2RadioController.initPanel();
            progData.primaryStageBig.show();
        }

        P2RadioFactory.setLastHistoryUrl();
    }
}
