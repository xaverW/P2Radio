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

public class InitGrade {

    private final AddFavouriteDto addFavouriteDto;

    public InitGrade(AddFavouriteDto addFavouriteDto) {
        this.addFavouriteDto = addFavouriteDto;
        init();
    }

    private void init() {
        addFavouriteDto.chkGrade1.selectedProperty().addListener((u, o, n) -> setGrade());
        addFavouriteDto.chkGrade2.selectedProperty().addListener((u, o, n) -> setGrade());
        addFavouriteDto.chkGrade3.selectedProperty().addListener((u, o, n) -> setGrade());
    }

    public void makeAct() {
        int grade = addFavouriteDto.getAct().stationData.getOwnGrade();
        addFavouriteDto.chkGrade1.setSelected(grade >= 1);
        addFavouriteDto.chkGrade2.setSelected(grade >= 2);
        addFavouriteDto.chkGrade3.setSelected(grade >= 3);
    }

    public void setGrade() {
        int grade = 0;
        if (addFavouriteDto.chkGrade1.isSelected()) ++grade;
        if (addFavouriteDto.chkGrade2.isSelected()) ++grade;
        if (addFavouriteDto.chkGrade3.isSelected()) ++grade;

        if (addFavouriteDto.chkGradeAll.isSelected()) {
            for (AddFavouriteData afd : addFavouriteDto.addFavouriteData) {
                afd.stationData.setOwnGrade(grade);
            }
        } else {
            addFavouriteDto.getAct().stationData.setOwnGrade(grade);
        }
    }
}
