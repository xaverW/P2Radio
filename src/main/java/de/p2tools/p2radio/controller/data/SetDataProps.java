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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import de.p2tools.p2radio.controller.data.start.StartRuntimeExec;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class SetDataProps extends PDataSample<SetData> {

    public static final String TAG = "ProgrammSet";
    private final StringProperty id = new SimpleStringProperty("");
    private final StringProperty visibleName = new SimpleStringProperty("");
    private final StringProperty progPath = new SimpleStringProperty("");
    private final StringProperty progSwitch = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    public String[] arr;

    public SetDataProps() {
        makeArray();
    }

    public static String makeProgAufrufArray(String pArray) {
        final String[] progArray = pArray.split(StartRuntimeExec.TRENNER_PROG_ARRAY);
        String execStr = "";
        for (final String s : progArray) {
            execStr = execStr + s + " ";
        }
        execStr = execStr.trim(); // letztes Leerzeichen wieder entfernen
        return execStr;
    }

    public String getProgrammAufrufArray(String progPath, String progSwitch) {
        String ret;
        ret = progPath;
        final String[] ar = progSwitch.split(" ");
        for (final String s : ar) {
            ret = ret + StartRuntimeExec.TRENNER_PROG_ARRAY + s;
        }
        return ret;
    }

//    public boolean progsContainPath() {
//        // ein Programmschalter mit
//        // "**" (Pfad/Datei) oder %a (Pfad) oder %b (Datei)
//        // damit ist es ein Set zum Speichern
//        boolean ret = false;
//
//        for (ProgramData progData : programList) {
//            if (progData.getProgSwitch().contains("**")
//                    || progData.getProgSwitch().contains("%a")
//                    || progData.getProgSwitch().contains("%b")) {
//                ret = true;
//                break;
//            }
//        }
//        return ret;
//    }

    @Override
    public String getTag() {
        return TAG;
    }

    void makeArray() {
        arr = new String[SetDataFieldNames.MAX_ELEM];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = "";
        }
        arr[SetDataFieldNames.PROGRAMSET_IS_STANDARDSET_INT] = Boolean.toString(false);
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("id", SetDataFieldNames.PROGRAMSET_ID, id));
        list.add(new ConfigStringPropExtra("visibleName", SetDataFieldNames.PROGRAMSET_VISIBLE_NAME, visibleName));
        list.add(new ConfigStringPropExtra("progPath", SetDataFieldNames.PROGRAMSET_PROGRAM_PATH, progPath));
        list.add(new ConfigStringPropExtra("progSwitch", SetDataFieldNames.PROGRAMSET_PROGRAM_SWITCH, progSwitch));
        list.add(new ConfigStringPropExtra("description", SetDataFieldNames.PROGRAMSET_DESCRIPTION, description));
        return list.toArray(new Config[]{});
    }


    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getVisibleName() {
        return visibleName.get();
    }

    public void setVisibleName(String visibleName) {
        this.visibleName.set(visibleName);
    }

    public StringProperty visibleNameProperty() {
        return visibleName;
    }

    public String getProgPath() {
        return progPath.get();
    }

    public void setProgPath(String progPath) {
        this.progPath.set(progPath);
    }

    public StringProperty progPathProperty() {
        return progPath;
    }

    public String getProgSwitch() {
        return progSwitch.get();
    }

    public void setProgSwitch(String progSwitch) {
        this.progSwitch.set(progSwitch);
    }

    public StringProperty progSwitchProperty() {
        return progSwitch;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String toString() {
        return getVisibleName();
    }

    @Override
    public int compareTo(SetData setData) {
        return this.getVisibleName().compareTo(setData.getVisibleName());
    }
}
