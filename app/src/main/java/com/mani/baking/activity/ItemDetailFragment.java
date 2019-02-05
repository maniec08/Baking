package com.mani.baking.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mani.baking.BuildConfig;
import com.mani.baking.R;
import com.mani.baking.datastruct.StepDetails;
import com.mani.baking.utils.KeyConstants;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    private StepDetails stepDetails;
    private ExoPlayer exoPlayer;

    public ItemDetailFragment() {
    }
    @BindView(R.id.video_view)
    PlayerView playerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        stepDetails = intent.getParcelableExtra(KeyConstants.RECIPE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (stepDetails != null) {
            ((TextView) rootView.findViewById(R.id.steps_describtion_tv)).setText(stepDetails.getDescribtion());
        }

        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        initializePlayer1(Uri.parse(stepDetails.getVideoUrl()));
    }
    @Override
    public void onStart(){
        super.onStart();
        initializePlayer1(Uri.parse(stepDetails.getVideoUrl()));
    }
    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), BuildConfig.APPLICATION_ID));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);
            // Prepare the MediaSource.
            exoPlayer.prepare(videoSource, true, false);
        }
    }
    private void initializePlayer1(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);

            // Prepare the MediaSource.
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultHttpDataSourceFactory("ua"),
                    new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource, true, false);
        }
    }
}
