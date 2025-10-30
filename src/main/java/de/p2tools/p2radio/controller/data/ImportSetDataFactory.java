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

import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.tools.file.GetFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStreamReader;

public class ImportSetDataFactory {
    private ImportSetDataFactory() {
    }

    public static SetDataList getStandarset() {
        // dann nehmen wir halt die im jar-File
        // liefert das Standard Programmset für das entsprechende BS
        InputStreamReader inReader;
        switch (P2InfoFactory.getOs()) {
            case LINUX:
                inReader = new GetFile().getPsetTamplateLinux();
                break;
            case MAC:
                inReader = new GetFile().getPsetTemplateMac();
                break;
            default:
                inReader = new GetFile().getPsetTemplateWindows();
        }

        // Standardgruppen laden
        SetDataList setDataList = importPset(inReader);
        if (setDataList != null) {
            // damit die Variablen ersetzt werden
            SetDataList.progReplacePattern(setDataList);
        }
        return setDataList;
    }

    private static SetDataList importPset(InputStreamReader in) {
        final SetDataList list = new SetDataList();
        try {
            int event;
            final XMLInputFactory inFactory = XMLInputFactory.newInstance();
            inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            inFactory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE); // Deaktiviere DTDs
            inFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE); // Deaktiviere externe Entitäten

            XMLStreamReader parser;
            parser = inFactory.createXMLStreamReader(in);
            while (parser.hasNext()) {
                event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT &&
                        parser.getLocalName().equals(SetData.TAG)) {

                    SetData setData = new SetData();
                    if (get(parser, SetData.TAG, setData)) {
                        if (!setData.getProgPath().isEmpty() && !setData.getProgSwitch().isEmpty()) {
                            list.add(setData);
                        }
                    }
                }
            }
            in.close();
        } catch (final Exception ex) {
            P2Log.errorLog(467810360, ex);

            return null;
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    private static boolean get(XMLStreamReader parser, String xmlElem, SetData setData) {
        boolean ret = true;
        try {
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event == XMLStreamConstants.END_ELEMENT) {
                    if (parser.getLocalName().equals(xmlElem)) {
                        break;
                    }
                }
                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (parser.getLocalName()) {
                        case SetDataFieldNames.PROGRAMSET_NAME:
                            setData.setVisibleName(parser.getElementText());
                            break;
                        case SetDataFieldNames.PROGRAMSET_PROGRAM_PATH:
                            setData.setProgPath(parser.getElementText());
                            break;
                        case SetDataFieldNames.PROGRAMSET_PROGRAM_SWITCH:
                            setData.setProgSwitch(parser.getElementText());
                            break;
                        case SetDataFieldNames.PROGRAMSET_DESCRIPTION:
                            setData.setDescription(parser.getElementText());
                            break;
                    }
                }
            }
        } catch (final Exception ex) {
            ret = false;
            P2Log.errorLog(467256394, ex);
        }
        return ret;
    }
}
