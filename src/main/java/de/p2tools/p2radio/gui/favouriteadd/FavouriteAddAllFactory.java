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

import de.p2tools.p2lib.guitools.P2Color;
import de.p2tools.p2radio.controller.config.ProgConfig;

public class FavouriteAddAllFactory {
    private FavouriteAddAllFactory() {
    }

    public static void init(AddFavouriteDto addFavouriteDto) {
        if (addFavouriteDto.addFavouriteData.length == 1) {
            // wenns nur einen gibt, macht dann keinen Sinn
            addFavouriteDto.btnAll.setVisible(false);
            addFavouriteDto.btnAll.setManaged(false);

            addFavouriteDto.chkCollectionAll.setVisible(false);
            addFavouriteDto.chkCollectionAll.setManaged(false);
            addFavouriteDto.chkGradeAll.setVisible(false);
            addFavouriteDto.chkGradeAll.setManaged(false);
            addFavouriteDto.chkDescriptionAll.setVisible(false);
            addFavouriteDto.chkDescriptionAll.setManaged(false);

        } else {
            addFavouriteDto.chkCollectionAll.getStyleClass().add("checkBoxAll");
            addFavouriteDto.chkGradeAll.getStyleClass().add("checkBoxAll");
            addFavouriteDto.chkDescriptionAll.getStyleClass().add("checkBoxAll");

            addFavouriteDto.chkCollectionAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addFavouriteDto);
                if (addFavouriteDto.chkCollectionAll.isSelected()) {
                    addFavouriteDto.initCollection.setCollection();
                }
            });
            addFavouriteDto.chkGradeAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addFavouriteDto);
                if (addFavouriteDto.chkGradeAll.isSelected()) {
                    addFavouriteDto.initGrade.setGrade();
                }
            });
            addFavouriteDto.chkDescriptionAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
                addCheckAllCss(addFavouriteDto);
                if (addFavouriteDto.chkDescriptionAll.isSelected()) {
                    addFavouriteDto.initDescription.setDescription();
                }
            });

            addFavouriteDto.btnAll.setOnAction(a -> changeAll(addFavouriteDto));
            addCheckAllCss(addFavouriteDto);
        }
    }

    private static void changeAll(AddFavouriteDto addFavouriteDto) {
        boolean isNotSelected = !isAllSelected(addFavouriteDto);

        addFavouriteDto.chkCollectionAll.setSelected(isNotSelected);
        addFavouriteDto.chkGradeAll.setSelected(isNotSelected);
        addFavouriteDto.chkDescriptionAll.setSelected(isNotSelected);

        addCheckAllCss(addFavouriteDto);
    }

    private static void addCheckAllCss(AddFavouriteDto addFavouriteDto) {
        if (isAllSelected(addFavouriteDto)) {
            final String c = P2Color.getCssColor(FavouriteAddDialogFactory.getBlue());
            addFavouriteDto.btnAll.setStyle("-fx-text-fill: " + c);

        } else {
            if (ProgConfig.SYSTEM_DARK_THEME.getValue()) {
                addFavouriteDto.btnAll.setStyle("-fx-text-fill: white");
            } else {
                addFavouriteDto.btnAll.setStyle("-fx-text-fill: black");
            }
        }
    }

    private static boolean isAllSelected(AddFavouriteDto addFavouriteDto) {
        return addFavouriteDto.chkCollectionAll.isSelected() ||
                addFavouriteDto.chkGradeAll.isSelected() ||
                addFavouriteDto.chkDescriptionAll.isSelected();
    }
}
