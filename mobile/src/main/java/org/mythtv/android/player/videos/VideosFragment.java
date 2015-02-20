package org.mythtv.android.player.videos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mythtv.android.library.core.domain.video.Video;
import org.mythtv.android.library.ui.adapters.VideoItemAdapter;
import org.mythtv.android.R;
import org.mythtv.android.player.AbstractBaseFragment;

import java.util.List;

/**
 * Created by dmfrey on 12/3/14.
 */
public class VideosFragment extends AbstractBaseFragment implements VideoItemAdapter.VideoItemClickListener {

    RecyclerView mRecyclerView;
    VideoItemAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    TextView mEmpty;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.video_list, container, false );

        mRecyclerView = (RecyclerView) view.findViewById( R.id.list );
        mLayoutManager = new LinearLayoutManager( getActivity() );
        mRecyclerView.setLayoutManager( mLayoutManager );
        mEmpty = (TextView) view.findViewById( R.id.empty );

        return view;
    }

    public void setVideos( List<Video> videos ) {

        mAdapter = new VideoItemAdapter( videos, this );
        mRecyclerView.setAdapter( mAdapter );

    }

    public void videoItemClicked( Video video ) {

        Bundle args = new Bundle();
        args.putSerializable( VideoDetailsFragment.VIDEO_KEY, video );

        Intent videoDetails = new Intent( getActivity(), VideoDetailsActivity.class );
        videoDetails.putExtras(args);
        startActivity( videoDetails );

    }

    @Override
    public void connected() {

        mRecyclerView.setVisibility( View.VISIBLE );
        mEmpty.setVisibility(View.GONE);

    }

    @Override
    public void notConnected() {

        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);

    }

}
