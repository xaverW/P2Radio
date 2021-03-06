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

package de.p2tools.p2radio.gui;

import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.tools.storedFilter.InitStoredFilter;
import de.p2tools.p2radio.tools.storedFilter.SelectedFilter;
import de.p2tools.p2radio.tools.storedFilter.SelectedFilterFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.Optional;

public class StationFilterControllerProfiles extends VBox {

    private final ComboBox<SelectedFilter> cboFilterProfiles = new ComboBox<>();
    private final MenuButton mbFilterTools = new MenuButton("");
    private final Button btnLoadFilter = new Button("laden");
    private final Button btnSaveFilter = new Button("speichern");
    private final Button btnNewFilter = new Button("neu anlegen");

    private final ProgData progData;

    private final IntegerProperty filterProp = ProgConfig.FILTER_STATION_SEL_FILTER;

    public StationFilterControllerProfiles() {
        super();
        progData = ProgData.getInstance();

        setPadding(new Insets(10));
        setSpacing(20);

        initButton();
        filterProfiles();
        initRest();

        progData.storedFilters.filterChangeProperty().addListener((observable, oldValue, newValue) -> checkCboFilter());
        checkCboFilter();
    }

    private void markFilterSelected(boolean ok) {
        if (ok) {
            cboFilterProfiles.getStyleClass().removeAll("markFilterSelected");
            cboFilterProfiles.getStyleClass().add("markFilterSelected");
        } else {
            cboFilterProfiles.getStyleClass().removeAll("markFilterSelected");
        }
    }

    private void initButton() {
        btnLoadFilter.setOnAction(a -> loadFilter());
        btnLoadFilter.disableProperty().bind(cboFilterProfiles.getSelectionModel().selectedItemProperty().isNull());
        btnLoadFilter.setGraphic(new ProgIcons().ICON_FILTER_STATION_LOAD);
        btnLoadFilter.setText("");
        btnLoadFilter.setTooltip(new Tooltip("Filterprofil wieder laden"));

        btnSaveFilter.setOnAction(a -> {
            if (cboFilterProfiles.getSelectionModel().getSelectedItem() == null
                    || PAlert.showAlertOkCancel("Speichern", "Filterprofil speichern",
                    "Soll das Filterprofil ??berschrieben werden?")) {
                saveFilter();
            }
        });
        btnSaveFilter.setGraphic(new ProgIcons().ICON_FILTER_STATION_SAVE);
        btnSaveFilter.setText("");
        btnSaveFilter.setTooltip(new Tooltip("Aktuelle Filtereinstellung als Filterprofil speichern"));

        btnNewFilter.setOnAction(a -> newFilter());
        btnNewFilter.setGraphic(new ProgIcons().ICON_FILTER_STATION_NEW);
        btnNewFilter.setText("");
        btnNewFilter.setTooltip(new Tooltip("Aktuelle Filtereinstellung als neues Filterprofil anlegen"));
    }

    private void filterProfiles() {
        // Filterprofile einrichten
        cboFilterProfiles.setItems(progData.storedFilters.getStoredFilterList());
        cboFilterProfiles.setTooltip(new Tooltip("Gespeicherte Filterprofile k??nnen\n" +
                "hier geladen werden"));

        final StringConverter<SelectedFilter> converter = new StringConverter<SelectedFilter>() {
            @Override
            public String toString(SelectedFilter selFilter) {
                return selFilter == null ? "" : selFilter.getName();
            }

            @Override
            public SelectedFilter fromString(String id) {
                final int i = cboFilterProfiles.getSelectionModel().getSelectedIndex();
                return progData.storedFilters.getStoredFilterList().get(i);
            }
        };
        cboFilterProfiles.setConverter(converter);

        final MenuItem miLoad = new MenuItem("aktuelles Filterprofil wieder laden");
        miLoad.setOnAction(e -> loadFilter());
        miLoad.disableProperty().bind(cboFilterProfiles.getSelectionModel().selectedItemProperty().isNull());

        final MenuItem miRename = new MenuItem("aktuelles Filterprofil umbenennen");
        miRename.setOnAction(e -> renameFilter());
        miRename.disableProperty().bind(cboFilterProfiles.getSelectionModel().selectedItemProperty().isNull());

        final MenuItem miDel = new MenuItem("aktuelles Filterprofil l??schen");
        miDel.setOnAction(e -> delFilter());
        miDel.disableProperty().bind(cboFilterProfiles.getSelectionModel().selectedItemProperty().isNull());

        final MenuItem miDelAll = new MenuItem("alle Filterprofile l??schen");
        miDelAll.setOnAction(e -> delAllFilter());
        miDelAll.disableProperty().bind(Bindings.size(cboFilterProfiles.getItems()).isEqualTo(0));

        final MenuItem miSave = new MenuItem("Filtereinstellungen in aktuellem Filterprofil speichern");
        miSave.setOnAction(e -> saveFilter());
        miSave.disableProperty().bind(cboFilterProfiles.getSelectionModel().selectedItemProperty().isNull());

        final MenuItem miNew = new MenuItem("Filtereinstellungen in neuem Filterprofil speichern");
        miNew.setOnAction(e -> newFilter());

        final MenuItem miReset = new MenuItem("alle Filterprofile wieder herstellen");
        miReset.setOnAction(e -> resetFilter());

        mbFilterTools.setGraphic(new ProgIcons().ICON_BUTTON_MENU);
        mbFilterTools.getItems().addAll(miLoad, miRename, miDel, miDelAll, miSave, miNew, /*miAbo,*/ new SeparatorMenuItem(), miReset);
        mbFilterTools.setTooltip(new Tooltip("Gespeicherte Filterprofile bearbeiten"));

        cboFilterProfiles.getSelectionModel().select(filterProp.get());
        filterProp.bind(cboFilterProfiles.getSelectionModel().selectedIndexProperty());

        cboFilterProfiles.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadFilter();
            }
        });

    }

    private void initRest() {
        // Filterprofile
        HBox hBox = new HBox(10);
        btnLoadFilter.setMaxWidth(Double.MAX_VALUE);
        btnSaveFilter.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnLoadFilter, Priority.ALWAYS);
        HBox.setHgrow(btnSaveFilter, Priority.ALWAYS);
        hBox.getChildren().addAll(btnLoadFilter, btnNewFilter, btnSaveFilter);
        getChildren().add(hBox);

        cboFilterProfiles.setMaxWidth(Double.MAX_VALUE);
