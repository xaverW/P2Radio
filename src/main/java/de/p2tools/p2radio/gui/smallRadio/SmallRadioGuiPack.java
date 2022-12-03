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

package de.p2tools.p2radio.gui.smallRadio;

import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SmallRadioGuiPack extends SmallRadioDialog {

    private final ProgData progData;
    private final FilteredList<Favourite> filteredFavourites;

    public SmallRadioGuiPack(ProgData progData) {
        super();
        this.progData = progData;
        filteredFavourites = new FilteredList<>(progData.favouriteList, p -> true);
        progData.smallRadioGuiController = new SmallRadioGuiController(this);
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(true);
    }

    @Override
    public void make() {
        VBox.setVgrow(super.getVBoxCompleteDialog(), Priority.ALWAYS);
        getStage().getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                changeGui();
            }
        });
        getStage().setOnCloseRequest(e -> {
            e.consume();
            close();
        });
    }

    public FilteredList<Favourite> getFiltertFavourite() {
        return filteredFavourites;
    }

    public void changeGui() {
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(false);
        getSize();
        close();
        progData.smallRadioGuiController = null;

        Platform.runLater(() -> {
                    PGuiSize.setPos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage);
                    progData.primaryStage.setWidth(PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.setHeight(PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.show();
                }
        );
    }

    public PMaskerPane getMaskerPane() {
        return super.getMaskerPane();
    }

    protected void getSize() {
        progData.smallRadioGuiController.saveTable();
        PGuiSize.getSizeStage(ProgConfig.SMALL_RADIO_SIZE, getStage());
    }
}
