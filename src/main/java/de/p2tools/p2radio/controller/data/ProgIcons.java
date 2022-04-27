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

import de.p2tools.p2Lib.icon.GetIcon;
import de.p2tools.p2radio.controller.config.ProgConfig;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ProgIcons {
    private final static String PATH = "/de/p2tools/p2radio/res/program/";

    public enum Icons {
        ICON_BUTTON_RESET("button-reset.png", 14, 14),
        ICON_BUTTON_EDIT_FILTER("button-edit-filter.png", 16, 16),
        ICON_BUTTON_BACKWARD("button-backward.png", 16, 16),
        ICON_BUTTON_FORWARD("button-forward.png", 16, 16),
        ICON_BUTTON_MENU("button-menu.png", 18, 15),
        ICON_BUTTON_QUIT("button-quit.png", 16, 16),
        ICON_BUTTON_FILE_OPEN("button-file-open.png", 16, 16),
        ICON_BUTTON_PLAY("button-play.png", 16, 16),
        ICON_BUTTON_STOP_PLAY("button-stop-play.png", 16, 16),
        ICON_BUTTON_STOP("button-stop.png", 16, 16),
        ICON_BUTTON_NEXT("button-next.png", 16, 16),
        ICON_BUTTON_PREV("button-prev.png", 16, 16),
        ICON_BUTTON_RANDOM("button-random.png", 16, 16),
        ICON_BUTTON_REMOVE("button-remove.png", 16, 16),
        ICON_BUTTON_ADD("button-add.png", 16, 16),
        ICON_BUTTON_MOVE_DOWN("button-move-down.png", 16, 16),
        ICON_BUTTON_MOVE_UP("button-move-up.png", 16, 16),
        ICON_BUTTON_DOWN("button-down.png", 16, 16),
        ICON_BUTTON_UP("button-up.png", 16, 16),
        ICON_DIALOG_QUIT("dialog-quit.png", 64, 64),
        ICON_FILTER_STATION_LOAD("filter-station-load.png", 22, 22),
        ICON_FILTER_STATION_SAVE("filter-station-save.png", 22, 22),
        ICON_FILTER_STATION_NEW("filter-station-new.png", 22, 22),

        //table
        IMAGE_TABLE_STATION_PLAY("table-station-play.png", 14, 14),
        IMAGE_TABLE_STATION_STOP_PLAY("table-station-stop-play.png", 14, 14),
        IMAGE_TABLE_STATION_SAVE("table-station-save.png", 14, 14),
        IMAGE_TABLE_FAVOURITE_DEL("table-favourite-del.png", 14, 14),
        IMAGE_TABLE_FAVOURITE_GRADE("table-favourite-grade.png", 18, 18),

        //toolBar
        ICON_TOOLBAR_MENU("toolbar-menu.png", 18, 15),
        ICON_TOOLBAR_MENU_TOP("toolbar-menu-top.png", 32, 18),
        ICON_TOOLBAR_SMALL_RADIO_24("toolbar-menu-smallRadio-24.png", 24, 24),
        ICON_TOOLBAR_SMALL_RADIO_20("toolbar-menu-smallRadio-20.png", 20, 20),
        ICON_TOOLBAR_FAVOURITE_CHANGE("toolbar-favourite-change.png", 32, 32),
        ICON_TOOLBAR_FAVOURITE_NEW("toolbar-favourite-new.png", 32, 32),
        ICON_TOOLBAR_FAVOURITE_DEL("toolbar-favourite-del.png", 32, 32),
        ICON_TOOLBAR_STATION_START("toolbar-station-start.png", 32, 32),
        ICON_TOOLBAR_STATION_STOP("toolbar-station-stop.png", 32, 32),
        ICON_TOOLBAR_STATION_REC("toolbar-station-rec.png", 32, 32),
        ICON_TOOLBAR_STATION_RANDOM("toolbar-station-random.png", 32, 32),
        ICON_TOOLBAR_STATION_INFO("toolbar-info.png", 32, 32),

        ICON_DIALOG_EIN_SW("dialog-ein-sw.png", "dialog-ein.png"),
        IMAGE_ACHTUNG_64("achtung_64.png");

        private String fileName;
        private String fileNameDark = "";
        private int w = 0;
        private int h = 0;

        Icons(String fileName, int w, int h) {
            this.fileName = fileName;
            this.w = w;
            this.h = h;
        }

        Icons(String fileName, String fileNameDark, int w, int h) {
            this.fileName = fileName;
            this.fileNameDark = fileNameDark;
            this.w = w;
            this.h = h;
        }

        Icons(String fileName) {
            this.fileName = fileName;
        }

        Icons(String fileName, String fileNameDark) {
            this.fileName = fileName;
            this.fileNameDark = fileNameDark;
        }

        public ImageView getImageView() {
            if (ProgConfig.SYSTEM_DARK_THEME.get() && !fileNameDark.isEmpty()) {
                return GetIcon.getImageView(fileNameDark, PATH, w, h);
            }
            return GetIcon.getImageView(fileName, PATH, w, h);
        }

        public Image getImage() {
            if (ProgConfig.SYSTEM_DARK_THEME.get() && !fileNameDark.isEmpty()) {
                return GetIcon.getImage(fileNameDark, PATH, w, h);
            }
            return GetIcon.getImage(fileName, PATH, w, h);
        }
    }
}
