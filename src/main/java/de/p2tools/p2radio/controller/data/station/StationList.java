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

import de.p2tools.p2Lib.configFile.pData.PDataListMeta;
import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.tools.stationListFilter.BlackFilterFactory;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SuppressWarnings("serial")
public class StationList extends SimpleListProperty<Favourite> implements PDataListMeta<Favourite> {

    public static final String TAG = "StationList";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
    //    public String[] genres = {""};
    private final StationListMeta meta = new StationListMeta();
    public int nr = 1;
    public String[] codecs = {""};
    public String[] countries = {""};
    int countDouble = 0;
    private FilteredList<Favourite> filteredList = null;
    private SortedList<Favourite> sortedList = null;

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
    public Favourite getNewItem() {
        return new Favourite();
    }

    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(Favourite.class)) {
//            ((Favourite) obj).init(); // damit wird auch das Datum! gesetzt
            importStationOnlyWithNr(((Favourite) obj));
        }
    }

    @Override
    public StationListMeta getMeta() {
        return meta;
    }

    public synchronized String getGenDate() {
        return meta.stationDate.getValue().getPDate().get_dd_MM_yyyy();
    }

    public synchronized void setGenDateNow() {
        meta.stationDate.getValue().setPLocalDateNow();
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
        final LocalDate now = LocalDate.now();
        final PLocalDate stationDate = meta.stationDate.getValue();
        if (stationDate != null) {
            long diff = new Date().getTime() - stationDate.getPDate().getTime();
            days = (int) TimeUnit.MILLISECONDS.toDays(diff);
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

    public SortedList<Favourite> getSortedList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return sortedList;
    }

    public FilteredList<Favourite> getFilteredList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<Favourite>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return filteredList;
    }

    public synchronized void filteredListSetPred(Predicate<Favourite> predicate) {
        filteredList.setPredicate(predicate);
    }

    public synchronized boolean importStationOnlyWithNr(Favourite station) {
        // hier nur beim Laden aus einer fertigen Senderliste mit der GUI
        // die Sender sind schon sortiert, nur die Nummer muss noch ergänzt werden
        station.setStationNo(nr++);
        return super.add(station);
    }

    public synchronized int markStations() {
        // läuft direkt nach dem Laden der Senderliste!
        // doppelte Sender (URL), Geo, InFuture markieren

        PDuration.counterStart("Sender markieren");
        final HashSet<String> urlHashSet = new HashSet<>(size(), 0.75F);
        try {
            countDouble = 0;
            this.stream().forEach(station -> {
                if (!urlHashSet.add(station.getStationUrl())) {
                    ++countDouble;
                    station.setDoubleUrl(true);
                }
            });

        } catch (Exception ex) {
            PLog.errorLog(951024789, ex);
        }
        urlHashSet.clear();
        PDuration.counterStop("Sender markieren");

        return countDouble;
    }

//    private boolean addInit(Favourite station) {
////        station.init();
//        return add(station);
//    }

    @Override
    public synchronized void clear() {
        nr = 1;
        super.clear();
    }

    public synchronized void sort() {
        Collections.sort(this);
        // und jetzt noch die Nummerierung in Ordnung bringen
        int i = 1;
        for (final Favourite station : this) {
            station.setStationNo(i++);
        }
    }

    public synchronized Favourite getSenderByUrl(final String url) {
        final Optional<Favourite> opt =
                parallelStream().filter(station -> station.getStationUrl().equalsIgnoreCase(url)).findAny();
        return opt.orElse(null);
    }

    public synchronized long countNewStations() {
        return stream().filter(Favourite::isNewStation).count();
    }

    /**
     * Erstellt StringArrays der Codecs/Länder eines Senders.
     */
    public synchronized void loadFilterLists() {
        PDuration.counterStart("Filter-Listen suchen");
        final LinkedHashSet<String> hashSet = new LinkedHashSet<>(21);

        //Codec
        hashSet.add(""); //der erste ist ""
        stream().forEach((radio) -> {
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

        PDuration.counterStop("Filter-Listen suchen");
    }

    public synchronized int countStartedAndRunningFavourites() {
        //es wird nach gestarteten und laufenden Stationen gesucht
        int ret = 0;
        for (final Favourite station : this) {
            if (station.getStart() != null &&
                    (station.getStart().getStartStatus().isStarted() || station.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }

}
