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
import de.p2tools.p2radio.controller.ProgQuit;
import de.p2tools.p2radio.controller.ProgStartAfterGui;
import de.p2tools.p2radio.controller.ProgStartBeforeGui;
import de.p2tools.p2radio.controller.config.*;
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
        progData.primaryStageBig = primaryStage;
        progData.primaryStage = primaryStage;

        initP2lib();
        ProgStartBeforeGui.workBeforeGui(progData);
        initRootLayout();
        ProgStartAfterGui.workAfterGui(progData);

        P2Duration.onlyPing("Gui steht!");
        P2Duration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initP2lib() {
        P2LibInit.initLib(progData.primaryStageBig, ProgConst.PROGRAM_NAME,
                "", ProgConfig.SYSTEM_DARK_THEME, null, ProgConfig.SYSTEM_THEME_CHANGED,
                ProgConst.CSS_FILE, ProgConst.CSS_FILE_DARK_THEME, ProgConfig.SYSTEM_FONT_SIZE,
                ProgData.debug, ProgData.duration);
    }

    private void initRootLayout() {
        try {
            progData.stationInfoDialogController = new StationInfoDialogController(progData);

            // bigGui
            progData.p2RadioController = new P2RadioController();
            Scene sceneBig = new Scene(progData.p2RadioController,
                    P2GuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI),
                    P2GuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));

            progData.primaryStageBig.setScene(sceneBig);
            progData.primaryStageBig.setOnCloseRequest(e -> {
                e.consume();
                ProgQuit.quit();
            });

            P2LibInit.addP2CssToScene(progData.primaryStageBig.getScene()); // und jetzt noch CSS einstellen
            PShortKeyFactory.addShortKey(progData.primaryStageBig.getScene());

            //Pos setzen
            sceneBig.heightProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig, sceneBig));
            sceneBig.widthProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig, sceneBig));
            progData.primaryStageBig.xProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig, sceneBig));
            progData.primaryStageBig.yProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig, sceneBig));
            P2GuiSize.setOnlyPos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig);

            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> ProgColorList.setColorTheme());
            ProgConfig.SYSTEM_SMALL_RADIO.addListener((u, o, n) -> selectGui());

            selectGui();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void selectGui() {
        if (ProgConfig.SYSTEM_SMALL_RADIO.getValue()) {
            ProgData.getInstance().smallRadioGuiController = new SmallRadioGuiController();
            progData.primaryStageSmall = ProgData.getInstance().smallRadioGuiController.getStage();
            progData.primaryStage = progData.primaryStageSmall;
            P2LibInit.setActStage(progData.primaryStageSmall);
            PShortKeyFactory.addShortKey(progData.primaryStageSmall.getScene());

            progData.primaryStageBig.close();
            progData.primaryStageSmall.show();

        } else {
            progData.primaryStage = progData.primaryStageBig;
            P2LibInit.setActStage(progData.primaryStageBig);

            System.out.println("=========> " + ProgConfig.SYSTEM_SIZE_GUI.getValueSafe());
            P2GuiSize.setOnlyPos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStageBig);
            progData.primaryStageBig.show();
        }
    }
}
