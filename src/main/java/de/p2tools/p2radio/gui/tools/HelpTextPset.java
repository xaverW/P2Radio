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

    public static final String PSET_STANDARD = "Das Standardset wird zum Abspielen der Sender " +
            "verwendet. Es können auch weitere Sets angelegt werden. Beim Start kann man dann auswählen, " +
            "mit welchem Set der Sender abgespielt werden soll. " +
            "Ist nur ein Set angelegt, ist es immer das Standardset.";

    public static final String HELP_PSET = "\n" +
            "Ein Set ist ein Satz von Hilfsprogrammen, an welches eine URL eines Senders übergeben wird. " +
            "Mit dem Programm wird dann der Sender abgespielt.\n" +
            "\n" +
            "\n" +
            "====================\n" +
            "Hilfsprogramme:\n" +
            "===\n" +
            "\n" +
            "Hier werden die Programme zum jeweiligen Set eingetragen. Sind mehrere Programme eingetragen, " +
            "kann man zu jedem Programm über die Felder Präfix und Suffix wählen, für welche URL ein Programm " +
            "zuständig ist.\n" +
            "\n" +
            "\"Programm\": In dem Feld steht NUR!! das Programm: \"Pfad/Programmdatei\"\n" +
            "\n" +
            "\"Schalter\": In diesem Feld werden die Programmschalter angegeben, die das Programm zum Start braucht. " +
            "Mögliche Parameter sind:\n" +
            "\n" +
            "Diese Angaben werden durch die URL ersetzt:\n" +
            "%f ist die URL des Senders (Original-URL)\n" +
            "\n" +
            "==================================================\n" +
            "Beispiel für den VLC:\n" +
            "Programm: \"/usr/bin/vlc\"\n" +
            "Schalter: \"%f --play-and-exit :http-user-agent=\"Mozilla/5.0\"\n" +
            "\n" +
            "Hier wird %f durch die URL des Senders ersetzt. " +
            "\n";
}
