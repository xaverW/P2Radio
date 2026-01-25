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

package de.p2tools.p2radio.gui.startdialog;

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2radio.controller.picon.PIconFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;


public class StartDialogController extends P2DialogExtra {

    private final TilePane tilePane = new TilePane();
    private final Button btnStart1 = new Button("Infos");
    private final Button btnStart2 = new Button("Infos");
    private final Button btnStart3 = new Button("Infos");
    private final Button btnColor = new Button("Farbe");
    private final Button btnConfig = new Button("Einstellungen");

    private boolean ok = false;
    private Button btnOk, btnCancel;
    private Button btnPrev, btnNext;
    private State aktState = State.START_1;
    private final VBox vBoxCont = new VBox();

    private StartPane startPane1;
    private StartPane startPane2;
    private StartPane startPane3;
    private StartPaneColorMode startPaneColorMode;
    private ConfigPane configPane;

    private enum State {START_1, START_2, START_3, COLOR, CONFIG}

    public StartDialogController() {
        super(null, null, "Starteinstellungen");
        init(true);
    }

    @Override
    public void make() {
        initTop();
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
        startPaneColorMode.close();
        configPane.close();
        super.close();
    }

    public boolean isOk() {
        return ok;
    }

    private void initTop() {
        tilePane.setAlignment(Pos.CENTER);
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.getChildren().addAll(btnStart1, btnStart2, btnStart3, btnColor, btnConfig);

        initTopButton(btnStart1, State.START_1);
        initTopButton(btnStart2, State.START_2);
        initTopButton(btnStart3, State.START_3);
        initTopButton(btnColor, State.COLOR);
        initTopButton(btnConfig, State.CONFIG);

        VBox.setVgrow(vBoxCont, Priority.ALWAYS);
        getVBoxCont().setPadding(new Insets(5));
        getVBoxCont().getChildren().addAll(tilePane, P2GuiTools.getHDistance(5), vBoxCont);
    }

    private void initTopButton(Button btn, State state) {
        btn.getStyleClass().addAll("btnStartDialog");
        btn.setAlignment(Pos.CENTER);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(a -> {
            aktState = state;
            selectActPane();
        });
    }

    private void initStack() {
        //startPane 1
        startPane1 = new StartPane();
        startPane1.makeStart1();

        //startPane 2
        startPane2 = new StartPane();
        startPane2.makeStart2();

        //startPane 3
        startPane3 = new StartPane();
        startPane3.makeStart3();

        //color
        startPaneColorMode = new StartPaneColorMode(getStage());
        startPaneColorMode.make();

        //updatePane
        configPane = new ConfigPane(getStage());
        configPane.make();
    }

    private void initButton() {
        btnOk = new Button("_Ok");
        btnOk.setDisable(true);
        btnOk.setOnAction(a -> {
            closeDialog(true);
        });

        btnCancel = new Button("_Abbrechen");
        btnCancel.setOnAction(a -> closeDialog(false));

        btnNext = P2Button.getButton(PIconFactory.PICON.BTN_NEXT.getFontIcon(), "nächste Seite");
        btnNext.setOnAction(event -> {
            switch (aktState) {
                case START_1:
                    aktState = State.START_2;
                    break;
                case START_2:
                    aktState = State.START_3;
                    break;
                case START_3:
                    aktState = State.COLOR;
                    break;
                case COLOR:
                    aktState = State.CONFIG;
                    break;
                case CONFIG:
                    break;
            }
            selectActPane();
        });
        btnPrev = P2Button.getButton(PIconFactory.PICON.BTN_PREV.getFontIcon(), "vorherige Seite");
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
                case COLOR:
                    aktState = State.START_3;
                    break;
                case CONFIG:
                    aktState = State.COLOR;
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
                vBoxCont.getChildren().clear();
                vBoxCont.getChildren().add(startPane1);
                setActButtonStyle(btnStart1);
                break;
            case START_2:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                vBoxCont.getChildren().clear();
                vBoxCont.getChildren().add(startPane2);
                setActButtonStyle(btnStart2);
                break;
            case START_3:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                vBoxCont.getChildren().clear();
                vBoxCont.getChildren().add(startPane3);
                setActButtonStyle(btnStart3);
                break;
            case COLOR:
                btnPrev.setDisable(false);
                btnNext.setDisable(false);
                vBoxCont.getChildren().clear();
                vBoxCont.getChildren().add(startPaneColorMode);
                setActButtonStyle(btnColor);
                break;
            case CONFIG:
                btnPrev.setDisable(false);
                btnNext.setDisable(true);
                btnOk.setDisable(false);
                vBoxCont.getChildren().clear();
                vBoxCont.getChildren().add(configPane);
                setActButtonStyle(btnConfig);
                break;
            default:
                btnOk.setDisable(false);
        }
    }

    private void setActButtonStyle(Button btnSel) {
        btnStart1.getStyleClass().setAll("btnStartDialog");
        btnStart2.getStyleClass().setAll("btnStartDialog");
        btnStart3.getStyleClass().setAll("btnStartDialog");
        btnColor.getStyleClass().setAll("btnStartDialog");
        btnConfig.getStyleClass().setAll("btnStartDialog");
        btnSel.getStyleClass().setAll("btnStartDialog", "btnStartDialogSel");
    }

    private void initTooltip() {
        btnStart1.setTooltip(new Tooltip("Infos über das Programm"));
        btnStart2.setTooltip(new Tooltip("Infos über das Programm"));
        btnStart3.setTooltip(new Tooltip("Infos über das Programm"));
        btnColor.setTooltip(new Tooltip("Wie soll die Programmoberfläche aussehen?"));
        btnConfig.setTooltip(new Tooltip("Einstellungen des Programms?"));

        btnOk.setTooltip(new Tooltip("Programm mit den gewählten Einstellungen starten"));
        btnCancel.setTooltip(new Tooltip("Das Programm nicht einrichten\n" +
                "und starten sondern Dialog wieder beenden"));
        btnNext.setTooltip(new Tooltip("Nächste Einstellmöglichkeit"));
        btnPrev.setTooltip(new Tooltip("Vorherige Einstellmöglichkeit"));
    }

}
