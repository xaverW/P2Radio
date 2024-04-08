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
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.tools.P2Index;

public class SetData extends SetDataProps {

    public SetData() {
        setId(P2Index.getIndexStr());
    }

    public SetData(String name) {
        setId(P2Index.getIndexStr());
        setVisibleName(name);
    }

    public SetData copy() {
        final SetData ret = new SetData();
        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();

        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        ret.setId(P2Index.getIndexStr()); //es darf nur einen geben!
        ret.setVisibleName("Kopie-" + getVisibleName());

        return ret;
    }

    public String setDataToString() {
        String ret = "";
        ret += "================================================" + P2LibConst.LINE_SEPARATOR;
        ret += "| Programmset" + P2LibConst.LINE_SEPARATOR;

        Config[] configs = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            ret += "     | " + configs[i].getName() + ": " + configs[i].getActValueString() + P2LibConst.LINE_SEPARATOR;
        }

        ret += "     |_______________________________________________" + P2LibConst.LINE_SEPARATOR;
        return ret;
    }
}
