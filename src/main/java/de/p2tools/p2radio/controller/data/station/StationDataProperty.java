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

package de.p2tools.p2radio.controller.data.station;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.tools.Data;
import javafx.beans.property.*;

import java.util.ArrayList;

public class StationDataProperty<T extends PDataSample> extends PDataSample<T> {

    public static final String TAG = "Favourite";
    private final IntegerProperty no = new SimpleIntegerProperty(ProgConst.NUMBER_DONT_USED);
    private final IntegerProperty stationNo = new SimpleIntegerProperty(ProgConst.NUMBER_DONT_USED);
    private final BooleanProperty newStation = new SimpleBooleanProperty(false);
    private final StringProperty stationName = new SimpleStringProperty("");
    private final StringProperty collectionName = new SimpleStringProperty("");
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty codec = new SimpleStringProperty("");
    private final StringProperty bitrate = new SimpleStringProperty("0");
    private final IntegerProperty bitrateInt = new SimpleIntegerProperty(0);
    private final IntegerProperty votes = new SimpleIntegerProperty(0);
    private final IntegerProperty ownGrade = new SimpleIntegerProperty(0);
    private final BooleanProperty own = new SimpleBooleanProperty(false);
    private final IntegerProperty clickCount = new SimpleIntegerProperty();
    private final IntegerProperty clickTrend = new SimpleIntegerProperty();
    private final StringProperty country = new SimpleStringProperty("");
    private final StringProperty state = new SimpleStringProperty("");
    private final StringProperty language = new SimpleStringProperty("");
    private final StringProperty countryCode = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty stationUrl = new SimpleStringProperty("");
    private final StringProperty stationUrlResolved = new SimpleStringProperty("");
    private final BooleanProperty doubleUrl = new SimpleBooleanProperty(false);
    private final BooleanProperty favouriteUrl = new SimpleBooleanProperty(false);
    private final BooleanProperty blackBlocked = new SimpleBooleanProperty(false);
    private final StringProperty website = new SimpleStringProperty("");
    private final PLocalDate stationDate = new PLocalDate();
    boolean station = false, favourite = false, history = false;

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigIntPropExtra("no", StationDataXml.STATION_PROP_NO, no));
        list.add(new ConfigIntPropExtra("stationNo", StationDataXml.STATION_PROP_STATION_NO, stationNo));
        list.add(new ConfigBoolPropExtra("newStation", StationDataXml.STATION_PROP_STATION_NEW, newStation));
        list.add(new ConfigStringPropExtra("station", StationDataXml.STATION_PROP_STATION_NAME, stationName));
        list.add(new ConfigStringPropExtra("collection", StationDataXml.STATION_PROP_COLLECTION, collectionName));
        list.add(new ConfigStringPropExtra("genre", StationDataXml.STATION_PROP_GENRE, genre));
        list.add(new ConfigStringPropExtra("codec", StationDataXml.STATION_PROP_CODEC, codec));
        list.add(new ConfigStringPropExtra("bitrate", StationDataXml.STATION_PROP_BITRATE, bitrate));
        list.add(new ConfigIntPropExtra("bitrateInt", StationDataXml.STATION_PROP_BITRATE, bitrateInt));
        list.add(new ConfigIntPropExtra("votes", StationDataXml.STATION_PROP_VOTES, votes));
        list.add(new ConfigIntPropExtra("grade", StationDataXml.STATION_PROP_OWN_GRADE, ownGrade));//todo kommt nächste Version wieder weg
        list.add(new ConfigIntPropExtra("ownGrade", StationDataXml.STATION_PROP_OWN_GRADE, ownGrade));
        list.add(new ConfigBoolPropExtra("own", StationDataXml.STATION_PROP_OWN, own));
        list.add(new ConfigIntPropExtra("clickCount", StationDataXml.STATION_PROP_CLICK_COUNT, clickCount));
        list.add(new ConfigIntPropExtra("clickTrend", StationDataXml.STATION_PROP_CLICK_TREND, clickTrend));

        list.add(new ConfigStringPropExtra("country", StationDataXml.STATION_PROP_COUNTRY, country));
        list.add(new ConfigStringPropExtra("state", StationDataXml.STATION_PROP_STATE, state));
        list.add(new ConfigStringPropExtra("countryCode", StationDataXml.STATION_PROP_COUNTRY_CODE, countryCode));
        list.add(new ConfigStringPropExtra("language", StationDataXml.STATION_PROP_LANGUAGE, language));
        list.add(new ConfigStringPropExtra("description", StationDataXml.STATION_PROP_DESCRIPTION, description));

        list.add(new ConfigStringPropExtra("url", StationDataXml.STATION_PROP_URL, stationUrl));
        list.add(new ConfigStringPropExtra("urlResolved", StationDataXml.STATION_PROP_URL_RESOLVED, stationUrlResolved));
        list.add(new ConfigBoolPropExtra("doubleUrl", StationDataXml.STATION_PROP_DOUBLE_URL, doubleUrl));
        list.add(new ConfigBoolPropExtra("favouriteUrl", StationDataXml.STATION_PROP_FAVOURITE_URL, favouriteUrl));
        list.add(new ConfigBoolPropExtra("blackBlocked", StationDataXml.STATION_PROP_BLACK_BLOCKED_URL, blackBlocked));
        list.add(new ConfigStringPropExtra("website", StationDataXml.STATION_PROP_WEBSITE, website));
        list.add(new ConfigLocalDateExtra("stationDate", StationDataXml.STATION_PROP_DATE, stationDate));

        return list.toArray(new Config[]{});
    }

    public int getNo() {
        return no.get();
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public IntegerProperty noProperty() {
        return no;
    }

    public int getStationNo() {
        return stationNo.get();
    }

    public void setStationNo(int stationNo) {
        this.stationNo.set(stationNo);
    }

    public IntegerProperty stationNoProperty() {
        return stationNo;
    }

    public boolean isNewStation() {
        return newStation.get();
    }

    public void setNewStation(boolean newStation) {
        this.newStation.set(newStation);
    }

    public BooleanProperty newStationProperty() {
        return newStation;
    }

    public String getStationName() {
        return stationName.get();
    }

    public void setStationName(String stationName) {
        this.stationName.set(stationName);
    }

    public StringProperty stationNameProperty() {
        return stationName;
    }

    public String getCollectionName() {
        return collectionName.get();
    }

    public void setCollectionName(String collectionName) {
        this.collectionName.set(collectionName);
    }

    public StringProperty collectionNameProperty() {
        return collectionName;
    }

    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public String getCodec() {
        return codec.get();
    }

    public void setCodec(String codec) {
        this.codec.set(codec);
    }

    public StringProperty codecProperty() {
        return codec;
    }

    public String getBitrate() {
        return bitrate.get();
    }

    public void setBitrate(String bitrate) {
        this.bitrate.set(bitrate);
    }

    public StringProperty bitrateProperty() {
        return bitrate;
    }

    public int getBitrateInt() {
        return bitrateInt.get();
    }

    public void setBitrateInt(int bitrateInt) {
        this.bitrateInt.set(bitrateInt);
    }

    public IntegerProperty bitrateIntProperty() {
        return bitrateInt;
    }

    public int getVotes() {
        return votes.get();
    }

    public void setVotes(int votes) {
        this.votes.set(votes);
    }

    public void setVotes(String votes) {
        try {
            this.votes.set(Integer.parseInt(votes));
        } catch (Exception ex) {
            this.votes.set(0);
        }
    }

    public IntegerProperty votesProperty() {
        return votes;
    }

    public int getOwnGrade() {
        return ownGrade.get();
    }

    public void setOwnGrade(int ownGrade) {
        this.ownGrade.set(ownGrade);
    }

    public IntegerProperty ownGradeProperty() {
        return ownGrade;
    }

    public boolean isOwn() {
        return own.get();
    }

    public void setOwn(boolean own) {
        this.own.set(own);
    }

    public BooleanProperty ownProperty() {
        return own;
    }

    public int getClickCount() {
        return clickCount.get();
    }

    public void setClickCount(int clickCount) {
        this.clickCount.set(clickCount);
    }

    public void setClickCount(String clickCount) {
        try {
            this.clickCount.set(Integer.parseInt(clickCount));
        } catch (Exception ex) {
            this.clickCount.set(0);
        }
    }

    public IntegerProperty clickCountProperty() {
        return clickCount;
    }

    public int getClickTrend() {
        return clickTrend.get();
    }

    public void setClickTrend(int clickTrend) {
        this.clickTrend.set(clickTrend);
    }

    public void setClickTrend(String clickTrend) {
        try {
            this.clickTrend.set(Integer.parseInt(clickTrend));
        } catch (Exception ex) {
            this.clickTrend.set(0);
        }
    }

    public IntegerProperty clickTrendProperty() {
        return clickTrend;
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public StringProperty countryProperty() {
        return country;
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public StringProperty stateProperty() {
        return state;
    }

    public String getCountryCode() {
        return countryCode.get();
    }

    public void setCountryCode(String countryCode) {
        this.countryCode.set(countryCode);
    }

    public StringProperty countryCodeProperty() {
        return countryCode;
    }

    public String getLanguage() {
        return language.get();
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public StringProperty languageProperty() {
        return language;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getStationUrl() {
        //        if (!arr[STATION_URL].isEmpty()
        //                && !arr[STATION_URL_RESOLVED].isEmpty()
        //                && !arr[STATION_URL].equals(arr[STATION_URL_RESOLVED])) {
        //            return "--> " + arr[STATION_URL] + " - " + arr[STATION_URL_RESOLVED];
        //
        //        } else {
        //            return arr[STATION_URL];
        //        }

        //        if (arr[STATION_URL_RESOLVED].isEmpty()) {
        //            return arr[STATION_URL];
        //        } else {
        //            return arr[STATION_URL_RESOLVED];
        //        }
        return stationUrl.get();
    }

    public void setStationUrl(String stationUrl) {
        this.stationUrl.set(stationUrl);
    }

    public StringProperty stationUrlProperty() {
        return stationUrl;
    }

    public String getStationUrlResolved() {
        return stationUrlResolved.get();
    }

    public void setStationUrlResolved(String stationUrlResolved) {
        this.stationUrlResolved.set(stationUrlResolved);
    }

    public StringProperty stationUrlResolvedProperty() {
        return stationUrlResolved;
    }

    public boolean isDoubleUrl() {
        return doubleUrl.get();
    }

    public void setDoubleUrl(boolean doubleUrl) {
        this.doubleUrl.set(doubleUrl);
    }

    public BooleanProperty doubleUrlProperty() {
        return doubleUrl;
    }

    public boolean isBlackBlocked() {
        return blackBlocked.get();
    }

    public void setBlackBlocked(boolean blackBlocked) {
        this.blackBlocked.set(blackBlocked);
    }

    public BooleanProperty blackBlockedProperty() {
        return blackBlocked;
    }

    public boolean isFavouriteUrl() {
        return favouriteUrl.get();
    }

    public void setFavouriteUrl(boolean set) {
    }

    public BooleanProperty favouriteUrlProperty() {
        return favouriteUrl;
    }

    public String getWebsite() {
        return website.get();
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    public StringProperty websiteProperty() {
        return website;
    }

    public PLocalDate getStationDate() {
        return stationDate;
    }

    public void setStationDate(PLocalDate stationDate) {
        this.stationDate.setPLocalDate(stationDate);
    }

    public void setStationDate(String date) {
        this.stationDate.setPLocalDate(date);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public int compareTo(StationDataProperty arg0) {
        int ret;
        if ((ret = Data.sorter.compare(getStationName(), arg0.getStationName())) == 0) {
            return getStationUrl().compareTo(arg0.getStationUrl());
        }
        return ret;
    }
}