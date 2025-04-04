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

package de.p2tools.p2radio.gui.filter;

import de.p2tools.p2lib.P2LibConst;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FilterController extends VBox {

    public static final int FILTER_SPACING_TEXTFILTER = 10;

    public FilterController() {
        setSpacing(P2LibConst.SPACING_VBOX);
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    public VBox getVBoxFilter() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(P2LibConst.PADDING));
        vbox.setSpacing(FILTER_SPACING_TEXTFILTER);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        getChildren().addAll(vbox);
        return vbox;
    }

    public VBox getVBoxBlack() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("extra-pane-filter");
        vBox.setPadding(new Insets(P2LibConst.PADDING));
        vBox.setSpacing(FILTER_SPACING_TEXTFILTER);
        getChildren().addAll(vBox);
        return vBox;
    }
}
