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
import de.p2tools.p2Lib.configFile.config.ConfigStringPropExtra;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class BlackDataProps extends PDataSample<BlackData> {
    public static final String TAG = "BlackData";

    private int no = 0;
    private final StringProperty name = new SimpleStringProperty("");
    private final BooleanProperty nameExact = new SimpleBooleanProperty(true);
    private final StringProperty genre = new SimpleStringProperty("");
    private final BooleanProperty genreExact = new SimpleBooleanProperty(true);
    private int countHits = 0;

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public Config[] getConfigsArr() {
        ArrayList<Config> list = new ArrayList<>();
        list.add(new ConfigStringPropExtra("name", BlackDataFieldNames.NAME, name));
        list.add(new ConfigBoolPropExtra("nameExact", BlackDataFieldNames.NAME_EXACT, nameExact));
        list.add(new ConfigStringPropExtra("genre", BlackDataFieldNames.GENRE, genre));
        list.add(new ConfigBoolPropExtra("genreExact", BlackDataFieldNames.GENRE_EXACT, genreExact));

        return list.toArray(new Config[]{});
    }

    public int getCountHits() {
        return countHits;
    }

    public void setCountHits(int countHits) {
        this.countHits = countHits;
    }

    public synchronized void incCountHits() {
        ++this.countHits;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isNameExact() {
        return nameExact.get();
    }

    public BooleanProperty nameExactProperty() {
        return nameExact;
    }

    public void setNameExact(boolean nameExact) {
        this.nameExact.set(nameExact);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public boolean isGenreExact() {
        return genreExact.get();
    }

    public BooleanProperty genreExactProperty() {
        return genreExact;
    }

    public void setGenreExact(boolean genreExact) {
        this.genreExact.set(genreExact);
    }
}
