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

import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.playable.PlayableXml;
import javafx.beans.property.*;
import org.apache.commons.lang3.time.FastDateFormat;

public class StationProps extends PDataSample<Favourite> {

    public static final String TAG = "Station";
    static final FastDateFormat sdf_date = FastDateFormat.getInstance("dd.MM.yyyy");
    public final String[] arr = new String[PlayableXml.MAX_ELEM];
    public int no;
    public PLocalDate stationDate = new PLocalDate();
    private int bitrateInt = 0;
    private int votes = 0;
    private int clickCount = 0;
    private int clickTrend = 0;
    private int ownGrade = 0;

    private boolean favouriteUrl = false;
    private boolean doubleUrl = false;
    private boolean newStation = false;
    private boolean blackBlocked = false;
    private boolean own = false;

    @Override
    public String getTag() {
        return TAG;
    }

    public void init() {
        preserveMemory();
        setDate();
    }

    public boolean isNewStation() {
        return newStation;
    }

    public void setNewStation(final boolean newStation) {
        this.newStation = newStation;
    }

    public BooleanProperty newStationProperty() {
        return new SimpleBooleanProperty(newStation);
    }

    public boolean isBlackBlocked() {
        return blackBlocked;
    }

    public void setBlackBlocked(boolean set) {
        blackBlocked = set;
    }

