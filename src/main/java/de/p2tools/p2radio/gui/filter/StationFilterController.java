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

package de.p2tools.p2radio.gui.filter;

import javafx.scene.layout.VBox;

public class StationFilterController extends FilterController {

    private final StationFilterControllerTextFilter sender;
    private final StationFilterControllerFilter filter;
    private final StationFilterControllerClearFilter clearFilter;
    private final StationFilterControllerProfiles profiles;
    private final StationFilterBlackList blackList;

    public StationFilterController() {
        sender = new StationFilterControllerTextFilter();//hat separator am ende??
        filter = new StationFilterControllerFilter();
        clearFilter = new StationFilterControllerClearFilter();
        profiles = new StationFilterControllerProfiles();
        blackList = new StationFilterBlackList();

        final VBox vBox = getVBoxFilter();
        vBox.getChildren().addAll(sender,
                filter,
                clearFilter);

        getVBoxBlack().getChildren().add(profiles);
        getVBoxBlack().getChildren().add(blackList);
    }
}
