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

import de.p2tools.p2radio.tools.storedfilter.Filter;

public class BlackData extends BlackDataProps {
    public Filter fName = new Filter();
    public Filter fGenre = new Filter();

    public BlackData() {
        super();
        initFilter();
    }

    public BlackData(String name, String genre) {
        super();
        initFilter();

        setName(name);
        setGenre(genre);
    }

    public void createFilter() {
        fName.filter = getName();
        fName.exact = isNameExact();
        fName.makeFilterArray();

        fGenre.filter = getGenre();
        fGenre.exact = isGenreExact();
        fGenre.makeFilterArray();
    }

    private void initFilter() {
        nameProperty().addListener(l -> createFilter());
        nameExactProperty().addListener(l -> createFilter());
        genreProperty().addListener(l -> createFilter());
        genreExactProperty().addListener(l -> createFilter());
    }
}
