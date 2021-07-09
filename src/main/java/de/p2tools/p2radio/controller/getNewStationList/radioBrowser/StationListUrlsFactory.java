/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.p2radio.controller.getNewStationList.radioBrowser;

import de.p2tools.p2Lib.tools.log.PLog;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class StationListUrlsFactory {

    private StationListUrlsFactory() {
    }

    public static Vector<String> getRadioListUrls() {
        // start a thread and do the DNS request
        Vector<String> listResult = new Vector<String>();
        try {
            // add all round robin servers one by one to select them separately
            InetAddress[] list = InetAddress.getAllByName("all.api.radio-browser.info");
            for (InetAddress item : list) {
//                String url = item.getHostName();
//                url = item.getCanonicalHostName();
//                if (!url.startsWith("https://")) {
//                    url = "https://" + url;
//                }

                listResult.add(item.getCanonicalHostName());
            }
            listResult.stream().forEach(s -> {
                PLog.sysLog("Listserver: " + s);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listResult;
    }


    public static String getRandRadioListUrl(ArrayList<String> alreadyUsed) {
        Vector<String> list = getRadioListUrls();
        if (list.isEmpty()) {
            return "";
        }

        // unbenutzte URLs
        List<String> listUnused = new ArrayList<>();
        for (String url : list) {
            if (alreadyUsed.contains(url)) {
                // wurde schon versucht
                continue;
            }
            listUnused.add(url);
        }

        String url;
        if (!listUnused.isEmpty()) {
            int nr = new Random().nextInt(listUnused.size());
            url = listUnused.get(nr);

        } else {
            // dann wird irgendeine Versucht
            int nr = new Random().nextInt(list.size());
            url = list.get(nr);
        }

        return url;
    }

}
