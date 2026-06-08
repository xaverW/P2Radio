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

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.mediathek.filter.FilterCheckRegEx;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StationFilterControllerTextFilter extends VBox {

    private final MenuButton mbCodec = new MenuButton("");
    private final ComboBox<String> cboCountry = new ComboBox<>();
    private final ComboBox<String> cboGenre = new ComboBox<>();
    private final TextField txtStationName = new TextField();
    private final TextField txtUrl = new TextField();
    private final TextField txtSomewhere = new TextField();
    private final ArrayList<MenuItemClass> codecMenuItemsList = new ArrayList<>();
    private final ProgData progData;

    public StationFilterControllerTextFilter() {
        super();
        progData = ProgData.getInstance();
        setPadding(new Insets(0, P2LibConst.PADDING, P2LibConst.PADDING, P2LibConst.PADDING));
        setSpacing(P2LibConst.DIST_BUTTON);

        initCodecFilter();
        initCountryFilter();
        initGenreFilter();
        initStringFilter();
        addFilter();
    }

    private void initCodecFilter() {
        mbCodec.getStyleClass().add("cbo-menu");
        mbCodec.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(mbCodec, Priority.ALWAYS);

        initCodecMenu();
        progData.storedFilters.getActFilterSettings().codecProperty().addListener((observable, oldValue, newValue) -> {
            initCodecMenu();
        });
        progData.filterWorker.getAllCodecsList().addListener((ListChangeListener<String>) c -> initCodecMenu());
        mbCodec.textProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().codecProperty());
    }

    private void initCodecMenu() {
        mbCodec.getItems().clear();
        codecMenuItemsList.clear();

        List<String> codecFilterList = new ArrayList<>();
        String codecFilter = progData.storedFilters.getActFilterSettings().codecProperty().get();
        if (codecFilter != null) {
            if (codecFilter.contains(",")) {
                codecFilterList.addAll(Arrays.asList(codecFilter.replace(" ", "").toLowerCase().split(",")));
            } else {
                codecFilterList.add(codecFilter.toLowerCase());
            }
            codecFilterList.stream().forEach(s -> s = s.trim());
        }

        CheckBox miCheckAll = new CheckBox();
        miCheckAll.setVisible(false);

        Button btnAll = new Button("Auswahl löschen");
        btnAll.getStyleClass().add("cbo-menu-button");
        btnAll.setMaxWidth(Double.MAX_VALUE);
        btnAll.minWidthProperty().bind(mbCodec.widthProperty().add(-50));
        btnAll.setOnAction(e -> {
            clearMenuText(mbCodec, codecMenuItemsList);
            mbCodec.hide();
        });

        HBox hBoxAll = new HBox(P2LibConst.DIST_BUTTON);
        hBoxAll.setAlignment(Pos.CENTER_LEFT);
        hBoxAll.getChildren().addAll(miCheckAll, btnAll);

        CustomMenuItem cmiAll = new CustomMenuItem(hBoxAll);
        mbCodec.getItems().add(cmiAll);

        for (String s : progData.filterWorker.getAllCodecsList()) {
            if (s.isEmpty()) {
                continue;
            }

            CheckBox miCheck = new CheckBox();
            if (codecFilterList.contains(s.toLowerCase())) {
                miCheck.setSelected(true);
            }
            miCheck.setOnAction(a -> setMenuText(mbCodec, codecMenuItemsList));

            MenuItemClass menuItemClass = new MenuItemClass(s, miCheck);
            codecMenuItemsList.add(menuItemClass);

            Button btnCodec = new Button(s);
            btnCodec.getStyleClass().add("cbo-menu-button");
            btnCodec.setMaxWidth(Double.MAX_VALUE);
            btnCodec.minWidthProperty().bind(mbCodec.widthProperty().add(-50));
            btnCodec.setOnAction(e -> {
                setCheckBoxAndMenuText(menuItemClass, mbCodec, codecMenuItemsList);
                mbCodec.hide();
            });

            HBox hBox = new HBox(10);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(miCheck, btnCodec);

            CustomMenuItem cmi = new CustomMenuItem(hBox);
            mbCodec.getItems().add(cmi);
        }
    }

    private void initCountryFilter() {
        cboCountry.editableProperty().set(false);
        cboCountry.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cboCountry.setVisibleRowCount(25);
        cboCountry.valueProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().countryProperty());
        cboCountry.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                progData.storedFilters.getActFilterSettings().setCountry(newValue);
            }
        });

        initCountryMenu();
        progData.storedFilters.getActFilterSettings().countryProperty().addListener((observable, oldValue, newValue) -> {
            initCountryMenu();
        });
        progData.filterWorker.getAllCountryLList().addListener((ListChangeListener<String>) c -> initCountryMenu());
    }

    private void initGenreFilter() {
        new FilterCheckRegEx(cboGenre.getEditor());
        cboGenre.editableProperty().set(true);
        cboGenre.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cboGenre.setVisibleRowCount(25);
        cboGenre.valueProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().genreProperty());
        cboGenre.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && newValue != null) {
                progData.storedFilters.getActFilterSettings().setGenre(newValue);
            }
        });
        initGenreMenu();

        progData.storedFilters.getActFilterSettings().genreProperty().addListener((observable, oldValue, newValue) -> {
            initGenreMenu();
        });
        progData.filterWorker.getAllGenreList().addListener((ListChangeListener<String>) c -> initGenreMenu());
    }

    private void initCountryMenu() {
        cboCountry.setItems(progData.filterWorker.getAllCountryLList());
    }

    private void initGenreMenu() {
        cboGenre.setItems(progData.filterWorker.getAllGenreList());
    }

    private void setCheckBoxAndMenuText(MenuItemClass cmi, MenuButton mb, ArrayList<MenuItemClass> menuItemsList) {
        for (MenuItemClass cm : menuItemsList) {
            cm.getCheckBox().setSelected(false);
        }
        cmi.getCheckBox().setSelected(true);
        setMenuText(mb, menuItemsList);
    }

    private void clearMenuText(MenuButton mb, ArrayList<MenuItemClass> menuItemsList) {
        for (MenuItemClass cmi : menuItemsList) {
            cmi.getCheckBox().setSelected(false);
        }
        mb.setText("");
    }

    private void setMenuText(MenuButton mb, ArrayList<MenuItemClass> menuItemsList) {
        String text = "";
        for (MenuItemClass cmi : menuItemsList) {
            if (cmi.getCheckBox().isSelected()) {
                text = text + (text.isEmpty() ? "" : ", ") + cmi.getText();
            }
        }
        mb.setText(text);
    }

    private void initStringFilter() {
        txtStationName.textProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().stationNameProperty());
        txtUrl.textProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().urlProperty());
        txtSomewhere.textProperty().bindBidirectional(progData.storedFilters.getActFilterSettings().somewhereProperty());
        new FilterCheckRegEx(txtStationName);
        new FilterCheckRegEx(txtUrl);
        new FilterCheckRegEx(txtSomewhere);
    }

    private void addFilter() {
        VBox vBox = new VBox(P2LibConst.DIST_BUTTON);

        addTxt("Sender", txtStationName, vBox, progData.storedFilters.getActFilterSettings().stationNameVisProperty());
        addGenre(vBox);
        addCodec(vBox);
        addCountry(vBox);
        addTxt("URL", txtUrl, vBox, progData.storedFilters.getActFilterSettings().urlVisProperty());
        addTxt("Irgendwo", txtSomewhere, vBox, progData.storedFilters.getActFilterSettings().somewhereVisProperty());

        vBox.visibleProperty().bind(
                (progData.storedFilters.getActFilterSettings().stationNameVisProperty()
                        .or(progData.storedFilters.getActFilterSettings().genreVisProperty()
                                .or(progData.storedFilters.getActFilterSettings().codecVisProperty())
                                .or(progData.storedFilters.getActFilterSettings().countryVisProperty()
                                        .or(progData.storedFilters.getActFilterSettings().urlVisProperty()
                                                .or(progData.storedFilters.getActFilterSettings().somewhereVisProperty())
                                        )))));

        vBox.managedProperty().bind(vBox.visibleProperty());
        getChildren().add(vBox);
    }

    private void addTxt(String txt, Control control, VBox vBoxComplete, BooleanProperty booleanProperty) {
        VBox vBox = new VBox();
        Label label = new Label(txt);
        vBox.getChildren().addAll(label, control);
        vBoxComplete.getChildren().add(vBox);

        vBox.visibleProperty().bind(booleanProperty);
        vBox.managedProperty().bind(booleanProperty);
    }

    private void addCodec(VBox vBoxComplete) {
        VBox vBox = new VBox();
        Label label = new Label("Codec");
        vBox.getChildren().addAll(label, mbCodec);
        vBoxComplete.getChildren().add(vBox);

        vBox.visibleProperty().bind(progData.storedFilters.getActFilterSettings().codecVisProperty());
        vBox.managedProperty().bind(progData.storedFilters.getActFilterSettings().codecVisProperty());
    }

    private void addCountry(VBox vBoxComplete) {
        VBox vBox = new VBox();
        Label label = new Label("Land");
        vBox.getChildren().addAll(label, cboCountry);
        vBoxComplete.getChildren().add(vBox);

        vBox.visibleProperty().bind(progData.storedFilters.getActFilterSettings().countryVisProperty());
        vBox.managedProperty().bind(progData.storedFilters.getActFilterSettings().countryVisProperty());
    }

    private void addGenre(VBox vBoxComplete) {
        VBox vBox = new VBox();
        Label label = new Label("Genre");
        vBox.getChildren().addAll(label, cboGenre);
        vBoxComplete.getChildren().add(vBox);

        vBox.visibleProperty().bind(progData.storedFilters.getActFilterSettings().genreVisProperty());
        vBox.managedProperty().bind(progData.storedFilters.getActFilterSettings().genreVisProperty());
    }

    private class MenuItemClass {

        private final String text;
        private final CheckBox checkBox;

        MenuItemClass(String text, CheckBox checkbox) {
            this.text = text;
            this.checkBox = checkbox;
        }

        public String getText() {
            return text;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }
}
