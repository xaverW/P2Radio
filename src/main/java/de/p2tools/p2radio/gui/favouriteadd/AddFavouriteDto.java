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

import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationDataXml;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class AddFavouriteDto {

    public boolean addNewFavourite = true;

    public InitConstValues initConstValues;
    public InitCollection initCollection;
    public InitGrade initGrade;
    public InitDescription initDescription;

    public ProgData progData;
    public AddFavouriteData[] addFavouriteData;

    public IntegerProperty actIsShown = new SimpleIntegerProperty(0);

    public final Button btnPrev = new Button("<");
    public final Button btnNext = new Button(">");
    public final Label lblSum = new Label("");
    public Button btnAll = new Button("Für alle\nändern");

    public CheckBox chkCollectionAll = new CheckBox();
    public CheckBox chkGradeAll = new CheckBox();
    public CheckBox chkDescriptionAll = new CheckBox();

    // Sender
    public final Label lblStationNo = new Label();
    public final Label lblStationName = new Label();
    public final TextField txtStationName = new TextField();
    public final Text textStationName = new Text(StationDataXml.STATION_PROP_STATION_NAME + ":");

    // Sammlung
    public final ComboBox<String> cboCollection = new ComboBox<>();

    // Bewertung
    public final CheckBox chkGrade1 = new CheckBox();
    public final CheckBox chkGrade2 = new CheckBox();
    public final CheckBox chkGrade3 = new CheckBox();

    // Genre
    public final Label lblGenre = new Label();
    public final TextField txtGenre = new TextField();
    public final Text textGenre = new Text(StationDataXml.STATION_PROP_GENRE + ":");
    // Codec
    public final Label lblCodec = new Label();
    public final TextField txtCodec = new TextField();
    public final Text textCodec = new Text(StationDataXml.STATION_PROP_CODEC + ":");
    // Bitrate
    public final Label lblBitrate = new Label();
    public final TextField txtBitrate = new TextField();
    public final Text textBitrate = new Text(StationDataXml.STATION_PROP_BITRATE + ":");
    // Klicks
    public final Label lblStarts = new Label();
    // Eigener
    public final CheckBox chkOwn = new CheckBox();
    // Land
    public final Label lblCountry = new Label();
    public final TextField txtCountry = new TextField();
    public final Text textCountry = new Text(StationDataXml.STATION_PROP_COUNTRY + ":");
    // Sprache
    public final Label lblLanguage = new Label();
    public final TextField txtLanguage = new TextField();
    public final Text textLanguage = new Text(StationDataXml.STATION_PROP_LANGUAGE + ":");
    // Beschreibung
    public final TextArea taDescription = new TextArea();
    // GenDate
    public final Label lblGenDate = new Label();
    // URL
    public final Label lblUrl = new Label();
    public final TextField txtUrl = new TextField();
    public final Text textUrl = new Text(StationDataXml.STATION_PROP_URL + ":");
    // URL Website
    public final Label lblWebsite = new Label();
    public final TextField txtWebsite = new TextField();
    public final Text textWebsite = new Text(StationDataXml.STATION_PROP_WEBSITE + ":");
    // Set Own
    public final Button btnSetOwn = new Button("Als eigenen Sender speichern");


    public AddFavouriteDto(ProgData progData, ArrayList<StationData> data, boolean addNew) {
        // einen neuen anlegen
        this.progData = progData;

        if (addNew) {
            addFavouriteData = InitAddArray.initInfoArrayNewFavourite(data);
        } else {
            this.addNewFavourite = false;
            addFavouriteData = InitAddArray.initInfoArrayFavourite(data);
        }

        initConstValues = new InitConstValues(this);
        initCollection = new InitCollection(this);
        initGrade = new InitGrade(this);
        initDescription = new InitDescription(this);
    }

    public AddFavouriteData getAct() {
        return addFavouriteData[actIsShown.getValue()];
    }

    public void updateAct() {
        final int nr = actIsShown.getValue() + 1;
        lblSum.setText("Favorite " + nr + " von " + addFavouriteData.length + " Favoriten");

        if (actIsShown.getValue() == 0) {
            btnPrev.setDisable(true);
            btnNext.setDisable(false);
        } else if (actIsShown.getValue() == addFavouriteData.length - 1) {
            btnPrev.setDisable(false);
            btnNext.setDisable(true);
        } else {
            btnPrev.setDisable(false);
            btnNext.setDisable(false);
        }

        initConstValues.makeAct();
        initCollection.makeAct();
        initGrade.makeAct();
        initDescription.makeAct();
    }
}
