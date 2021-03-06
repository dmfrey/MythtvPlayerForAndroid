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

package org.mythtv.android.presentation.view.activity.phone;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.cast.framework.CastSession;

import org.mythtv.android.R;
import org.mythtv.android.domain.Media;
import org.mythtv.android.domain.SettingsKeys;
import org.mythtv.android.presentation.internal.di.HasComponent;
import org.mythtv.android.presentation.internal.di.components.DaggerMediaComponent;
import org.mythtv.android.presentation.internal.di.components.MediaComponent;
import org.mythtv.android.presentation.internal.di.modules.MediaItemModule;
import org.mythtv.android.presentation.model.MediaItemModel;
import org.mythtv.android.presentation.view.fragment.phone.CastNotReadyDialogFragment;
import org.mythtv.android.presentation.view.fragment.phone.MediaItemDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 9/30/15.
 */
public class MediaItemDetailsActivity extends AbstractBasePhoneActivity implements HasComponent<MediaComponent>, MediaItemDetailsFragment.MediaItemDetailsListener {

    private static final String TAG = MediaItemDetailsActivity.class.getSimpleName();

    private static final String INTENT_EXTRA_PARAM_ID = "org.mythtv.android.INTENT_PARAM_ID";
    private static final String INTENT_EXTRA_PARAM_MEDIA = "org.mythtv.android.INTENT_PARAM_MEDIA";
    private static final String INSTANCE_STATE_PARAM_ID = "org.mythtv.android.STATE_PARAM_ID";
    private static final String INSTANCE_STATE_PARAM_MEDIA = "org.mythtv.android.STATE_PARAM_MEDIA";

    private int id;
    private Media media;
    private MediaComponent mediaComponent;

    private MediaItemModel mediaItemModel;

    @BindView( R.id.media_item_image )
    ImageView backdrop;

    @BindView( R.id.fab )
    FloatingActionButton fab;

    MediaItemDetailsFragment fragment;

    private Unbinder unbinder;

    public static Intent getCallingIntent( Context context, int id, Media media ) {

        Intent callingIntent = new Intent( context, MediaItemDetailsActivity.class );
        callingIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        callingIntent.putExtra( INTENT_EXTRA_PARAM_ID, id );
        callingIntent.putExtra( INTENT_EXTRA_PARAM_MEDIA, media.name() );

        return callingIntent;
    }

    @Override
    public int getLayoutResource() {

        return R.layout.activity_phone_media_item_details;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        Log.d( TAG, "onCreate : enter" );

        super.onCreate( savedInstanceState );

        ButterKnife.bind( this );
        unbinder = ButterKnife.bind( this );

        this.initializeActivity( savedInstanceState );
        this.initializeInjector();

        Log.d( TAG, "onCreate : exit" );
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        Log.d( TAG, "onSaveInstanceState : enter" );

        if( null != outState ) {
            Log.d( TAG, "onSaveInstanceState : outState is not null" );

            outState.putInt( INSTANCE_STATE_PARAM_ID, this.id );
            outState.putString( INSTANCE_STATE_PARAM_MEDIA, this.media.name() );

        }

        super.onSaveInstanceState( outState );

        Log.d( TAG, "onSaveInstanceState : exit" );
    }

    @Override
    protected void onRestoreInstanceState( Bundle savedInstanceState ) {
        Log.d( TAG, "onRestoreInstanceState : enter" );
        super.onRestoreInstanceState( savedInstanceState );

        if( null != savedInstanceState ) {
            Log.d( TAG, "onRestoreInstanceState : savedInstanceState != null" );

            this.id = savedInstanceState.getInt( INSTANCE_STATE_PARAM_ID );
            this.media = Media.valueOf( savedInstanceState.getString( INSTANCE_STATE_PARAM_MEDIA ) );

            Log.d( TAG, "onRestoreInstanceState : id=" + id + ", media=" + media.name() );
        }

        Log.d( TAG, "onRestoreInstanceState : exit" );
    }

