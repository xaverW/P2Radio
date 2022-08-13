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

import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PEventHandler;
import de.p2tools.p2radio.P2RadioController;
import de.p2tools.p2radio.controller.data.BlackDataList;
import de.p2tools.p2radio.controller.data.P2RadioShortCuts;
import de.p2tools.p2radio.controller.data.SetDataList;
import de.p2tools.p2radio.controller.data.collection.CollectionList;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFilter;
import de.p2tools.p2radio.controller.data.favourite.FavouriteList;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayed;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedFilter;
import de.p2tools.p2radio.controller.data.lastPlayed.LastPlayedList;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationList;
import de.p2tools.p2radio.controller.getNewStationList.LoadNewStationList;
import de.p2tools.p2radio.controller.worker.FavouriteInfos;
import de.p2tools.p2radio.controller.worker.StationInfos;
import de.p2tools.p2radio.controller.worker.Worker;
import de.p2tools.p2radio.gui.FavouriteGuiController;
import de.p2tools.p2radio.gui.LastPlayedGuiController;
import de.p2tools.p2radio.gui.StationGuiController;
import de.p2tools.p2radio.gui.dialog.StationInfoDialogController;
import de.p2tools.p2radio.gui.filter.StationFilterControllerClearFilter;
import de.p2tools.p2radio.gui.smallRadio.SmallRadioGuiController;
import de.p2tools.p2radio.gui.tools.ProgTray;
import de.p2tools.p2radio.tools.stationListFilter.StationListFilter;
import de.p2tools.p2radio.tools.storedFilter.FilterWorker;
import de.p2tools.p2radio.tools.storedFilter.StoredFilters;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgData {

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean duration = false; // Duration ausgeben
    public static boolean reset = false; // Programm auf Starteinstellungen zurücksetzen
    // Infos
    public static String configDir = ""; // Verzeichnis zum Speichern der Programmeinstellungen
    private static ProgData instance;
    public final ProgTray progTray;
    // zentrale Klassen
    public LoadNewStationList loadNewStationList; // erledigt das laden und updaten der Radioliste
    public P2RadioShortCuts pShortcut; // verwendete Shortcuts
    public StoredFilters storedFilters; // gespeicherte Filterprofile
    public StationListFilter stationListFilter;
    // Gui
    public Stage primaryStage = null;
    public PMaskerPane maskerPane = null;
    public P2RadioController p2RadioController = null;
    public StationGuiController stationGuiController = null; // Tab mit den Sender
    public FavouriteGuiController favouriteGuiController = null; // Tab mit den Favoriten
    public SmallRadioGuiController smallRadioGuiController = null; // Tab mit den Favoriten
    public LastPlayedGuiController lastPlayedGuiController = null; // Tab mit den Favoriten
    public StationInfoDialogController stationInfoDialogController = null;
    public StationFilterControllerClearFilter stationFilterControllerClearFilter = null;
    // Worker
    public Worker worker; // Liste aller Sender, Themen, ...
    public FilterWorker filterWorker; // Liste aller Sender, Themen, ...
    public FavouriteInfos favouriteInfos;
    public StationInfos stationInfos;
    public StartFactory startFactory;

    // Programmdaten
    public StationList stationList; //ist die komplette Senderliste
    public StationList stationListBlackFiltered; //Senderliste nach Blacklist, wie im TabSender angezeigt

    public FavouriteList favouriteList; //Sender die als "Favoriten" geladen werden sollen
    public FilteredList<Favourite> filteredFavourites;
    public FavouriteFilter favouriteFilter;

    public LastPlayedList lastPlayedList; //Sender die zuletzt gespielt wurden
    public FilteredList<LastPlayed> filteredLastPlayedList;
    public LastPlayedFilter lastPlayedFilter;

    public CollectionList collectionList; //Liste der Sender-Sammlungen
    public BlackDataList blackDataList;
    public SetDataList setDataList;
    public PEventHandler pEventHandler;
    boolean oneSecond = false;

    private ProgData() {
        pEventHandler = new PEventHandler();

        pShortcut = new P2RadioShortCuts();
//        eventNotifyLoadRadioList = new EventNotifyLoadRadioList();
        loadNewStationList = new LoadNewStationList(this);
        storedFilters = new StoredFilters(this);
        storedFilters.init();
        stationList = new StationList();
        stationListBlackFiltered = new StationList();

        blackDataList = new BlackDataList(this);
        setDataList = new SetDataList();

        favouriteList = new FavouriteList(this);
        filteredFavourites = new FilteredList<>(favouriteList, p -> true);
        favouriteFilter = new FavouriteFilter();

        lastPlayedList = new LastPlayedList(this);
        filteredLastPlayedList = new FilteredList<>(lastPlayedList, p -> true);
        lastPlayedFilter = new LastPlayedFilter();

        collectionList = new CollectionList(this);
        stationListFilter = new StationListFilter(this);

        worker = new Worker(this);
        filterWorker = new FilterWorker(this);
        favouriteInfos = new FavouriteInfos(this);
        stationInfos = new StationInfos(this);
        startFactory = new StartFactory(this);
        progTray = new ProgTray(this);
    }

    public synchronized static final ProgData getInstance(String dir) {
        if (!dir.isEmpty()) {
            configDir = dir;
        }
        return getInstance();
    }

    public synchronized static final ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }

    public void initProgData() {
        startTimer();
        progTray.initProgTray();
    }

    private void startTimer() {
        // extra starten, damit er im Einrichtungsdialog nicht dazwischen funkt
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ae -> {
            oneSecond = !oneSecond;
            if (oneSecond) {
                doTimerWorkOneSecond();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
        PDuration.onlyPing("Timer gestartet");
    }

    private void doTimerWorkOneSecond() {
        pEventHandler.notifyListener(new PEvent(Events.TIMER));
//        Listener.notify(Listener.EVENT_TIMER, ProgData.class.getName());
    }
}
