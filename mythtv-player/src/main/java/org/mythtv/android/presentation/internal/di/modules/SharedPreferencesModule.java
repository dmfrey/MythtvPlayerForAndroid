/*
 * MythtvPlayerForAndroid. An application for Android users to play MythTV Recordings and Videos
 * Copyright (c) 2016. Daniel Frey
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

package org.mythtv.android.presentation.internal.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.mythtv.android.domain.SettingsKeys;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 3/1/16.
 */
@Module
public class SharedPreferencesModule {

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesModule( Context context ) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );

    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {

        return sharedPreferences;
    }

    @Provides
    @Singleton
    public String getMasterBackendUrl() {

        String host = getStringFromPreferences( SettingsKeys.KEY_PREF_BACKEND_URL );
        String port = getStringFromPreferences( SettingsKeys.KEY_PREF_BACKEND_PORT );

        return "http://" + host + ":" + port;
    }

    public boolean getInternalPlayer() {

        return getBooleanFromPreferences( SettingsKeys.KEY_PREF_INTERNAL_PLAYER );
    }

    public boolean getShowAdultContent() {

        return getBooleanFromPreferences( SettingsKeys.KEY_PREF_SHOW_ADULT_TAB );
    }

    public String getStringFromPreferences( String key ) {

        return sharedPreferences.getString( key, "" );
    }

    public boolean getBooleanFromPreferences( String key ) {

        return sharedPreferences.getBoolean( key, false );
    }

}