    @Override
    protected void onDestroy() {

        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_details, menu );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {

        switch( item.getItemId() ) {

            case android.R.id.home:

//                supportFinishAfterTransition();
                NavUtils.navigateUpFromSameTask( this );

                return true;

            case R.id.menu_settings:

                navigator.navigateToSettings( this );

                return true;

            case R.id.menu_troubleshoot:

                navigator.navigateToTroubleshoot( this );

                return true;

            default :

                break;

        }

        return super.onOptionsItemSelected( item );
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity( Bundle savedInstanceState ) {
        Log.d( TAG, "initializeActivity : enter" );

        if( null == savedInstanceState ) {
            Log.d( TAG, "initializeActivity : savedInstanceState is null" );

            Bundle extras = getIntent().getExtras();
            if( null != extras ) {
                Log.d( TAG, "initializeActivity : extras != null" );

                if( extras.containsKey( INTENT_EXTRA_PARAM_ID ) ) {

                    this.id = getIntent().getIntExtra( INTENT_EXTRA_PARAM_ID, -1 );

                }

                if( extras.containsKey( INTENT_EXTRA_PARAM_MEDIA ) ) {

                    this.media = Media.valueOf( getIntent().getStringExtra( INTENT_EXTRA_PARAM_MEDIA ) );

                }

                fragment = MediaItemDetailsFragment.newInstance();
                addFragment( R.id.fl_fragment, fragment );

            }

        } else {
            Log.d( TAG, "initializeActivity : savedInstanceState is not null" );

            this.id = savedInstanceState.getInt( INSTANCE_STATE_PARAM_ID );
            this.media = Media.valueOf( savedInstanceState.getString( INSTANCE_STATE_PARAM_MEDIA ) );

        }

        Log.d( TAG, "initializeActivity : exit" );
    }

    private void initializeInjector() {
        Log.d( TAG, "initializeInjector : enter" );

        this.mediaComponent = DaggerMediaComponent.builder()
                .applicationComponent( getApplicationComponent() )
                .mediaItemModule( new MediaItemModule( this.id, this.media ) )
                .build();

        Log.d( TAG, "initializeInjector : exit" );
    }

    @Override
    public MediaComponent getComponent() {
        Log.d( TAG, "getComponent : enter" );

        Log.d( TAG, "getComponent : exit" );
        return mediaComponent;
    }

    @Override
    public void onMediaItemLoaded( final MediaItemModel mediaItemModel ) {
        Log.d( TAG, "onMediaItemLoaded : enter" );

        this.mediaItemModel = mediaItemModel;
        loadBackdrop();

        Log.d( TAG, "onMediaItemLoaded : exit" );
    }

    @Override
    public void onMediaItemRefreshed( final MediaItemModel mediaItemModel ) {
        Log.d( TAG, "onMediaItemRefreshed : enter" );

        this.mediaItemModel = mediaItemModel;

        Log.d( TAG, "onMediaItemRefreshed : exit" );
    }

    private void loadBackdrop() {
        Log.d( TAG, "loadBackdrop : enter" );

        if( null == mediaItemModel.media() ) {
            Log.d( TAG, "loadBackdrop : exit, media not set" );

            return;
        }

        String backdropUrl = null;
        if( mediaItemModel.media() == Media.PROGRAM ) {

            if( null != mediaItemModel.previewUrl() && !"".equals( mediaItemModel.previewUrl() ) ) {

                backdropUrl = getMasterBackendUrl() + mediaItemModel.previewUrl();

            }


        } else {

            if( null != mediaItemModel.fanartUrl() && !"".equals( mediaItemModel.fanartUrl() ) ) {

                backdropUrl = getMasterBackendUrl() + mediaItemModel.fanartUrl();

            }


        }

        if( null != backdropUrl && !"".equals( backdropUrl ) ) {

            Log.i( TAG, "loadBackdrop : backdropUrl=" + backdropUrl );
            Glide
                    .with( this )
                    .load( backdropUrl )
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy( DiskCacheStrategy.RESULT )
                    .into( backdrop );

        }

        Log.d( TAG, "loadBackdrop : exit" );
    }

    @OnClick( R.id.fab )
    void onButtonFabPlay() {
        Log.d( TAG, "onButtonFabPlay : enter" );

        if( null == mediaItemModel || !mediaItemModel.isValid() ) {
            Log.d( TAG, "onButtonFabPlay : exit, mediaItemModel is not valid" );

            showToastMessage( fab, "Entry is not valid and cannot be played.", null, null );

            return;
        }

        if( mediaItemModel.url().endsWith( "m3u8" ) ) {
            Log.d( TAG, "onButtonFabPlay : trying to play HLS stream" );

            if( mediaItemModel.percentComplete() <= 2 ) {

                FragmentManager fm = getFragmentManager();
                CastNotReadyDialogFragment fragment = new CastNotReadyDialogFragment();
                fragment.show(fm, "Cast Not Ready Dialog Fragment");

                Log.d( TAG, "onButtonFabPlay : HLS stream is not ready" );
                return;
            }

        }

        CastSession castSession = mCastContext.getSessionManager().getCurrentCastSession();
        if( null != castSession && castSession.isConnected() ) {
            Log.d( TAG, "onButtonFabPlay : always favor cast device, sending stream to cast device via local player" );

            navigator.navigateToLocalPlayer( this, mediaItemModel );

        } else if( getSharedPreferencesComponent().sharedPreferences().getBoolean( SettingsKeys.KEY_PREF_INTERNAL_PLAYER, true ) ) {
            Log.d( TAG, "onButtonFabPlay : sending stream to internal player" );

            navigator.navigateToLocalPlayer( this, mediaItemModel );

        } else {
            Log.d( TAG, "onButtonFabPlay : sending stream to external player" );

            navigator.navigateToExternalPlayer( this, (getMasterBackendUrl() + mediaItemModel.url() ), mediaItemModel.contentType() );

        }

        Log.d( TAG, "onButtonFabPlay : exit" );
    }

}