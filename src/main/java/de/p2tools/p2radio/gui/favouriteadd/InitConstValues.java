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

public class InitConstValues {
    AddFavouriteDto addFavouriteDto;

    public InitConstValues(AddFavouriteDto addFavouriteDto) {
        this.addFavouriteDto = addFavouriteDto;
        addFavouriteDto.chkOwn.setDisable(true);
    }

    public void makeAct() {
        addFavouriteDto.lblStationNo.setText(addFavouriteDto.getAct().stationData.getStationNo() + "");
        addFavouriteDto.lblStationName.setText(addFavouriteDto.getAct().stationData.getStationName());
        addFavouriteDto.lblGenre.setText(addFavouriteDto.getAct().stationData.getGenre());
        addFavouriteDto.lblCodec.setText(addFavouriteDto.getAct().stationData.getCodec());
        addFavouriteDto.lblBitrate.setText(addFavouriteDto.getAct().stationData.getBitrateStr());
        addFavouriteDto.lblClicks.setText(addFavouriteDto.getAct().stationData.getClickCount() + "");
        addFavouriteDto.chkOwn.setSelected(addFavouriteDto.getAct().stationData.isOwn());
        addFavouriteDto.lblCountry.setText(addFavouriteDto.getAct().stationData.getCountry());
        addFavouriteDto.lblLanguage.setText(addFavouriteDto.getAct().stationData.getLanguage());
        addFavouriteDto.lblGenDate.setText(P2LDateFactory.toString(addFavouriteDto.getAct().stationData.getStationDateLastChange()));
        addFavouriteDto.lblUrl.setText(addFavouriteDto.getAct().stationData.getStationUrl());
        addFavouriteDto.lblWebsite.setText(addFavouriteDto.getAct().stationData.getWebsite());
    }
}
