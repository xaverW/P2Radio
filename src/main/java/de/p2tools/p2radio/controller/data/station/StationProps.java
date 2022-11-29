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
import de.p2tools.p2Lib.configFile.config.ConfigLocalDateExtra;
import de.p2tools.p2Lib.configFile.config.ConfigStringExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.data.playable.PlayableXml;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.ArrayList;

public class StationProps extends PDataSample<Station> {

    public static final String TAG = "Station";
    static final FastDateFormat sdf_date_time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    static final FastDateFormat sdf_date = FastDateFormat.getInstance("dd.MM.yyyy");
    public final String[] arr = new String[PlayableXml.MAX_ELEM];
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
        list.add(new ConfigStringExtra("name", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_STATION_NAME_INT], arr, PlayableXml.STATION_PROP_STATION_NAME_INT));
        list.add(new ConfigStringExtra("genre", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_GENRE_INT], arr, PlayableXml.STATION_PROP_GENRE_INT));
        list.add(new ConfigStringExtra("codec", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_CODEC_INT], arr, PlayableXml.STATION_PROP_CODEC_INT));
        list.add(new ConfigStringExtra("bitrate", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_BITRATE_INT], arr, PlayableXml.STATION_PROP_BITRATE_INT));
        list.add(new ConfigStringExtra("state", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_STATE_INT], arr, PlayableXml.STATION_PROP_STATE_INT));
        list.add(new ConfigStringExtra("country", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_COUNTRY_INT], arr, PlayableXml.STATION_PROP_COUNTRY_INT));
        list.add(new ConfigStringExtra("countryCode", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_COUNTRY_CODE_INT], arr, PlayableXml.STATION_PROP_COUNTRY_CODE_INT));
        list.add(new ConfigStringExtra("language", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_LANGUAGE_INT], arr, PlayableXml.STATION_PROP_LANGUAGE_INT));
        list.add(new ConfigStringExtra("votes", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_VOTES_INT], arr, PlayableXml.STATION_PROP_VOTES_INT));
        list.add(new ConfigStringExtra("clickCount", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_CLICK_COUNT_INT], arr, PlayableXml.STATION_PROP_CLICK_COUNT_INT));
        list.add(new ConfigStringExtra("trend", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_CLICK_TREND_INT], arr, PlayableXml.STATION_PROP_CLICK_TREND_INT));
        list.add(new ConfigStringExtra("url", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_URL_INT], arr, PlayableXml.STATION_PROP_URL_INT));
        list.add(new ConfigStringExtra("urlR", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_URL_RESOLVED_INT], arr, PlayableXml.STATION_PROP_URL_RESOLVED_INT));
        list.add(new ConfigStringExtra("website", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_WEBSITE_INT], arr, PlayableXml.STATION_PROP_WEBSITE_INT));
        list.add(new ConfigStringExtra("date", PlayableXml.COLUMN_NAMES[PlayableXml.STATION_PROP_DATE_INT], arr, PlayableXml.STATION_PROP_DATE_INT));
        list.add(new ConfigLocalDateExtra("stationDate", PlayableXml.STATION_PROP_DATE, stationDate));
        return list.toArray(new Config[]{});
    }

    @Override
    public String getTag() {
        return TAG;
    }


    public boolean isNewStation() {
        return newStation;
    }

    public void setNewStation(final boolean newStation) {
        this.newStation = newStation;
    }

    public boolean isBlackBlocked() {
        return blackBlocked;
    }

    public void setBlackBlocked(boolean set) {
        blackBlocked = set;
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

    public int getNo() {
        return no;
    }

    public String getStationName() {
        return arr[PlayableXml.STATION_PROP_STATION_NAME_INT];
    }

    //=========================================================
    //die RadioFelder

    public String getGenre() {
        return arr[PlayableXml.STATION_PROP_GENRE_INT];
    }

    public String getCodec() {
        return arr[PlayableXml.STATION_PROP_CODEC_INT];
    }

    public String getBitrate() {
        return arr[PlayableXml.STATION_PROP_BITRATE_INT];
    }

    public void setBitrate(String bitrate) {
        try {
            this.bitrateInt = Integer.parseInt(bitrate);
        } catch (Exception ex) {
            this.bitrateInt = 0;
        }
    }

    public String getState() {
        return arr[PlayableXml.STATION_PROP_STATE_INT];
    }

    public String getCountry() {
        return arr[PlayableXml.STATION_PROP_COUNTRY_INT];
    }

    public String getCountryCode() {
        return arr[PlayableXml.STATION_PROP_COUNTRY_CODE_INT];
    }

    public String getLanguage() {
        return arr[PlayableXml.STATION_PROP_LANGUAGE_INT];
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

    public String getUrlResolved() {
        return arr[PlayableXml.STATION_PROP_URL_RESOLVED_INT];
    }

    public String getWebsite() {
        return arr[PlayableXml.STATION_PROP_WEBSITE_INT];
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


    public Station getCopy() {
        final Station ret = new Station();
        System.arraycopy(arr, 0, ret.arr, 0, arr.length);
        ret.init(); //Datum und int-Werte setzen
        return ret;
    }

    @Override
    public int compareTo(Station arg0) {
        int ret;
        if ((ret = sorter.compare(arr[PlayableXml.STATION_PROP_STATION_NAME_INT], arg0.arr[PlayableXml.STATION_PROP_STATION_NAME_INT])) == 0) {
            return sorter.compare(arr[PlayableXml.STATION_PROP_GENRE_INT], arg0.arr[PlayableXml.STATION_PROP_GENRE_INT]);
        }
        return ret;
    }
}
