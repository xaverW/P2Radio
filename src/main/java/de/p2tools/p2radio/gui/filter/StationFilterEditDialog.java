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

import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.gui.tools.HelpText;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class StationFilterEditDialog extends PDialogExtra {

    final ProgData progData;

    public StationFilterEditDialog(ProgData progData) {
        super(progData.primaryStage, null, "Filter ein- und ausschalten");
        this.progData = progData;

        init(true);
    }

    @Override
    public void make() {
        init(getvBoxCont());

        final Button btnHelp = PButton.helpButton(getStage(), "Filter ein- und ausschalten",
                HelpText.GUI_STATIONS_EDIT_FILTER);

        Button btnOk = new Button("_Ok");
        btnOk.setOnAction(event -> close());

        addOkButton(btnOk);
        addHlpButton(btnHelp);
    }

    public void init(VBox vbox) {
        vbox.setSpacing(15);

        PToggleSwitch tglName = new PToggleSwitch("Sendername");
        tglName.setMaxWidth(Double.MAX_VALUE);
        tglName.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().stationNameVisProperty());
        vbox.getChildren().add(tglName);

        PToggleSwitch tglGenre = new PToggleSwitch("Genre");
        tglGenre.setMaxWidth(Double.MAX_VALUE);
        tglGenre.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().genreVisProperty());
        vbox.getChildren().add(tglGenre);

        PToggleSwitch tglCodec = new PToggleSwitch("Codec");
        tglCodec.setMaxWidth(Double.MAX_VALUE);
        tglCodec.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().codecVisProperty());
        vbox.getChildren().add(tglCodec);

        PToggleSwitch tglCountry = new PToggleSwitch("Land");
        tglCountry.setMaxWidth(Double.MAX_VALUE);
        tglCountry.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().countryVisProperty());
        vbox.getChildren().add(tglCountry);

        PToggleSwitch tglUrl = new PToggleSwitch("Url");
        tglUrl.setMaxWidth(Double.MAX_VALUE);
        tglUrl.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().urlVisProperty());
        vbox.getChildren().add(tglUrl);

        PToggleSwitch tglSomewhere = new PToggleSwitch("irgendwo");
        tglSomewhere.setMaxWidth(Double.MAX_VALUE);
        tglSomewhere.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().somewhereVisProperty());
        vbox.getChildren().add(tglSomewhere);

        PToggleSwitch tglMinMax = new PToggleSwitch("Bitrate Min/Max");
        tglMinMax.setMaxWidth(Double.MAX_VALUE);
        tglMinMax.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().minMaxBitVisProperty());
        vbox.getChildren().add(tglMinMax);

        PToggleSwitch tglOnly = new PToggleSwitch("\"anzeigen\"");
        tglOnly.setMaxWidth(Double.MAX_VALUE);
        tglOnly.selectedProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().onlyVisProperty());
        vbox.getChildren().add(tglOnly);
    }
}
