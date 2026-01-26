package de.p2tools.p2radio.controller.tips;

import java.util.List;

public class TipsFactory {

    public enum TIPPS {
        INFOS("Infos", TipListInfos.getTips()),
        GUI("Gui", TipListGui.getTips()),
        STATION("Sender", TipListStation.getTips()),
        FAVORITE("Favoriten", TipListFavorite.getTips()),
        HISTORY("History", TipListHistory.getTips()),
        FILTER("Filter", TipListFilter.getTips()),
        SET("Sets", TipListSet.getTips());

        private final String name;
        private final List<TipData> tipsList;

        TIPPS(String name, List<TipData> tipsList) {
            this.name = name;
            this.tipsList = tipsList;
        }

        public String getName() {
            return name;
        }

        public List<TipData> getTipsList() {
            return tipsList;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private TipsFactory() {
    }
}
