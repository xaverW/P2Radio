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

import de.p2tools.p2radio.controller.data.start.StartRuntimeExec;

public class ProgramData extends ProgramProps {

    public ProgramData() {
//        setRestart(false);
    }

    public ProgramData copy() {
        setXmlFromProps();

        final ProgramData ret = new ProgramData();
        System.arraycopy(arr, 0, ret.arr, 0, arr.length);
        ret.setPropsFromXml();

        return ret;
    }

    public boolean urlTesten(String url) {
        //prüfen ob das Programm zur Url passt
        boolean ret = false;
        if (url != null) {
            //Felder sind entweder leer oder passen
            if (SetFactory.testPrefix(getPraefix(), url, true)
                    && SetFactory.testPrefix(getSuffix(), url, false)) {
                ret = true;
            }
        }
        return ret;
    }

    public String getProgrammAufruf() {
        return getProgPath() + " " + getProgSwitch();
    }

    public String getProgrammAufrufArray() {
        String ret;
        ret = getProgPath();
        final String[] ar = getProgSwitch().split(" ");
        for (final String s : ar) {
            ret = ret + StartRuntimeExec.TRENNER_PROG_ARRAY + s;
        }
        return ret;
    }

    public static String makeProgAufrufArray(String pArray) {
        final String[] progArray = pArray.split(StartRuntimeExec.TRENNER_PROG_ARRAY);
        String execStr = "";
        for (final String s : progArray) {
            execStr = execStr + s + " ";
        }
        execStr = execStr.trim(); // letztes Leerzeichen wieder entfernen
        return execStr;
    }
}
