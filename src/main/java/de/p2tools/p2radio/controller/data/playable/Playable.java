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

package de.p2tools.p2radio.controller.data.playable;

import de.p2tools.p2Lib.tools.date.PLocalDate;
import de.p2tools.p2radio.controller.data.start.Start;

public interface Playable {

    boolean station = false, favourite = false, lastPlayed = false;
    boolean blackBlocked = false;

    Start getStart();

    void setStart(Start start);

    boolean getFavouriteUrl();

    void setFavouriteUrl(boolean set);

    boolean isStation();

    boolean isFavourite();

    boolean isLastPlayed();

    boolean isBlackBlocked();

    void setBlackBlocked(boolean set);

    //====================================
    int getNo();

    int getStationNo();

    void setStationNo(int no);

    String getStationName();

    String getCollectionName();

    String getGenre();

    String getCodec();

    String getBitrate();

    int getBitrateInt();

    int getGrade();

    boolean isOwn();

    void setOwn(boolean own);

    int getClickCount();

    void setClickCount(int clickCount);

    String getCountry();

    String getLanguage();

    String getCountryCode();

    String getDescription();

    String getStationUrl();

    String getWebsite();

    PLocalDate getStationDate();

    void setStationDate(PLocalDate stationDate);

    void setStationDate(String date);

    Playable getCopy();
}
