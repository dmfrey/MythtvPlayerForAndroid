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

package org.mythtv.android.presentation.view.fragment.phone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.mythtv.android.R;
import org.mythtv.android.presentation.internal.di.components.DvrComponent;
import org.mythtv.android.presentation.model.ProgramModel;
import org.mythtv.android.presentation.presenter.phone.UpcomingListPresenter;
import org.mythtv.android.presentation.view.ProgramListView;
import org.mythtv.android.presentation.view.adapter.phone.ProgramsAdapter;
import org.mythtv.android.presentation.view.adapter.phone.ProgramsLayoutManager;
import org.mythtv.android.presentation.view.adapter.phone.UpcomingProgramsAdapter;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dmfrey on 1/20/16.
 */
public class UpcomingListFragment extends AbstractBaseFragment implements ProgramListView {

    private static final String TAG = UpcomingListFragment.class.getSimpleName();

    /**
     * Interface for listening program list events.
     */
    public interface ProgramListListener {

        void onProgramClicked( final ProgramModel programModel );

    }

    @Inject
    UpcomingListPresenter upcomingListPresenter;

    @BindView( R.id.rv_programs )
    RecyclerView rv_programs;

    @BindView( R.id.rl_progress )
    RelativeLayout rl_progress;

    private Unbinder unbinder;

    private UpcomingProgramsAdapter programsAdapter;

    private ProgramListListener programListListener;

    public UpcomingListFragment() { super(); }

    public static RecentListFragment newInstance() {

        return new RecentListFragment();
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        Log.d( TAG, "onAttach : enter" );

        if( activity instanceof ProgramListListener ) {
            this.programListListener = (ProgramListListener) activity;
        }

        Log.d( TAG, "onAttach : exit" );
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        Log.d( TAG, "onCreateView : enter" );

        View fragmentView = inflater.inflate( R.layout.fragment_phone_program_list, container, false );
        ButterKnife.bind( this, fragmentView );
        unbinder = ButterKnife.bind( this, fragmentView );

        setupUI();

        Log.d( TAG, "onCreateView : exit" );
        return fragmentView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState ) {
        Log.d( TAG, "onActivityCreated : enter" );
        super.onActivityCreated( savedInstanceState );

        this.initialize();
        this.loadUpcomingList();

        Log.d( TAG, "onActivityCreated : exit" );
    }

    @Override
    public void onResume() {
        Log.d( TAG, "onResume : enter" );
        super.onResume();

        this.upcomingListPresenter.resume();

        Log.d( TAG, "onResume : exit" );
    }

    @Override
    public void onPause() {
        Log.d( TAG, "onPause : enter" );
        super.onPause();

        this.upcomingListPresenter.pause();

        Log.d( TAG, "onPause : exit" );
    }

    @Override
    public void onDestroy() {
        Log.d( TAG, "onDestroy : enter" );
        super.onDestroy();

        this.upcomingListPresenter.destroy();

        Log.d( TAG, "onDestroy : exit" );
    }

    @Override
    public void onDestroyView() {
        Log.d( TAG, "onDestroyView : enter" );
        super.onDestroyView();

        unbinder.unbind();

        Log.d( TAG, "onDestroyView : exit" );
    }

    private void initialize() {
        Log.d( TAG, "initialize : enter" );

        this.getComponent( DvrComponent.class ).inject( this );
        this.upcomingListPresenter.setView( this );

        Log.d( TAG, "initialize : exit" );
    }

    private void setupUI() {
        Log.d( TAG, "setupUI : enter" );

        ProgramsLayoutManager programsLayoutManager = new ProgramsLayoutManager(getActivity());
        this.rv_programs.setLayoutManager(programsLayoutManager);

        this.programsAdapter = new UpcomingProgramsAdapter( getActivity(), new ArrayList<ProgramModel>() );
        this.rv_programs.setAdapter( programsAdapter );

        Log.d( TAG, "setupUI : exit" );
    }

    @Override
    public void showLoading() {
        Log.d( TAG, "showLoading : enter" );

        this.rl_progress.setVisibility( View.VISIBLE );
        this.getActivity().setProgressBarIndeterminateVisibility( true );

        Log.d( TAG, "showLoading : exit" );
    }

    @Override
    public void hideLoading() {
        Log.d( TAG, "hideLoading : enter" );

        this.rl_progress.setVisibility( View.GONE );
        this.getActivity().setProgressBarIndeterminateVisibility( false );

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

    public void reload() {

        initialize();
        loadUpcomingList();

    }

    @Override
    public void renderProgramList( Collection<ProgramModel> programModelCollection ) {
        Log.d( TAG, "renderProgramList : enter" );

        if( null != programModelCollection ) {

            this.programsAdapter.setProgramsCollection( programModelCollection );

        }

        Log.d( TAG, "renderProgramList : exit" );
    }

    @Override
    public void viewProgram( ProgramModel programModel ) {
        Log.d( TAG, "viewProgram : enter" );

        if( null != this.programListListener ) {
            Log.d( TAG, "viewProgram : programModel=" + programModel.toString() );

            this.programListListener.onProgramClicked( programModel );

        }

        Log.d( TAG, "viewProgram : exit" );
    }

    @Override
    public void showError( String message ) {
        Log.d( TAG, "showError : enter" );

        this.showToastMessage( message, getResources().getString( R.string.retry ), v -> UpcomingListFragment.this.loadUpcomingList());

        Log.d( TAG, "showError : exit" );
    }

    @Override
    public void showMessage( String message ) {
        Log.d( TAG, "showMessage : enter" );

        this.showToastMessage( message, null, null );

        Log.d( TAG, "showMessage : exit" );
    }

    @Override
    public Context getContext() {
        Log.d( TAG, "getContext : enter" );

        Log.d( TAG, "getContext : exit" );
        return this.getActivity().getApplicationContext();
    }

    /**
     * Loads upcoming programs.
     */
    private void loadUpcomingList() {
        Log.d( TAG, "loadUpcomingList : enter" );

        this.upcomingListPresenter.initialize();

        Log.d( TAG, "loadUpcomingList : exit" );
    }

    private ProgramsAdapter.OnItemClickListener onItemClickListener = programModel -> {

        if( null != UpcomingListFragment.this.upcomingListPresenter && null != programModel ) {
            Log.i( TAG, "onProgramItemClicked : programModel=" + programModel.toString() );

            UpcomingListFragment.this.upcomingListPresenter.onProgramClicked( programModel );

        }

    };

}
