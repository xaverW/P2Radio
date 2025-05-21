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

import de.p2tools.p2lib.configfile.pdata.P2DataList;
import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2lib.tools.date.P2LDateProperty;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.tools.stationlistfilter.BlackFilterFactory;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SimpleTimeZone;
import java.util.function.Predicate;

public class StationList extends SimpleListProperty<StationData> implements P2DataList<StationData> {

    public static final String TAG = "StationList";
    public static final String KEY_STATION_DATE = "stationDate";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    private final P2LDateProperty stationDate = new P2LDateProperty();
    public int no = 1;
    public String[] codecs = {""};
    public String[] countries = {""};
    int countDouble = 0;
    private FilteredList<StationData> filteredList = null;
    private SortedList<StationData> sortedList = null;

    {
        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
    }

    public StationList() {
        super(FXCollections.observableArrayList());
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public String getComment() {
        return "Liste aller Sender";
    }

    @Override
    public StationData getNewItem() {
        return new StationData();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(StationData.class)) {
            importStationOnlyWithNr(((StationData) obj));
        }
    }

    public LocalDate getStationDate() {
        return stationDate.get();
    }

    public void setStationDate(LocalDate stationDate) {
        this.stationDate.set(stationDate);
    }

    public synchronized String getGenDate() {
        return P2LDateFactory.toString(stationDate.getValue());
    }

    public synchronized void setGenDateNow() {
        stationDate.setValue(LocalDate.now());
    }

    public boolean isTooOld() {
        return getAge() > ProgConst.LOAD_STATION_LIST_EVERY_DAYS;
    }

    public void triggerFilter() {
        filteredListSetPred(ProgData.getInstance().storedFilters.getActFilterSettings().getPredicate());
    }

    /**
     * Get the age of the station list.
     *
     * @return Age in days.
     */
    public int getAge() {
        int days = 0;
        final LocalDate sDate = stationDate.getValue();
        if (sDate != null) {
            days = (int) ChronoUnit.DAYS.between(sDate, LocalDate.now());
            if (days < 0) {
                days = 0;
            }
        }
        return days;
    }

    public synchronized void filterListWithBlacklist(boolean mark) {
        // damit wird die Senderliste gegen die Blacklist geprüft:
        // Senderliste geladen, add Black, ConfigDialog, Filter blkBtn
        if (mark) {
            BlackFilterFactory.markStationBlack();
        }
        BlackFilterFactory.getBlackFiltered();
    }

    public SortedList<StationData> getSortedList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return sortedList;
    }

    public FilteredList<StationData> getFilteredList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<StationData>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return filteredList;
    }

    public synchronized void filteredListSetPred(Predicate<StationData> predicate) {
        if (filteredList != null) {
            // beim Start lock-file abfrage todo
            filteredList.setPredicate(predicate);
        }
    }

    public synchronized boolean importStationOnlyWithNr(StationData station) {
        // hier nur beim Laden aus einer fertigen Senderliste mit der GUI
        // die Sender sind schon sortiert, nur die Nummer muss noch ergänzt werden
        station.setStationNo(no++);
        return super.add(station);
    }

    public synchronized int getNextNo() {
        return no++;
    }

    public synchronized int markDoubleStations() {
        // läuft direkt nach dem Laden der Senderliste!
        // doppelte Sender (URL), Geo, InFuture markieren

        P2Duration.counterStart("Sender markieren");
        final HashSet<String> urlHashSet = new HashSet<>(size(), 0.75F);
        try {
            countDouble = 0;
            this.forEach(station -> {
                if (!urlHashSet.add(station.getStationUrl())) {
                    ++countDouble;
                    station.setDoubleUrl(true);
                }
            });

        } catch (Exception ex) {
            P2Log.errorLog(951024789, ex);
        }
        urlHashSet.clear();
        P2Duration.counterStop("Sender markieren");

        return countDouble;
    }

    @Override
    public synchronized void clear() {
        no = 1;
        super.clear();
    }

    public synchronized void sort() {
        Collections.sort(this);
        // und jetzt noch die Nummerierung in Ordnung bringen
        int i = 1;
        for (final StationData station : this) {
            station.setStationNo(i++);
        }
    }

    public synchronized StationData getStationByUrl(final String url) {
        return StationListFactory.getStationByUrl(this, url);
    }

    public synchronized long countNewStations() {
        return stream().filter(StationData::isNewStation).count();
    }

    /**
     * Erstellt StringArrays der Codecs/Länder eines Senders.
     */
    public synchronized void loadFilterLists() {
        P2Duration.counterStart("Filter-Listen suchen");
        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(21);

        //Codec
        hashSet.add(""); //der erste ist ""
        this.forEach((radio) -> {
            String[] codecArr = radio.getCodec().split(",");
            for (String s : codecArr) {
                hashSet.add(s);
            }
        });
        codecs = hashSet.toArray(new String[hashSet.size()]);

        //Länder
        hashSet.clear();
        hashSet.add(""); //der erste ist ""
        stream().forEach((radio) -> hashSet.add(radio.getCountry()));
        countries = hashSet.toArray(new String[hashSet.size()]);

        P2Duration.counterStop("Filter-Listen suchen");
    }
}
