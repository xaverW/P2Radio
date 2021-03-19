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
import de.p2tools.p2Lib.configFile.config.ConfigBoolPropExtra;
import de.p2tools.p2Lib.configFile.config.ConfigPDataList;
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class SetDataProps extends SetDataBase {

    final ProgramList programList = new ProgramList();

    private StringProperty id = new SimpleStringProperty("");
    private StringProperty visibleName = new SimpleStringProperty("");
    private StringProperty prefix = new SimpleStringProperty("");
    private StringProperty suffix = new SimpleStringProperty("");
    private BooleanProperty play = new SimpleBooleanProperty(false);//ist das Standard-Set
    private StringProperty description = new SimpleStringProperty("");

    public SetDataProps() {
    }

    public boolean addProg(ProgramData prog) {
        return programList.add(prog);
    }

    public ProgramList getProgramList() {
        return programList;
    }

    public ProgramData getProg(int i) {
        return programList.get(i);
    }

    public boolean progsContainPath() {
        // ein Programmschalter mit
        // "**" (Pfad/Datei) oder %a (Pfad) oder %b (Datei)
        // damit ist es ein Set zum Speichern
        boolean ret = false;

        for (ProgramData progData : programList) {
            if (progData.getProgSwitch().contains("**")
                    || progData.getProgSwitch().contains("%a")
                    || progData.getProgSwitch().contains("%b")) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("id", SetDataFieldNames.ID, id));
        list.add(new ConfigStringPropExtra("visibleName", SetDataFieldNames.VISIBLE_NAME, visibleName));
        list.add(new ConfigStringPropExtra("praefixDirect", SetDataFieldNames.PRAEFIX_DIRECT, prefix));
        list.add(new ConfigStringPropExtra("suffixDirect", SetDataFieldNames.SUFFIX_DIRECT, suffix));
        list.add(new ConfigBoolPropExtra("isPlay", SetDataFieldNames.IS_PLAY, play));
        list.add(new ConfigStringPropExtra("description", SetDataFieldNames.DESCRIPTION, description));
        list.add(new ConfigPDataList(programList));

        return list.toArray(new Config[]{});
    }


    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getVisibleName() {
        return visibleName.get();
    }

    public StringProperty visibleNameProperty() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName.set(visibleName);
    }

    public String getPrefix() {
        return prefix.get();
    }

    public StringProperty prefixProperty() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix.set(prefix);
    }

    public String getSuffix() {
        return suffix.get();
    }

    public StringProperty suffixProperty() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix.set(suffix);
    }

    public boolean isPlay() {
        return play.get();
    }

    public BooleanProperty playProperty() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play.set(play);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
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
