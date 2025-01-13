package de.p2tools.p2radio.controller.data.start;

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

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;

/**
 * das sind die Meldungen die externe Programme liefern (z.B.: VLC)
 * und werden auch im P2Log eingetragen
 */
public class PlayerMessage {

    private static int LINE_NO = 0;
    private final int lineNo;

    public PlayerMessage() {
        lineNo = ++LINE_NO;
    }

    private static String getNr(int nr) {
        final int MAX_STELLEN = 5;
        final String FILL_SIGN = "0";
        StringBuilder str = new StringBuilder(String.valueOf(nr));
        while (str.length() < MAX_STELLEN) {
            str.insert(0, FILL_SIGN);
        }
        return str.toString();
    }

    public synchronized void playerMessage(String text) {
        if (ProgData.debug) {
            final String z = "[" + getNr(lineNo) + "] >> " + text;
            P2Log.extToolLog(z);
        }
    }
}
