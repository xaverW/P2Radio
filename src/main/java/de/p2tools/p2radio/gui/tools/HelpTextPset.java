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

package de.p2tools.p2radio.gui.tools;

public class HelpTextPset {

    public static final String HELP_PSET =
            "Ein Set ist ein Hilfsprogramm (z.B. VLC), an das die URL des Senders übergeben wird. " +
                    "Mit dem Programm wird dann der Sender abgespielt." +
                    "\n" +
                    "\n" +
                    "Das Standardset ist immer das erste in der Tabelle. Ist nur ein Set angelegt, " +
                    "ist es immer das Standardset. " +
                    "Das Standardset wird beim Start eines Senders über die Buttons zum Abspielen verwendet. " +
                    "Sind weitere Sets vorhanden, " +
                    "können diese über das Menü gestartet werden. " +
                    "\n" +
                    "\n" +
                    "==================================================" +
                    "\n" +
                    "Set Einstellungen: " +
                    "\n" +
                    "\n" +
                    "\"Name\"\n" +
                    "\"Beschreibung\"\n" +
                    "Hier können Infos zum Set angegeben werden, sie dienen nur der eigenen Info." +
                    "\n" +
                    "\n" +
                    "\"Programm\"\n" +
                    "In dem Feld steht NUR!! das Programm: \"Pfad/Programmdatei\"" +
                    "\n" +
                    "\n" +
                    "\"Schalter\"\n" +
                    "In diesem Feld werden die Programmschalter angegeben, die das Programm zum Start braucht. " +
                    "Der Parameter %f steht für die URL des Senders und wird beim Start des " +
                    "Senders durch die URL ersetzt." +
                    "\n" +
                    "\n" +
                    "==================================================" +
                    "\n" +
                    "Beispiel für den VLC:" +
                    "\n" +
                    "Programm (Linux):  \"/usr/bin/vlc\"\n" +
                    "Programm (Windows):  \"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe\"\n" +
                    "Schalter: \"%f -vvv\"" +
                    "\n" +
                    "\n" +
                    "Hier wird %f durch die URL des Senders ersetzt. Der Programmaufruf sieht dann so aus:" +
                    "\n" +
                    "/usr/bin/vlc SENDER-URL -vvv" +
                    "\n" +
                    "\n" +
                    "==================================================" +
                    "\n" +
                    "Viele Radiosender (aber nicht alle!) schicken Infos (Titel, Interpret) " +
                    "in ihrem Stream mit. P2Radio kann diese Infos (Titel, Interpret) aus den " +
                    "Programmausgaben folgender Programme auslesen: \n" +
                    "VLC, ffplay und audacious\n" +
                    "\n" +
                    "VLC muss mit dem Parameter \"-vvv\" gestartet werden." +
                    "\n" +
                    "\n" +
                    "Im Programm audacious muss im PlugIn \"Titelwechsel\" eine Zeile:\n" +
                    "echo p2radio: PARAMETER >&2\n" +
                    "z.B.: echo p2radio: %a - %T >&2\n" +
                    "eingetragen sein. Dann erzeugt audacious eine Ausgabe die P2Radio verarbeiten kann." +
                    "\n";
}
