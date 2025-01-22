/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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

import de.p2tools.p2lib.tools.date.P2LDateFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InitConstValues {
    AddFavouriteDto addFavouriteDto;

    public InitConstValues(AddFavouriteDto addFavouriteDto) {
        this.addFavouriteDto = addFavouriteDto;
        addFavouriteDto.chkOwn.setDisable(true);

        addFavouriteDto.txtStationName.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.stationNameProperty().set(n));
        addFavouriteDto.txtGenre.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.genreProperty().set(n));
        addFavouriteDto.txtCodec.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.codecProperty().set(n));
        addFavouriteDto.txtBitrate.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.setBitrate(n));
        addFavouriteDto.txtCountry.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.countryProperty().set(n));
        addFavouriteDto.txtLanguage.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.languageProperty().set(n));
        addFavouriteDto.txtUrl.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.stationUrlProperty().set(n));
        addFavouriteDto.txtWebsite.textProperty().addListener((u, o, n) ->
                addFavouriteDto.getAct().stationData.websiteProperty().set(n));
    }

    public void makeAct() {
        boolean own = addFavouriteDto.getAct().stationData.isOwn();

        addFavouriteDto.lblStationNo.setText(own ? "" : addFavouriteDto.getAct().stationData.getStationNo() + "");
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textStationName, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textGenre, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textCodec, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textBitrate, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textCountry, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textLanguage, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textUrl, own);
        FavouriteAddDialogFactory.makeTextBold(addFavouriteDto.textWebsite, own);

        // stationName
        set(own, addFavouriteDto.getAct().stationData.getStationName(),
                addFavouriteDto.lblStationName, addFavouriteDto.txtStationName);
        // genre
        set(own, addFavouriteDto.getAct().stationData.getGenre(),
                addFavouriteDto.lblGenre, addFavouriteDto.txtGenre);

        // codec
        set(own, addFavouriteDto.getAct().stationData.getCodec(),
                addFavouriteDto.lblCodec, addFavouriteDto.txtCodec);

        // bitrate
        set(own, addFavouriteDto.getAct().stationData.getBitrateStr(),
                addFavouriteDto.lblBitrate, addFavouriteDto.txtBitrate);

        addFavouriteDto.lblStarts.setText(addFavouriteDto.getAct().stationData.getStarts() + "");
        addFavouriteDto.chkOwn.setSelected(addFavouriteDto.getAct().stationData.isOwn());

        // country
        set(own, addFavouriteDto.getAct().stationData.getCountry(),
                addFavouriteDto.lblCountry, addFavouriteDto.txtCountry);

        // language
        set(own, addFavouriteDto.getAct().stationData.getLanguage(),
                addFavouriteDto.lblLanguage, addFavouriteDto.txtLanguage);

        addFavouriteDto.lblGenDate.setText(P2LDateFactory.toString(addFavouriteDto.getAct().stationData.getStationDateLastChange()));

        // url
        set(own, addFavouriteDto.getAct().stationData.getStationUrl(),
                addFavouriteDto.lblUrl, addFavouriteDto.txtUrl);

        // website
        set(own, addFavouriteDto.getAct().stationData.getWebsite(),
                addFavouriteDto.lblWebsite, addFavouriteDto.txtWebsite);
    }

    private void set(boolean own, String value, Label lbl, TextField txt) {
        txt.setText(value);
        lbl.setText(value);
        txt.setVisible(own);
        lbl.setVisible(!own);
    }
}
