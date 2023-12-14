/*
 * P2tools Copyright (C) 2023 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
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


package de.p2tools.p2radio.gui.favouriteadd;

import de.p2tools.p2radio.controller.config.ProgData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class InitCollection {
    private final ObservableList<String> collectionList;
    AddFavouriteDto addFavouriteDto;

    public InitCollection(AddFavouriteDto addFavouriteDto) {
        this.addFavouriteDto = addFavouriteDto;
        this.collectionList = FXCollections.observableArrayList();
        init();
    }

    private void init() {
        ProgData.getInstance().favouriteList.forEach(stationData -> {
            String collection = stationData.getCollectionName();
            if (!collection.isEmpty() && !collectionList.contains(collection)) {
                collectionList.add(collection);
            }
        });

        if (collectionList.isEmpty() ||
                collectionList.size() == 1 && collectionList.get(0).isEmpty()) {
            //leer oder und nur ein leerer Eintrag
            collectionList.clear();
            collectionList.add("Rock");
        }

        addFavouriteDto.cboCollection.setEditable(true);
        addFavouriteDto.cboCollection.setItems(collectionList);

        addFavouriteDto.cboCollection.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Collection: " + addFavouriteDto.cboCollection.getEditor().getText());
            if (!addFavouriteDto.cboCollection.isFocused()) {
                System.out.println("not");
                return;
            }
            if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                setCollection();
            }
        });
    }

    public void makeAct() {
        if (!addFavouriteDto.cboCollection.getItems().contains(addFavouriteDto.getAct().stationData.getCollectionName())) {
            addFavouriteDto.cboCollection.getItems().add(addFavouriteDto.getAct().stationData.getCollectionName());
        }
        addFavouriteDto.cboCollection.getSelectionModel().select(addFavouriteDto.getAct().stationData.getCollectionName());
    }

    public void setCollection() {
        // beim Ändern der cbo oder manuellem Eintragen
        String collection = addFavouriteDto.cboCollection.getEditor().getText();
        if (!addFavouriteDto.cboCollection.getItems().contains(collection)) {
            addFavouriteDto.cboCollection.getItems().add(collection);
        }

        if (addFavouriteDto.chkCollectionAll.isSelected()) {
            Arrays.stream(addFavouriteDto.addFavouriteData).forEach(addPodcastData -> {
                addPodcastData.stationData.setCollectionName(collection);
            });
        } else {
            addFavouriteDto.getAct().stationData.setCollectionName(collection);
        }
    }
}
