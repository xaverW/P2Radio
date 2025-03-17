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

package de.p2tools.p2radio.gui.tools.table;

import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2radio.controller.config.ProgConfig;
import de.p2tools.p2radio.controller.data.station.StationData;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Table {
    private static final String SORT_ASCENDING = "ASCENDING";
    private static final String SORT_DESCENDING = "DESCENDING";
    private static String width = "";
    private static String sort = "";
    private static String upDown = "";
    private static String vis = "";
    private static String order = "";
    private static int maxSpalten;
    private static double[] breite;
    private static boolean[] visAr;
    private static TableColumn[] tArray;
    private static StringProperty confWidth; //Spaltenbreite
    private static StringProperty confSort; //"Sortieren"  der Tabelle nach Spalte
    private static StringProperty confUpDown; //Sortierung UP oder Down
    private static StringProperty confVis; //Spalte ist sichtbar
    private static StringProperty confOrder; //"Reihenfolge" der Spalten

    public enum TABLE_ENUM {
        STATION, FAVOURITE, HISTORY, SMALL_RADIO_STATION, SMALL_RADIO_FAVOURITE, SMALL_RADIO_HISTORY, OWN_AUTOSTART
    }

    public static void setTable(TableStation table) {
        // Tabelle setzen
        try {
            initConf(table.getETable());
            initColumn(table);

            maxSpalten = table.getColumns().size();
            breite = getDoubleArray(maxSpalten);
            visAr = getBoolArray(maxSpalten);

            if (!confWidth.get().isEmpty()) {
                width = confWidth.get();
                if (arrLesen(width, breite)) {
                    for (int i = 0; i < breite.length; ++i) {
                        ((TableColumn<?, ?>) table.getColumns().get(i)).setPrefWidth(breite[i]);
                    }
                }
            }

            if (!confVis.get().isEmpty()) {
                vis = confVis.get();
                if (arrLesen(vis, visAr)) {
                    for (int i = 0; i < visAr.length; ++i) {
                        ((TableColumn<?, ?>) table.getColumns().get(i)).setVisible(visAr[i]);
                    }
                }
            }

            if (!confSort.get().isEmpty()) {
                String sort = confSort.get();
                String[] arSort = sort.split(",");
                String sortUp = confUpDown.get();
                String[] arSortUp = sortUp.split(",");

                for (int i = 0; i < arSort.length; ++i) {
                    String s = arSort[i];
                    ObservableList<TableColumn<StationData, ?>> l = table.getColumns();
                    TableColumn<StationData, ?> co = l.stream()
                            .filter(c -> c.getText().equals(s))
                            .findFirst().get();

                    table.getSortOrder().add(co);
                    if (arSort.length == arSortUp.length) {
                        co.setSortType(arSortUp[i].equals(SORT_ASCENDING) ? TableColumn.SortType.ASCENDING : TableColumn.SortType.DESCENDING);
                    }
                }
            }
        } catch (final Exception ex) {
            P2Log.errorLog(642103218, ex.getMessage());
            resetTable(table);
        }
    }

    public static void saveTable(TableView table, TABLE_ENUM table_enum) {
        // Tabellendaten sichern
        initConf(table_enum);
        maxSpalten = table.getColumns().size();
        width = "";
        sort = "";
        upDown = "";
        vis = "";
        order = "";

        ObservableList<TableColumn> columns = table.getColumns();
        columns.stream().forEach(c -> {
            width += c.getWidth() + ",";
            vis += c.isVisible() + ",";
        });
        ObservableList<TableColumn> sortOrder = table.getSortOrder();
        sortOrder.stream().forEach(so -> {
            sort += so.getText() + ",";
        });
        if (table.getSortOrder().size() > 0) {
            sortOrder.stream().forEach(so -> {
                upDown += (so.getSortType().equals(TableColumn.SortType.ASCENDING) ? SORT_ASCENDING : SORT_DESCENDING) + ",";
            });
        }
        columns.stream().forEach(c -> {
            order += c.getText() + ",";
        });

        confWidth.set(width);
        confVis.set(vis);
        confSort.set(sort);
        confUpDown.set(upDown);
        confOrder.set(order);
    }

    public static void resetTable(TableStation ta) {
        initConf(ta.getETable());
        reset(ta);
        setTable(ta);
    }

    private static void initConf(TABLE_ENUM table_enum) {
        switch (table_enum) {

            case STATION:
                confWidth = ProgConfig.STATION_GUI_TABLE_WIDTH;
                confSort = ProgConfig.STATION_GUI_TABLE_SORT;
                confUpDown = ProgConfig.STATION_GUI_TABLE_UP_DOWN;
                confVis = ProgConfig.STATION_GUI_TABLE_VIS;
                confOrder = ProgConfig.STATION_GUI_TABLE_ORDER;
                break;

            case FAVOURITE:
                confWidth = ProgConfig.FAVOURITE_GUI_TABLE_WIDTH;
                confSort = ProgConfig.FAVOURITE_GUI_TABLE_SORT;
                confUpDown = ProgConfig.FAVOURITE_GUI_TABLE_UP_DOWN;
                confVis = ProgConfig.FAVOURITE_GUI_TABLE_VIS;
                confOrder = ProgConfig.FAVOURITE_GUI_TABLE_ORDER;
                break;

            case HISTORY:
                confWidth = ProgConfig.HISTORY_GUI_TABLE_WIDTH;
                confSort = ProgConfig.HISTORY_GUI_TABLE_SORT;
                confUpDown = ProgConfig.HISTORY_GUI_TABLE_UP_DOWN;
                confVis = ProgConfig.HISTORY_GUI_TABLE_VIS;
                confOrder = ProgConfig.HISTORY_GUI_TABLE_ORDER;
                break;

            case SMALL_RADIO_STATION:
                confWidth = ProgConfig.SMALL_RADIO_TABLE_STATION_WIDTH;
                confSort = ProgConfig.SMALL_RADIO_TABLE_STATION_SORT;
                confUpDown = ProgConfig.SMALL_RADIO_TABLE_STATION_UP_DOWN;
                confVis = ProgConfig.SMALL_RADIO_TABLE_STATION_VIS;
                confOrder = ProgConfig.SMALL_RADIO_TABLE_STATION_ORDER;
                break;
            case SMALL_RADIO_FAVOURITE:
                confWidth = ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_WIDTH;
                confSort = ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_SORT;
                confUpDown = ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_UP_DOWN;
                confVis = ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_VIS;
                confOrder = ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_ORDER;
                break;
            case SMALL_RADIO_HISTORY:
                confWidth = ProgConfig.SMALL_RADIO_TABLE_HISTORY_WIDTH;
                confSort = ProgConfig.SMALL_RADIO_TABLE_HISTORY_SORT;
                confUpDown = ProgConfig.SMALL_RADIO_TABLE_HISTORY_UP_DOWN;
                confVis = ProgConfig.SMALL_RADIO_TABLE_HISTORY_VIS;
                confOrder = ProgConfig.SMALL_RADIO_TABLE_HISTORY_ORDER;
                break;
            case OWN_AUTOSTART:
                confWidth = ProgConfig.OWN_AUTOSTART_TABLE_WIDTH;
                confSort = ProgConfig.OWN_AUTOSTART_TABLE_SORT;
                confUpDown = ProgConfig.OWN_AUTOSTART_TABLE_UP_DOWN;
                confVis = ProgConfig.OWN_AUTOSTART_TABLE_VIS;
                confOrder = ProgConfig.OWN_AUTOSTART_TABLE_ORDER;
                break;
        }
    }

    public static void clearConf() {
        ProgConfig.STATION_GUI_TABLE_WIDTH.set("");
        ProgConfig.STATION_GUI_TABLE_SORT.set("");
        ProgConfig.STATION_GUI_TABLE_UP_DOWN.set("");
        ProgConfig.STATION_GUI_TABLE_VIS.set("");
        ProgConfig.STATION_GUI_TABLE_ORDER.set("");

        ProgConfig.FAVOURITE_GUI_TABLE_WIDTH.set("");
        ProgConfig.FAVOURITE_GUI_TABLE_SORT.set("");
        ProgConfig.FAVOURITE_GUI_TABLE_UP_DOWN.set("");
        ProgConfig.FAVOURITE_GUI_TABLE_VIS.set("");
        ProgConfig.FAVOURITE_GUI_TABLE_ORDER.set("");

        ProgConfig.HISTORY_GUI_TABLE_WIDTH.set("");
        ProgConfig.HISTORY_GUI_TABLE_SORT.set("");
        ProgConfig.HISTORY_GUI_TABLE_UP_DOWN.set("");
        ProgConfig.HISTORY_GUI_TABLE_VIS.set("");
        ProgConfig.HISTORY_GUI_TABLE_ORDER.set("");

        ProgConfig.SMALL_RADIO_TABLE_STATION_WIDTH.set("");
        ProgConfig.SMALL_RADIO_TABLE_STATION_SORT.set("");
        ProgConfig.SMALL_RADIO_TABLE_STATION_UP_DOWN.set("");
        ProgConfig.SMALL_RADIO_TABLE_STATION_VIS.set("");
        ProgConfig.SMALL_RADIO_TABLE_STATION_ORDER.set("");

        ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_WIDTH.set("");
        ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_SORT.set("");
        ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_UP_DOWN.set("");
        ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_VIS.set("");
        ProgConfig.SMALL_RADIO_TABLE_FAVOURITE_ORDER.set("");

        ProgConfig.SMALL_RADIO_TABLE_HISTORY_WIDTH.set("");
        ProgConfig.SMALL_RADIO_TABLE_HISTORY_SORT.set("");
        ProgConfig.SMALL_RADIO_TABLE_HISTORY_UP_DOWN.set("");
        ProgConfig.SMALL_RADIO_TABLE_HISTORY_VIS.set("");
        ProgConfig.SMALL_RADIO_TABLE_HISTORY_ORDER.set("");

        ProgConfig.OWN_AUTOSTART_TABLE_WIDTH.set("");
        ProgConfig.OWN_AUTOSTART_TABLE_SORT.set("");
        ProgConfig.OWN_AUTOSTART_TABLE_UP_DOWN.set("");
        ProgConfig.OWN_AUTOSTART_TABLE_VIS.set("");
        ProgConfig.OWN_AUTOSTART_TABLE_ORDER.set("");
    }

    private static void initColumn(TableView<StationData> table) {
        tArray = table.getColumns().toArray(TableColumn[]::new);
        table.getColumns().clear();

        String order = confOrder.get();
        String[] arOrder = order.split(",");
        if (confOrder.get().isEmpty() || arOrder.length != tArray.length) {
            // dann gibts keine Einstellungen oder die Anzahl der Spalten hat sich geändert
            for (TableColumn tc : tArray) {
                table.getColumns().add(tc);
            }
        } else {
            for (int i = 0; i < arOrder.length; ++i) {
                String s = arOrder[i];
                for (TableColumn tc : tArray) {
                    if (s.equals(tc.getText())) {
                        if (!table.getColumns().contains(tc)) {
                            //aus Fehlern wird man klug :(
                            table.getColumns().add(tc);
                        }
                    }
                }
            }
        }
        table.getColumns().forEach(c -> c.setSortable(true));
        table.getColumns().forEach(c -> c.setVisible(true));
    }

    private static void reset(TableView ta) {
        maxSpalten = ta.getColumns().size();

        String set = "";
        for (int i = 0; i < maxSpalten; ++i) {
            set += Boolean.TRUE + ",";
        }
        confVis.set(set);

        confWidth.set("");
        confSort.set("");
        confUpDown.set("");
        confOrder.set("");
    }

    private static boolean arrLesen(String s, double[] arr) {
        String[] sarr = s.split(",");
        if (maxSpalten != sarr.length) {
            // dann hat sich die Anzahl der Spalten der Tabelle geändert: Versionswechsel
            return false;
        } else {
            for (int i = 0; i < maxSpalten; i++) {
                try {
                    arr[i] = Double.parseDouble(sarr[i]);
                } catch (final Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean arrLesen(String s, boolean[] arr) {
        String[] sarr = s.split(",");
        if (maxSpalten != sarr.length) {
            // dann hat sich die Anzahl der Spalten der Tabelle geändert: Versionswechsel
            return false;
        } else {
            for (int i = 0; i < maxSpalten; i++) {
                try {
                    arr[i] = Boolean.parseBoolean(sarr[i]);
                } catch (final Exception ex) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean arrLesen(String s, String[] arr) {
        arr = s.split(",");
        // dann hat sich die Anzahl der Spalten der Tabelle geändert: Versionswechsel
        return maxSpalten == arr.length;
    }

    private static int countString(String s) {
        int ret = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == ',') {
                ++ret;
            }
        }
        return ++ret;
    }

    private static double[] getDoubleArray(int anzahl) {
        final double[] arr = new double[anzahl];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = -1;
        }
        return arr;
    }

    private static boolean[] getBoolArray(int anzahl) {
        final boolean[] arr = new boolean[anzahl];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = true;
        }
        return arr;
    }
}
