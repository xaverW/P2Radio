/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio;

import de.p2tools.p2Lib.dialogs.ProgInfoDialog;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pMask.PMaskerPane;
import de.p2tools.p2radio.controller.config.ProgData;
import de.p2tools.p2radio.controller.data.favourite.Favourite;
import de.p2tools.p2radio.controller.starter.MTNotification;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.HashSet;

public class MTPTester {
    private final ProgInfoDialog progInfoDialog;
    private final HashSet<String> hashSet = new HashSet<>();
    private final ProgData progData;
    private final TextArea textArea = new TextArea();
    private String text = "";
    private final PMaskerPane maskerPane = new PMaskerPane();
    private final WaitTask waitTask = new WaitTask();

    public MTPTester(ProgData progData) {
        this.progData = progData;
        this.progInfoDialog = new ProgInfoDialog(false);
        addProgTest();
    }

    public void showDialog() {
        progInfoDialog.showDialog();
    }

    private void addProgTest() {
        if (progInfoDialog != null) {

            final GridPane gridPane = new GridPane();
            gridPane.setHgap(5);
            gridPane.setVgap(5);
            gridPane.setPadding(new Insets(10));
            gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow());

            maskerPane.setMaskerVisible(false);
            maskerPane.setButtonText("Abbrechen");
            maskerPane.getButton().setOnAction(a -> close());

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(gridPane, maskerPane);
            progInfoDialog.getvBoxCont().getChildren().addAll(stackPane);


            // Create the ButtonBar instance
            ButtonBar buttonBar = new ButtonBar();
            Button okButton = new Button("OK");
            ButtonBar.setButtonData(okButton, ButtonBar.ButtonData.OK_DONE);
            Button cButton = new Button("Abbrechen");
            ButtonBar.setButtonData(cButton, ButtonBar.ButtonData.CANCEL_CLOSE);
            buttonBar.getButtons().addAll(okButton, cButton);
            progInfoDialog.getvBoxCont().getChildren().add(buttonBar);


            Text text = new Text("Debugtools");
            text.setFont(Font.font(null, FontWeight.BOLD, 15));

            Button btnStartWaiting = new Button("start waiting");
            btnStartWaiting.setMaxWidth(Double.MAX_VALUE);
            btnStartWaiting.setOnAction(a -> startWaiting());

            Button btnNotify = new Button("Notify");
            btnNotify.setMaxWidth(Double.MAX_VALUE);
            btnNotify.setOnAction(a -> MTNotification.addNotification(new Favourite(), true));

            Button btnText = new Button("change text");
            btnText.setMaxWidth(Double.MAX_VALUE);
            btnText.setOnAction(a -> {
//                ProgConfig.MEDIA_DB_SUFFIX.setValue("soso");
            });

            Button btnDownload = new Button("Download");
            btnDownload.setMaxWidth(Double.MAX_VALUE);
            btnDownload.setOnAction(a -> {

//                if (DownloadFactory.downloadFile(progInfoDialog.getStage(),
//                        "http://p2.localhost:8080/extra/beta/MTPlayer-8-42__2020.02.22.zip",
//                        "MTPlayer-8-42__20200222.zip")) {
//                    System.out.println("Download OK");
//                } else {
//                    System.out.println("Download NOT OK");
//                }
            });

            int row = 0;
            gridPane.add(text, 0, row, 2, 1);
            gridPane.add(btnStartWaiting, 0, ++row);
            gridPane.add(btnNotify, 0, ++row);
            gridPane.add(btnText, 0, ++row);
            gridPane.add(btnDownload, 0, ++row);

            gridPane.add(textArea, 0, ++row, 2, 1);

        }
    }

    private void startWaiting() {
        maskerPane.setMaskerText("Senderliste ist zu alt, eine neue downloaden");
        maskerPane.setButtonText("Button Text");
        maskerPane.setMaskerVisible(true, true, true);
        Thread th = new Thread(waitTask);
        th.setName("startWaiting");
        th.start();
    }

    private class WaitTask extends Task<Void> {

        @Override
        protected Void call() throws Exception {
            try {
                Thread.sleep(5000);
            } catch (Exception ignore) {
            }
            return null;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }

    public void close() {
        if (waitTask.isRunning()) {
            waitTask.cancel();
        }
        maskerPane.switchOffMasker();
    }
}
