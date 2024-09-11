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
import de.p2tools.p2lib.tools.IoReadWriteStyle;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2radio.controller.ProgQuit;
import de.p2tools.p2radio.controller.ProgStartAfterGui;
import de.p2tools.p2radio.controller.ProgStartBeforeGui;
import de.p2tools.p2radio.controller.config.*;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import de.p2tools.p2radio.gui.dialog.StationInfoDialogController;
import de.p2tools.p2radio.gui.smallradio.SmallRadioGuiController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class P2Radio extends Application {

    private static final String LOG_TEXT_PROGRAM_START = "Dauer Programmstart";
    protected ProgData progData;
    Scene scene = null;
    int i = 0;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        P2Duration.counterStart(LOG_TEXT_PROGRAM_START);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        initP2lib();
        ProgStartBeforeGui.workBeforeGui(progData);
        initRootLayout();
        ProgStartAfterGui.workAfterGui(progData);

        ProgConfig.CONFIG_DIALOG_SET_DIVIDER.addListener((u, o, n) -> System.out.println("=============>>" + i++));

        P2Duration.onlyPing("Gui steht!");
        P2Duration.counterStop(LOG_TEXT_PROGRAM_START);
    }

    private void initP2lib() {
        ProgIconsP2Radio.initIcons();
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME,
                "", ProgConfig.SYSTEM_DARK_THEME, null,
                ProgData.debug, ProgData.duration);
        //css-files in die Liste aufnehmen
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private void initRootLayout() {
        try {
            addThemeCss(); // damit es für die 2 schon mal stimmt
            progData.stationInfoDialogController = new StationInfoDialogController(progData);
            progData.p2RadioController = new P2RadioController();

            scene = new Scene(progData.p2RadioController,
                    P2GuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI),
                    P2GuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));

            ProgColorList.setColorTheme();
            addThemeCss(); //und jetzt noch für die neue Scene

            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                ProgColorList.setColorTheme();
                addThemeCss();
                ProgConfig.SYSTEM_THEME_CHANGED.setValue(!ProgConfig.SYSTEM_THEME_CHANGED.get());
            });

            if (ProgConfig.SYSTEM_STYLE.get()) {
                P2LibInit.setStyleFile(ProgInfos.getStyleFile().toString());
                IoReadWriteStyle.readStyle(ProgInfos.getStyleFile(), scene);
            }

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuit.quit(primaryStage, true);
            });
            //Pos setzen
            P2GuiSize.setOnlyPos(ProgConfig.SYSTEM_SIZE_GUI, primaryStage);

            scene.heightProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            scene.widthProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            primaryStage.xProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));
            primaryStage.yProperty().addListener((v, o, n) -> P2GuiSize.getSizeScene(ProgConfig.SYSTEM_SIZE_GUI, primaryStage, scene));

            if (ProgConfig.SYSTEM_SMALL_RADIO.getValue()) {
                //dann gleich mit smallRadio starten
                Platform.runLater(() -> new SmallRadioGuiController());
            } else {
                primaryStage.show();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
        P2LibInit.addP2CssToScene(scene);
    }
}
