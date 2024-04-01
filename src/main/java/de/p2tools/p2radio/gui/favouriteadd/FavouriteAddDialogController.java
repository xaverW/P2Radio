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


import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

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
                addNew ? "Favoriten anlegen" : "Favoriten ändern", true, false, DECO.BORDER_SMALL);

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
        FavouriteAddDialogGui favouriteAddDialogGui = new FavouriteAddDialogGui(addFavouriteDto, getVBoxCont());
        favouriteAddDialogGui.addCont();
        favouriteAddDialogGui.init();
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
                PAlert.showErrorAlert("Fehlerhafter Favorite!", "Fehlerhafter Favorite!",
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

        if (addFavouriteDto.addNewDownloads) {
            // dann neue Downloads anlegen
            addNewFavourite();
        } else {
            // oder die bestehenden ändern
            changeFavourite();
        }

        close();
    }

    private void addNewFavourite() {
        List<StationData> list = new ArrayList<>();
        for (AddFavouriteData addFavouriteData : addFavouriteDto.addFavouriteData) {
            final StationData stationData = addFavouriteData.stationData;
            list.add(stationData);
        }
        progData.favouriteList.addAll(list);
    }

    private void changeFavourite() {
        for (AddFavouriteData addFavouriteData : addFavouriteDto.addFavouriteData) {
            addFavouriteData.stationDataOrg.copyToMe(addFavouriteData.stationData);
        }
    }
}
