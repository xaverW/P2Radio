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

import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.controller.data.ProgramData;


public class StartProgramFactory {

    private StartProgramFactory() {
    }

    public static boolean makeProgParameter(Start start) {
        try {
            final ProgramData programData = start.getSetData().getProgForUrl(start.getUrl());
            if (programData == null) {
                return false; //todo ist das gut da wenn kein Set???
            }

            start.setProgram(programData.getName());
            buildProgParameter(start, programData);
        } catch (final Exception ex) {
            PLog.errorLog(825600145, ex);
        }
        return true;
    }

    private static void buildProgParameter(Start start, ProgramData program) {
        String befehlsString = program.getProgrammAufruf();
        befehlsString = replaceExec(start, befehlsString);
        start.setProgramCall(befehlsString);

        String progArray = program.getProgrammAufrufArray();
        progArray = replaceExec(start, progArray);
        start.setProgramCallArray(progArray);
    }


    private static String replaceExec(Start start, String execString) {
        execString = execString.replace("%f", start.getUrl());
        return execString;
    }
}
