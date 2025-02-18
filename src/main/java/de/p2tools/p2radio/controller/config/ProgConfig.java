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

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.pdata.P2Data;
import de.p2tools.p2lib.configfile.pdata.P2DataProgConfig;
import de.p2tools.p2lib.tools.P2SystemUtils;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import de.p2tools.p2radio.controller.data.AutoStartFactory;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.controller.data.collection.CollectionList;
import de.p2tools.p2radio.controller.data.filter.FilterFactory;
import de.p2tools.p2radio.tools.stationlistfilter.StationFilterFactory;
import javafx.beans.property.*;

public class ProgConfig extends P2DataProgConfig {

    private static ProgConfig instance;

    private ProgConfig() {
        super("ProgConfig");
    }

    public static ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static void addConfigData(ConfigFile configFile) {
        // Configs der Programmversion, nur damit sie (zur Update-Suche) im Config-File stehen
        ProgConfig.SYSTEM_PROG_VERSION.set(P2ToolsFactory.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(P2ToolsFactory.getBuildNo());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(P2ToolsFactory.getBuildDateR());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getInstance());
        configFile.addConfigs(ProgData.getInstance().setDataList);
        configFile.addConfigs(ProgData.getInstance().favouriteList);
        configFile.addConfigs(ProgData.getInstance().historyList);
        configFile.addConfigs(ProgData.getInstance().storedFilters.getActFilterSettings());
        configFile.addConfigs(ProgData.getInstance().storedFilters.getStoredFilterList());
        configFile.addConfigs(ProgData.getInstance().blackDataList);
        configFile.addConfigs(ProgData.getInstance().favouriteFilter);
        configFile.addConfigs(ProgData.getInstance().historyFilter);
        configFile.addConfigs(ProgData.getInstance().stationAutoStart);
        configFile.addConfigs(ProgData.getInstance().stationLastPlayed);
    }

    public static String SHORTCUT_CHANGE_GUI_INIT = "Ctrl+G";
    public static StringProperty SHORTCUT_CHANGE_GUI = addStrProp("SHORTCUT_CHANGE_GUI", SHORTCUT_CHANGE_GUI_INIT);

    public static String SHORTCUT_CENTER_INIT = "Ctrl+W";
    public static StringProperty SHORTCUT_CENTER_GUI = addStrProp("SHORTCUT_CENTER_GUI", SHORTCUT_CENTER_INIT);

    public static String SHORTCUT_MINIMIZE_INIT = "Alt+M";
    public static StringProperty SHORTCUT_MINIMIZE_GUI = addStrProp("SHORTCUT_MINIMIZE_GUI", SHORTCUT_MINIMIZE_INIT);

    //Shorcuts Hauptmenü
    public static final String SHORTCUT_QUIT_PROGRAM_INIT = "Ctrl+Q";
    public static StringProperty SHORTCUT_QUIT_PROGRAM = addStrProp("SHORTCUT_QUIT_PROGRAM", SHORTCUT_QUIT_PROGRAM_INIT);

    //Shortcuts Sendermenü
    public static final String SHORTCUT_PLAY_STATION_INIT = "Ctrl+P";
    public static StringProperty SHORTCUT_PLAY_STATION = addStrProp("SHORTCUT_PLAY_STATION", SHORTCUT_PLAY_STATION_INIT);
    public static final String SHORTCUT_SAVE_STATIION_INIT = "Ctrl+S";
    public static StringProperty SHORTCUT_SAVE_STATION = addStrProp("SHORTCUT_SAVE_STATION", SHORTCUT_SAVE_STATIION_INIT);

    // Shortcuts Favoritenmenü
    public static final String SHORTCUT_FAVOURITE_START_INIT = "Ctrl+F";
    public static StringProperty SHORTCUT_FAVOURITE_START = addStrProp("SHORTCUT_FAVOURITE_START", SHORTCUT_FAVOURITE_START_INIT);
    public static final String SHORTCUT_STOP_STATION_INIT = "Ctrl+T";
    public static StringProperty SHORTCUT_STOP_STATION = addStrProp("SHORTCUT_FAVOURITE_STOP", SHORTCUT_STOP_STATION_INIT);
    public static final String SHORTCUT_FAVOURITE_CHANGE_INIT = "Ctrl+C";
    public static StringProperty SHORTCUT_FAVOURITE_CHANGE = addStrProp("SHORTCUT_FAVOURITE_CHANGE", SHORTCUT_FAVOURITE_CHANGE_INIT);


