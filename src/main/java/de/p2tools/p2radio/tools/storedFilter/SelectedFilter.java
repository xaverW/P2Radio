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

package de.p2tools.p2radio.tools.storedFilter;

import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.tools.stationListFilter.StationFilterFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.function.Predicate;

public final class SelectedFilter extends SelectedFilterProps {

    private final BooleanProperty filterChange = new SimpleBooleanProperty(false);
    private final BooleanProperty blacklistChange = new SimpleBooleanProperty(false);
    private boolean reportChange = true;

    public SelectedFilter() {
        initFilter();
        setName("Filter");
    }

    public SelectedFilter(String name) {
        initFilter();
        setName(name);
    }

    public boolean isReportChange() {
        return reportChange;
    }

    public void setReportChange(boolean reportChange) {
        this.reportChange = reportChange;
    }

    public BooleanProperty filterChangeProperty() {
        return filterChange;
    }

    public BooleanProperty blacklistChangeProperty() {
        return blacklistChange;
    }

    public void setNameAndVis(String set) {
        setStationName(set);
        setStationNameVis(true);
    }

    public void setGenreAndVis(String set) {
        setGenre(set);
        setGenreVis(true);
    }

    public void setCodecAndVis(String set) {
        setCodec(set);
        setCodecVis(true);
    }

    public void setCountryAndVis(String set) {
        setCountry(set);
        setCountryVis(true);
    }

    public void setUrlAndVis(String set) {
        setUrl(set);
        setUrlVis(true);
    }

    public void setSomewhereAndVis(String set) {
        setSomewhere(set);
        setSomewhereVis(true);
    }

    public void initFilter() {
        clearFilter();

        setStationNameVis(true);
        setGenreVis(true);
        setCodecVis(false);
        setCountryVis(false);
        setUrlVis(false);

        setMinMaxBitVis(false);
        setOnlyVis(false);

        stationNameVisProperty().addListener(l -> reportFilterChange());
        stationNameProperty().addListener(l -> reportFilterChange());

        codecVisProperty().addListener(l -> reportFilterChange());
        codecProperty().addListener(l -> reportFilterChange());

        countryVisProperty().addListener(l -> reportFilterChange());
        countryProperty().addListener(l -> reportFilterChange());

        genreVisProperty().addListener(l -> reportFilterChange());
        genreProperty().addListener(l -> reportFilterChange());

        urlVisProperty().addListener(l -> reportFilterChange());
        urlProperty().addListener(l -> reportFilterChange());

        somewhereVisProperty().addListener(l -> reportFilterChange());
        somewhereProperty().addListener(l -> reportFilterChange());

        minMaxBitVisProperty().addListener(l -> reportFilterChange());
        minBitProperty().addListener(l -> reportFilterChange());
        maxBitProperty().addListener(l -> reportFilterChange());

        onlyVisProperty().addListener(l -> reportFilterChange());
        onlyNewProperty().addListener(l -> reportFilterChange());
        noFavouritesProperty().addListener(l -> reportFilterChange());
        noDoublesProperty().addListener(l -> reportFilterChange());

        blacklistOnProperty().addListener(l -> reportBlacklistChange());
        blacklistOnlyProperty().addListener(l -> reportBlacklistChange());

    }

    private void reportFilterChange() {
        if (reportChange) {
            filterChange.setValue(!filterChange.getValue());
        }
    }

    private void reportBlacklistChange() {
        if (reportChange) {
            blacklistChange.setValue(!blacklistChange.getValue());
        }
    }

    public void clearFilter() {
        // alle Filter löschen, Button Black bleibt wie er ist
        setStationName("");
        setCodec("");
        setGenre("");
        setCountry("");
        setUrl("");
        setSomewhere("");

        setMinBit(0);
        setMaxBit(StationFilterFactory.FILTER_BITRATE_MAX);

        setOnlyNew(false);
        setNoFavourites(false);
        setNoDoubles(false);
    }

    public boolean isTextFilterEmpty() {
        return getStationName().isEmpty() &&
                getCodec().isEmpty() &&
                getGenre().isEmpty() &&
                getCountry().isEmpty() &&
                getUrl().isEmpty() &&
                getSomewhere().isEmpty();
    }


