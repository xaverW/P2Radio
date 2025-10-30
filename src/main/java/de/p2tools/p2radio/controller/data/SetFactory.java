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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.start.StartRuntimeExec;

import java.io.File;
import java.util.ArrayList;

public class SetFactory {

    private static final ArrayList<String> winPath = new ArrayList<>();

    private static void setWinProgPath() {
        String pfad;
        if (System.getenv("ProgramFiles") != null) {
            pfad = System.getenv("ProgramFiles");
            if (new File(pfad).exists() && !winPath.contains(pfad)) {
                winPath.add(pfad);
            }
        }
        if (System.getenv("ProgramFiles(x86)") != null) {
            pfad = System.getenv("ProgramFiles(x86)");
            if (new File(pfad).exists() && !winPath.contains(pfad)) {
                winPath.add(pfad);
            }
        }
        final String[] PATH = {"C:\\Program Files", "C:\\Programme", "C:\\Program Files (x86)"};
        for (final String s : PATH) {
            if (new File(s).exists() && !winPath.contains(s)) {
                winPath.add(s);
            }
        }
    }

    public static String getTemplatePathVlc() {
        // liefert den Standardpfad für das entsprechende BS
        // Programm muss auf dem Rechner installiert sein
        final String PATH_LINUX_VLC = "/usr/bin/vlc";
        final String PATH_FREEBSD = "/usr/local/bin/vlc";
        final String PATH_WIN = "\\VideoLAN\\VLC\\vlc.exe";
        final String PATH_MAC = "/Applications/VLC.app/Contents/MacOS/VLC";
        String path = "";
        try {
            switch (P2InfoFactory.getOs()) {
                case LINUX:
                    if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
                        path = PATH_FREEBSD;
                    } else {
                        path = PATH_LINUX_VLC;
                    }
                    break;
                case MAC:
                    path = PATH_MAC;
                    break;
                default:
                    setWinProgPath();
                    for (final String s : winPath) {
                        path = s + PATH_WIN;
                        if (new File(path).exists()) {
                            break;
                        }
                    }
            }
            if (!new File(path).exists() && System.getenv("PATH_VLC") != null) {
                path = System.getenv("PATH_VLC");
            }
            if (!new File(path).exists()) {
                path = "";
            }
        } catch (final Exception ignore) {
        }
        return path;
    }

    public static boolean addSetTemplate(SetDataList pSet) {
        if (pSet == null) {
            return false;
        }
        return ProgData.getInstance().setDataList.addSetData(pSet);
    }

    public static boolean checkProgram(String prog) {
        // prüfen ob die eingestellten Programmsets passen
        boolean ret = true;

        if (!new File(prog).canExecute()) {
            // dann noch mit RuntimeExec versuchen
            final StartRuntimeExec r = new StartRuntimeExec(prog);
            final Process pr = r.exec();
            if (pr != null) {
                // dann passts ja
                pr.destroy();
            } else {
                // läßt sich nicht starten
                ret = false;
            }
        }

        return ret;
    }

    public static boolean checkPrograms(ProgData data) {
        // prüfen ob die eingestellten Programmsets passen
        final String PIPE = "| ";
        final String LEER = "      ";
        final String PFEIL = " -> ";
        boolean ret = true;
        String text = "";

        for (final SetData setData : data.setDataList) {
            ret = true;
            text += "++++++++++++++++++++++++++++++++++++++++++++" + P2LibConst.LINE_SEPARATOR;
            text += PIPE + "Programmgruppe: " + setData.getVisibleName() + P2LibConst.LINE_SEPARATOR;

            // Programmpfad prüfen
            if (setData.getProgPath().isEmpty()) {
                ret = false;
                text += PIPE + LEER + "Kein Programm angegeben!" + P2LibConst.LINE_SEPARATOR;
                text += PIPE + LEER + LEER + "Pfad: " + setData.getProgPath() + P2LibConst.LINE_SEPARATOR;
            } else if (!new File(setData.getProgPath()).canExecute()) {
                // dann noch mit RuntimeExec versuchen
                final StartRuntimeExec r = new StartRuntimeExec(setData.getProgPath());
                final Process pr = r.exec();
                if (pr != null) {
                    // dann passts ja
                    pr.destroy();
                } else {
                    // läßt sich nicht starten
                    ret = false;
                    text += PIPE + LEER + "Falscher Programmpfad!" + P2LibConst.LINE_SEPARATOR;
                    text += PIPE + LEER + LEER + "Pfad: " + setData.getProgPath() + P2LibConst.LINE_SEPARATOR;
                    if (!setData.getProgPath().contains(File.separator)) {
                        text += PIPE + LEER + PFEIL + "Wenn das Programm nicht im Systempfad liegt, " + P2LibConst.LINE_SEPARATOR;
                        text += PIPE + LEER + LEER + "wird der Start nicht klappen!" + P2LibConst.LINE_SEPARATOR;
                    }
                }
            }
            if (ret) {
                //sollte alles passen
                text += PIPE + PFEIL + "Ok!" + P2LibConst.LINE_SEPARATOR;
            }
            text += "++++++++++++++++++++++++++++++++++++++++++++" + "" + P2LibConst.LINE_SEPARATORx3;
        }
        P2Alert.showInfoAlert("Set", "Sets prüfen", text, true);
        return ret;
    }
}
