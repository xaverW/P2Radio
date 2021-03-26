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

package de.p2tools.p2radio.controller.data.favourite;

public class FavouriteConstants {

    //Startnummer (Reihenfolge) der Sender
    public static final int FAVOURITE_NUMBER_NOT_STARTED = Integer.MAX_VALUE;

    //SenderNr wenn kein Sender mehr gefunden wird
    public static final int STATION_NUMBER_NOT_FOUND = Integer.MAX_VALUE;

    //Stati
    public static final int STATE_INIT = 0; //nicht gestart
    public static final int STATE_STARTED_RUN = 1; //Favorite läuft
    public static final int STATE_STOPPED = 4; //abgebrochen
    public static final int STATE_ERROR = 5; // fertig, fehlerhaft

    public static final String ALL = "";
    public static final String SRC_BUTTON = "Button"; //sind im Tab Sender mit Button gestartete
    public static final int MAX_FAVOURITE_GRADE = 3;
}
