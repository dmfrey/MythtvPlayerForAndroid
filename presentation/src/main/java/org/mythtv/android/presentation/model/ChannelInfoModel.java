/*
 * MythTV Player An application for Android users to play MythTV Recordings and Videos
 * Copyright (c) 2015. Daniel Frey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mythtv.android.presentation.model;

import java.util.List;

import lombok.Data;

/*
 * Created by dmfrey on 11/12/14.
 */
@Data
public class ChannelInfoModel {

    private Integer chanId;
    private String chanNum;
    private String callSign;
    private String iconURL;
    private String channelName;
    private Integer mplexId;
    private Integer serviceId;
    private Integer aTSCMajorChan;
    private Integer aTSCMinorChan;
    private String format;
    private String frequencyId;
    private Integer fineTune;
    private String chanFilters;
    private Integer sourceId;
    private Integer inputId;
    private Boolean commFree;
    private Boolean useEIT;
    private Boolean visible;
    private String xMLTVID;
    private String defaultAuth;
    private List<ProgramModel> programs;

}