    // ============================================
    // Downloadfehlermeldung wird xx Sedunden lang angezeigt
    public static IntegerProperty SYSTEM_PARAMETER_START_STATION_ERRORMSG_IN_SECOND = addIntProp("__system-parameter__download-errormsg-in-second_30__", 30);

    // Configs der Programmversion, nur damit sie (zur Update-Suche) im Config-File stehen
    public static StringProperty SYSTEM_PROG_VERSION = addStrProp("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStrProp("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStrProp("system-prog-build-date", P2ToolsFactory.getBuildDateR()); // 2024.08.12

    public static StringProperty SYSTEM_DOWNLOAD_DIR_NEW_VERSION = addStrProp("system-download-dir-new-version", "");

    // Configs zum Aktualisieren beim Programmupdate
    public static BooleanProperty SYSTEM_CHANGE_LOG_DIR = addBoolProp("system-change-log-dir", Boolean.FALSE);
    public static BooleanProperty SYSTEM_CHANGE_TABLE_COLUM = addBoolProp("system-change-table-colum", Boolean.FALSE);
    public static BooleanProperty SYSTEM_RESET_COLOR = addBoolProp("system-reset-color", Boolean.FALSE);

    // Configs zur Programmupdatesuche
    public static StringProperty SYSTEM_SEARCH_UPDATE_TODAY_DONE = addStrProp("system-update-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_ACT = addBoolProp("system-update-search-act", true); //Infos und Programm
    public static StringProperty SYSTEM_SEARCH_UPDATE_LAST_DATE = addStrProp("system-search-update-last-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_SEARCH_UPDATE = addBoolProp("system-search-update" + P2Data.TAGGER + "system-update-search-act", Boolean.TRUE); // nach einem Update suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_BETA = addBoolProp("system-update-search-beta", false); //beta suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_DAILY = addBoolProp("system-update-search-daily", false); //daily suchen

    // ConfigDialog, Dialog nach Start immer gleich öffnen
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_TAB = new SimpleIntegerProperty(0);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_BLACKLIST = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_PLAY = new SimpleIntegerProperty(-1);

    // Configs
    public static BooleanProperty SYSTEM_USE_OWN_PROGRAM_ICON = addBoolProp("system-use-own-program-icon", Boolean.FALSE);
    public static StringProperty SYSTEM_PROGRAM_ICON_PATH = addStrProp("system-program-icon", ""); //ein eigenes Programm-Icon
    public static BooleanProperty SYSTEM_SMALL_RADIO = addBoolProp("system-small-radio", false);
    public static BooleanProperty SYSTEM_SMALL_RADIO_SHOW_START_HELP = addBoolProp("system-small-radio-show-start-help", false);
    public static BooleanProperty SYSTEM_TRAY = addBoolProp("system-tray", Boolean.FALSE);
    public static BooleanProperty SYSTEM_TRAY_USE_OWN_ICON = addBoolProp("system-tray-own-icon", Boolean.FALSE);
    public static StringProperty SYSTEM_TRAY_ICON_PATH = addStrProp("system-tray-icon", ""); //ein eigenes Tray-Icon
    public static StringProperty SYSTEM_USERAGENT = addStrProp("system-useragent", ProgConst.USER_AGENT_DEFAULT); // Useragent für direkte Downloads
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-url");
    public static StringProperty SYSTEM_LOG_DIR = addStrProp("system-log-dir", "");
    public static BooleanProperty SYSTEM_LOG_ON = addBoolProp("system-log-on", Boolean.TRUE);
    public static BooleanProperty SYSTEM_LOAD_STATION_LIST_EVERY_DAYS = addBoolProp("system-load-station-list-every-days", Boolean.TRUE);
    public static BooleanProperty SYSTEM_SMALL_ROW_TABLE = addBoolProp("system-small-row-table", Boolean.FALSE);
    public static IntegerProperty SYSTEM_LAST_TAB_STATION = addIntProp("system-last-tab-station", 0);
    public static StringProperty SYSTEM_HISTORY = addStrProp("system-history", "");
    public static IntegerProperty SYSTEM_AUTO_START = addIntProp("system-auto-start", AutoStartFactory.AUTOSTART_NOTHING);

    public static IntegerProperty SYSTEM_FONT_SIZE = addIntProp("system-font-size", 0);
    public static BooleanProperty SYSTEM_FONT_SIZE_CHANGE = addBoolProp("system-font-size-change", Boolean.FALSE); // für die Schriftgröße
    public static BooleanProperty SYSTEM_DARK_THEME = addBoolProp("system-dark-theme", Boolean.FALSE);
    public static BooleanProperty SYSTEM_BLACK_WHITE_ICON = addBoolProp("system-black-white-icon", Boolean.FALSE);
    public static BooleanProperty SYSTEM_THEME_CHANGED = addBoolProp("system-theme-changed");
    public static BooleanProperty SYSTEM_DARK_THEME_START = addBoolProp("system-dark-theme-start", Boolean.FALSE);
    public static BooleanProperty SYSTEM_BLACK_WHITE_ICON_START = addBoolProp("system-black-white-icon-start", Boolean.FALSE);

    // Fenstereinstellungen
    public static StringProperty SYSTEM_SIZE_GUI = addStrProp("system-size-gui", "1000:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_STATION_INFO = addStrProp("system-size-dialog-station-info", "500:500");

    // Einstellungen Senderliste
    public static StringProperty SYSTEM_PATH_VLC = addStrProp("path-vlc", SetFactory.getTemplatePathVlc());

    // Blacklist
    public static IntegerProperty SYSTEM_BLACKLIST_MIN_BITRATE = addIntProp("blacklist-min-bitrate", 0);
    public static IntegerProperty SYSTEM_BLACKLIST_MAX_BITRATE = addIntProp("blacklist-max-bitrate", StationFilterFactory.FILTER_BITRATE_MAX);
    public static BooleanProperty SYSTEM_BLACKLIST_IS_WHITELIST = addBoolProp("blacklist-is-whitelist");

    // Gui Station
    public static StringProperty STATION_GUI_TABLE_WIDTH = addStrProp("station-gui-table-width");
    public static StringProperty STATION_GUI_TABLE_SORT = addStrProp("station-gui-table-sort");
    public static StringProperty STATION_GUI_TABLE_UP_DOWN = addStrProp("station-gui-table-up-down");
    public static StringProperty STATION_GUI_TABLE_VIS = addStrProp("station-gui-table-vis");
    public static StringProperty STATION_GUI_TABLE_ORDER = addStrProp("station-gui-table-order");

    //Gui SmallRadio
    public static StringProperty SMALL_RADIO_SIZE = addStrProp("small-radio-size", "600:400");

    public static StringProperty SMALL_RADIO_TABLE_STATION_WIDTH = addStrProp("small-radio-table-station-width", "");
    public static StringProperty SMALL_RADIO_TABLE_STATION_SORT = addStrProp("small-radio-table-station-sort");
    public static StringProperty SMALL_RADIO_TABLE_STATION_UP_DOWN = addStrProp("small-radio-table-station-up-down");
    public static StringProperty SMALL_RADIO_TABLE_STATION_VIS = addStrProp("small-radio-table-station-vis", "");
    public static StringProperty SMALL_RADIO_TABLE_STATION_ORDER = addStrProp("small-radio-table-station-order", "");

    public static StringProperty SMALL_RADIO_TABLE_FAVOURITE_WIDTH = addStrProp("small-radio-table-favourite-width", "");
    public static StringProperty SMALL_RADIO_TABLE_FAVOURITE_SORT = addStrProp("small-radio-table-favourite-sort");
    public static StringProperty SMALL_RADIO_TABLE_FAVOURITE_UP_DOWN = addStrProp("small-radio-table-favourite-up-down");
    public static StringProperty SMALL_RADIO_TABLE_FAVOURITE_VIS = addStrProp("small-radio-table-favourite-vis", "");
    public static StringProperty SMALL_RADIO_TABLE_FAVOURITE_ORDER = addStrProp("small-radio-table-favourite-order", "");

    public static StringProperty SMALL_RADIO_TABLE_HISTORY_WIDTH = addStrProp("small-radio-table-history-width", "");
    public static StringProperty SMALL_RADIO_TABLE_HISTORY_SORT = addStrProp("small-radio-table-history-sort");
    public static StringProperty SMALL_RADIO_TABLE_HISTORY_UP_DOWN = addStrProp("small-radio-table-history-up-down");
    public static StringProperty SMALL_RADIO_TABLE_HISTORY_VIS = addStrProp("small-radio-table-history-vis", "");
    public static StringProperty SMALL_RADIO_TABLE_HISTORY_ORDER = addStrProp("small-radio-table-history-order", "");

    public static StringProperty SMALL_RADIO_SELECTED_STATION_GENRE = addStrProp("small-radio-selected-station-genre");
    public static StringProperty SMALL_RADIO_SELECTED_FAVOURITE_GENRE = addStrProp("small-radio-selected-favourite-genre");
    public static StringProperty SMALL_RADIO_SELECTED_HISTORY_GENRE = addStrProp("small-radio-selected-history-genre");
    public static StringProperty SMALL_RADIO_SELECTED_COLLECTION_NAME = addStrProp("small-radio-selected-collection-name", CollectionList.COLLECTION_ALL);
    public static StringProperty SMALL_RADIO_SELECTED_LIST = addStrProp("small-radio-selected-list", FilterFactory.LIST_STATION);

    // Gui Favorite
    public static StringProperty FAVOURITE_DIALOG_EDIT_SIZE = addStrProp("favourite-dialog-edit-size", "800:800");
    public static StringProperty START_STATION_ERROR_DIALOG_SIZE = addStrProp("start-station-error-dialog-size", "");
    public static StringProperty FAVOURITE_DIALOG_ADD_MORE_SIZE = addStrProp("favourite-dialog-add-more-size", "800:850");
    public static StringProperty FAVOURITE_DIALOG_ADD_SIZE = addStrProp("favourite-dialog-add-size", "800:800");

    public static StringProperty FAVOURITE_GUI_TABLE_WIDTH = addStrProp("favourite-gui-table-width");
    public static StringProperty FAVOURITE_GUI_TABLE_SORT = addStrProp("favourite-gui-table-sort");
    public static StringProperty FAVOURITE_GUI_TABLE_UP_DOWN = addStrProp("favourite-gui-table-up-down");
    public static StringProperty FAVOURITE_GUI_TABLE_VIS = addStrProp("favourite-gui-table-vis");
    public static StringProperty FAVOURITE_GUI_TABLE_ORDER = addStrProp("favourite-gui-table-order");

    public static BooleanProperty FAVOURITE_SHOW_NOTIFICATION = addBoolProp("favourite-show-notification", Boolean.TRUE);

    // Gui History
    public static StringProperty HISTORY_GUI_TABLE_WIDTH = addStrProp("history-gui-table-width");
    public static StringProperty HISTORY_GUI_TABLE_SORT = addStrProp("history-gui-table-sort");
    public static StringProperty HISTORY_GUI_TABLE_UP_DOWN = addStrProp("history-gui-table-up-down");
    public static StringProperty HISTORY_GUI_TABLE_VIS = addStrProp("history-gui-table-vis");
    public static StringProperty HISTORY_GUI_TABLE_ORDER = addStrProp("history-gui-table-order");


    // HISTORY Info
    public static BooleanProperty HISTORY__INFO_IS_SHOWING = addBoolProp("history--info-is-showing", Boolean.TRUE);
    public static BooleanProperty HISTORY__INFO_PANE_IS_RIP = addBoolProp("history--info-pane-is-rip", Boolean.FALSE);
    public static StringProperty HISTORY__INFO_DIALOG_SIZE = addStrProp("history--info-dialog-size", "400:400");
    public static DoubleProperty HISTORY__INFO_DIVIDER = addDoubleProp("history--info-divider", ProgConst.GUI_INFO_DIVIDER_LOCATION);

    public static DoubleProperty HISTORY__FILTER_DIVIDER = addDoubleProp("history--filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty HISTORY__FILTER_IS_SHOWING = addBoolProp("history--filter-is-showing", Boolean.TRUE);
    public static BooleanProperty HISTORY__FILTER_IS_RIP = addBoolProp("history--filter-is-rip", Boolean.FALSE);
    public static StringProperty HISTORY__FILTER_DIALOG_SIZE = addStrProp("history--filter-dialog-size", "400:600");


    // FAVOURITE Info
    public static BooleanProperty FAVOURITE__INFO_IS_SHOWING = addBoolProp("favourite--info-is-showing", Boolean.TRUE);
    public static BooleanProperty FAVOURITE__INFO_PANE_IS_RIP = addBoolProp("favourite--info-pane-is-rip", Boolean.FALSE);
    public static StringProperty FAVOURITE__INFO_DIALOG_SIZE = addStrProp("favourite--info-dialog-size", "400:400");
    public static DoubleProperty FAVOURITE__INFO_DIVIDER = addDoubleProp("favourite--info-divider", ProgConst.GUI_INFO_DIVIDER_LOCATION);

    public static DoubleProperty FAVOURITE__FILTER_DIVIDER = addDoubleProp("favourite--filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty FAVOURITE__FILTER_IS_SHOWING = addBoolProp("favourite--filter-is-showing", Boolean.TRUE);
    public static BooleanProperty FAVOURITE__FILTER_IS_RIP = addBoolProp("favourite--filter-is-rip", Boolean.FALSE);
    public static StringProperty FAVOURITE__FILTER_DIALOG_SIZE = addStrProp("favourite--filter-dialog-size", "800:800");


    // STATION Info
    public static BooleanProperty STATION__INFO_IS_SHOWING = addBoolProp("station--info-is-showing", Boolean.TRUE);
    public static BooleanProperty STATION__INFO_PANE_IS_RIP = addBoolProp("station--info-pane-is-rip", Boolean.FALSE);
    public static StringProperty STATION__INFO_DIALOG_SIZE = addStrProp("station--info-dialog-size", "400:400");
    public static DoubleProperty STATION__INFO_DIVIDER = addDoubleProp("station--info-divider", ProgConst.GUI_INFO_DIVIDER_LOCATION);

    public static DoubleProperty STATION__FILTER_DIVIDER = addDoubleProp("station--filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty STATION__FILTER_IS_SHOWING = addBoolProp("station--filter-is-showing", Boolean.TRUE);
    public static BooleanProperty STATION__FILTER_IS_RIP = addBoolProp("station--filter-is-rip", Boolean.FALSE);
    public static StringProperty STATION__FILTER_DIALOG_SIZE = addStrProp("station--filter-dialog-size", "400:600");


    // ConfigDialog
    public static StringProperty CONFIG_DIALOG_SIZE = addStrProp("config-dialog-size", "600:500");
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBoolProp("config_dialog-accordion", Boolean.TRUE);
    public static DoubleProperty CONFIG_DIALOG_SET_DIVIDER = addDoubleProp("config-dialog-set-divider", ProgConst.CONFIG_DIALOG_SET_DIVIDER);
    public static StringProperty CONFIG_DIALOG_IMPORT_SET_SIZE = addStrProp("config-dialog-import-set-size", "600:400");
    public static DoubleProperty CONFIG_DIALOG_SHORTCUT_DIVIDER = addDoubleProp("config-dialog-shortcut-divider", 0.1);

    //StartDialog
    public static StringProperty START_DIALOG_DOWNLOAD_PATH = addStrProp("start-dialog-download-path", P2SystemUtils.getStandardDownloadPath());

    //Filter Sender
    public static IntegerProperty FILTER_STATION_SEL_FILTER = addIntProp("filter-station-sel-filter");
}
