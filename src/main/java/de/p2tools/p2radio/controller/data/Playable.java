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

import de.p2tools.p2radio.controller.data.start.Start;

public interface Playable {
    boolean station = false, favourite = false, lastPlayed = false;

    public Start getStart();

    public void setStart(Start start);

    public String getStationName();

    public int getNo();

    public String getStationUrl();

    public void setClickCount(int clickCount);

    public int getClickCount();

    public boolean isStation();

    public boolean isFavourite();

    public boolean isLastPlayed();

    public Playable getCopy();
}
