/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.res.GetIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProgIcons {

    public final Image ICON_DIALOG_EIN_SW = GetIcon.getImage(ProgConfig.SYSTEM_DARK_THEME.get() ? "dialog-ein.png" : "dialog-ein-sw.png");
    public final Image IMAGE_ACHTUNG_64 = GetIcon.getImage("achtung_64.png");


    public final ImageView ICON_BUTTON_RESET = GetIcon.getImageView("button-reset.png", 14, 14);
    public final ImageView ICON_BUTTON_EDIT_FILTER = GetIcon.getImageView("button-edit-filter.png", 16, 16);
    public final ImageView ICON_BUTTON_BACKWARD = GetIcon.getImageView("button-backward.png", 16, 16);
    public final ImageView ICON_BUTTON_FORWARD = GetIcon.getImageView("button-forward.png", 16, 16);
    public final ImageView ICON_BUTTON_MENU = GetIcon.getImageView("button-menu.png", 18, 15);
    public final ImageView ICON_BUTTON_QUIT = GetIcon.getImageView("button-quit.png", 16, 16);
    public final ImageView ICON_BUTTON_FILE_OPEN = GetIcon.getImageView("button-file-open.png", 16, 16);
    public final ImageView ICON_DIALOG_QUIT = GetIcon.getImageView("dialog-quit.png", 64, 64);


    // table
    public static final Image IMAGE_TABLE_STATION_PLAY = GetIcon.getImage("table-station-play.png", 14, 14);
    public static final Image IMAGE_TABLE_STATION_STOP_PLAY = GetIcon.getImage("table-station-stop-play.png", 14, 14);
    public static final Image IMAGE_TABLE_STATION_SAVE = GetIcon.getImage("table-station-save.png", 14, 14);
    public static final Image IMAGE_TABLE_FAVOURITE_START = GetIcon.getImage("table-favourite-start.png", 14, 14);
    public static final Image IMAGE_TABLE_FAVOURITE_DEL = GetIcon.getImage("table-favourite-del.png", 14, 14);
    public static final Image IMAGE_TABLE_FAVOURITE_STOP = GetIcon.getImage("table-favourite-stop.png", 14, 14);

    public final ImageView ICON_BUTTON_STOP = GetIcon.getImageView("button-stop.png", 16, 16);
    public final ImageView ICON_BUTTON_NEXT = GetIcon.getImageView("button-next.png", 16, 16);
    public final ImageView ICON_BUTTON_PREV = GetIcon.getImageView("button-prev.png", 16, 16);
    public final ImageView ICON_BUTTON_REMOVE = GetIcon.getImageView("button-remove.png", 16, 16);
    public final ImageView ICON_BUTTON_ADD = GetIcon.getImageView("button-add.png", 16, 16);
    public final ImageView ICON_BUTTON_MOVE_DOWN = GetIcon.getImageView("button-move-down.png", 16, 16);
    public final ImageView ICON_BUTTON_MOVE_UP = GetIcon.getImageView("button-move-up.png", 16, 16);

    public final ImageView FX_ICON_TOOLBAR_MENU = GetIcon.getImageView("toolbar-menu.png", 18, 15);
    public final ImageView FX_ICON_TOOLBAR_MENU_TOP = GetIcon.getImageView("toolbar-menu-top.png", 32, 18);

    public final ImageView FX_ICON_TOOLBAR_FAVOURITE_CHANGE = GetIcon.getImageView("toolbar-favourite-change.png", 32, 32);
    public final ImageView FX_ICON_TOOLBAR_FAVOURITE_DEL = GetIcon.getImageView("toolbar-favourite-del.png", 32, 32);

    public final ImageView FX_ICON_TOOLBAR_STATION_START = GetIcon.getImageView("toolbar-station-start.png", 32, 32);
    public final ImageView FX_ICON_TOOLBAR_STATION_STOP = GetIcon.getImageView("toolbar-station-stop.png", 32, 32);
    public final ImageView FX_ICON_TOOLBAR_STATION_REC = GetIcon.getImageView("toolbar-station-rec.png", 32, 32);

    public final ImageView FX_ICON_FILTER_STATION_LOAD = GetIcon.getImageView("filter-station-load.png", 22, 22);
    public final ImageView FX_ICON_FILTER_STATION_SAVE = GetIcon.getImageView("filter-station-save.png", 22, 22);
    public final ImageView FX_ICON_FILTER_STATION_NEW = GetIcon.getImageView("filter-station-new.png", 22, 22);
}
