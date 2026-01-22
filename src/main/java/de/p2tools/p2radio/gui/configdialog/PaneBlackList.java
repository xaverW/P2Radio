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

package de.p2tools.p2radio.gui.configdialog;

import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.grid.P2GridConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.BlackData;
import de.p2tools.p2radio.controller.pevent.PEvents;
import de.p2tools.p2radio.controller.picon.PIconFactory;
import de.p2tools.p2radio.gui.tools.HelpText;
import de.p2tools.p2radio.tools.stationlistfilter.BlackFilterCountHitsFactory;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneBlackList {

    private final TableView<BlackData> tableView = new TableView<>();
    private final GridPane gridPane = new GridPane();
    private final TextField name = new TextField();
    private final P2ToggleSwitch tgName = new P2ToggleSwitch("exakt:");
    private final TextField genre = new TextField();
    private final P2ToggleSwitch tgGenre = new P2ToggleSwitch("exakt:");
    private final RadioButton rbBlack = new RadioButton();
    private final RadioButton rbWhite = new RadioButton();
    private final BooleanProperty blackChanged;
    private final Stage stage;
    BooleanProperty propWhite = ProgConfig.SYSTEM_BLACKLIST_IS_WHITELIST;
    P2Listener listenerStart;
    P2Listener listenerFinished;
    private BlackData blackData = null;

    public PaneBlackList(Stage stage, BooleanProperty blackChanged) {
        this.stage = stage;
        this.blackChanged = blackChanged;
    }

    public void makeBlackTable(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(P2LibConst.PADDING));

        makeConfig(vBox);
        initTable(vBox);
        addConfigs(vBox);

        TitledPane tpBlack = new TitledPane("Blacklist", vBox);
        result.add(tpBlack);
        tpBlack.setMaxHeight(Double.MAX_VALUE);
    }

    public void close() {
        rbWhite.selectedProperty().unbindBidirectional(propWhite);
        ProgData.getInstance().pEventHandler.removeListener(listenerStart);
        ProgData.getInstance().pEventHandler.removeListener(listenerFinished);
    }

    private void makeConfig(VBox vBox) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        vBox.getChildren().add(gridPane);

        final ToggleGroup group = new ToggleGroup();
        rbBlack.setToggleGroup(group);
        rbWhite.setToggleGroup(group);
        rbBlack.setSelected(!ProgConfig.SYSTEM_BLACKLIST_IS_WHITELIST.get());

        int row = 0;
        gridPane.add(rbBlack, 0, row);
        gridPane.add(new Label("\"Sender mit Name/Genre\" werden nicht angezeigt (Blacklist)"), 1, row);
        final Button btnHelp = P2Button.helpButton(stage, "Blacklist / Whitelist",
                HelpText.BLACKLIST_WHITELIST);
        gridPane.add(btnHelp, 2, row);

        rbWhite.selectedProperty().bindBidirectional(propWhite);
        rbWhite.selectedProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
        gridPane.add(rbWhite, 0, ++row);
        gridPane.add(new Label("Nur diese \"Sender mit Name/Genre\" anzeigen (Whitelist)"), 1, row);

        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcPrefSize(),
                P2GridConstraints.getCcComputedSizeAndHgrow());
    }


    private void initTable(VBox vBox) {
        tableView.setTableMenuButtonVisible(false);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        SortedList<BlackData> sortedList;
        sortedList = new SortedList<>(ProgData.getInstance().blackDataList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        final TableColumn<BlackData, String> noColumn = new TableColumn<>("Nr");
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        noColumn.getStyleClass().add("center");

        final TableColumn<BlackData, String> nameColumn = new TableColumn<>("Sendername");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        final TableColumn<BlackData, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        final TableColumn<BlackData, Integer> hitsColumn = new TableColumn<>("Treffer");
        hitsColumn.setCellValueFactory(new PropertyValueFactory<>("countHits"));
        hitsColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableView.getColumns().addAll(noColumn, nameColumn, genreColumn, hitsColumn);
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(this::setActBlackData));

        Button btnDel = new Button("");
        btnDel.setGraphic(PIconFactory.PICON.TABLE_ABO_DEL.getFontIcon());
        btnDel.setOnAction(event -> {
            blackChanged.set(true);
            final ObservableList<BlackData> selected = tableView.getSelectionModel().getSelectedItems();

            if (selected == null || selected.isEmpty()) {
                P2Alert.showInfoNoSelection();
            } else {
                ProgData.getInstance().blackDataList.removeAll(selected);
                tableView.scrollTo(tableView.getSelectionModel().getSelectedIndex());
            }
        });

        Button btnNew = new Button("");
        btnNew.setGraphic(PIconFactory.PICON.TABLE_BOOKMARK_ADD.getFontIcon());
        btnNew.setOnAction(event -> {
            blackChanged.set(true);
            BlackData blackData = new BlackData();
            ProgData.getInstance().blackDataList.add(blackData);
            tableView.getSelectionModel().clearSelection();
            tableView.getSelectionModel().select(blackData);
            tableView.scrollTo(blackData);
        });

        final Button btnHelpCount = P2Button.helpButton(stage, "_Treffer zählen",
                HelpText.BLACKLIST_COUNT);


        Button btnSortList = new Button("_Liste nach Treffer sortieren");
        btnSortList.setTooltip(new Tooltip("Damit kann die Blacklist anhand der \"Treffer\"\n" +
                "sortiert werden."));
        btnSortList.setOnAction(a -> {
            ProgData.getInstance().blackDataList.sortIncCounter(true);
            tableView.refresh();
        });


        Button btnCountHits = new Button("_Treffer zählen");
        btnCountHits.setTooltip(new Tooltip("Damit wird die Senderliste nach \"Treffern\" durchsucht.\n" +
                "Für jeden Eintrag in der Blacklist wird gezählt,\n" +
                "wieviele Sender damit geblockt werden."));
        btnCountHits.setOnAction(a -> {
            BlackFilterCountHitsFactory.countHits(true);
            tableView.refresh();
        });

        listenerStart = new P2Listener(PEvents.LOAD_RADIO_LIST_START) {
            @Override
            public void pingGui(P2Event event) {
                btnSortList.setDisable(true);
                btnCountHits.setDisable(true);
            }
        };
        listenerFinished = new P2Listener(PEvents.LOAD_RADIO_LIST_FINISHED) {
            @Override
            public void pingGui(P2Event event) {
                btnSortList.setDisable(false);
                btnCountHits.setDisable(false);
            }
        };
        ProgData.getInstance().pEventHandler.addListener(listenerStart);
        ProgData.getInstance().pEventHandler.addListener(listenerFinished);

        HBox hBoxCount = new HBox(P2LibConst.DIST_BUTTON);
        hBoxCount.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(hBoxCount, Priority.ALWAYS);
        hBoxCount.getChildren().addAll(btnSortList, btnCountHits, btnHelpCount);

        HBox hBox = new HBox(P2LibConst.DIST_BUTTON);
        hBox.getChildren().addAll(btnNew, btnDel, hBoxCount);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        vBox.getChildren().addAll(scrollPane, hBox);
    }

    private void addConfigs(VBox vBox) {
        gridPane.getStyleClass().add("extra-pane");
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.PADDING));

        int row = 0;

        gridPane.add(new Label("Name:"), 0, ++row);
        gridPane.add(name, 1, row);
        gridPane.add(tgName, 2, row);

        gridPane.add(new Label("Genre:"), 0, ++row);
        gridPane.add(genre, 1, row);
        gridPane.add(tgGenre, 2, row);

        gridPane.getColumnConstraints().addAll(P2GridConstraints.getCcPrefSize(),
                P2GridConstraints.getCcComputedSizeAndHgrow(),
                P2GridConstraints.getCcPrefSize());

        vBox.getChildren().add(gridPane);
        gridPane.setDisable(true);

        name.textProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
        tgName.selectedProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
        genre.textProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
        tgGenre.selectedProperty().addListener((observable, oldValue, newValue) -> blackChanged.set(true));
    }

    private void setActBlackData() {
        BlackData blackDataAct = tableView.getSelectionModel().getSelectedItem();
        if (blackDataAct == blackData) {
            return;
        }

        if (blackData != null) {
            name.textProperty().unbindBidirectional(blackData.nameProperty());
            tgName.selectedProperty().unbindBidirectional(blackData.nameExactProperty());
            genre.textProperty().unbindBidirectional(blackData.genreProperty());
            tgGenre.selectedProperty().unbindBidirectional(blackData.genreExactProperty());
        }

        blackData = blackDataAct;
        gridPane.setDisable(blackData == null);
        if (blackData != null) {
            name.textProperty().bindBidirectional(blackData.nameProperty());
            tgName.selectedProperty().bindBidirectional(blackData.nameExactProperty());
            genre.textProperty().bindBidirectional(blackData.genreProperty());
            tgGenre.selectedProperty().bindBidirectional(blackData.genreExactProperty());
        }
    }
}