    public BooleanProperty blackBlockedProperty() {
        return new SimpleBooleanProperty(isBlackBlocked());
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setVotes(String votes) {
        try {
            this.votes = Integer.parseInt(votes);
            arr[PlayableXml.STATION_PROP_VOTES_INT] = votes;
        } catch (Exception ex) {
            this.votes = 0;
            arr[PlayableXml.STATION_PROP_VOTES_INT] = "0";
        }
    }

    public IntegerProperty votesProperty() {
        return new SimpleIntegerProperty(getVotes());
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void setClickCount(String clickCount) {
        try {
            this.clickCount = Integer.parseInt(clickCount);
            arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT] = clickCount;
        } catch (Exception ex) {
            this.clickCount = 0;
            arr[PlayableXml.STATION_PROP_CLICK_COUNT_INT] = "0";
        }
    }

    public IntegerProperty clickCountProperty() {
        return new SimpleIntegerProperty(getClickCount());
    }

    public int getClickTrend() {
        return clickTrend;
    }

    public void setClickTrend(int clickTrend) {
        this.clickTrend = clickTrend;
        arr[PlayableXml.STATION_PROP_CLICK_TREND_INT] = clickTrend + "";
    }

    public void setClickTrend(String clickTrend) {
        try {
            this.clickTrend = Integer.parseInt(clickTrend);
            arr[PlayableXml.STATION_PROP_CLICK_TREND_INT] = clickTrend;
        } catch (Exception ex) {
            this.clickTrend = 0;
            arr[PlayableXml.STATION_PROP_CLICK_TREND_INT] = "0";
        }
    }

    public IntegerProperty clickTrendProperty() {
        return new SimpleIntegerProperty(getClickTrend());
    }

    public boolean isFavouriteUrl() {
        return favouriteUrl;
    }

    public boolean isDoubleUrl() {
        return doubleUrl;
    }

    public void setDoubleUrl(boolean doubleUrl) {
        this.doubleUrl = doubleUrl;
    }

    public BooleanProperty doubleUrlProperty() {
        return new SimpleBooleanProperty(isDoubleUrl());
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public IntegerProperty noProperty() {
        return new SimpleIntegerProperty(no);
    }

    public int getStationNo() {
        return no;
    }

    public void setStationNo(int stationNo) {
        this.no = stationNo;
        arr[PlayableXml.STATION_PROP_STATION_NO_INT] = stationNo + "";
    }

    public IntegerProperty stationNoProperty() {
        return new SimpleIntegerProperty(no);
    }

    public String getStationName() {
        return arr[PlayableXml.STATION_PROP_STATION_NAME_INT];
    }

    public void setStationName(String stationName) {
        arr[PlayableXml.STATION_PROP_STATION_NAME_INT] = stationName;
    }

    public StringProperty stationNameProperty() {
        return new SimpleStringProperty(getStationName());
    }

    public String getGenre() {
        return arr[PlayableXml.STATION_PROP_GENRE_INT];
    }

    public void setGenre(String genre) {
        arr[PlayableXml.STATION_PROP_GENRE_INT] = genre;
    }

    public StringProperty genreProperty() {
        return new SimpleStringProperty(getGenre());
    }

    public String getCodec() {
        return arr[PlayableXml.STATION_PROP_CODEC_INT];
    }

    public void setCodec(String codec) {
        arr[PlayableXml.STATION_PROP_CODEC_INT] = codec;
    }

    public StringProperty codecProperty() {
        return new SimpleStringProperty(getCodec());
    }

    public String getBitrate() {
        return arr[PlayableXml.STATION_PROP_BITRATE_INT];
    }

    public void setBitrate(String bitrate) {
        try {
            this.bitrateInt = Integer.parseInt(bitrate);
            arr[PlayableXml.STATION_PROP_BITRATE_INT] = bitrate;
        } catch (Exception ex) {
            this.bitrateInt = 0;
            arr[PlayableXml.STATION_PROP_BITRATE_INT] = "0";
        }
    }

    public StringProperty bitrateProperty() {
        return new SimpleStringProperty(getBitrate());
    }

    public int getBitrateInt() {
        return bitrateInt;
    }

    public void setBitrateInt(int bitrate) {
        this.bitrateInt = bitrate;
        arr[PlayableXml.STATION_PROP_BITRATE_INT] = bitrate + "";
    }

    public IntegerProperty bitrateIntProperty() {
        return new SimpleIntegerProperty(getBitrateInt());
    }

    public String getState() {
        return arr[PlayableXml.STATION_PROP_STATE_INT];
    }

    public void setState(String state) {
        arr[PlayableXml.STATION_PROP_STATE_INT] = state;
    }

    public StringProperty stateProperty() {
        return new SimpleStringProperty(getState());
    }

    public String getCountry() {
        return arr[PlayableXml.STATION_PROP_COUNTRY_INT];
    }

    public void setCountry(String country) {
        arr[PlayableXml.STATION_PROP_COUNTRY_INT] = country;
    }

    public StringProperty countryProperty() {
        return new SimpleStringProperty(getCountry());
    }

    public String getCountryCode() {
        return arr[PlayableXml.STATION_PROP_COUNTRY_CODE_INT];
    }

    public void setCountryCode(String countryCode) {
        arr[PlayableXml.STATION_PROP_COUNTRY_CODE_INT] = countryCode;
    }

    public StringProperty countryCodeProperty() {
        return new SimpleStringProperty(getCountryCode());
    }

    public String getLanguage() {
        return arr[PlayableXml.STATION_PROP_LANGUAGE_INT];
    }

    public void setLanguage(String language) {
        arr[PlayableXml.STATION_PROP_LANGUAGE_INT] = language;
    }

    public StringProperty languageProperty() {
        return new SimpleStringProperty(getLanguage());
    }

    public String getStationUrl() {
        return arr[PlayableXml.STATION_PROP_URL_INT];

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
    }

    public void setStationUrl(String stationUrl) {
        arr[PlayableXml.STATION_PROP_URL_INT] = stationUrl;
    }

    public StringProperty stationUrlProperty() {
        return new SimpleStringProperty(getStationUrl());
    }

    public String getUrlResolved() {
        return arr[PlayableXml.STATION_PROP_URL_RESOLVED_INT];
    }

    public String getWebsite() {
        return arr[PlayableXml.STATION_PROP_WEBSITE_INT];
    }

    public void setWebsite(String website) {
        arr[PlayableXml.STATION_PROP_WEBSITE_INT] = website;
    }

    public StringProperty websiteProperty() {
        return new SimpleStringProperty(getWebsite());
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

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
        arr[PlayableXml.STATION_PROP_OWN_INT] = Boolean.toString(own);
    }

    public BooleanProperty ownProperty() {
        return new SimpleBooleanProperty(isOwn());
    }

    public boolean getFavouriteUrl() {
        return favouriteUrl;
    }

    public void setFavouriteUrl(boolean favouriteUrl) {
        this.favouriteUrl = favouriteUrl;
    }

    public String getCollectionName() {
        return arr[PlayableXml.STATION_PROP_COLLECTION_INT];
    }

    public void setCollectionName(String name) {
        arr[PlayableXml.STATION_PROP_COLLECTION_INT] = name;
    }

    public StringProperty collectionNameProperty() {
        return new SimpleStringProperty(getCollectionName());
    }

    public int getOwnGrade() {
        return ownGrade;
    }

    public void setOwnGrade(int ownGrade) {
        this.ownGrade = ownGrade;
        arr[PlayableXml.STATION_PROP_OWN_GRADE_INT] = ownGrade + "";
    }

    public IntegerProperty ownGradeProperty() {
        return new SimpleIntegerProperty(getOwnGrade());
    }

    public void setGrade(String grade) {
        try {
            this.ownGrade = Integer.parseInt(grade);
            arr[PlayableXml.STATION_PROP_OWN_GRADE_INT] = grade;
        } catch (Exception ex) {
            this.ownGrade = 0;
            arr[PlayableXml.STATION_PROP_OWN_GRADE_INT] = "0";
        }
    }

    public String getDescription() {
        return arr[PlayableXml.STATION_PROP_DESCRIPTION_INT];
    }

    public void setDescription(String description) {
        arr[PlayableXml.STATION_PROP_DESCRIPTION_INT] = description;
    }

    public StringProperty descriptionProperty() {
        return new SimpleStringProperty(getDescription());
    }

    void preserveMemory() {
        // Speicher sparen
        arr[PlayableXml.STATION_PROP_DATE_INT] = arr[PlayableXml.STATION_PROP_DATE_INT].intern();
        arr[PlayableXml.STATION_PROP_LANGUAGE_INT] = arr[PlayableXml.STATION_PROP_LANGUAGE_INT].intern();
    }

    void setDate() {
        getStationDate().setPLocalDateNow();
        if (!arr[PlayableXml.STATION_PROP_DATE_INT].isEmpty()) {
            // nur dann gibts ein Datum
            try {
                PDate pd = new PDate(sdf_date.parse(arr[PlayableXml.STATION_PROP_DATE_INT]));
                setStationDate(pd.getPlocalDate());

            } catch (final Exception ex) {
                PLog.errorLog(854121547, ex, new String[]{"Datum: " + arr[PlayableXml.STATION_PROP_DATE_INT]});
                getStationDate().setPLocalDateNow();
                arr[PlayableXml.STATION_PROP_DATE_INT] = "";
            }
        }
    }

//    public Station getCopy() {
//        final Station ret = new Station();
//        System.arraycopy(arr, 0, ret.arr, 0, arr.length);
//        ret.init(); //Datum und int-Werte setzen
//        return ret;
//    }
//
//    public void copyToMe(Playable playable) {
//        System.arraycopy(playable.getConfigsArr(), 0, arr, 0, arr.length);
//        this.init();
//    }
//
//    @Override
//    public int compareTo(Station arg0) {
//        int ret;
//        if ((ret = sorter.compare(arr[PlayableXml.STATION_PROP_STATION_NAME_INT], arg0.arr[PlayableXml.STATION_PROP_STATION_NAME_INT])) == 0) {
//            return sorter.compare(arr[PlayableXml.STATION_PROP_GENRE_INT], arg0.arr[PlayableXml.STATION_PROP_GENRE_INT]);
//        }
//        return ret;
//    }
}
