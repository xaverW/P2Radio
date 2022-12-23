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

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.dialog.PDialogOnly;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationListFactory;
import de.p2tools.p2radio.gui.FavouriteGuiInfoController;
import de.p2tools.p2radio.gui.dialog.FavouriteEditDialogController;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Optional;

public class SmallRadioGuiController extends PDialogOnly {

    final SmallRadioGuiCenter smallRadioGuiCenter;
    final SmallRadioGuiBottom smallRadioGuiBottom;
    private final ProgData progData;
    private final FavouriteGuiInfoController favouriteGuiInfoController;
    private final PListener listener = new PListener(Events.REFRESH_TABLE) {
        public void pingGui(PEvent event) {
            tableRefresh();
        }
    };


    public SmallRadioGuiController() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SMALL_RADIO_SIZE,
                "Radio", false, false, true);

        progData = ProgData.getInstance();
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(true);

        progData.smallRadioGuiController = this;

        smallRadioGuiCenter = new SmallRadioGuiCenter(this);
        smallRadioGuiBottom = new SmallRadioGuiBottom(this);
        favouriteGuiInfoController = new FavouriteGuiInfoController();

        init(true);
    }

    @Override
    public void make() {
        SmallRadioFactory.addBorderListener(getStage());
        getStage().initStyle(StageStyle.TRANSPARENT);
        getVBoxCompleteDialog().getStyleClass().add("smallRadio");

        getVBoxCompleteDialog().getChildren().addAll(smallRadioGuiCenter, smallRadioGuiBottom);
        VBox.setVgrow(smallRadioGuiCenter, Priority.ALWAYS);

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
        progData.pEventHandler.addListener(listener);
    }

    @Override
    public void close() {
        saveMe();
        ProgData.getInstance().pEventHandler.removeListener(listener);
        super.close();
    }

    public PMaskerPane getMaskerPane() {
        return super.getMaskerPane();
    }

    private void saveMe() {
        progData.smallRadioGuiController.saveTable();
        PGuiSize.getSizeStage(ProgConfig.SMALL_RADIO_SIZE, getStage());
    }

    public void changeGui() {
        close();
        ProgConfig.SYSTEM_SMALL_RADIO.setValue(false);
        progData.smallRadioGuiController = null;

        Platform.runLater(() -> {
                    PGuiSize.setPos(ProgConfig.SYSTEM_SIZE_GUI, progData.primaryStage);
                    progData.primaryStage.setWidth(PGuiSize.getWidth(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.setHeight(PGuiSize.getHeight(ProgConfig.SYSTEM_SIZE_GUI));
                    progData.primaryStage.show();
                }
        );
    }

    public void tableRefresh() {
        smallRadioGuiCenter.tableRefresh();
    }

    public void isShown() {
        smallRadioGuiCenter.isShown();
        setSelectedFavourite();
    }

    public void copyUrl() {
        final Optional<StationData> favourite = getSel();
        if (!favourite.isPresent()) {
            return;
        }
        PSystemUtils.copyToClipboard(favourite.get().getStationUrl());
    }

    private void setSelectedFavourite() {
        StationData favourite = smallRadioGuiCenter.getSel().get();
        if (favourite != null) {
            favouriteGuiInfoController.setFavourite(favourite);
            StationData station = progData.stationList.getSenderByUrl(favourite.getStationUrl());
            progData.stationInfoDialogController.setStation(station);
        } else {
            favouriteGuiInfoController.setFavourite(null);
        }
    }

    public void playStation() {
        // bezieht sich auf den ausgewählten Favoriten
        final Optional<StationData> favourite = getSel();
        if (favourite.isPresent()) {
            progData.startFactory.playPlayable(favourite.get());
        }
    }

    public void stopStation(boolean all) {
        // bezieht sich auf "alle" oder nur die markierten Sender
        if (all) {
            progData.favouriteList.stream().forEach(f -> progData.startFactory.stopPlayable(f));

        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                progData.startFactory.stopPlayable(favourite.get());
            }
        }
    }

    public void deleteFavourite(boolean all) {
        if (all) {
            final ArrayList<StationData> list = getSelList();
            if (list.isEmpty()) {
                return;
            }

            final String text;
            if (list.size() == 1) {
                text = "Soll der Favorit gelöscht werden?";
            } else {
                text = "Sollen die Favoriten gelöscht werden?";
            }
            if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?",
                    "Favoriten löschen?", text).equals(PAlert.BUTTON.YES)) {
                progData.favouriteList.removeAll(list);
                StationListFactory.findAndMarkFavouriteStations(progData);
            }

        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                deleteFavourite(favourite.get());
            }
        }
    }

    public void deleteFavourite(StationData favourite) {
        if (PAlert.showAlert_yes_no(ProgData.getInstance().primaryStage, "Favoriten löschen?",
                "Favoriten löschen?",
                "Soll der Favorite gelöscht werden?").equals(PAlert.BUTTON.YES)) {
            progData.favouriteList.remove(favourite);
            StationListFactory.findAndMarkFavouriteStations(progData);
        }
    }

    public void changeFavourite(boolean allSel) {
        ArrayList<StationData> list = new ArrayList<>();
        ArrayList<StationData> listCopy = new ArrayList<>();
        if (allSel) {
            list.addAll(getSelList());
        } else {
            final Optional<StationData> favourite = getSel();
            if (favourite.isPresent()) {
                list.add(favourite.get());
            }
        }

        if (list.isEmpty()) {
            return;
        }
        list.stream().forEach(f -> {
            StationData favouriteCopy = f.getCopy();
            listCopy.add(favouriteCopy);
        });

        FavouriteEditDialogController favouriteEditDialogController =
                new FavouriteEditDialogController(progData, listCopy);

        if (favouriteEditDialogController.isOk()) {
            for (int i = 0; i < listCopy.size(); ++i) {
                final StationData f, fCopy;
                f = list.get(i);
                fCopy = listCopy.get(i);
                f.copyToMe(fCopy);
            }
            progData.collectionList.updateNames();//könnte ja geändert sein
        }
    }

    public void saveTable() {
        smallRadioGuiCenter.saveTable();
    }

    public ArrayList<StationData> getSelList() {
        return smallRadioGuiCenter.getSelList();
    }

    public Optional<StationData> getSel() {
        return getSel(true);
    }

    public Optional<StationData> getSel(boolean show) {
        return smallRadioGuiCenter.getSel();
    }

    public void playRandomStation() {
        smallRadioGuiCenter.playRandomStation();
    }
}
