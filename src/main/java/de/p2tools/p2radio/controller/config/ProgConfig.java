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

import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.tools.PSystemUtils;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.data.SetFactory;
import de.p2tools.p2radio.tools.stationListFilter.StationFilterFactory;
import javafx.beans.property.*;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static ProgConfig instance;
    private static final ArrayList<Config> arrayList = new ArrayList<>();
    public static final String SYSTEM = "system";

    // Programm-Configs, änderbar nur im Config-File
    // ============================================
    // Downloadfehlermeldung wird xx Sedunden lang angezeigt
    public static IntegerProperty SYSTEM_PARAMETER_START_STATION_ERRORMSG_IN_SECOND = addInt("__system-parameter__download-errormsg-in-second_30__", 30);
    // ===========================================


    // Configs der Programmversion
    public static StringProperty SYSTEM_PROG_VERSION = addStr("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStr("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStr("system-prog-build-date");

    // Configs zum Aktualisieren beim Programmupdate
    public static IntegerProperty SYSTEM_UPDATE_STATE = addInt("system-update-state", 0);

    // Configs zur Programmupdatesuche
    public static StringProperty SYSTEM_UPDATE_DATE = addStr("system-update-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_UPDATE_SEARCH = addBool("system-update-search", true); // ob beim Start nach Updates gesucht werden soll
    public static IntegerProperty SYSTEM_UPDATE_INFO_NR_SHOWN = addInt("system-update-info-nr-shown"); // zuletzt angezeigte Info
    public static IntegerProperty SYSTEM_UPDATE_VERSION_SHOWN = addInt("system-update-version-shown"); // zuletzt angezeigte Version
    public static BooleanProperty SYSTEM_UPDATE_BETA_SEARCH = addBool("system-update-beta-search", false);
    public static IntegerProperty SYSTEM_UPDATE_BETA_VERSION_SHOWN = addInt("system-update-beta-version-shown"); // zuletzt angezeigtes Update mit versionNo
    public static IntegerProperty SYSTEM_UPDATE_BETA_BUILD_NO_SHOWN = addInt("system-update-beta-build-nr-shown"); // zuletzt angezeigtes Update mit buildNo
    public static StringProperty SYSTEM_UPDATE_PROGSET_VERSION = addStr("system-update-progset-version");

    // ConfigDialog, Dialog nach Start immer gleich öffnen
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_TAB = new SimpleIntegerProperty(0);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_BLACKLIST = new SimpleIntegerProperty(-1);

    // Configs
    public static BooleanProperty SYSTEM_TRAY = addBool("system-tray", Boolean.TRUE);
    public static StringProperty SYSTEM_USERAGENT = addStr("system-useragent", ProgConst.USER_AGENT_DEFAULT);    // Useragent für direkte Downloads
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStr("system-prog-open-url");
    public static BooleanProperty SYSTEM_MARK_GEO = addBool("system-mark-geo", Boolean.TRUE);
    public static BooleanProperty SYSTEM_STYLE = addBool("system-style", Boolean.FALSE);
    public static IntegerProperty SYSTEM_STYLE_SIZE = addInt("system-geo-home-place", 14);
    public static StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");
    public static BooleanProperty SYSTEM_LOG_ON = addBool("system-log-on", Boolean.TRUE);
    public static BooleanProperty SYSTEM_LOAD_STATION_LIST_EVERY_DAYS = addBool("system-load-station-list-every-days", Boolean.TRUE);
    public static BooleanProperty SYSTEM_SMALL_ROW_TABLE = addBool("system-small-row-table", Boolean.FALSE);
    public static BooleanProperty SYSTEM_DARK_THEME = addBool("system-dark-theme", Boolean.FALSE);
    public static BooleanProperty SYSTEM_THEME_CHANGED = addBool("system-theme-changed");
    public static IntegerProperty SYSTEM_LAST_TAB_STATION = addInt("system-last-tab-station", 0);
    public static StringProperty SYSTEM_LAST_PLAYED = addStr("system-last-played", "");

    // Fenstereinstellungen
    public static StringProperty SYSTEM_SIZE_GUI = addStr("system-size-gui", "1000:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_STATION_INFO = addStr("system-size-dialog-station-info", "600:800");
    public static StringProperty SYSTEM_SIZE_DIALOG_STATION_INFO_SMALL = addStr("system-size-dialog-station-info-small", "600:300");

    // Einstellungen Senderliste
    public static StringProperty SYSTEM_PATH_VLC = addStr("path-vlc", SetFactory.getTemplatePathVlc());

    // Blacklist
    public static IntegerProperty SYSTEM_BLACKLIST_MIN_BITRATE = addInt("blacklist-min-bitrate", 0);
    public static IntegerProperty SYSTEM_BLACKLIST_MAX_BITRATE = addInt("blacklist-max-bitrate", StationFilterFactory.FILTER_BITRATE_MAX);
    public static BooleanProperty SYSTEM_BLACKLIST_IS_WHITELIST = addBool("blacklist-is-whitelist");

    // Download
    public static BooleanProperty DOWNLOAD_BEEP = addBool("download-beep");

    // Gui Station
    public static DoubleProperty STATION_GUI_FILTER_DIVIDER = addDouble("station-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty STATION_GUI_FILTER_DIVIDER_ON = addBool("station-gui-filter-divider-on", Boolean.TRUE);
    public static DoubleProperty STATION_GUI_DIVIDER = addDouble("station-gui-divider", ProgConst.GUI_STATION_DIVIDER_LOCATION);
    public static BooleanProperty STATION_GUI_DIVIDER_ON = addBool("station-gui-divider-on", Boolean.TRUE);
    public static StringProperty STATION_GUI_TABLE_WIDTH = addStr("station-gui-table-width");
    public static StringProperty STATION_GUI_TABLE_SORT = addStr("station-gui-table-sort");
    public static StringProperty STATION_GUI_TABLE_UP_DOWN = addStr("station-gui-table-up-down");
    public static StringProperty STATION_GUI_TABLE_VIS = addStr("station-gui-table-vis");
    public static StringProperty STATION_GUI_TABLE_ORDER = addStr("station-gui-table-order");

    //Gui SmallRadio
    public static StringProperty SMALL_RADIO_SIZE = addStr("small-radio-size");
    public static StringProperty SMALL_RADIO_TABLE_WIDTH = addStr("small-radio-table-width", "50.0,70.0,86.0,183.0,143.0,80.0,180.0,80.0,80.0,80.0,80.0,80.0,80.0,80.0,80.0,80.0");
    public static StringProperty SMALL_RADIO_TABLE_SORT = addStr("small-radio-table-sort");
    public static StringProperty SMALL_RADIO_TABLE_UP_DOWN = addStr("small-radio-table-up-down");
    public static StringProperty SMALL_RADIO_TABLE_VIS = addStr("small-radio-table-vis", "false,false,true,true,true,false,true,false,false,false,true,false,false,false,false,false");
    public static StringProperty SMALL_RADIO_TABLE_ORDER = addStr("small-radio-table-order");

    // Gui Favorite
    public static DoubleProperty FAVOURITE_GUI_FILTER_DIVIDER = addDouble("favourite-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty FAVOURITE_GUI_FILTER_DIVIDER_ON = addBool("favourite-gui-filter-divider-on", Boolean.TRUE);
    public static StringProperty FAVOURITE_DIALOG_EDIT_SIZE = addStr("favourite-dialog-edit-size", "800:800");
    public static StringProperty FAVOURITE_DIALOG_ADD_SIZE = addStr("favourite-dialog-add-size", "800:800");
    public static StringProperty START_STATION_ERROR_DIALOG_SIZE = addStr("start-station-error-dialog-size", "");
    public static DoubleProperty FAVOURITE_GUI_DIVIDER = addDouble("favourite-gui-divider", ProgConst.GUI_FAVOURITE_DIVIDER_LOCATION);
    public static BooleanProperty FAVOURITE_GUI_DIVIDER_ON = addBool("favourite-gui-divider-on", Boolean.TRUE);
    public static StringProperty FAVOURITE_GUI_TABLE_WIDTH = addStr("favourite-gui-table-width");
    public static StringProperty FAVOURITE_GUI_TABLE_SORT = addStr("favourite-gui-table-sort");
    public static StringProperty FAVOURITE_GUI_TABLE_UP_DOWN = addStr("favourite-gui-table-up-down");
    public static StringProperty FAVOURITE_GUI_TABLE_VIS = addStr("favourite-gui-table-vis");
    public static StringProperty FAVOURITE_GUI_TABLE_ORDER = addStr("favourite-gui-table-order");
    public static BooleanProperty FAVOURITE_SHOW_NOTIFICATION = addBool("favourite-show-notification", Boolean.TRUE);

    // Gui LastPlayed
    public static DoubleProperty LAST_PLAYED_GUI_FILTER_DIVIDER = addDouble("last-played-gui-filter-divider", ProgConst.GUI_FILTER_DIVIDER_LOCATION);
    public static BooleanProperty LAST_PLAYED_GUI_FILTER_DIVIDER_ON = addBool("last-played-gui-filter-divider-on", Boolean.TRUE);
    public static StringProperty LAST_PLAYED_DIALOG_EDIT_SIZE = addStr("last-played-dialog-edit-size", "800:800");
    public static StringProperty LAST_PLAYED_DIALOG_ADD_SIZE = addStr("last-played-dialog-add-size", "800:800");
    public static DoubleProperty LAST_PLAYED_GUI_DIVIDER = addDouble("last-played-gui-divider", ProgConst.GUI_FAVOURITE_DIVIDER_LOCATION);
    public static BooleanProperty LAST_PLAYED_GUI_DIVIDER_ON = addBool("last-played-gui-divider-on", Boolean.TRUE);
    public static StringProperty LAST_PLAYED_GUI_TABLE_WIDTH = addStr("last-played-gui-table-width");
    public static StringProperty LAST_PLAYED_GUI_TABLE_SORT = addStr("last-played-gui-table-sort");
    public static StringProperty LAST_PLAYED_GUI_TABLE_UP_DOWN = addStr("last-played-gui-table-up-down");
    public static StringProperty LAST_PLAYED_GUI_TABLE_VIS = addStr("last-played-gui-table-vis");
    public static StringProperty LAST_PLAYED_GUI_TABLE_ORDER = addStr("last-played-gui-table-order");
    public static BooleanProperty LAST_PLAYED_SHOW_NOTIFICATION = addBool("last-played-show-notification", Boolean.TRUE);

    // ConfigDialog
    public static StringProperty CONFIG_DIALOG_SIZE = addStr("config-dialog-size");
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBool("config_dialog-accordion", Boolean.TRUE);
    public static DoubleProperty CONFIG_DIALOG_SET_DIVIDER = addDouble("config-dialog-set-divider", ProgConst.CONFIG_DIALOG_SET_DIVIDER);
    public static StringProperty CONFIG_DIALOG_IMPORT_SET_SIZE = addStr("config-dialog-import-set-size", "800:700");
    public static DoubleProperty CONFIG_DIALOG_SHORTCUT_DIVIDER = addDouble("config-dialog-shortcut-divider", 0.1);

    //StartDialog
    public static StringProperty START_DIALOG_DOWNLOAD_PATH = addStr("start-dialog-download-path", PSystemUtils.getStandardDownloadPath());

    //SenderInfoDialog
    public static BooleanProperty STATION_INFO_DIALOG_SHOW_URL = addBool("station-info-dialog-show-url", Boolean.TRUE);

    //Filter Sender
    public static IntegerProperty FILTER_STATION_SEL_FILTER = addInt("filter-station-sel-filter");

    //Shorcuts Hauptmenü
    public static final String SHORTCUT_QUIT_PROGRAM_INIT = "Ctrl+Q";
    public static StringProperty SHORTCUT_QUIT_PROGRAM = addStr("SHORTCUT_QUIT_PROGRAM", SHORTCUT_QUIT_PROGRAM_INIT);

    //Shortcuts Sendermenü
    public static final String SHORTCUT_PLAY_STATION_INIT = "Ctrl+P";
    public static StringProperty SHORTCUT_PLAY_STATION = addStr("SHORTCUT_PLAY_STATION", SHORTCUT_PLAY_STATION_INIT);
    public static final String SHORTCUT_SAVE_STATIION_INIT = "Ctrl+S";
    public static StringProperty SHORTCUT_SAVE_STATION = addStr("SHORTCUT_SAVE_STATION", SHORTCUT_SAVE_STATIION_INIT);

    // Shortcuts Favoritenmenü
    public static final String SHORTCUT_FAVOURITE_START_INIT = "Ctrl+F";
    public static StringProperty SHORTCUT_FAVOURITE_START = addStr("SHORTCUT_FAVOURITE_START", SHORTCUT_FAVOURITE_START_INIT);
    public static final String SHORTCUT_FAVOURITE_STOP_INIT = "Ctrl+T";
    public static StringProperty SHORTCUT_FAVOURITE_STOP = addStr("SHORTCUT_FAVOURITE_STOP", SHORTCUT_FAVOURITE_STOP_INIT);
    public static final String SHORTCUT_FAVOURITE_CHANGE_INIT = "Ctrl+C";
    public static StringProperty SHORTCUT_FAVOURITE_CHANGE = addStr("SHORTCUT_FAVOURITE_CHANGE", SHORTCUT_FAVOURITE_CHANGE_INIT);

    private ProgConfig() {
        super(arrayList, "ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static void addConfigData(ConfigFile configFile) {
        ProgConfig.SYSTEM_PROG_VERSION.set(ProgramTools.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(ProgramTools.getBuild());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(ProgramTools.getCompileDate());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getConfigsData());
        configFile.addConfigs(ProgData.getInstance().setDataList);
        configFile.addConfigs(ProgData.getInstance().favouriteList);
        configFile.addConfigs(ProgData.getInstance().lastPlayedList);
        configFile.addConfigs(ProgData.getInstance().storedFilters.getActFilterSettings());
        configFile.addConfigs(ProgData.getInstance().storedFilters.getStoredFilterList());
        configFile.addConfigs(ProgData.getInstance().blackDataList);
    }

    public static void getConfigLog(ArrayList<String> list) {
        list.add(PLog.LILNE2);
        list.add("Programmeinstellungen");
        list.add("===========================");
        arrayList.stream().forEach(c -> {
            String s = c.getKey();
            if (s.startsWith("_")) {
                while (s.length() < 55) {
                    s += " ";
                }
            } else {
                while (s.length() < 35) {
                    s += " ";
                }
            }

            list.add(s + "  " + c.getActValueString());
        });
    }

    private static StringProperty addStr(String key) {
        return addStrProp(arrayList, key);
    }

    private static StringProperty addStrC(String comment, String key) {
        return addStrPropC(comment, arrayList, key);
    }

    private static StringProperty addStr(String key, String init) {
        return addStrProp(arrayList, key, init);
    }

    private static StringProperty addStrC(String comment, String key, String init) {
        return addStrPropC(comment, arrayList, key, init);
    }

    private static DoubleProperty addDouble(String key, double init) {
        return addDoubleProp(arrayList, key, init);
    }

    private static DoubleProperty addDoubleC(String comment, String key, double init) {
        return addDoublePropC(comment, arrayList, key, init);
    }

    private static IntegerProperty addInt(String key) {
        return addIntProp(arrayList, key, 0);
    }

    private static IntegerProperty addInt(String key, int init) {
        return addIntProp(arrayList, key, init);
    }

    private static IntegerProperty addIntC(String comment, String key, int init) {
        return addIntPropC(comment, arrayList, key, init);
    }

    private static LongProperty addLong(String key) {
        return addLongProp(arrayList, key, 0);
    }

    private static LongProperty addLong(String key, long init) {
        return addLongProp(arrayList, key, init);
    }

    private static LongProperty addLongC(String comment, String key, long init) {
        return addLongPropC(comment, arrayList, key, init);
    }

    private static BooleanProperty addBool(String key, boolean init) {
        return addBoolProp(arrayList, key, init);
    }

    private static BooleanProperty addBool(String key) {
        return addBoolProp(arrayList, key, Boolean.FALSE);
    }

    private static BooleanProperty addBoolC(String comment, String key, boolean init) {
        return addBoolPropC(comment, arrayList, key, init);
    }
}
