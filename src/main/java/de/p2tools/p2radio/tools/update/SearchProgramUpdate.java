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

package de.p2tools.p2radio.tools.update;

import de.p2tools.p2Lib.checkForActInfos.FoundAll;
import de.p2tools.p2Lib.checkForActInfos.FoundSearchData;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.application.Platform;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public class SearchProgramUpdate {

    private final ProgData progData;
    private static final String TITLE_TEXT_PROGRAM_VERSION_IS_UPTODATE = "Programmversion ist aktuell";
    private static final String TITLE_TEXT_PROGRAMMUPDATE_EXISTS = "Ein Programmupdate ist verfügbar";
    private String title = "";

    public SearchProgramUpdate(ProgData progData) {
        this.progData = progData;
    }

    public SearchProgramUpdate(Stage stage, ProgData progData) {
        this.progData = progData;
    }

    /**
     * @return
     */
    public void searchNewProgramVersion(boolean showAllways) {
        FoundSearchData foundSearchData = new FoundSearchData(
                progData.primaryStage,

                ProgConfig.SYSTEM_UPDATE_SEARCH_ACT,
                ProgConfig.SYSTEM_UPDATE_SEARCH_BETA,
                ProgConfig.SYSTEM_UPDATE_SEARCH_DAILY,

                ProgConfig.SYSTEM_UPDATE_LAST_INFO,
                ProgConfig.SYSTEM_UPDATE_LAST_ACT,
                ProgConfig.SYSTEM_UPDATE_LAST_BETA,
                ProgConfig.SYSTEM_UPDATE_LAST_DAILY,

                ProgConst.URL_WEBSITE,
                ProgConst.URL_WEBSITE_DOWNLOAD,
                ProgConst.PROGRAM_NAME,
                ProgConfig.SYSTEM_PROG_VERSION.getValue()
        );

        FoundAll.foundAll(foundSearchData, showAllways);
        setTitleInfo(foundSearchData.foundNewVersionProperty().getValue());
    }

    private void setTitleInfo(boolean newVersion) {
        title = progData.primaryStage.getTitle();
        if (newVersion) {
            Platform.runLater(() -> setUpdateTitle());
        } else {
            Platform.runLater(() -> setNoUpdateTitle());
        }
        try {
            sleep(10_000);
        } catch (Exception ignore) {
        }
        Platform.runLater(() -> setOrgTitle());
    }

    private void setUpdateTitle() {
        progData.primaryStage.setTitle(TITLE_TEXT_PROGRAMMUPDATE_EXISTS);
    }

    private void setNoUpdateTitle() {
        progData.primaryStage.setTitle(TITLE_TEXT_PROGRAM_VERSION_IS_UPTODATE);
    }

    private void setOrgTitle() {
        progData.primaryStage.setTitle(title);
    }
}
