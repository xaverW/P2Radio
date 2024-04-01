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

package de.p2tools.p2radio.gui.dialog;


import de.p2tools.p2lib.dialogs.dialog.P2DialogExtra;
import de.p2tools.p2lib.guitools.P2BigButton;
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2radio.controller.data.ProgIconsP2Radio;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class QuitDialogController extends P2DialogExtra {

    private final StackPane stackPane = new StackPane();
    private final P2MaskerPane maskerPane = new P2MaskerPane();
    private boolean canQuit = false;

    public QuitDialogController(Stage stage) {
        super(stage, null, "Programm beenden", true, true);
        init(true);
    }

    @Override
    public void make() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(25);

        maskerPane.switchOffMasker();
        maskerPane.setButtonText("Abbrechen");
        maskerPane.getButton().setOnAction(a -> close());

        Label headerLabel = new Label("Es laufen noch Radiosender!");
        headerLabel.setStyle("-fx-font-size: 1.5em;");

        P2BigButton cancelButton = new P2BigButton(ProgIconsP2Radio.ICON_BUTTON_QUIT.getImageView(),
                "Nicht beenden", "");
        cancelButton.setOnAction(e -> {
            close();
        });

        P2BigButton quitButton = new P2BigButton(ProgIconsP2Radio.ICON_BUTTON_QUIT.getImageView(),
                "Beenden", "Alle laufenden Sender abbrechen und das Programm beenden.");
        quitButton.setOnAction(e -> {
            canQuit = true;
            close();
        });

        gridPane.add(ProgIconsP2Radio.ICON_DIALOG_QUIT.getImageView(), 0, 0, 1, 1);
        gridPane.add(headerLabel, 1, 0);
        gridPane.add(cancelButton, 1, 1);
        gridPane.add(quitButton, 1, 2);

        ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);
        stackPane.getChildren().addAll(gridPane, maskerPane);
        getVBoxCont().getChildren().addAll(stackPane);
    }

    public void close() {
        maskerPane.switchOffMasker();
        super.close();
    }

    public boolean canTerminate() {
        return canQuit;
    }
}