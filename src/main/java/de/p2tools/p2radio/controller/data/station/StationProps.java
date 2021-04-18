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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigStringExtra;
import de.p2tools.p2Lib.tools.date.PLocalDate;

import java.util.ArrayList;

public class StationProps extends StationXml {

    public static final String TAG = "Station";

    public int no;
    public PLocalDate stationDate = new PLocalDate();
    private int bitrateInt = 0;
    private int votes = 0;
    private int clickCount = 0;
    private int clickTrend = 0;

    private boolean favouriteUrl = false;
    private boolean doubleUrl = false;
    private boolean newStation = false;
    private boolean blackBlocked = false;

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringExtra("name", StationFieldNames.STATION_NAME, arr, STATION_NAME));
        list.add(new ConfigStringExtra("genre", StationFieldNames.STATION_GENRE, arr, STATION_GENRE));
        list.add(new ConfigStringExtra("codec", StationFieldNames.STATION_CODEC, arr, STATION_CODEC));
        list.add(new ConfigStringExtra("bitrate", StationFieldNames.STATION_BITRATE, arr, STATION_BITRATE));
        list.add(new ConfigStringExtra("state", StationFieldNames.STATION_STATE, arr, STATION_STATE));
        list.add(new ConfigStringExtra("country", StationFieldNames.STATION_COUNTRY, arr, STATION_COUNTRY));
        list.add(new ConfigStringExtra("countryCode", StationFieldNames.STATION_COUNTRY_CODE, arr, STATION_COUNTRY_CODE));
        list.add(new ConfigStringExtra("language", StationFieldNames.STATION_LANGUAGE, arr, STATION_LANGUAGE));
        list.add(new ConfigStringExtra("votes", StationFieldNames.STATION_VOTES, arr, STATION_VOTES));
        list.add(new ConfigStringExtra("clickCount", StationFieldNames.STATION_CLICK_COUNT, arr, STATION_CLICK_COUNT));
        list.add(new ConfigStringExtra("trend", StationFieldNames.STATION_CLICK_TREND, arr, STATION_CLICK_TREND));
        list.add(new ConfigStringExtra("url", StationFieldNames.STATION_URL, arr, STATION_URL));
        list.add(new ConfigStringExtra("urlR", StationFieldNames.STATION_URL_RESOLVED, arr, STATION_URL_RESOLVED));
        list.add(new ConfigStringExtra("website", StationFieldNames.STATION_WEBSITE, arr, STATION_WEBSITE));
        list.add(new ConfigStringExtra("date", StationFieldNames.STATION_DATE, arr, STATION_DATE));

        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public int getBitrateInt() {
        return bitrateInt;
    }

    public void setBitrateInt(int bitrateInt) {
        this.bitrateInt = bitrateInt;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public int getClickTrend() {
        return clickTrend;
    }

    public void setClickTrend(int clickTrend) {
        this.clickTrend = clickTrend;
    }


    public boolean isFavouriteUrl() {
        return favouriteUrl;
    }

    public void setFavouriteUrl(boolean favouriteUrl) {
        this.favouriteUrl = favouriteUrl;
    }

    public boolean isDoubleUrl() {
        return doubleUrl;
    }

    public void setDoubleUrl(boolean doubleUrl) {
        this.doubleUrl = doubleUrl;
    }


    //=========================================================
    //die RadioFelder

    public int getNo() {
        return no;
    }

    public String getName() {
        return arr[STATION_NAME];
    }

    public String getGenre() {
        return arr[STATION_GENRE];
    }

    public String getCodec() {
        return arr[STATION_CODEC];
    }

    public String getBitrate() {
        return arr[STATION_BITRATE];
    }

    public String getState() {
        return arr[STATION_STATE];
    }

    public String getCountry() {
        return arr[STATION_COUNTRY];
    }

    public String getCountryCode() {
        return arr[STATION_COUNTRY_CODE];
    }

    public String getLanguage() {
        return arr[STATION_LANGUAGE];
    }

//    public String getVotes() {
//        return arr[STATION_VOTES];
//    }
//
//    public String getClickCount() {
//        return arr[STATION_CLICK_COUNT];
//    }
//
//    public String getClickTrend() {
//        return arr[STATION_CLICK_TREND];
//    }

    public String getUrl() {
//        if (arr[STATION_URL_RESOLVED].isEmpty()) {
        return arr[STATION_URL];
//        } else {
//            return arr[STATION_URL_RESOLVED];
//        }
    }

    public String getUrlResolved() {
        return arr[STATION_URL_RESOLVED];
    }

    public String getWebsite() {
        return arr[STATION_WEBSITE];
    }


    public PLocalDate getDate() {
        return stationDate;
    }

    //===================================================================

    public boolean isNewStation() {
        return newStation;
    }

    public void setNewStation(final boolean newStation) {
        this.newStation = newStation;
    }

    public boolean isBlackBlocked() {
        return blackBlocked;
    }

    public void setBlackBlocked(boolean blackBlocked) {
        this.blackBlocked = blackBlocked;
    }
}
