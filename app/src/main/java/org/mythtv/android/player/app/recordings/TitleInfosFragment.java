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

package org.mythtv.android.player.app.recordings;

import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mythtv.android.library.core.MainApplication;
import org.mythtv.android.library.core.domain.dvr.TitleInfo;
import org.mythtv.android.library.core.utils.RefreshTitleInfosTask;
import org.mythtv.android.library.events.dvr.AllProgramsCountEvent;
import org.mythtv.android.library.events.dvr.AllProgramsEvent;
import org.mythtv.android.library.events.dvr.RequestAllRecordedProgramsCountEvent;
import org.mythtv.android.library.events.dvr.RequestAllRecordedProgramsEvent;
import org.mythtv.android.player.common.ui.adapters.TitleInfoItemAdapter;
import org.mythtv.android.R;
import org.mythtv.android.player.app.loaders.TitleInfosAsyncTaskLoader;
import org.mythtv.android.player.app.AbstractBaseFragment;

import java.util.List;

/**
 * Created by dmfrey on 12/3/14.
 */
public class TitleInfosFragment extends AbstractBaseFragment implements LoaderManager.LoaderCallbacks<List<TitleInfo>>, TitleInfoItemAdapter.TitleInfoItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshTitleInfosTask.OnRefreshRecordedProgramTaskListener {

    private static final String TAG = TitleInfosFragment.class.getSimpleName();

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    TitleInfoItemAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    RelativeLayout mHeader;
    TextView mAllRecordings, mAllRecordingsCount, mEmpty;

    @Override
    public Loader<List<TitleInfo>> onCreateLoader( int id, Bundle args ) {
        Log.v( TAG, "onCreateLoader : enter" );

        Log.v( TAG, "onCreateLoader : exit" );
        return new TitleInfosAsyncTaskLoader( getActivity() );
    }

    @Override
    public void onLoadFinished( Loader<List<TitleInfo>> loader, List<TitleInfo> titleInfos ) {
        Log.v( TAG, "onLoadFinished : enter" );

        if( !titleInfos.isEmpty() ) {
            Log.v(TAG, "onLoadFinished : loaded titleInfos from db");

            mAdapter.getTitleInfos().addAll( titleInfos );
            mAdapter.notifyDataSetChanged();

            AllProgramsCountEvent countEvent = MainApplication.getInstance().getDvrService().requestAllRecordedProgramsCount( new RequestAllRecordedProgramsCountEvent() );
            if( countEvent.isEntityFound() ) {

                mAllRecordingsCount.setText( String.valueOf( countEvent.getCount() ) );

            }

            mHeader.setVisibility( View.VISIBLE );
        }

        Log.v( TAG, "onLoadFinished : exit" );
    }

    @Override
    public void onLoaderReset( Loader<List<TitleInfo>> loader ) {

        mRecyclerView.setAdapter( null );

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        Log.v( TAG, "onCreateView : enter" );

        View view = inflater.inflate( R.layout.title_info_list, container, false );

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById( R.id.swipe_refresh_layout );
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new TitleInfoItemAdapter( this );

        mRecyclerView = (RecyclerView) view.findViewById( R.id.list );
        mRecyclerView.setAdapter( mAdapter );

        mLayoutManager = new LinearLayoutManager( getActivity() );
        mRecyclerView.setLayoutManager( mLayoutManager );
        mAllRecordings = (TextView) view.findViewById( R.id.title_info_all_recordings );
        mAllRecordingsCount = (TextView) view.findViewById( R.id.title_info_all_recordings_count );
        mEmpty = (TextView) view.findViewById( R.id.empty );

        mHeader = (RelativeLayout) view.findViewById( R.id.title_info_all_recordings_header );
        mHeader.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent recordings = new Intent( getActivity(), RecordingsActivity.class );
                recordings.putExtras( new Bundle() );
                startActivity( recordings );

            }

        });

        Log.v( TAG, "onCreateView : exit" );
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        Log.v( TAG, "onActivityCreated : enter" );
        super.onActivityCreated( savedInstanceState );

        getLoaderManager().initLoader( 0, null, this );

        Log.v( TAG, "onActivityCreated : exit" );
    }

    public void reload() {
        Log.v( TAG, "setTitleInfos : enter" );

        getLoaderManager().restartLoader( 0, null, this );

        Log.v( TAG, "setTitleInfos : exit" );
    }

    public void titleInfoItemClicked( View v, TitleInfo titleInfo ) {

        Bundle args = new Bundle();
        args.putSerializable( RecordingsActivity.TITLE_INFO, titleInfo );

        Intent recordings = new Intent( getActivity(), RecordingsActivity.class );
        recordings.putExtras( args );

        if( Build.VERSION.SDK_INT >= 16 ) {

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation( getActivity(), null );
            getActivity().startActivity(recordings, options.toBundle());

        } else {

            startActivity( recordings );

        }

    }

    @Override
    public void connected() {

//        mHeader.setVisibility( View.VISIBLE );
//        mRecyclerView.setVisibility( View.VISIBLE );
//        mEmpty.setVisibility( View.GONE );

    }

    @Override
    public void notConnected() {

//        mHeader.setVisibility( View.GONE );
//        mRecyclerView.setVisibility( View.GONE );
//        mEmpty.setVisibility( View.VISIBLE );

    }

    @Override
    public void onRefresh() {

        new RefreshTitleInfosTask( this ).execute();

    }

    @Override
    public void onRefreshComplete() {

        if( mSwipeRefreshLayout.isRefreshing() ) {
            mSwipeRefreshLayout.setRefreshing( false );
        }

    }

}
