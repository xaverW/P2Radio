package de.p2tools.p2radio.gui.tools.table;

import de.p2tools.p2lib.tools.date.P2LDateFactory;
import de.p2tools.p2radio.controller.config.ProgColorList;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.ProgIcons;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.favourite.FavouriteFactory;
import de.p2tools.p2radio.controller.data.history.HistoryFactory;
import de.p2tools.p2radio.controller.data.start.StartFactory;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class TableStationFactory {
    private TableStationFactory() {

    }

    public static void columnFactoryString(Table.TABLE_ENUM tableEnum, TableColumn<StationData, String> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(item);
                StationData data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryInteger(Table.TABLE_ENUM tableEnum, TableColumn<StationData, Integer> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }

                StationData data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryIntegerMax(Table.TABLE_ENUM tableEnum, TableColumn<StationData, Integer> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(null);
                    setText(item + "");
                }

                StationData data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }


    public static void columnFactoryGrade(Table.TABLE_ENUM tableEnum, TableColumn<StationData, Integer> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                if (item == 0) {
                    setGraphic(null);
                    setText(null);

                } else {
                    HBox hBox = new HBox(3);
                    hBox.setAlignment(Pos.CENTER);
                    for (int i = 0; i < FavouriteConstants.MAX_FAVOURITE_GRADE; ++i) {
                        if (item.longValue() > i) {
                            Label l = new Label();
                            l.setGraphic(ProgIcons.IMAGE_TABLE_FAVOURITE_GRADE.getImageView());
                            hBox.getChildren().add(l);
                        }
                    }
                    setGraphic(hBox);
                }

                StationData data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryBoolean(Table.TABLE_ENUM tableEnum, TableColumn<StationData, Boolean> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                setAlignment(Pos.CENTER);
                CheckBox box = new CheckBox();
                box.setMaxHeight(6);
                box.setMinHeight(6);
                box.setPrefSize(6, 6);
                box.setDisable(true);
                box.getStyleClass().add("checkbox-table");
                box.setSelected(item);
                setGraphic(box);

                StationData data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryLocalDate(Table.TABLE_ENUM tableEnum, TableColumn<StationData, LocalDate> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                    return;
                }

                setText(P2LDateFactory.toString(item));
                StationData data = getTableView().getItems().get(getIndex());
                set(tableEnum, data, this);
            }
        });
    }

    public static void columnFactoryButton(Table.TABLE_ENUM tableEnum, TableColumn<StationData, String> column) {
        column.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                final HBox hbox = new HBox();
                hbox.setSpacing(4);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                StationData stationData = getTableView().getItems().get(getIndex());
                final boolean playing = StartFactory.startPlaying != null &&
                        StartFactory.startPlaying.getStationUrl().equals(stationData.getStationUrl());

                if (playing) {
                    //stoppen
                    final Button btnStop = new Button("");
                    btnStop.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnStop.setTooltip(new Tooltip("Sender stoppen"));
                    btnStop.setGraphic(ProgIcons.IMAGE_TABLE_STATION_STOP_PLAY.getImageView());
                    btnStop.setOnAction((ActionEvent event) -> {
                        StartFactory.stopRunningStation();
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
                        btnStop.setMaxHeight(18);
                        btnStop.setMinHeight(18);
                    }
                    hbox.getChildren().add(btnStop);

                } else {
                    //starten
                    final Button btnPlay = new Button("");
                    btnPlay.getStyleClass().addAll("btnFunction", "btnFuncTable");
                    btnPlay.setTooltip(new Tooltip("Sender abspielen"));
                    btnPlay.setGraphic(ProgIcons.IMAGE_TABLE_STATION_PLAY.getImageView());
                    btnPlay.setOnAction((ActionEvent event) -> {
                        StartFactory.playPlayable(stationData);
                        getTableView().getSelectionModel().clearSelection();
                        getTableView().getSelectionModel().select(getIndex());
                    });

                    if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
                        btnPlay.setMaxHeight(18);
                        btnPlay.setMinHeight(18);
                    }
                    hbox.getChildren().add(btnPlay);
                }

                if (tableEnum.equals(Table.TABLE_ENUM.STATION) ||
                        tableEnum.equals(Table.TABLE_ENUM.FAVOURITE) ||
                        tableEnum.equals(Table.TABLE_ENUM.HISTORY)) {

                    final Button btn;
                    btn = new Button("");
                    btn.getStyleClass().addAll("btnFunction", "btnFuncTable");

                    if (tableEnum.equals(Table.TABLE_ENUM.STATION)) {
                        btn.setTooltip(new Tooltip("Sender als Favoriten sichern"));
                        btn.setGraphic(ProgIcons.IMAGE_TABLE_STATION_SAVE.getImageView());
                        btn.setOnAction(event -> {
                            FavouriteFactory.favouriteStation(stationData);
                        });

                    } else if (tableEnum.equals(Table.TABLE_ENUM.FAVOURITE)) {
                        btn.setTooltip(new Tooltip("Favoriten löschen"));
                        btn.setGraphic(ProgIcons.IMAGE_TABLE_FAVOURITE_DEL.getImageView());
                        btn.setOnAction(event -> {
                            FavouriteFactory.deleteFavourite(stationData);
                        });

                    } else if (tableEnum.equals(Table.TABLE_ENUM.HISTORY)) {
                        btn.setTooltip(new Tooltip("Sender aus History löschen"));
                        btn.setGraphic(ProgIcons.IMAGE_TABLE_FAVOURITE_DEL.getImageView());
                        btn.setOnAction(event -> {
                            HistoryFactory.deleteHistory(stationData);
                        });
                    }

                    if (ProgConfig.SYSTEM_SMALL_ROW_TABLE.get()) {
                        btn.setMinHeight(18);
                        btn.setMaxHeight(18);
                    }
                    hbox.getChildren().add(btn);
                }

                setGraphic(hbox);
                set(tableEnum, stationData, this);
            }
        });
    }

    private static void set(Table.TABLE_ENUM tableEnum, StationData stationData, TableCell tableCell) {
        tableCell.setStyle("");
        if (stationData != null) {

            final boolean error = stationData.getStart() != null
                    && stationData.getStart().getStartStatus().isStateError();
            final boolean playing = stationData.getStart() != null; // schließt error mit ein!
            final boolean fav = stationData.isFavourite();
            final boolean newStation = stationData.isNewStation();

            if (error) {
                if (ProgColorList.STATION_ERROR_KEY.isUse()) {
                    tableCell.setStyle(ProgColorList.STATION_ERROR_KEY.getCssFont());
                }

            } else if (playing) {
                if (ProgColorList.STATION_RUN_KEY.isUse()) {
                    tableCell.setStyle(ProgColorList.STATION_RUN_KEY.getCssFont());
                }

            } else if (fav
                    && tableEnum != null
                    && (tableEnum.equals(Table.TABLE_ENUM.STATION)
                    || tableEnum.equals(Table.TABLE_ENUM.HISTORY)
                    || tableEnum.equals(Table.TABLE_ENUM.SMALL_RADIO_STATION)
                    || tableEnum.equals(Table.TABLE_ENUM.SMALL_RADIO_HISTORY))
            ) {

                if (ProgColorList.STATION_FAVOURITE_KEY.isUse()) {
                    tableCell.setStyle(ProgColorList.STATION_FAVOURITE_KEY.getCssFont());
                }

            } else if (newStation) {
                if (ProgColorList.STATION_NEW_KEY.isUse()) {
                    tableCell.setStyle(ProgColorList.STATION_NEW_KEY.getCssFont());
                }

            }
        }
    }
}