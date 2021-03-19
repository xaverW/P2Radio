/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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

import de.p2tools.p2Lib.configFile.pData.PDataSample;

public class SetDataBase extends PDataSample<SetData> {

    //Tags Programmgruppen
    public static final int PROGRAMSET_ID = 0;
    public static final int PROGRAMSET_VISIBLE_NAME = 1;
    public static final int PROGRAMSET_PRAEFIX_DIRECT = 2;
    public static final int PROGRAMSET_SUFFIX_DIRECT = 3;
    public static final int PROGRAMSET_IST_ABSPIELEN = 4;
    public static final int PROGRAMSET_BESCHREIBUNG = 5;

    public static final int MAX_ELEM = 6;

    public static final String[] COLUMN_NAMES = {"Name", "Setname", "Präfix", "Suffix",
            "Abspielen", "Beschreibung"};
    public static final String[] XML_NAMES = {"Name", "Setname", "Praefix", "Suffix",
            "Abspielen", "Beschreibung"}; //ist im Set zum Download so festgesetzt

    public String[] arr;
    public static final String TAG = "Programmset"; //ist im Set zum Download so festgesetzt

    public SetDataBase() {
        makeArray();
    }

    void makeArray() {
        arr = new String[MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = "";
        }
        arr[PROGRAMSET_IST_ABSPIELEN] = Boolean.toString(false);
    }
}
