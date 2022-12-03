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

import de.p2tools.p2Lib.tools.ProgramToolsFactory;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2radio.tools.file.GetFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStreamReader;
import java.util.LinkedList;


@SuppressWarnings("serial")
public class PsetVorlagen {

    public static final String PGR = "Vorlage";
    public static final String PGR_NAME = "Name";
    public static final int PGR_NAME_NR = 0;
    public static final String PGR_DESCRIPTION = "Beschreibung";
    public static final int PGR_DESCRIOPTION_NR = 1;
    public static final String PGR_VERSION = "Version";
    public static final int PGR_VERSION_NR = 2;
    public static final String PGR_BS = "Bs";
    public static final int PGR_BS_NR = 3;
    public static final String PGR_URL = "URL";
    public static final int PGR_URL_NR = 4;
    public static final String PGR_INFO = "Info";
    public static final int PGR_INFO_NR = 5;
    public static final int PGR_MAX_ELEM = 6;
    public static final String[] PGR_COLUMN_NAMES = {PGR_NAME, PGR_DESCRIPTION, PGR_VERSION, PGR_BS, PGR_URL, PGR_INFO};
    private final static int TIMEOUT = 10000;

    private final LinkedList<String[]> liste = new LinkedList<>();

    public SetDataList getStandarset(boolean replaceMuster) {
        // dann nehmen wir halt die im jar-File
        // liefert das Standard Programmset für das entsprechende BS
        InputStreamReader inReader;
        switch (ProgramToolsFactory.getOs()) {
            case LINUX:
                inReader = new GetFile().getPsetTamplateLinux();
                break;
            default:
                inReader = new GetFile().getPsetTemplateWindows();
        }
        // Standardgruppen laden
        SetDataList setDataList = importPset(inReader);
        if (replaceMuster && setDataList != null) {
            // damit die Variablen ersetzt werden
            SetDataList.progReplacePattern(setDataList);
        }
        return setDataList;
    }

    private SetDataList importPset(InputStreamReader in) {
        SetData psetData = null;
        final SetDataList list = new SetDataList();
        try {
            int event;
            final XMLInputFactory inFactory = XMLInputFactory.newInstance();
            inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            XMLStreamReader parser;
            parser = inFactory.createXMLStreamReader(in);
            while (parser.hasNext()) {
                event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (parser.getLocalName()) {
                        case SetData.TAG:
                            psetData = new SetData();
                            if (!get(parser, SetData.TAG, SetData.XML_NAMES, psetData.arr)) {
                                psetData = null;
                            } else {
                                if (!psetData.isEmpty()) {
                                    //kann beim Einlesen der Konfigdatei vorkommen
                                    psetData.setPropsFromXml();
                                    list.add(psetData);
                                }
                            }
                            break;
                        case ProgramData.TAG:
                            if (psetData != null) {
                                final ProgramData progData = new ProgramData();
                                if (get(parser, ProgramData.TAG, ProgramData.XML_NAMES, progData.arr)) {
                                    progData.setPropsFromXml();
                                    psetData.addProg(progData);
                                }
                            }
                            break;
                    }
                }
            }
            in.close();
        } catch (final Exception ex) {
            PLog.errorLog(467810360, ex);

            return null;
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    private boolean get(XMLStreamReader parser, String xmlElem, String[] xmlNames, String[] strRet) {
        boolean ret = true;
        final int maxElem = strRet.length;
        for (int i = 0; i < maxElem; ++i) {
            strRet[i] = "";
        }
        try {
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals(xmlElem)) {
                        break;
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    for (int i = 0; i < maxElem; ++i) {
                        if (parser.getLocalName().equals(xmlNames[i])) {
                            strRet[i] = parser.getElementText();
                            break;
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            ret = false;
            PLog.errorLog(467256394, ex);
        }
        return ret;
    }
}
