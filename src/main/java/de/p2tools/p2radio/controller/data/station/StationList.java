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
public class StationList extends SimpleListProperty<Station> implements PDataListMeta<Station> {

    {
        sdfUtc.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
    }

    public static final String TAG = "StationList";

    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy, HH:mm";
    private static final SimpleDateFormat sdfUtc = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

    public int nr = 1;
    public String[] codecs = {""};
    public String[] countries = {""};
    //    public String[] genres = {""};
    private StationListMeta meta = new StationListMeta();

    private FilteredList<Station> filteredList = null;
    private SortedList<Station> sortedList = null;

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
    public Station getNewItem() {
        return new Station();
    }


    @Override
    public void addNewItem(Object obj) {
        if (obj.getClass().equals(Station.class)) {
            ((Station) obj).init(); // damit wird auch das Datum! gesetzt
            importStationOnlyWithNr(((Station) obj));
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
        if (getAge() > ProgConst.LOAD_STATION_LIST_EVERY_DAYS) {
            return true;
        } else {
            return false;
        }
    }

    public void triggerFilter() {
//        filteredList.setPredicate(filteredList.getPredicate());
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

    public SortedList<Station> getSortedList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return sortedList;
    }

    public FilteredList<Station> getFilteredList() {
        if (sortedList == null || filteredList == null) {
            filteredList = new FilteredList<Station>(this, p -> true);
            sortedList = new SortedList<>(filteredList);
        }
        return filteredList;
    }

    public synchronized void filteredListSetPred(Predicate<Station> predicate) {
        filteredList.setPredicate(predicate);
    }

    public synchronized boolean importStationOnlyWithNr(Station station) {
        // hier nur beim Laden aus einer fertigen Senderliste mit der GUI
        // die Sender sind schon sortiert, nur die Nummer muss noch ergänzt werden
        station.no = nr++;
        return super.add(station);
    }

    int countDouble = 0;

    public synchronized int markStations() {
        // läuft direkt nach dem Laden der Senderliste!
        // doppelte Sender (URL), Geo, InFuture markieren

        final HashSet<String> urlHashSet = new HashSet<>(size(), 0.75F);

        // todo exception parallel?? Unterschied ~10ms (bei Gesamt: 110ms)
        PDuration.counterStart("Sender markieren");
        try {
            countDouble = 0;
            this.stream().forEach(station -> {
                if (!urlHashSet.add(station.getUrl())) {
                    ++countDouble;
                    station.setDoubleUrl(true);
                }
            });

        } catch (Exception ex) {
            PLog.errorLog(951024789, ex);
        }
        PDuration.counterStop("Sender markieren");

        urlHashSet.clear();
        return countDouble;
    }

    private boolean addInit(Station station) {
        station.init();
        return add(station);
    }

    @Override
    public synchronized void clear() {
        nr = 1;
        super.clear();
    }

    public synchronized void sort() {
        Collections.sort(this);
        // und jetzt noch die Nummerierung in Ordnung bringen
        int i = 1;
        for (final Station station : this) {
            station.no = i++;
        }
    }

    public synchronized Station getSenderByUrl(final String url) {
        final Optional<Station> opt =
                parallelStream().filter(station -> station.getUrl().equalsIgnoreCase(url)).findAny();
        return opt.orElse(null);
    }

    public synchronized long countNewStations() {
        return stream().filter(Station::isNewStation).count();
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

        //Genres, sinnlos, gibt zu viel Unsinn darin
//        hashSet.add(""); //der erste ist ""
//        stream().forEach((radio) -> {
//            String[] genreArr = radio.getGenre().split(",");
//            for (String s : genreArr) {
//                hashSet.add(s);
//            }
//        });
//        genres = hashSet.toArray(new String[hashSet.size()]);

        PDuration.counterStop("Filter-Listen suchen");
    }

    public synchronized int countStartedAndRunningFavourites() {
        //es wird nach gestarteten und laufenden Stationen gesucht
        int ret = 0;
        for (final Station station : this) {
            if (station.getStart() != null &&
                    (station.getStart().getStartStatus().isStarted() || station.getStart().getStartStatus().isStateStartedRun())) {
                ++ret;
            }
        }
        return ret;
    }

}
