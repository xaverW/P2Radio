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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FavouriteAddDialogGui {

    private final AddFavouriteDto addFavouriteDto;
    private final VBox vBoxCont;
    private final HBox hBoxTop = new HBox();
    private final GridPane gridPane = new GridPane();

    public FavouriteAddDialogGui(AddFavouriteDto addFavouriteDto, VBox vBoxCont) {
        this.addFavouriteDto = addFavouriteDto;
        this.vBoxCont = vBoxCont;
    }

    public void addCont() {
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(5));
        VBox.setVgrow(gridPane, Priority.ALWAYS);
        int row = 0;

        if (addFavouriteDto.addFavouriteData.length > 1) {
            // Top, nur wenn es mehre gibt
            hBoxTop.getStyleClass().add("downloadDialog");
            hBoxTop.setSpacing(20);
            hBoxTop.setAlignment(Pos.CENTER);
            hBoxTop.setPadding(new Insets(5));
            hBoxTop.getChildren().addAll(addFavouriteDto.btnPrev, addFavouriteDto.lblSum, addFavouriteDto.btnNext);
            vBoxCont.setPadding(new Insets(10));
            vBoxCont.getChildren().add(hBoxTop);

            Font font = Font.font(null, FontWeight.BOLD, -1);
            addFavouriteDto.btnAll.setFont(font);
            addFavouriteDto.btnAll.setWrapText(true);
            addFavouriteDto.btnAll.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.add(addFavouriteDto.btnAll, 4, row, 1, 2);
            GridPane.setValignment(addFavouriteDto.btnAll, VPos.TOP);
            ++row;
        }

        // Nr
        gridPane.add(FavouriteAddDialogFactory.makeTextBold(StationDataXml.STATION_PROP_STATION_NO + ":"), 0, row);
        gridPane.add(addFavouriteDto.lblStationNo, 1, row, 3, 1);

        // Name
        gridPane.add(addFavouriteDto.textStationName, 0, ++row);
        gridPane.add(addFavouriteDto.lblStationName, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.txtStationName, 1, row, 3, 1);

        // Sammlung
        addFavouriteDto.cboCollection.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(FavouriteAddDialogFactory.getTextBold(StationDataXml.STATION_PROP_COLLECTION + ":"), 0, ++row);
        gridPane.add(addFavouriteDto.cboCollection, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.chkCollectionAll, 4, row);

        // eigene Bewertung
        HBox hBox = new HBox(P2LibConst.PADDING_HBOX);
        hBox.getChildren().addAll(addFavouriteDto.chkGrade1, addFavouriteDto.chkGrade2, addFavouriteDto.chkGrade3);
        gridPane.add(FavouriteAddDialogFactory.getTextBold(StationDataXml.STATION_PROP_OWN_GRADE + ":"), 0, ++row);
        gridPane.add(hBox, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.chkGradeAll, 4, row);

        // Genre
        gridPane.add(addFavouriteDto.textGenre, 0, ++row);
        gridPane.add(addFavouriteDto.lblGenre, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.txtGenre, 1, row, 3, 1);

        // Codec
        gridPane.add(addFavouriteDto.textCodec, 0, ++row);
        gridPane.add(addFavouriteDto.lblCodec, 1, row);
        gridPane.add(addFavouriteDto.txtCodec, 1, row);
        // Bitrate
        gridPane.add(addFavouriteDto.textBitrate, 2, row);
        gridPane.add(addFavouriteDto.lblBitrate, 3, row);
        gridPane.add(addFavouriteDto.txtBitrate, 3, row);

        // Clicks
        gridPane.add(FavouriteAddDialogFactory.makeTextBold(StationDataXml.STATION_PROP_STARTS + ":"), 0, ++row);
        gridPane.add(addFavouriteDto.lblStarts, 1, row);
        // Eigener
        gridPane.add(FavouriteAddDialogFactory.makeTextBold(StationDataXml.STATION_PROP_OWN + ":"), 2, row);
        gridPane.add(addFavouriteDto.chkOwn, 3, row);

        // Land
        gridPane.add(addFavouriteDto.textCountry, 0, ++row);
        gridPane.add(addFavouriteDto.lblCountry, 1, row);
        gridPane.add(addFavouriteDto.txtCountry, 1, row);
        // Sprache
        gridPane.add(addFavouriteDto.textLanguage, 2, row);
        gridPane.add(addFavouriteDto.lblLanguage, 3, row);
        gridPane.add(addFavouriteDto.txtLanguage, 3, row);

        // Beschreibung
        gridPane.add(FavouriteAddDialogFactory.getTextBold(StationDataXml.STATION_PROP_DESCRIPTION + ":"), 0, ++row);
        gridPane.add(addFavouriteDto.taDescription, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.chkDescriptionAll, 4, row);

        // GenDate
        gridPane.add(FavouriteAddDialogFactory.makeTextBold(StationDataXml.STATION_PROP_DATE + ":"), 0, ++row);
        gridPane.add(addFavouriteDto.lblGenDate, 1, row, 3, 1);

        // URL
        gridPane.add(addFavouriteDto.textUrl, 0, ++row);
        gridPane.add(addFavouriteDto.lblUrl, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.txtUrl, 1, row, 3, 1);

        // Website
        gridPane.add(addFavouriteDto.textWebsite, 0, ++row);
        gridPane.add(addFavouriteDto.lblWebsite, 1, row, 3, 1);
        gridPane.add(addFavouriteDto.txtWebsite, 1, row, 3, 1);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(),
                P2ColumnConstraints.getCcPrefSizeCenter());

        vBoxCont.getChildren().add(gridPane);
    }

    public void init() {
        if (addFavouriteDto.addFavouriteData.length == 1) {
            // wenns nur einen Download gibt, macht dann keinen Sinn
            hBoxTop.setVisible(false);
            hBoxTop.setManaged(false);
        }
        FavouriteAddAllFactory.init(addFavouriteDto);
    }
}
