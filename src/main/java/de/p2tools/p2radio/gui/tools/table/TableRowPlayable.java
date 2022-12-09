/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2radio.gui.tools.table;

import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.data.playable.Playable;
import javafx.scene.control.Tooltip;

public class TableRowPlayable<T extends Playable> extends javafx.scene.control.TableRow {

    public TableRowPlayable() {
    }

    @Override
    public void updateItem(Object f, boolean empty) {
        super.updateItem(f, empty);

        Playable favourite = (Playable) f;
        setStyle("");
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).setStyle("");
        }

        if (favourite != null && !empty) {
            final boolean fav = favourite.isFavourite();
            final boolean playing = favourite.getStart() != null;
            final boolean error = favourite.getStart() != null && favourite.getStart().getStartStatus().isStateError();
            final boolean newStation = favourite.isNewStation();

            if (favourite.getStart() != null && favourite.getStart().getStartStatus().isStateError()) {
                Tooltip tooltip = new Tooltip();
                tooltip.setText(favourite.getStart().getStartStatus().getErrorMessage());
                setTooltip(tooltip);
            }

            if (error) {
                if (ProgColorList.STATION_ERROR_BG.isUse()) {
                    setStyle(ProgColorList.STATION_ERROR_BG.getCssBackgroundAndSel());
                }
                if (ProgColorList.STATION_ERROR.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_ERROR.getCssFont());
                    }
                }

            } else if (playing) {
                if (ProgColorList.STATION_RUN_BG.isUse()) {
                    setStyle(ProgColorList.STATION_RUN_BG.getCssBackgroundAndSel());
                }
                if (ProgColorList.STATION_RUN.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_RUN.getCssFont());
                    }
                }

            } else if (newStation) {
                // neue Sender
                if (ProgColorList.STATION_NEW_BG.isUse()) {
                    setStyle(ProgColorList.STATION_NEW_BG.getCssBackgroundAndSel());
                }
                if (ProgColorList.STATION_NEW.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_NEW.getCssFont());
                    }
                }

            } else if (fav) {
                if (ProgColorList.STATION_FAVOURITE_BG.isUse()) {
                    setStyle(ProgColorList.STATION_FAVOURITE_BG.getCssBackgroundAndSel());
                }
                if (ProgColorList.STATION_FAVOURITE.isUse()) {
                    System.out.println("isUse");
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.STATION_FAVOURITE.getCssFont());
                    }
                }
            }
        }
    }
}
