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


package de.p2tools.p2radio.controller.data;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.icons.P2Icon;
import de.p2tools.p2radio.P2RadioController;
import de.p2tools.p2radio.controller.config.ProgConst;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProgIconsP2Radio {
    public static String ICON_PATH = "res/program/";
    public static String ICON_PATH_LONG = "de/p2tools/p2radio/res/program/";

    private static final List<P2IconInfo> iconList = new ArrayList<>();

    public static P2IconInfo ICON_BUTTON_RESET = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-reset.png", 14, 14);
    public static P2IconInfo ICON_BUTTON_EDIT_FILTER = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-edit-filter.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_BACKWARD = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-backward.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_CLEAN = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "clean_16.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_FORWARD = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-forward.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_MENU = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-menu.png", 18, 15);
    public static P2IconInfo ICON_BUTTON_QUIT = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-quit.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_FILE_OPEN = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-file-open.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_PLAY = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-play.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_STOP_PLAY = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-stop-play.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_STOP = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-stop.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_NEXT = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-next.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_PREV = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-prev.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_RANDOM = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-random.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_REMOVE = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-remove.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_ADD = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-add.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_MOVE_DOWN = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-move-down.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_MOVE_UP = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-move-up.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_DOWN = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-down.png", 16, 16);
    public static P2IconInfo ICON_BUTTON_UP = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-up.png", 16, 16);
    public static P2IconInfo ICON_DIALOG_QUIT = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "dialog-quit.png", 64, 64);
    public static P2IconInfo ICON_FILTER_STATION_LOAD = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "filter-station-load.png", 22, 22);
    public static P2IconInfo ICON_FILTER_STATION_SAVE = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "filter-station-save.png", 22, 22);
    public static P2IconInfo ICON_FILTER_STATION_NEW = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "filter-station-new.png", 22, 22);

    //table
    public static P2IconInfo IMAGE_TABLE_STATION_PLAY = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "table-station-play.png", 14, 14);
    public static P2IconInfo IMAGE_TABLE_STATION_STOP_PLAY = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "table-station-stop-play.png", 14, 14);
    public static P2IconInfo IMAGE_TABLE_STATION_SAVE = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "table-station-save.png", 14, 14);
    public static P2IconInfo IMAGE_TABLE_FAVOURITE_DEL = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "table-favourite-del.png", 14, 14);
    public static P2IconInfo IMAGE_TABLE_FAVOURITE_GRADE = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "table-favourite-grade.png", 18, 18);

    //toolBar
    public static P2IconInfo ICON_TOOLBAR_MENU = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-menu.png", 18, 15);
    public static P2IconInfo ICON_TOOLBAR_MENU_TOP = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-top.png", 32, 18);
    public static P2IconInfo ICON_TOOLBAR_SMALL_RADIO_24 = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-smallRadio-24.png", 24, 24);
    public static P2IconInfo ICON_TOOLBAR_SMALL_RADIO_20 = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-menu-smallRadio-20.png", 20, 20);
    public static P2IconInfo ICON_TOOLBAR_FAVOURITE_CHANGE = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-favourite-change.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_FAVOURITE_NEW = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-favourite-new.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_FAVOURITE_DEL = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-favourite-del.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_STATION_START = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-station-start.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_STATION_STOP = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-station-stop.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_STATION_REC = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-station-rec.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_STATION_RANDOM = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-station-random.png", 26, 26);
    public static P2IconInfo ICON_TOOLBAR_STATION_INFO = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "toolbar-info.png", 26, 26);


    public static P2IconInfo ICON_DIALOG_EIN_SW = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "dialog-ein-sw.png", 16, 16);
    public static P2IconInfo ICON_DIALOG_AUS_SW = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "dialog-aus-sw.png", 18, 18);
    // public static P2IconInfo ICON_DIALOG_EIN = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "dialog-ein.png", 16, 16);
    // public static P2IconInfo ICON_DIALOG_AUS = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "button-reset.png", 14, 14);
    public static P2IconInfo IMAGE_ACHTUNG_64 = new P2IconInfo(ICON_PATH_LONG, ICON_PATH, "achtung_64.png", 64, 64);

    public static void initIcons() {
        iconList.forEach(p -> {
            String url = p.genUrl(P2IconInfo.class, P2RadioController.class, ProgConst.class, ProgIconsP2Radio.class, P2LibConst.class);
            if (url.isEmpty()) {
                // dann wurde keine gefunden
                System.out.println("ProgIconsInfo: keine URL, icon: " + p.getPathFileNameDark() + " - " + p.getFileName());
            }
        });
    }

    public static class P2IconInfo extends P2Icon {
        public P2IconInfo(String longPath, String path, String fileName, int w, int h) {
            super(longPath, path, fileName, w, h);
            iconList.add(this);
        }

        public boolean searchUrl(String p, Class<?>... clazzAr) {
            URL url;
            url = P2RadioController.class.getResource(p);
            if (set(url, p, "P2RadioController.class.getResource")) return true;
            url = ProgConst.class.getResource(p);
            if (set(url, p, "ProgConst.class.getResource")) return true;
            url = ProgIconsP2Radio.class.getResource(p);
            if (set(url, p, "ProgIconsP2Radio.class.getResource")) return true;
            url = this.getClass().getResource(p);
            if (set(url, p, "this.getClass().getResource")) return true;

            url = ClassLoader.getSystemResource(p);
            if (set(url, p, "ClassLoader.getSystemResource")) return true;
            url = P2LibConst.class.getClassLoader().getResource(p);
            if (set(url, p, "P2LibConst.class.getClassLoader().getResource")) return true;
            url = ProgConst.class.getClassLoader().getResource(p);
            if (set(url, p, "ProgConst.class.getClassLoader().getResource")) return true;
            url = this.getClass().getClassLoader().getResource(p);
            if (set(url, p, "this.getClass().getClassLoader().getResource")) return true;

            return false;
        }
    }
}
