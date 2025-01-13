package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2lib.tools.events.P2Event;
import de.p2tools.p2lib.tools.events.P2Listener;
import de.p2tools.p2radio.controller.config.Events;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.application.Platform;

public class PlayingTitle {

    private static final int maxCount = 4 * 60; // Minuten
    public static String nowPlaying = "";

    private int count = 0;

    public PlayingTitle() {
        nowPlaying = "";
        ProgData.getInstance().pEventHandler.addListener(new P2Listener(Events.TIMER) {
            public void pingGui(P2Event event) {
                ++count;
                if (count > maxCount) {
                    nowPlaying = "";
                    count = 0;
                }
            }
        });
    }

    public static void stopNowPlaying() {
        nowPlaying = "";
    }

    public void setNowPlaying(String nowPlaying, Start start) {
        if (start.getSetData().getProgPath().contains("vlc")) {
            checkVlc(nowPlaying);
        }
    }

    private void checkVlc(String line) {
        // vlc
        // [0000750694001600] http stream debug: New Icy-Title=Battlelore - We Are the Legions
        final String search = "Icy-Title=";
        if (line.contains(search)) {
            String title = line.substring(line.indexOf(search) + search.length());
            Platform.runLater(() -> {
                PlayingTitle.nowPlaying = title;
                count = 0;
            });
        }
    }
}