    public boolean clearTxtFilter() {
        boolean ret = false;
        if (!getStationName().isEmpty()) {
            ret = true;
            setStationName("");
        }
        if (!getCodec().isEmpty()) {
            ret = true;
            setCodec("");
        }
        if (!getGenre().isEmpty()) {
            ret = true;
            setGenre("");
        }
        if (!getCountry().isEmpty()) {
            ret = true;
            setCountry("");
        }
        if (!getUrl().isEmpty()) {
            ret = true;
            setUrl("");
        }
        if (!getSomewhere().isEmpty()) {
            ret = true;
            setSomewhere("");
        }
        return ret;
    }

    public Predicate<Favourite> getPredicate() {
        SelectedFilter selectedFilter = this;

        Filter fStationName;
        Filter fGenre;
        Filter fCodec;
        Filter fCountry;
        Filter fUrl;
        Filter fSomewhere;

        String filterStationName = selectedFilter.isStationNameVis() ? selectedFilter.getStationName() : "";
        String filterGenre = selectedFilter.isGenreVis() ? selectedFilter.getGenre() : "";
        String filterCodec = selectedFilter.isCodecVis() ? selectedFilter.getCodec() : "";
        String filterCountry = selectedFilter.isCountryVis() ? selectedFilter.getCountry() : "";
        String filterUrl = selectedFilter.isUrlVis() ? selectedFilter.getUrl() : "";
        String filterSomewhere = selectedFilter.isSomewhereVis() ? selectedFilter.getSomewhere() : "";

        fStationName = new Filter(filterStationName, true);
        fGenre = new Filter(filterGenre, true);
        fCodec = new Filter(filterCodec, true);
        fCountry = new Filter(filterCountry, true);
        fUrl = new Filter(filterUrl, false); // gibt URLs mit ",", das also nicht trennen
        fSomewhere = new Filter(filterSomewhere, true);

        // Länge am Slider in Min
        final int minBitrate = selectedFilter.isMinMaxBitVis() ? selectedFilter.getMinBit() : 0;
        final int maxBitrate = selectedFilter.isMinMaxBitVis() ? selectedFilter.getMaxBit() : StationFilterFactory.FILTER_BITRATE_MAX;

        final boolean onlyNew = selectedFilter.isOnlyVis() && selectedFilter.isOnlyNew();
        final boolean noFavourite = selectedFilter.isOnlyVis() && selectedFilter.isNoFavourites();
        final boolean noDouble = selectedFilter.isOnlyVis() && selectedFilter.isNoDoubles();
        final boolean onlyBlack = selectedFilter.isBlacklistOnly();


        Predicate<Favourite> predicate = station -> true;

        if (onlyNew) {
            predicate = predicate.and(station -> station.isNewStation());
        }

        if (noFavourite) {
            predicate = predicate.and(station -> !station.isFavourite());
        }

        if (noDouble) {
            predicate = predicate.and(station -> !station.isDoubleUrl());
        }

        if (onlyBlack) {
            predicate = predicate.and(station -> station.isBlackBlocked());
        }

        // Bitrate
        if (minBitrate != 0) {
            predicate = predicate.and(station -> StationFilterFactory.checkBitrateMin(minBitrate, station.getBitrateInt()));
        }
        if (maxBitrate != StationFilterFactory.FILTER_BITRATE_MAX) {
            predicate = predicate.and(station -> StationFilterFactory.checkBitrateMax(maxBitrate, station.getBitrateInt()));
        }

        if (!fStationName.empty) {
            predicate = predicate.and(station -> StationFilterFactory.checkSenderName(fStationName, station));
        }

        if (!fGenre.empty) {
            predicate = predicate.and(station -> StationFilterFactory.checkGenre(fGenre, station));
        }

        if (!fCodec.empty) {
            predicate = predicate.and(station -> StationFilterFactory.checkCodec(fCodec, station));
        }

        if (!fCountry.empty) {
            predicate = predicate.and(station -> StationFilterFactory.checkCountry(fCountry, station));
        }

        if (!fUrl.empty) {
            predicate = predicate.and(station -> StationFilterFactory.checkUrl(fUrl, station));
        }

        if (!fSomewhere.empty) {
            predicate = predicate.and(station -> StationFilterFactory.checkSomewhere(fSomewhere, station));
        }

        return predicate;
    }
}
