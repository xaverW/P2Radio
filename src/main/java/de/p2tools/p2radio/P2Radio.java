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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.configFile.IoReadWriteStyle;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.ProgLoadFactory;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.ProgStartFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;
import de.p2tools.p2radio.gui.dialog.StationInfoDialogController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class P2Radio extends Application {

    private Stage primaryStage;
    private static final String LOG_TEXT_PROGRAM_START = "Dauer Programmstart";
    protected ProgData progData;
    Scene scene = null;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        boolean firstProgramStart;
        PDuration.counterStart(LOG_TEXT_PROGRAM_START);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        initP2lib();
        firstProgramStart = ProgStartFactory.workBeforeGui(progData);
        initRootLayout();
        ProgStartFactory.workAfterGui(progData, firstProgramStart);
        ProgLoadFactory.loadStationProgStart(firstProgramStart);

        PDuration.onlyPing("Gui steht!");
        PDuration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initP2lib() {
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME,
                "", ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(P2LibConst.CSS_GUI);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private void initRootLayout() {
        try {
            addThemeCss(); // damit es für die 2 schon mal stimmt
            progData.stationInfoDialogController = new StationInfoDialogController();
            progData.p2RadioController = new P2RadioController();

            scene = new Scene(progData.p2RadioController,
                    PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI),
                    PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
            addThemeCss(); //und jetzt noch für die neue Scene

            if (ProgConfig.SYSTEM_STYLE.get()) {
                P2LibInit.setStyleFile(ProgInfos.getStyleFile().toString());
                IoReadWriteStyle.readStyle(ProgInfos.getStyleFile(), scene);
            }

            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                addThemeCss();
                ProgConfig.SYSTEM_THEME_CHANGED.setValue(!ProgConfig.SYSTEM_THEME_CHANGED.get());
            });

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuitFactory.quit(true);
            });

            if (!PGuiSize.setPos(ProgConfig.SYSTEM_SIZE_GUI, primaryStage)) {
                primaryStage.centerOnScreen();
            }
            primaryStage.show();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(P2LibConst.CSS_GUI_DARK);
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(P2LibConst.CSS_GUI_DARK);
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
        P2LibInit.addP2LibCssToScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
