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

import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2radio.controller.config.ProgConst;

public class HelpText {

    public static final String SHORTCUT =
            "Zum Ändern eines Tastenkürzels, seinen \"Ändern\"-Button klicken und dann " +
                    "die gewünschten neuen Tasten drücken.\n" +
                    "\n" +
                    "Der \"Zurücksetzen\"-Button stellt den Originalzustand wieder her.\n" +
                    "\n" +
                    "Damit die Änderungen wirksam werden, muss das Programm neu gestartet werden." +
                    "\n";

    public static final String PROG_PATHS = "Hier kann das Programm zum \"Abspielen\" " +
            "der Sender eingetragen werden. Wird der Pfad nicht automatisch erkannt, " +
            "kann man ihn auch per Hand auswählen." +
            P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            "Um alle Sender abspielen zu können, muss " +
            "ein Programm installiert sein, z.B. VLC. " +
            "Der VLC-Player ist zum Abspielen der Sender gut geeignet und kann so geladen werden:" +
            P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            "Linux:" + P2LibConst.LINE_SEPARATOR +
            "VLC kann über die Paketverwaltung eingespielt werden." + P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            P2LibConst.LINE_SEPARATOR +
            "Windows:" + P2LibConst.LINE_SEPARATOR +
            "VLC ist hier zu finden: " + "http://www.videolan.org" + P2LibConst.LINE_SEPARATOR;

    public static final String FILTER_FIELD =
            P2LibConst.LINE_SEPARATOR +
                    "In den Feldern mit Texteingabe " +
                    "muss die Eingabe im " +
                    "entsprechendem Feld des Senders nur enthalten sein." +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR;

    public static final String FILTER_EXACT =
            P2LibConst.LINE_SEPARATOR +
                    "Groß- und Kleinschreibung wird beim Filtern " +
                    "nicht beachtet." +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "In allen Feldern " +
                    "kann auch nach mehreren Begriffen gesucht werden (diese " +
                    "werden durch \"Komma\" oder \"Doppelpunkt\" getrennt angegeben " +
                    "und können auch Leerzeichen enthalten)." +
                    P2LibConst.LINE_SEPARATOR +
                    "\"Sport,Fussball\" sucht nach Sendern die im jeweiligen Feld den " +
                    "Begriff \"Sport\" ODER \"Fussball\" haben." +
                    P2LibConst.LINE_SEPARATOR +
                    "\"Sport:Fussball\" sucht nach Sendern die im jeweiligen Feld den " +
                    "Begriff \"Sport\" UND \"Fussball\" haben." +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "In allen Feldern mit Texteingabe " +
                    "kann auch mit regulären Ausdrücken gesucht " +
                    "werden. Diese müssen mit \"#:\" eingeleitet werden. " +
                    "Auch bei den regulären Ausdrücken wird nicht zwischen " +
                    "Groß- und Kleinschreibung unterschieden. " +
                    P2LibConst.LINE_SEPARATOR +
                    "#:Abend.*" + P2LibConst.LINE_SEPARATOR +
                    "Das bedeutet z.B.: Es werden alle Sender gefunden, die " + P2LibConst.LINE_SEPARATOR +
                    "im jeweiligen Feld mit \"Abend\" beginnen." + P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "https://de.wikipedia.org/wiki/Regul%C3%A4rer_Ausdruck" + P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR;

    public static final String GUI_STATION_FILTER =
            P2LibConst.LINE_SEPARATOR +
                    "Beim Feld \"URL\" muss der Filter in der URL " +
                    "des Senders ODER der Website des Senders enthalten sein." +
                    P2LibConst.LINE_SEPARATOR +
                    FILTER_EXACT +
                    P2LibConst.LINE_SEPARATOR +
                    P2LibConst.LINE_SEPARATOR +
                    "Filterprofile:" + P2LibConst.LINE_SEPARATOR +
                    "==================" + P2LibConst.LINE_SEPARATOR +
                    "Mit den Buttons unten, kann man eingestellte Filter speichern " +
                    "und auch wieder abrufen. So wird der gespeicherte Zustand genau " +
                    "wieder hergestellt. Ist der Profilname im Auswahlfeld unterstrichen, " +
                    "besagt das, dass die aktuellen Filtereinstellungen unverändert sind und " +
                    "denen des Profils entsprechen." +
                    "\n";

    public static final String BLACKLIST_WHITELIST =
            "Bei der Funktion \"Blacklist\" werden Sender, die den " +
                    "Angaben in einer Zeile in der Tabelle entsprechen, " +
                    "nicht angezeigt." +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Die Funktion \"Whitelist\" zeigt nur die Sender an, die " +
                    "den Angaben in einer Zeile in der Tabelle entsprechen." +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Beim Umschalten \"Blacklist - Whitelist\" werden genau " +
                    "die vorher nicht angezeigten Sender jetzt angezeigt." +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Mit \"Treffer zählen\" kann überprüft werden, wieviele Sender " +
                    "in der Senderliste jeder Eintrag in der Blacklist findet. Damit lässt " +
                    "sich die Blacklist optimieren. Eine kürzere Blacklist führt zu schnelleren " +
                    "Ergebnissen." +
                    P2LibConst.LINE_SEPARATORx2 +

                    FILTER_FIELD +
                    FILTER_EXACT;

