/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.p2radio.controller;

import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadConfigFile;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.config.ProgInfos;

import java.nio.file.Path;

public class LoadXmlFactory {

    private LoadXmlFactory() {
    }

    public static boolean loadStationList() {
        boolean ret;
        //XML laden ~650ms
        PDuration.counterStart("LoadStationXML");
        final Path pathXml = ProgInfos.getStationFileXml();
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, pathXml);
        configFile.addConfigs(ProgData.getInstance().stationList);

        ReadConfigFile readConfigFile = new ReadConfigFile();
        readConfigFile.addConfigFile(configFile);
        ret = readConfigFile.readConfigFile(false);
        PDuration.counterStop("LoadStationXML");

        return ret;
    }
}


