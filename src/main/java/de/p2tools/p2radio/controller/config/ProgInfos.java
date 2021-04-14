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

package de.p2tools.p2radio.controller.config;

import de.p2tools.p2Lib.configFile.SettingsDirectory;
import de.p2tools.p2Lib.tools.PException;
import de.p2tools.p2radio.Main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;

public class ProgInfos {

    private ProgInfos() {
    }

    public static String getUserAgent() {
        return ProgConfig.SYSTEM_USERAGENT.get();
    }


    /**
     * Retrieve the path to the program jar file.
     *
     * @return The program jar file path with a separator added.
     */
    public static String getPathJar() {
        // macht Probleme bei Win und Netzwerkpfaden, liefert dann Absolute Pfade zB. \\VBOXSVR\share\Mediathek\...
        final String pFilePath = "pFile";
        File propFile = new File(pFilePath);
        if (!propFile.exists()) {
            try {
                final CodeSource cS = Main.class.getProtectionDomain().getCodeSource();
                final File jarFile = new File(cS.getLocation().toURI().getPath());
                final String jarDir = jarFile.getParentFile().getPath();
                propFile = new File(jarDir + File.separator + pFilePath);
            } catch (final Exception ignored) {
            }
        }
        String s = propFile.getAbsolutePath().replace(pFilePath, "");
        if (!s.endsWith(File.separator)) {
            s = s + File.separator;
        }
        return s;
    }

    public static String getLogDirectoryString() {
        final String logDir;
        if (ProgConfig.SYSTEM_LOG_DIR.get().isEmpty()) {
            logDir = getStandardLogDirectoryString();
        } else {
            logDir = ProgConfig.SYSTEM_LOG_DIR.get();
        }
        return logDir;
    }

    public static String getStandardLogDirectoryString() {
        return Paths.get(getSettingsDirectoryString(), ProgConst.LOG_DIR).toString();
    }

    /**
     * Liefert den Pfad zur XML-Senderliste
     *
     * @return Path
     */
    public static Path getStationFileXml() {
        return SettingsDirectory.getSettingsFile(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY,
                ProgConst.STATION_FILE_XML);
    }

    /**
     * Liefert den Pfad zur JSON-Senderliste
     *
     * @return Path
     */
    public static Path getStationFileJson() {
        return SettingsDirectory.getSettingsFile(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY,
                ProgConst.STATION_FILE_JSON);
    }

    /**
     * Liefert den Pfad zur Senderliste
     *
     * @return Pfad als String
     */
    public static String getStationFileJsonString() {
        return getStationFileJson().toString();
    }

    /**
     * Return the path to style
     *
     * @return Path object to style.css file
     */
    public static Path getStyleFile() {
        return SettingsDirectory.getSettingsFile(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY,
                ProgConst.STYLE_FILE);
    }

    /**
     * Return the path to "p2radio.xml"
     *
     * @return Path object to p2radio.xml file
     */
    public static Path getSettingsFile() {
        return SettingsDirectory.getSettingsFile(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY,
                ProgConst.CONFIG_FILE);
    }

    /**
     * Return the location of the settings directory. If it does not exist, create one.
     *
     * @return Path to the settings directory
     * @throws IllegalStateException Will be thrown if settings directory don't exist and if there is
     *                               an error on creating it.
     */
    public static Path getSettingsDirectory() throws PException {
        return SettingsDirectory.getSettingsDirectory(ProgData.configDir,
                ProgConst.CONFIG_DIRECTORY);
    }

    public static String getSettingsDirectoryString() {
        return getSettingsDirectory().toString();
    }

}
