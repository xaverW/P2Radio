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


package de.p2tools.p2radio.controller.config;

import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.p2event.P2EventHandler;
import de.p2tools.p2radio.P2RadioController;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.BlackDataList;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.collection.CollectionList;
import de.p2tools.p2radio.controller.data.favourite.FavouriteList;
import de.p2tools.p2radio.controller.data.filter.FavouriteFilter;
import de.p2tools.p2radio.controller.data.filter.HistoryFilter;
import de.p2tools.p2radio.controller.data.history.HistoryList;
import de.p2tools.p2radio.controller.data.station.StationData;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.stationweb.WebWorker;
import de.p2tools.p2radio.gui.FavouriteGuiPack;
import de.p2tools.p2radio.gui.HistoryGuiPack;
import de.p2tools.p2radio.gui.StationGuiPack;
import de.p2tools.p2radio.gui.dialog.StationInfoDialogController;
import de.p2tools.p2radio.gui.filter.StationFilterControllerClearFilter;
import de.p2tools.p2radio.gui.smallradio.SmallRadioGuiController;
import de.p2tools.p2radio.gui.tools.ProgTray;
import de.p2tools.p2radio.tools.stationlistfilter.StationListFilter;
import de.p2tools.p2radio.tools.storedfilter.FilterWorker;
import de.p2tools.p2radio.tools.storedfilter.StoredFilters;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Worker;
import javafx.stage.Stage;

public class ProgData {

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean duration = false; // Duration ausgeben
    public static boolean reset = false; // Programm auf Starteinstellungen zurücksetzen
    public static boolean firstProgramStart = false; // ist der allererste Programmstart: Init wird gemacht
    public static boolean startMinimized = false; // Minimiert starten
    public static boolean raspberry = false; // läuft auf einem Raspberry

    public static String gui = "";
    public static String small = "";
    public static String dialog = "";

    // Infos
    public static String configDir = ""; // Verzeichnis zum Speichern der Programmeinstellungen
    private static ProgData instance;
    public final ProgTray progTray;

    // zentrale Klassen
    public WebWorker webWorker; // erledigt das laden und updaten der Radioliste
    public PShortCut pShortcut; // verwendete Shortcuts
    public StoredFilters storedFilters; // gespeicherte Filterprofile
    public StationListFilter stationListFilter;

    // Gui
    public Stage primaryStage = null;
    public Stage primaryStageBig = null;
    public Stage primaryStageSmall = null;

    public P2MaskerPane maskerPane = new P2MaskerPane();
    public P2RadioController p2RadioController = null;
    public SmallRadioGuiController smallRadioGuiController = null;

    public StationGuiPack stationGuiPack = null; // Tab mit den Sender
    public FavouriteGuiPack favouriteGuiPack = null; // Tab mit den Favoriten
    public HistoryGuiPack historyGuiPack = null; // Tab mit den Favoriten

    public StationInfoDialogController stationInfoDialogController = null;
    public StationFilterControllerClearFilter stationFilterControllerClearFilter = null;

    // Worker
    public Worker worker; // Liste aller Sender, Themen, ...
    public FilterWorker filterWorker; // Liste aller Sender, Themen, ...

    // Programmdaten
    public static BooleanProperty STATION_TAB_ON = new SimpleBooleanProperty(Boolean.FALSE);
    public static BooleanProperty FAVOURITE_TAB_ON = new SimpleBooleanProperty(Boolean.FALSE);
    public static BooleanProperty HISTORY_TAB_ON = new SimpleBooleanProperty(Boolean.FALSE);

    public StationList stationList; //ist die komplette Senderliste
    public StationList stationListBlackFiltered; //Senderliste nach Blacklist, wie im TabSender angezeigt

    public FavouriteList favouriteList; //Sender die als "Favoriten" geladen werden sollen
    public FilteredList<StationData> filteredFavoriteList;
    public FavouriteFilter favouriteFilter;

    public HistoryList historyList; //Sender die zuletzt gespielt wurden
    public FilteredList<StationData> filteredHistoryList;
    public HistoryFilter historyFilter;

    public StationList ownAutoStartList; // Liste der eigenen Autostarts

    public StationData stationAutoStart = new StationData(AutoStartFactory.TAG_AUTOSTART);
    public StationData stationLastPlayed = new StationData(AutoStartFactory.TAG_LAST_PLAYED);
    public boolean autoStartAfterNewList = false;

    public CollectionList collectionList; //Liste der Sender-Sammlungen
    public BlackDataList blackDataList;
    public SetDataList setDataList;
    public P2EventHandler pEventHandler;

    private ProgData() {
        pEventHandler = new P2EventHandler();

        pShortcut = new PShortCut();
        webWorker = new WebWorker(this);
        storedFilters = new StoredFilters(this);
        stationList = new StationList();
        stationListBlackFiltered = new StationList();

        blackDataList = new BlackDataList(this);
        setDataList = new SetDataList();

        favouriteList = new FavouriteList(this);
        filteredFavoriteList = new FilteredList<>(favouriteList, p -> true);
        favouriteFilter = new FavouriteFilter();

        historyList = new HistoryList(this);
        filteredHistoryList = new FilteredList<>(historyList, p -> true);
        historyFilter = new HistoryFilter();

        ownAutoStartList = new StationList();

        collectionList = new CollectionList(this);
        stationListFilter = new StationListFilter(this);

        filterWorker = new FilterWorker(this);
        progTray = new ProgTray(this);

        //init
        storedFilters.init();
        stationListFilter.init();
    }

    public synchronized static ProgData getInstance(String dir) {
        if (!dir.isEmpty()) {
            configDir = dir;
        }
        return getInstance();
    }

    public synchronized static ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }

    public void initProgData() {
        progTray.initProgTray();
    }
}
