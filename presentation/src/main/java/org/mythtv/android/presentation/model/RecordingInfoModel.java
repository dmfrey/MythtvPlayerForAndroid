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

import org.joda.time.DateTime;

import lombok.Data;

/*
 * Created by dmfrey on 11/12/14.
 */
@Data
public class RecordingInfoModel {

    private Integer recordedId;
    private Integer status;
    private Integer priority;
    private DateTime startTs;
    private DateTime endTs;
    private Integer recordId;
    private String recGroup;
    private String playGroup;
    private String storageGroup;
    private Integer recType;
    private Integer dupInType;
    private Integer dupMethod;
    private Integer encoderId;
    private String encoderName;
    private String profile;

}
