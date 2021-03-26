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

package de.p2tools.p2radio.controller.data.favourite;

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.date.PLocalDateProperty;
import de.p2tools.p2radio.tools.Data;
import javafx.beans.property.*;

import java.util.ArrayList;

public class FavouriteProps extends FavouriteXml {

    private final IntegerProperty no = new SimpleIntegerProperty(FavouriteConstants.FAVOURITE_NUMBER_NOT_STARTED);
    private final IntegerProperty stationNo = new SimpleIntegerProperty(FavouriteConstants.STATION_NUMBER_NOT_FOUND);

    private final StringProperty stationName = new SimpleStringProperty("");
    private final StringProperty collectionName = new SimpleStringProperty("");
    private final StringProperty genre = new SimpleStringProperty("");
    private final StringProperty codec = new SimpleStringProperty("");
    private final IntegerProperty bitrate = new SimpleIntegerProperty(0);
    private final IntegerProperty grade = new SimpleIntegerProperty(0);
    private final BooleanProperty own = new SimpleBooleanProperty(false);
    private final StringProperty country = new SimpleStringProperty("");
    private final StringProperty language = new SimpleStringProperty("");
    private final StringProperty countryCode = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty votes = new SimpleStringProperty();
    private final StringProperty clickCount = new SimpleStringProperty();
    private final StringProperty clickTrend = new SimpleStringProperty();

    private final StringProperty url = new SimpleStringProperty("");
    private final StringProperty website = new SimpleStringProperty("");
    private final StringProperty date = new SimpleStringProperty("");

    private final PLocalDateProperty stationDate = new PLocalDateProperty();


    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigIntPropExtra("no", FavouriteFieldNames.FAVOURITE_NO, no));
        list.add(new ConfigIntPropExtra("stationNo", FavouriteFieldNames.FAVOURITE_STATION_NO, stationNo));
        list.add(new ConfigStringPropExtra("station", FavouriteFieldNames.FAVOURITE_STATION, stationName));
        list.add(new ConfigStringPropExtra("collection", FavouriteFieldNames.FAVOURITE_COLLECTION, collectionName));
        list.add(new ConfigStringPropExtra("genre", FavouriteFieldNames.FAVOURITE_GENRE, genre));
        list.add(new ConfigStringPropExtra("codec", FavouriteFieldNames.FAVOURITE_GENRE, codec));
        list.add(new ConfigIntPropExtra("bitrate", FavouriteFieldNames.FAVOURITE_BITRATE, bitrate));
        list.add(new ConfigIntPropExtra("grade", FavouriteFieldNames.FAVOURITE_GRADE, grade));
        list.add(new ConfigBoolPropExtra("own", FavouriteFieldNames.FAVOURITE_OWN, own));

        list.add(new ConfigStringPropExtra("country", FavouriteFieldNames.FAVOURITE_COUNTRY, country));
        list.add(new ConfigStringPropExtra("countryCode", FavouriteFieldNames.FAVOURITE_COUNTRY, countryCode));
        list.add(new ConfigStringPropExtra("language", FavouriteFieldNames.FAVOURITE_COUNTRY, language));
        list.add(new ConfigStringPropExtra("description", FavouriteFieldNames.FAVOURITE_DESCRIPTION, description));

        list.add(new ConfigStringPropExtra("votes", FavouriteFieldNames.FAVOURITE_STATE, votes));
        list.add(new ConfigStringPropExtra("clickCount", FavouriteFieldNames.FAVOURITE_STATE, clickCount));
        list.add(new ConfigStringPropExtra("clickTrend", FavouriteFieldNames.FAVOURITE_STATE, clickTrend));

        list.add(new ConfigStringPropExtra("url", FavouriteFieldNames.FAVOURITE_URL, url));
        list.add(new ConfigStringPropExtra("website", FavouriteFieldNames.FAVOURITE_URL, website));
        list.add(new ConfigLocalDatePropExtra("stationDate", FavouriteFieldNames.FAVOURITE_DATE, stationDate));

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }


    public int getNo() {
        return no.get();
    }

    public IntegerProperty noProperty() {
        return no;
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public int getStationNo() {
        return stationNo.get();
    }

    public IntegerProperty stationNoProperty() {
        return stationNo;
    }

    public void setStationNo(int stationNo) {
        this.stationNo.set(stationNo);
    }

    public String getStationName() {
        return stationName.get();
    }

    public StringProperty stationNameProperty() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName.set(stationName);
    }

    public String getCollectionName() {
        return collectionName.get();
    }

    public StringProperty collectionNameProperty() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName.set(collectionName);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }


    public String getCodec() {
        return codec.get();
    }

    public StringProperty codecProperty() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec.set(codec);
    }

    public int getBitrate() {
        return bitrate.get();
    }

    public IntegerProperty bitrateProperty() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate.set(bitrate);
    }

    public int getGrade() {
        return grade.get();
    }

    public IntegerProperty gradeProperty() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade.set(grade);
    }

    public boolean isOwn() {
        return own.get();
    }

    public BooleanProperty ownProperty() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own.set(own);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getCountryCode() {
        return countryCode.get();
    }

    public StringProperty countryCodeProperty() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode.set(countryCode);
    }

    public String getLanguage() {
        return language.get();
    }

    public StringProperty languageProperty() {
        return language;
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getVotes() {
        return votes.get();
    }

    public StringProperty votesProperty() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes.set(votes);
    }

    public String getClickCount() {
        return clickCount.get();
    }

    public StringProperty clickCountProperty() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount.set(clickCount);
    }

    public String getClickTrend() {
        return clickTrend.get();
    }

    public StringProperty clickTrendProperty() {
        return clickTrend;
    }

    public void setClickTrend(String clickTrend) {
        this.clickTrend.set(clickTrend);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public String getWebsite() {
        return website.get();
    }

    public StringProperty websiteProperty() {
        return website;
    }

    public void setWebsite(String website) {
        this.website.set(website);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public PLocalDate getStationDate() {
        return stationDate.get();
    }

    public PLocalDateProperty stationDateProperty() {
        return stationDate;
    }

    public void setStationDate(PLocalDate stationDate) {
        this.stationDate.setValue(stationDate);
    }

    public void setStationDate(String date) {
        this.stationDate.setPLocalDate(date);
    }

    public int compareTo(FavouriteProps arg0) {
        int ret;
        if ((ret = Data.sorter.compare(getStationName(), arg0.getStationName())) == 0) {
            return getUrl().compareTo(arg0.getUrl());
        }
        return ret;
    }
}
