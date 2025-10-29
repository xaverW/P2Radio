/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.gui.favouriteadd;


import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2radio.controller.ProgQuitFactory;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class FavouriteAddDialogController extends P2DialogExtra {

    private final ProgData progData;
    private final Button btnOk = new Button("_Ok");
    private final Button btnCancel = new Button("_Abbrechen");
    private boolean ok = false;

    private final AddFavouriteDto addFavouriteDto;

    public FavouriteAddDialogController(ProgData progData, ArrayList<StationData> list, boolean addNew) {
        super(progData.primaryStage,
                list.size() > 1 ? ProgConfig.FAVOURITE_DIALOG_ADD_MORE_SIZE :
                        ProgConfig.FAVOURITE_DIALOG_ADD_SIZE,
                addNew ? "Favoriten anlegen" : "Favoriten ändern", true, true, true, DECO.BORDER_SMALL);

        // neue Podcast anlegen / ändern
        this.progData = progData;
        this.addFavouriteDto = new AddFavouriteDto(progData, list, addNew);

        init(true);
    }

    @Override
    public void make() {
        initGui();
        initButton();
        addFavouriteDto.updateAct();
    }

    private void initGui() {
        new FavouriteAddDialogGui(addFavouriteDto, getVBoxCont());
        addOkCancelButtons(btnOk, btnCancel);
    }

    private void initButton() {
        addFavouriteDto.btnPrev.setOnAction(event -> {
            addFavouriteDto.actIsShown.setValue(addFavouriteDto.actIsShown.getValue() - 1);
            addFavouriteDto.updateAct();
        });
        addFavouriteDto.btnNext.setOnAction(event -> {
            addFavouriteDto.actIsShown.setValue(addFavouriteDto.actIsShown.getValue() + 1);
            addFavouriteDto.updateAct();
        });
        addFavouriteDto.btnSetOwn.setOnAction(event -> {
            addFavouriteDto.getAct().addNewFavourite = true;
            addFavouriteDto.getAct().stationData.setOwn();
            addFavouriteDto.getAct().stationData.setStationNo(progData.stationList.getNextNo());
            addFavouriteDto.updateAct();
        });
        btnOk.setOnAction(event -> {
            if (check()) {
                quit();
            }
        });
        btnCancel.setOnAction(event -> {
            ok = false;
            quit();
        });
    }

    private boolean check() {
        ok = false;
        for (AddFavouriteData d : addFavouriteDto.addFavouriteData) {
            if (d.stationData == null) {
                P2Alert.showErrorAlert("Fehlerhafter Favorite!", "Fehlerhafter Favorite!",
                        "Favorite konnte nicht erstellt werden.");

            } else {
                ok = true;
            }
        }
        return ok;
    }

    private void quit() {
        if (!ok) {
            close();
            return;
        }

        boolean changeBlack = false;
        for (AddFavouriteData addFavouriteData : addFavouriteDto.addFavouriteData) {
            if (addFavouriteData.addNewFavourite) {
                // dann ein neuer
                final StationData stationData = addFavouriteData.stationData;
                progData.favouriteList.addAll(stationData);
                if (stationData.isOwn()) {
                    // nur eigene müsen eingefügt werden, damit sie sofort verfügbar sind
                    progData.stationList.addAll(stationData);
                    changeBlack = true;
                }
            } else {
                // Änderung
                addFavouriteData.stationDataOrg.copyToMe(addFavouriteData.stationData);
            }
        }

        if (changeBlack) {
            progData.storedFilters.getActFilterSettings().reportBlacklistChange();
        }

//        if (addFavouriteDto.addNewFavourite) {
//            // dann neue anlegen
//            addNewFavourite();
//        } else {
//            // oder die bestehenden ändern
//            changeFavourite();
//        }

        ProgQuitFactory.saveProgConfig();
        close();
    }

//    private void addNewFavourite() {
//        List<StationData> list = new ArrayList<>();
//        for (AddFavouriteData addFavouriteData : addFavouriteDto.addFavouriteData) {
//            final StationData stationData = addFavouriteData.stationData;
//            list.add(stationData);
//        }
//        progData.favouriteList.addAll(list);
//        progData.stationList.addAll(list); // damit sie gleich verfügbar sind
//    }
//
//    private void changeFavourite() {
//        for (AddFavouriteData addFavouriteData : addFavouriteDto.addFavouriteData) {
//            addFavouriteData.stationDataOrg.copyToMe(addFavouriteData.stationData);
//        }
//    }
}
