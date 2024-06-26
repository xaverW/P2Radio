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

package de.p2tools.p2radio.tools.storedfilter;

import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.configfile.config.Config_boolProp;
import de.p2tools.p2lib.configfile.config.Config_intProp;
import de.p2tools.p2lib.configfile.config.Config_stringProp;
import de.p2tools.p2lib.configfile.pdata.P2DataSample;
import de.p2tools.p2radio.tools.stationlistfilter.StationFilterFactory;
import javafx.beans.property.*;

import java.util.ArrayList;

public class SelectedFilterProps extends P2DataSample<SelectedFilter> {
    public static final String TAG = "SelectedFilter";

    private final StringProperty name = new SimpleStringProperty();
    private final BooleanProperty stationNameVis = new SimpleBooleanProperty(false);
    private final StringProperty stationName = new SimpleStringProperty();
    private final BooleanProperty codecVis = new SimpleBooleanProperty(true);
    private final StringProperty codec = new SimpleStringProperty();
    private final BooleanProperty genreVis = new SimpleBooleanProperty(true);
    private final StringProperty genre = new SimpleStringProperty();
    private final BooleanProperty countryVis = new SimpleBooleanProperty(false);
    private final StringProperty country = new SimpleStringProperty();
    private final BooleanProperty urlVis = new SimpleBooleanProperty(false);
    private final StringProperty url = new SimpleStringProperty();
    private final BooleanProperty somewhereVis = new SimpleBooleanProperty(false);
    private final StringProperty somewhere = new SimpleStringProperty();


    private final BooleanProperty minMaxBitVis = new SimpleBooleanProperty(true);
    private final IntegerProperty minBit = new SimpleIntegerProperty(0);
    private final IntegerProperty maxBit = new SimpleIntegerProperty(StationFilterFactory.FILTER_BITRATE_MAX);

    private final BooleanProperty onlyVis = new SimpleBooleanProperty(false);
    private final BooleanProperty onlyNew = new SimpleBooleanProperty(false);
    private final BooleanProperty noFavourites = new SimpleBooleanProperty(false);
    private final BooleanProperty noDoubles = new SimpleBooleanProperty(false);

    private final BooleanProperty blacklistOn = new SimpleBooleanProperty(false);
    private final BooleanProperty blacklistOnly = new SimpleBooleanProperty(false);

    public BooleanProperty[] sfBooleanPropArr = {codecVis, genreVis, countryVis, stationNameVis,
            urlVis, somewhereVis, minMaxBitVis,
            onlyVis, onlyNew, noFavourites, noDoubles, blacklistOn, blacklistOnly};

    public StringProperty[] sfStringPropArr = {name, stationName, genre, codec, country, url, somewhere};
    public IntegerProperty[] sfIntegerPropArr = {minBit, maxBit};

    @Override
    public String getComment() {
        return "aktuelle Filtereinstellungen";
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new Config_stringProp("name", SelectedFilterFieldNames.NAME, name));
        list.add(new Config_boolProp("stationNameVis", SelectedFilterFieldNames.STATION_NAME_VIS, stationNameVis));
        list.add(new Config_stringProp("stationName", SelectedFilterFieldNames.STATION_NAME, stationName));
        list.add(new Config_boolProp("codecVis", SelectedFilterFieldNames.CODEC_VIS, codecVis));
        list.add(new Config_stringProp("codec", SelectedFilterFieldNames.CODEC, codec));
        list.add(new Config_boolProp("genreVis", SelectedFilterFieldNames.GENRE_VIS, genreVis));
        list.add(new Config_stringProp("genre", SelectedFilterFieldNames.GENRE, genre));
        list.add(new Config_boolProp("countryVis", SelectedFilterFieldNames.COUNTRY_VIS, countryVis));
        list.add(new Config_stringProp("country", SelectedFilterFieldNames.COUNTRY, country));
        list.add(new Config_boolProp("urlVis", SelectedFilterFieldNames.URL_VIS, urlVis));
        list.add(new Config_stringProp("url", SelectedFilterFieldNames.URL, url));
        list.add(new Config_boolProp("somewhereVis", SelectedFilterFieldNames.SOMEWHERE_VIS, somewhereVis));
        list.add(new Config_stringProp("somewhere", SelectedFilterFieldNames.SOMEWHERE, somewhere));

        list.add(new Config_boolProp("minMaxBitVis", SelectedFilterFieldNames.MIN_MAX_BIT_VIS, minMaxBitVis));
        list.add(new Config_intProp("minBit", SelectedFilterFieldNames.MIN_BIT, minBit));
        list.add(new Config_intProp("maxBit", SelectedFilterFieldNames.MAX_BIT, maxBit));
        list.add(new Config_boolProp("onlyVis", SelectedFilterFieldNames.ONLY_VIS, onlyVis));
        list.add(new Config_boolProp("onlyNew", SelectedFilterFieldNames.ONLY_NEW, onlyNew));
        list.add(new Config_boolProp("noFavourites", SelectedFilterFieldNames.NO_FAVOURITES, noFavourites));
        list.add(new Config_boolProp("noDoubles", SelectedFilterFieldNames.NO_DOUBLES, noDoubles));
        list.add(new Config_boolProp("blacklistOn", SelectedFilterFieldNames.BLACKLIST_ON, blacklistOn));
        list.add(new Config_boolProp("blacklistOnly", SelectedFilterFieldNames.BLACKLIST_ONLY, blacklistOnly));

