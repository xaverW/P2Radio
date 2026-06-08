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

import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class StationFilterEditDialog extends P2DialogExtra {

    final ProgData progData;

    public StationFilterEditDialog(ProgData progData) {
        super(progData.primaryStage, null, "Filter ein- und ausschalten",
                true, false, false, DECO.BORDER_SMALL);
        this.progData = progData;

        init(true);
    }

    @Override
    public void make() {
        init(getVBoxCont());

        final Button btnHelp = P2Button.helpButton(getStage(), "Filter ein- und ausschalten",
                HelpText.GUI_STATIONS_EDIT_FILTER);

        Button btnOk = new Button("_Ok");
        btnOk.setOnAction(event -> close());

        addOkButton(btnOk);
        addHlpButton(btnHelp);
    }

    public void init(VBox vbox) {
        vbox.setSpacing(15);

        P2ToggleSwitch tglName = new P2ToggleSwitch("Sendername");
        tglName.setMaxWidth(Double.MAX_VALUE);
        tglName.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().stationNameVisProperty());
        vbox.getChildren().add(tglName);

        P2ToggleSwitch tglGenre = new P2ToggleSwitch("Genre");
        tglGenre.setMaxWidth(Double.MAX_VALUE);
        tglGenre.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().genreVisProperty());
        vbox.getChildren().add(tglGenre);

        P2ToggleSwitch tglCodec = new P2ToggleSwitch("Codec");
        tglCodec.setMaxWidth(Double.MAX_VALUE);
        tglCodec.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().codecVisProperty());
        vbox.getChildren().add(tglCodec);

        P2ToggleSwitch tglCountry = new P2ToggleSwitch("Land");
        tglCountry.setMaxWidth(Double.MAX_VALUE);
        tglCountry.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().countryVisProperty());
        vbox.getChildren().add(tglCountry);

        P2ToggleSwitch tglUrl = new P2ToggleSwitch("Url");
        tglUrl.setMaxWidth(Double.MAX_VALUE);
        tglUrl.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().urlVisProperty());
        vbox.getChildren().add(tglUrl);

        P2ToggleSwitch tglSomewhere = new P2ToggleSwitch("irgendwo");
        tglSomewhere.setMaxWidth(Double.MAX_VALUE);
        tglSomewhere.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().somewhereVisProperty());
        vbox.getChildren().add(tglSomewhere);

        P2ToggleSwitch tglMinMax = new P2ToggleSwitch("Bitrate Min/Max");
        tglMinMax.setMaxWidth(Double.MAX_VALUE);
        tglMinMax.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().minMaxBitVisProperty());
        vbox.getChildren().add(tglMinMax);

        P2ToggleSwitch tglOnly = new P2ToggleSwitch("\"anzeigen\"");
        tglOnly.setMaxWidth(Double.MAX_VALUE);
        tglOnly.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().onlyVisProperty());
        vbox.getChildren().add(tglOnly);
    }
}