//        final Button btnHelp = PButton.helpButton("Filter", HelpText.GUI_STATION_FILTER);

        hBox = new HBox(10);
        HBox.setHgrow(cboFilterProfiles, Priority.ALWAYS);
        hBox.getChildren().addAll(cboFilterProfiles, mbFilterTools);


        VBox vBox = new VBox(3);
        vBox.getChildren().addAll(new Label("Filterprofile:"), hBox);
        getChildren().add(vBox);

//        hBox = new HBox(10);
//        hBox.getChildren().addAll(mbFilterTools, PGuiTools.getHBoxGrower(), btnHelp);
//        getChildren().add(hBox);
    }

    private void loadFilter() {
        progData.storedFilters.setActFilterSettings(cboFilterProfiles.getSelectionModel().getSelectedItem());
    }

    private void saveFilter() {
        final SelectedFilter sf = cboFilterProfiles.getSelectionModel().getSelectedItem();
        if (sf == null) {
            newFilter();
        } else {
            progData.storedFilters.saveStoredFilter(sf);
            checkCboFilter();
        }
    }

    private void delFilter() {
        progData.storedFilters.removeStoredFilter(cboFilterProfiles.getSelectionModel().getSelectedItem());
        cboFilterProfiles.getSelectionModel().selectFirst();
    }

    private void delAllFilter() {
        if (PAlert.showAlertOkCancel("L??schen", "Filterprofile l??schen",
                "Sollen alle Filterprofile gel??scht werden?")) {
            progData.storedFilters.removeAllStoredFilter();
            cboFilterProfiles.getSelectionModel().selectFirst();
        }
    }

    private void resetFilter() {
        if (PAlert.showAlertOkCancel("Zur??cksetzen", "Filterprofile zur??cksetzen",
                "Sollen alle Filterprofile gel??scht " +
                        "und durch die Profile vom ersten Programmstart " +
                        "ersetzt werden?")) {
            progData.storedFilters.getStoredFilterList().clear();
            InitStoredFilter.initFilter();
            cboFilterProfiles.getSelectionModel().selectFirst();
        }
    }

    private void newFilter() {
        final TextInputDialog dialog = new TextInputDialog(progData.storedFilters.getNextName());
        dialog.setTitle("Filterprofilname");
        dialog.setHeaderText("Den Namen des Filterprofils vorgeben");
        dialog.setContentText("Name:");
        dialog.setResizable(true);
        dialog.initOwner(progData.primaryStage);

        final Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            progData.storedFilters.addNewStoredFilter(result.get());
            cboFilterProfiles.getSelectionModel().selectLast();
        }
    }

    private void renameFilter() {
        final SelectedFilter sf = cboFilterProfiles.getSelectionModel().getSelectedItem();
        if (sf == null) {
            return;
        }
        final TextInputDialog dialog = new TextInputDialog(sf.getName());
        dialog.setTitle("Filterprofil umbenennen");
        dialog.setHeaderText("Den Namen des Filterprofils ??ndern");
        dialog.setContentText("Neuer Name:");
        dialog.setResizable(true); // sonst geht der Dialog nicht "auf" und l??sst sich nicht vergr????ern, bug??
        dialog.initOwner(progData.primaryStage);

        final Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            sf.setName(result.get());
            cboFilterProfiles.getSelectionModel().select(sf);
        }
    }

    private void checkCboFilter() {
        SelectedFilter sf = progData.storedFilters.getActFilterSettings();
        SelectedFilter sfCbo = cboFilterProfiles.getSelectionModel().getSelectedItem();
        if (SelectedFilterFactory.compareFilterWithoutNameOfFilter(sf, sfCbo)) {
            markFilterSelected(true);
        } else {
            markFilterSelected(false);
        }
    }

}