    public static final String CONFIG_STYLE = "Hier kann die Schriftgröße des Programms angepasst werden." + P2LibConst.LINE_SEPARATORx2 +
            "Die sollte sich automatisch an die vorgegebene Größe im Betriebssystem einstellen. " +
            "Wenn die Automatik nicht korrekt funktioniert oder eine andere gewünscht wird, kann " +
            "sie hier angepasst werden." +
            P2LibConst.LINE_SEPARATORx2 +
            "Damit die Änderungen wirksam werden, kann ein Programmneustart notwendig sein." +
            "\n";

    public static final String GUI_STATIONS_EDIT_FILTER = "Hier können die angezeigten Filter " +
            "ein- und ausgeschaltet werden. " +
            "Ausgeschaltete Filter werden beim Suchen auch nicht berücksichtigt. " +
            "Mit weniger Filtern ist auch der Suchvorgang schneller" +
            "\n";

    public static final String SET = "" +
            P2LibConst.LINE_SEPARATORx2 +
            "Mehrere Einträge können mit \"Komma\" getrennt, angegeben werden" +
            "\n";

    public static final String BLACKLIST_BITRATE = "Es werden nur Sender mit der vorgegebenen Bitrate angezeigt." +
            "\n";

    public static final String BLACKLIST_COUNT = "Beim Treffer zählen wird jeder Sender gegen die Blacklist geprüft. " +
            "Jeder Sender läuft also die Blacklist von Anfang nach Ende ab und jeder Treffer wird gezählt. " +
            P2LibConst.LINE_SEPARATORx2 +
            "Beim Filtern der Senderliste wird nach einem Treffer die weitere Suche abgebrochen. Es beschleunigt also die Suche " +
            "wenn die Blacklisteinträge mit den meisten Treffern am Anfang liegen" +
            "\n";

    public static final String LOAD_STATION_LIST_EVERY_DAYS = "Ist die Liste der Sender älter als " + ProgConst.LOAD_STATION_LIST_EVERY_DAYS +
            " Tage, wird automatisch eine neue Liste geladen." +
            "\n";

    public static final String SMALL_BUTTON = "In der Tabelle Sender Favoriten und History können auch " +
            "kleine Buttons angezeigt werden. Die Zeilenhöhe wird dadurch kleiner." +
            "\n";

    public static final String TRAY =
            "Im System Tray wird für das Programm ein Symbol angezeigt. " +
                    "Damit kann das Programm auf dem Desktop ausgeblendet werden." +
                    "\n";

    public static final String TRAY_OWN_ICON =
            "Im System Tray wird für das Programm ein Symbol angezeigt. " +
                    "Damit kann ein eigens Bild dafür verwendet werden." +
                    "\n";
    public static final String DARK_THEME = "Das Programm wird damit mit einer dunklen " +
            "Programmoberfläche angezeigt. Damit alle Elemente der Programmoberfläche " +
            "geändert werden, kann ein Programmneustart notwendig sein." +
            "\n";

    public static final String USER_AGENT = "Hier kann ein User Agent angegeben werden. " +
            "Beim Laden der Senderliste wird er dann als Absender verwendet. Es sollte der Name des Programms " +
            " enthalten sein. Wird kein User Agent angegeben, wird auch keiner verwendet." +
            P2LibConst.LINE_SEPARATORx2 +
            "(Es sind nur ASCII-Zeichen erlaubt und die Textlänge ist begrenzt auf 100 Zeichen)" +
            "\n";

    public static final String LOGFILE = "Hier kann ein Ordner angegeben werden " +
            "in dem ein Logfile erstellt wird. Darin wird der Programmverlauf skizziert. " +
            "Das kann hilfreich sein, wenn das Programm nicht wie erwartet funktioniert." + P2LibConst.LINE_SEPARATORx2 +
            "Der Standardordner ist \"Log\" im Konfigordner des Programms." + P2LibConst.LINE_SEPARATORx2 +
            "Wird der Pfad zum Logfile geändert, wirkt sich das erst beim Neustart des Programms " +
            "aus. Mit dem Button \"Pfad zum Logfile jetzt schon verwenden\" wird die Programmausgabe ab " +
            "Klick darauf ins neue Logfile geschrieben." +
            "\n";

    public static final String WEBBROWSER = "Wenn das Programm versucht, einen Link zu öffnen " +
            "(z.B. den Link im Menüpunkt \"Hilfe\" zu den \"Hilfeseiten\") " +
            "und die Standardanwendung (z.B. \"Firefox\") nicht startet, " +
            "kann damit ein Programm ausgewählt und " +
            "fest zugeordnet werden (z.B. der Browser \"Firefox\")." +
            "\n";

    public static final String RESET_DIALOG =
            "==> Einstellungen zum Abspielen zurücksetzen" +

                    P2LibConst.LINE_SEPARATORx2 +
                    "Damit werden alle Sets (auch eigene), die zum Abspielen der " +
                    "Sender gebraucht werden, gelöscht. Anschließend wird das aktuelle Standardset eingerichtet. " +
                    "Es kann dann direkt damit weitergearbeitet werden. Blacklist bleibt erhalten." +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Das sollte vor dem kompletten Zurücksetzen des Programms versucht werden. " +

                    P2LibConst.LINE_SEPARATORx3 +
                    "=====   ODER   =====" +
                    P2LibConst.LINE_SEPARATORx3 +

                    "==> Alle Einstellungen zurücksetzen" +
                    P2LibConst.LINE_SEPARATORx2 +
                    "Damit wird das Programm in den Ursprungszustand zurückgesetzt. Es gehen " +
                    "ALLE Einstellungen verloren. Das Programm beendet sich " +
                    "und muss neu gestartet werden. Der neue Start beginnt " +
                    "mit dem Einrichtungsdialog." +
                    P2LibConst.LINE_SEPARATOR;
}
