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

package de.p2tools.p2radio.controller.data.start;

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2radio.controller.config.ProgConst;
import de.p2tools.p2radio.controller.data.SetData;
import de.p2tools.p2radio.controller.data.favourite.FavouriteConstants;
import de.p2tools.p2radio.controller.data.station.StationData;

public final class Start extends StartProps {

    private final StartStatus startStatus = new StartStatus();
    private Starter starter = new Starter(this);

    private StationData playable = null;
    private SetData setData = null;

    public Start() {
    }

    public Start(SetData setData, StationData playable) {
        this.playable = playable;
        setStationNo(playable.getNo());
        setStationName(playable.getStationName());
        setUrl(playable.getStationUrl());

        setSetData(setData);
        StartProgramFactory.makeProgParameter(this);
    }

    public StartStatus getStartStatus() {
        return startStatus;
    }

    public Starter getStarter() {
        return starter;
    }

    public void initStart() {
        getStarter().setStartTime();
        getStarter().setRestartCounter(0);
        getStartStatus().setState(FavouriteConstants.STATE_INIT);
        getStartStatus().setErrorMessage("");
    }

    public void stopStart() {
        if (getStartStatus().isStateError()) {
            // damit fehlerhafte nicht wieder starten
//            getStart().setRestartCounter(ProgConfig.SYSTEM_PARAMETER_DOWNLOAD_MAX_RESTART.get());
        } else {
            getStartStatus().setStateStopped();
        }

        setNo(ProgConst.NUMBER_DONT_USED);
    }

    //==============================================
    // Get/Set
    //==============================================

    public String getStationUrl() {
        return playable.getStationUrl();
    }

    public StationData getPlayable() {
        return playable;
    }

    public SetData getSetData() {
        return setData;
    }

    public void setSetData(SetData setData) {
        this.setData = setData;
        setSetDataId(setData.getId());
    }

    public Start getCopy() {
        final Start ret = new Start();
        ret.starter = starter;
        ret.playable = playable;
        ret.setData = setData;

        Config[] configs = getConfigsArr();
        Config[] configsCopy = ret.getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
        return ret;
    }

    public void copyToMe(Start start) {
        starter = start.starter;
        playable = start.playable;
        setData = start.setData;

        Config[] configs = start.getConfigsArr();
        Config[] configsCopy = getConfigsArr();
        for (int i = 0; i < configs.length; ++i) {
            configsCopy[i].setActValue(configs[i].getActValueString());
        }
    }
}
