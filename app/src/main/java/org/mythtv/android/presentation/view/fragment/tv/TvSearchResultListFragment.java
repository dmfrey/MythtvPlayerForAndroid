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

package org.mythtv.android.presentation.view.fragment.tv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;

import org.mythtv.android.domain.SearchResult;
import org.mythtv.android.presentation.model.ProgramModel;
import org.mythtv.android.presentation.model.SearchResultModel;
import org.mythtv.android.presentation.model.VideoMetadataInfoModel;
import org.mythtv.android.presentation.presenter.phone.SearchResultListPresenter;
import org.mythtv.android.presentation.view.SearchResultListView;
import org.mythtv.android.R;
import org.mythtv.android.presentation.internal.di.components.SearchComponent;
import org.mythtv.android.presentation.presenter.tv.CardPresenter;
import org.mythtv.android.presentation.view.activity.tv.RecordingsDetailsActivity;
import org.mythtv.android.presentation.view.activity.tv.VideoDetailsActivity;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by dmfrey on 2/27/16.
 */
public class TvSearchResultListFragment extends AbstractBaseSearchFragment implements SearchFragment.SearchResultProvider, SearchResultListView {

    private static final String TAG = TvSearchResultListFragment.class.getSimpleName();

    private static final String ARGUMENT_KEY_SEARCH_TEXT = "org.mythtv.android.ARGUMENT_SEARCH_TEXT";

    private String searchText;

    /**
     * Interface for listening program list events.
     */
    public interface SearchResultListListener {

        void onSearchResultClicked( final SearchResultModel searchResultModel );

    }

    @Inject
    SearchResultListPresenter searchResultListPresenter;

    private ArrayObjectAdapter mRowsAdapter;
    private Handler mHandler = new Handler();

    private SearchResultListListener searchResultListListener;

    public TvSearchResultListFragment() {
        super();
    }

    public static TvSearchResultListFragment newInstance(String searchText ) {

        TvSearchResultListFragment fragment = new TvSearchResultListFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putString( ARGUMENT_KEY_SEARCH_TEXT, searchText );
        fragment.setArguments( argumentsBundle );

        return fragment;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        Log.d( TAG, "onAttach : enter" );

        if( activity instanceof SearchResultListListener ) {
            this.searchResultListListener = (SearchResultListListener) activity;
        }

        Log.d( TAG, "onAttach : exit" );
    }

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        Log.d( TAG, "onCreate : enter" );
        super.onCreate( savedInstanceState );

        setupUI();

        Log.d( TAG, "onCreate : exit" );
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        Log.d( TAG, "onActivityCreated : enter" );
        super.onActivityCreated( savedInstanceState );

        this.initialize();
        this.loadSearchResultList();

