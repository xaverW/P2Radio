package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2radio.controller.config.PEvents;
import de.p2tools.p2radio.controller.config.ProgData;
import javafx.application.Platform;

public class PlayingTitle {

    private static final int maxCount = 4 * 60; // Minuten
    public static String nowPlaying = "";

    private int count = 0;

    public PlayingTitle() {
        nowPlaying = "";
        ProgData.getInstance().pEventHandler.addListener(new P2Listener(PEvents.EVENT_TIMER_SECOND) {
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

    public void setNowPlaying(String nowPlaying) {
        P2Duration.counterStart("setNowPlaying");
        check(nowPlaying);
        P2Duration.counterStop("setNowPlaying");
    }

    private void check(String line) {
        // --> vlc
        // Meta-Info: icy-description: RADIO BOB - AC/DC Collection
        // http stream debug: Icy-Name: RADIO BOB - AC/DC
        // --> ffplay
        // icy-name        : RADIO BOB - AC/DC Collection
        // --> audacious
        // audacious-title: RADIO BOB - Best of Rock Nirvana - Lithium

        final String[] searchArr = {"Icy-Title=", "Icy-Name:", "icy-name        :", "p2radio:"};
        for (String s : searchArr) {
            if (line.contains(s)) {
                String title = line.substring(line.indexOf(s) + s.length()).trim();
                Platform.runLater(() -> {
                    PlayingTitle.nowPlaying = title;
                    count = 0;
                });
            }
        }
    }
}