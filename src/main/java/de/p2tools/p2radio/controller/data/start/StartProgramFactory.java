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

package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.station.StationData;


public class StartProgramFactory {

    private StartProgramFactory() {
    }

    public static void setStartCounter(StationData playable) {
        int clickCount = 0;
        //größten ClickCount suchen
        for (StationData stationData : ProgData.getInstance().favouriteList) {
            if (stationData.getStationUrl().equals(playable.getStationUrl()) && stationData.getStarts() > clickCount) {
                clickCount = stationData.getStarts();
            }
        }
        for (StationData stationData : ProgData.getInstance().historyList) {
            if (stationData.getStationUrl().equals(playable.getStationUrl()) && stationData.getStarts() > clickCount) {
                clickCount = stationData.getStarts();
            }
        }

        //dann erhöhen und setzen
        ++clickCount;
        for (StationData stationData : ProgData.getInstance().favouriteList) {
            if (stationData.getStationUrl().equals(playable.getStationUrl())) {
                stationData.setStarts(clickCount);
            }
        }
        for (StationData stationData : ProgData.getInstance().historyList) {
            if (stationData.getStationUrl().equals(playable.getStationUrl())) {
                stationData.setStarts(clickCount);
            }
        }
    }

    public static boolean makeProgParameter(StartDto startDto) {
        try {
            buildProgParameter(startDto);
        } catch (final Exception ex) {
            P2Log.errorLog(825600145, ex);
        }
        return true;
    }

    private static void buildProgParameter(StartDto startDto) {
        String prog = startDto.getSetData().getProgPath() + " " + startDto.getSetData().getProgSwitch();
        prog = replaceExec(startDto, prog);
        startDto.setProgramCall(prog);

        String progArray = getProgrammAufrufArray(startDto.getSetData().getProgPath(), startDto.getSetData().getProgSwitch());
        progArray = replaceExec(startDto, progArray);
        startDto.setProgramCallArray(progArray);
    }

    private static String getProgrammAufrufArray(String progPath, String progSwitch) {
        String ret;
        ret = progPath;
        final String[] ar = progSwitch.split(" ");
        for (final String s : ar) {
            ret = ret + StartRuntimeExec.TRENNER_PROG_ARRAY + s;
        }
        return ret;
    }

    private static String replaceExec(StartDto startDto, String execString) {
        execString = execString.replace("%f", startDto.getStationData().getStationUrl());
        return execString;
    }
}