        Log.d( TAG, "onActivityCreated : exit" );
    }

    @Override
    public void onResume() {
        Log.d( TAG, "onResume : enter" );
        super.onResume();

        this.searchResultListPresenter.resume();

        Log.d( TAG, "onResume : exit" );
    }

    @Override
    public void onPause() {
        Log.d( TAG, "onPause : enter" );
        super.onPause();

        this.searchResultListPresenter.pause();

        Log.d( TAG, "onPause : exit" );
    }

    @Override
    public void onDestroy() {
        Log.d( TAG, "onDestroy : enter" );
        super.onDestroy();

        this.searchResultListPresenter.destroy();

        Log.d( TAG, "onDestroy : exit" );
    }

    @Override
    public void onDestroyView() {
        Log.d( TAG, "onDestroyView : enter" );
        super.onDestroyView();

        Log.d( TAG, "onDestroyView : exit" );
    }

    private void initialize() {
        Log.d( TAG, "initialize : enter" );

        Log.d( TAG, "initialize : get searchText" );
        this.searchText = getArguments().getString( ARGUMENT_KEY_SEARCH_TEXT );

        Log.d( TAG, "initialize : get component" );
        this.getComponent( SearchComponent.class ).inject( this );

        Log.d( TAG, "initialize : set view" );
        this.searchResultListPresenter.setView( this );

        Log.d( TAG, "initialize : exit" );
    }

    private void setupUI() {
        Log.d( TAG, "setupUI : enter" );

        mRowsAdapter = new ArrayObjectAdapter( new ListRowPresenter() );
        setSearchResultProvider( this );
        setOnItemViewClickedListener( new ItemViewClickedListener() );

        Log.d( TAG, "setupUI : exit" );
    }

    @Override
    public void showLoading() {
        Log.d( TAG, "showLoading : enter" );

        Log.d( TAG, "showLoading : exit" );
    }

    @Override
    public void hideLoading() {
        Log.d( TAG, "hideLoading : enter" );

        Log.d( TAG, "hideLoading : exit" );
    }

    @Override
    public void showRetry() {
        Log.d( TAG, "showRetry : enter" );

        Log.d( TAG, "showRetry : exit" );
    }

    @Override
    public void hideRetry() {
        Log.d( TAG, "hideRetry : enter" );

        Log.d( TAG, "hideRetry : exit" );
    }

    @Override
    public void renderSearchResultList( Collection<SearchResultModel> searchResultModelCollection ) {
        Log.d( TAG, "renderSearchResultList : enter" );

        if( null != searchResultModelCollection ) {
            Log.d( TAG, "renderSearchResultList : searchResultModelCollection is not null" );

            mRowsAdapter.clear();

            ArrayObjectAdapter programRowAdapter = new ArrayObjectAdapter( new CardPresenter() );
            ArrayObjectAdapter videoRowAdapter = new ArrayObjectAdapter( new CardPresenter() );

            for( SearchResultModel searchResultModel : searchResultModelCollection ) {
                Log.d( TAG, "renderSearchResultList : searchResult=" + searchResultModel );

                if( SearchResult.Type.RECORDING.equals( searchResultModel.getType() ) ) {

                    programRowAdapter.add( searchResultModel );

                } else {

                    videoRowAdapter.add( searchResultModel );

                }

            }

            if( programRowAdapter.size() > 0 ) {
                Log.d( TAG, "renderSearchResultList : programRowAdapter has programs" );

                HeaderItem header = new HeaderItem( 0, getResources().getString( R.string.recording ) + " " + getResources().getString( R.string.search_results ) + " '" + this.searchText + "'" );
                mRowsAdapter.add( new ListRow( header, programRowAdapter ) );

            }

            if( videoRowAdapter.size() > 0 ) {
                Log.d( TAG, "renderSearchResultList : videoRowAdapter has videos" );

                HeaderItem header = new HeaderItem( 1, getResources().getString( R.string.video ) + " " + getResources().getString( R.string.search_results ) + " '" + this.searchText + "'" );
                mRowsAdapter.add( new ListRow( header, videoRowAdapter ) );

            }

        }

        Log.d( TAG, "renderSearchResultList : exit" );
    }

    @Override
    public void viewSearchResult( SearchResultModel searchResultModel ) {
        Log.d( TAG, "viewSearchResult : enter" );

        if( null != this.searchResultListListener ) {
            Log.d( TAG, "viewSearchResult : searchResultModel=" + searchResultModel.toString() );

            this.searchResultListListener.onSearchResultClicked( searchResultModel );

        }

        Log.d( TAG, "viewSearchResult : exit" );
    }

    @Override
    public void showError( String message ) {
        Log.d( TAG, "showError : enter" );


        Log.d( TAG, "showError : exit" );
    }

    @Override
    public void showMessage( String message ) {
        Log.d( TAG, "showMessage : enter" );

        this.showToastMessage( message );

        Log.d( TAG, "showMessage : exit" );
    }

    @Override
    public Context getContext() {
        Log.d( TAG, "getContext : enter" );

        Log.d( TAG, "getContext : exit" );
        return this.getActivity().getApplicationContext();
    }

    /**
     * Loads all search results.
     */
    private void loadSearchResultList() {
        Log.d( TAG, "loadSearchResultList : enter" );

        Log.d( TAG, "loadSearchResultList : searchText=" + searchText );
        this.searchResultListPresenter.initialize( this.searchText );

        Log.d( TAG, "loadSearchResultList : exit" );
    }

    @Override
    public ObjectAdapter getResultsAdapter() {

        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange( String newQuery ) {
        Log.d( TAG, "onQueryTextChange : enter" );

        if( !TextUtils.isEmpty( newQuery ) && newQuery.length() > 3 ) {
            Log.d( TAG, "onQueryTextChange : newQuery is not empty and length is greater than 3" );

            this.searchText = newQuery;
            this.loadSearchResultList();

        }

        Log.d( TAG, "onQueryTextChange : exit" );
        return true;
    }

    @Override
    public boolean onQueryTextSubmit( String query ) {
        Log.d( TAG, "onQueryTextSubmit : enter" );

        if( !TextUtils.isEmpty( query ) && query.length() > 3 ) {
            Log.d( TAG, "onQueryTextSubmit : query is not empty and length is greater than 3" );

            this.searchText = query;
            this.loadSearchResultList();

        }

        Log.d( TAG, "onQueryTextSubmit : exit" );
        return true;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {

        @Override
        public void onItemClicked( Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row ) {

            if( item instanceof ProgramModel ) {

                ProgramModel programModel = (ProgramModel) item;
//                Log.d( TAG, "Program: " + tv_item.toString() );

                Intent intent = new Intent( getActivity(), RecordingsDetailsActivity.class );
                intent.putExtra( RecordingsDetailsActivity.PROGRAM, programModel );

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        RecordingsDetailsActivity.SHARED_ELEMENT_NAME ).toBundle();

                getActivity().startActivity( intent, bundle );

            } else {

                VideoMetadataInfoModel videoMetadataInfoModel = (VideoMetadataInfoModel) item;

                Intent intent = new Intent( getActivity(), VideoDetailsActivity.class );
                intent.putExtra( VideoDetailsActivity.VIDEO, videoMetadataInfoModel );

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ( (ImageCardView) itemViewHolder.view ).getMainImageView(),
                        VideoDetailsActivity.SHARED_ELEMENT_NAME ).toBundle();

                getActivity().startActivity( intent, bundle );

            }

        }

    }

}
