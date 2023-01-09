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

package de.p2tools.p2radio.gui.startDialog;

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;


public class StartDialogController extends PDialogExtra {

    private final ProgData progData;
    private final TilePane tilePane = new TilePane();
    private final Button btnStart1 = new Button("Infos");
    private final Button btnStart2 = new Button("Infos");
    private final Button btnStart3 = new Button("Infos");
    private final Button btnConfig = new Button("Einstellungen");
    private boolean ok = false;
    private StackPane stackpane;
    private Button btnOk, btnCancel;
    private Button btnPrev, btnNext;
    private State aktState = State.START_1;

    private TitledPane tStart1;
    private TitledPane tStart2;
    private TitledPane tStart3;
    private TitledPane tConfig;

    private StartPane startPane1;
    private StartPane startPane2;
    private StartPane startPane3;
    private ConfigPane configPane;

    public StartDialogController() {
        super(null, null, "Starteinstellungen", true, false);

        this.progData = ProgData.getInstance();
        init(true);
    }

    @Override
    public void make() {
        initTopButton();
        initStack();
        initButton();
        initTooltip();
        selectActPane();
    }

    private void closeDialog(boolean ok) {
        this.ok = ok;
        startPane1.close();
        startPane2.close();
        startPane3.close();
        configPane.close();
        super.close();
    }

    public boolean isOk() {
        return ok;
    }

    private void initTopButton() {
        getvBoxCont().getChildren().add(tilePane);
        tilePane.getChildren().addAll(btnStart1, btnStart2, btnStart3, btnConfig);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setPadding(new Insets(10, 10, 20, 10));
        tilePane.setHgap(10);
        tilePane.setVgap(10);

        initButton(btnStart1, State.START_1);
        initButton(btnStart2, State.START_2);
        initButton(btnStart3, State.START_3);
        initButton(btnConfig, State.CONFIG);
    }

    private void initButton(Button btn, State state) {
        btn.getStyleClass().addAll("btnFunction", "btnFuncStartDialog");
        btn.setAlignment(Pos.CENTER);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(a -> {
            aktState = state;
            selectActPane();
        });
    }

    private void initStack() {
        stackpane = new StackPane();
        VBox.setVgrow(stackpane, Priority.ALWAYS);
        getvBoxCont().getChildren().add(stackpane);

        //startPane 1
        startPane1 = new StartPane(getStage());
        tStart1 = startPane1.makeStart1();
        tStart1.setMaxHeight(Double.MAX_VALUE);
        tStart1.setCollapsible(false);

        //startPane 2
        startPane2 = new StartPane(getStage());
        tStart2 = startPane2.makeStart2();
        tStart2.setMaxHeight(Double.MAX_VALUE);
        tStart2.setCollapsible(false);

        //startPane 3
        startPane3 = new StartPane(getStage());
        tStart3 = startPane3.makeStart3();
        tStart3.setMaxHeight(Double.MAX_VALUE);
        tStart3.setCollapsible(false);

        //updatePane
        configPane = new ConfigPane(getStage());
        tConfig = configPane.makeStart();
        tConfig.setMaxHeight(Double.MAX_VALUE);
        tConfig.setCollapsible(false);

        stackpane.getChildren().addAll(tStart1, tStart2, tStart3, tConfig);
    }

    private void initButton() {
        btnOk = new Button("_Ok");
        btnOk.setDisable(true);
        btnOk.setOnAction(a -> {
            closeDialog(true);
        });

        btnCancel = new Button("_Abbrechen");
        btnCancel.setOnAction(a -> closeDialog(false));

        btnNext = PButton.getButton(ProgIcons.Icons.ICON_BUTTON_NEXT.getImageView(), "nächste Seite");
        btnNext.setOnAction(event -> {
            switch (aktState) {
                case START_1:
                    aktState = State.START_2;
                    break;
                case START_2:
                    aktState = State.START_3;
                    break;
                case START_3:
                    aktState = State.CONFIG;
                    break;
                case CONFIG:
                    break;
            }
            selectActPane();
        });
        btnPrev = PButton.getButton(ProgIcons.Icons.ICON_BUTTON_PREV.getImageView(), "vorherige Seite");
        btnPrev.setOnAction(event -> {
            switch (aktState) {
                case START_1:
                    break;
                case START_2:
                    aktState = State.START_1;
                    break;
                case START_3:
                    aktState = State.START_2;
                    break;
                case CONFIG:
                    aktState = State.START_3;
                    break;
            }
            selectActPane();
        });

        addOkCancelButtons(btnOk, btnCancel);
        ButtonBar.setButtonData(btnPrev, ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonBar.setButtonData(btnNext, ButtonBar.ButtonData.NEXT_FORWARD);
        addAnyButton(btnNext);
        addAnyButton(btnPrev);
        getButtonBar().setButtonOrder("BX+CO");
    }

    private void selectActPane() {
        switch (aktState) {
            case START_1:
                btnPrev.setDisable(true);
                btnNext.setDisable(false);
                tStart1.toFront();
                setActButtonStyle(btnStart1);
                break;
            case START_2:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                tStart2.toFront();
                setActButtonStyle(btnStart2);
                break;
            case START_3:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                tStart3.toFront();
                setActButtonStyle(btnStart3);
                break;
            case CONFIG:
                btnPrev.setDisable(false);
                btnNext.setDisable(true);
                tConfig.toFront();
                setActButtonStyle(btnConfig);
                btnOk.setDisable(false);
                break;
            default:
                btnOk.setDisable(false);
        }
    }

    private void setActButtonStyle(Button btnSel) {
        btnStart1.getStyleClass().setAll("btnFunction", "btnFuncStartDialog");
        btnStart2.getStyleClass().setAll("btnFunction", "btnFuncStartDialog");
        btnStart3.getStyleClass().setAll("btnFunction", "btnFuncStartDialog");
        btnConfig.getStyleClass().setAll("btnFunction", "btnFuncStartDialog");
        btnSel.getStyleClass().setAll("btnFunction", "btnFuncStartDialogSel");
    }

    private void initTooltip() {
        btnStart1.setTooltip(new Tooltip("Infos über das Programm"));
        btnStart2.setTooltip(new Tooltip("Infos über das Programm"));
        btnStart3.setTooltip(new Tooltip("Infos über das Programm"));
        btnConfig.setTooltip(new Tooltip("Einstellungen des Programms?"));

        btnOk.setTooltip(new Tooltip("Programm mit den gewählten Einstellungen starten"));
        btnCancel.setTooltip(new Tooltip("Das Programm nicht einrichten\n" +
                "und starten sondern Dialog wieder beenden"));
        btnNext.setTooltip(new Tooltip("Nächste Einstellmöglichkeit"));
        btnPrev.setTooltip(new Tooltip("Vorherige Einstellmöglichkeit"));
    }

    private enum State {START_1, START_2, START_3, CONFIG}
}