        return list.toArray(new Config[]{});
    }


    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public boolean isStationNameVis() {
        return stationNameVis.get();
    }

    public void setStationNameVis(boolean stationNameVis) {
        this.stationNameVis.set(stationNameVis);
    }

    public BooleanProperty stationNameVisProperty() {
        return stationNameVis;
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

    public boolean isCodecVis() {
        return codecVis.get();
    }

    public void setCodecVis(boolean codecVis) {
        this.codecVis.set(codecVis);
    }

    public BooleanProperty codecVisProperty() {
        return codecVis;
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

    public boolean isGenreVis() {
        return genreVis.get();
    }

    public void setGenreVis(boolean genreVis) {
        this.genreVis.set(genreVis);
    }

    public BooleanProperty genreVisProperty() {
        return genreVis;
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

    public boolean isCountryVis() {
        return countryVis.get();
    }

    public void setCountryVis(boolean countryVis) {
        this.countryVis.set(countryVis);
    }

    public BooleanProperty countryVisProperty() {
        return countryVis;
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

    public boolean isUrlVis() {
        return urlVis.get();
    }

    public void setUrlVis(boolean urlVis) {
        this.urlVis.set(urlVis);
    }

    public BooleanProperty urlVisProperty() {
        return urlVis;
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public StringProperty urlProperty() {
        return url;
    }

    public boolean isSomewhereVis() {
        return somewhereVis.get();
    }

    public void setSomewhereVis(boolean somewhereVis) {
        this.somewhereVis.set(somewhereVis);
    }

    public BooleanProperty somewhereVisProperty() {
        return somewhereVis;
    }

    public String getSomewhere() {
        return somewhere.get();
    }

    public void setSomewhere(String somewhere) {
        this.somewhere.set(somewhere);
    }

    public StringProperty somewhereProperty() {
        return somewhere;
    }

    public boolean isMinMaxBitVis() {
        return minMaxBitVis.get();
    }

    public void setMinMaxBitVis(boolean minMaxBitVis) {
        this.minMaxBitVis.set(minMaxBitVis);
    }

    public BooleanProperty minMaxBitVisProperty() {
        return minMaxBitVis;
    }

    public int getMinBit() {
        return minBit.get();
    }

    public void setMinBit(int minBit) {
        this.minBit.set(minBit);
    }

    public IntegerProperty minBitProperty() {
        return minBit;
    }

    public int getMaxBit() {
        return maxBit.get();
    }

    public void setMaxBit(int maxBit) {
        this.maxBit.set(maxBit);
    }

    public IntegerProperty maxBitProperty() {
        return maxBit;
    }

    public boolean isOnlyVis() {
        return onlyVis.get();
    }

    public void setOnlyVis(boolean onlyVis) {
        this.onlyVis.set(onlyVis);
    }

    public BooleanProperty onlyVisProperty() {
        return onlyVis;
    }

    public boolean isOnlyNew() {
        return onlyNew.get();
    }

    public void setOnlyNew(boolean onlyNew) {
        this.onlyNew.set(onlyNew);
    }

    public BooleanProperty onlyNewProperty() {
        return onlyNew;
    }

    public boolean isNoDoubles() {
        return noDoubles.get();
    }

    public void setNoDoubles(boolean noDoubles) {
        this.noDoubles.set(noDoubles);
    }

    public BooleanProperty noDoublesProperty() {
        return noDoubles;
    }

    public boolean isNoFavourites() {
        return noFavourites.get();
    }

    public void setNoFavourites(boolean noFavourites) {
        this.noFavourites.set(noFavourites);
    }

    public BooleanProperty noFavouritesProperty() {
        return noFavourites;
    }

    public boolean isBlacklistOn() {
        return blacklistOn.get();
    }

    public void setBlacklistOn(boolean blacklistOn) {
        this.blacklistOn.set(blacklistOn);
    }

    public BooleanProperty blacklistOnProperty() {
        return blacklistOn;
    }

    public boolean isBlacklistOnly() {
        return blacklistOnly.get();
    }

    public void setBlacklistOnly(boolean blacklistOnly) {
        this.blacklistOnly.set(blacklistOnly);
    }

    public BooleanProperty blacklistOnlyProperty() {
        return blacklistOnly;
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    @Override
    public int compareTo(SelectedFilter setData) {
        return this.getName().compareTo(setData.getName());
    }
}
