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

package de.p2tools.p2radio.controller.data.playable;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.data.start.Start;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public interface Playable {

    boolean station = false, favourite = false, lastPlayed = false;

    boolean isStation();

    boolean isFavourite();

    boolean isLastPlayed();

    Start getStart();

    void setStart(Start start);

    //===========================
    //Station
    boolean getFavouriteUrl();

    void setFavouriteUrl(boolean set);

    //===========================
    //Favourite
    int getNo();

    void setNo(int no);

    IntegerProperty noProperty();

    int getStationNo();

    void setStationNo(int no);

    IntegerProperty stationNoProperty();

    boolean isNewStation();

    void setNewStation(boolean newStation);

    BooleanProperty newStationProperty();

    String getStationName();

    void setStationName(String stationName);

    StringProperty stationNameProperty();

    String getCollectionName();

    void setCollectionName(String collectionName);

    StringProperty collectionNameProperty();

    String getGenre();

    void setGenre(String genre);

    StringProperty genreProperty();

    String getCodec();

    void setCodec(String codec);

    StringProperty codecProperty();

    String getBitrate();

    void setBitrate(String bitrate);

    StringProperty bitrateProperty();

    int getBitrateInt();

    void setBitrateInt(int bitrateInt);

    IntegerProperty bitrateIntProperty();

    int getVotes();

    void setVotes(int votes);

    IntegerProperty votesProperty();

    int getOwnGrade();

    void setOwnGrade(int ownGrade);

    IntegerProperty ownGradeProperty();

    boolean isOwn();

    void setOwn(boolean own);

    BooleanProperty ownProperty();

    int getClickCount();

    void setClickCount(int clickCount);

    IntegerProperty clickCountProperty();

    int getClickTrend();

    void setClickTrend(int clickTrend);

    IntegerProperty clickTrendProperty();

    String getCountry();

    void setCountry(String country);

    StringProperty countryProperty();

    String getState();

    void setState(String state);

    StringProperty stateProperty();

    String getCountryCode();

    void setCountryCode(String countryCode);

    StringProperty countryCodeProperty();

    String getLanguage();

    void setLanguage(String language);

    StringProperty languageProperty();

    String getDescription();

    void setDescription(String description);

    StringProperty descriptionProperty();

    String getStationUrl();

    void setStationUrl(String stationUrl);

    StringProperty stationUrlProperty();

    boolean isDoubleUrl();

    void setDoubleUrl(boolean doubleUrl);

    BooleanProperty doubleUrlProperty();

    boolean isBlackBlocked();

    void setBlackBlocked(boolean blackBlocked);

    BooleanProperty blackBlockedProperty();

    String getWebsite();

    void setWebsite(String website);

    StringProperty websiteProperty();

    PLocalDate getStationDate();

    void setStationDate(PLocalDate stationDate);

    void setStationDate(String date);

    //=======================================
    Config[] getConfigsArr();

    Playable getCopy();

    void copyToMe(Playable playable);
}
