package com.mani.baking.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.mani.baking.R;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.RecipeDetails;
import com.mani.baking.datastruct.StepDetails;
import com.mani.baking.utils.KeyConstants;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    private static final String TAG = ItemDetailFragment.class.getSimpleName();
    private boolean twoPane = false;
    private ExoPlayer exoPlayer;
    private long playerPosition = 100L;

    public ItemDetailFragment() {
    }

    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.steps_description_tv)
    TextView description;
    @BindView(R.id.steps_short_description_tv)
    TextView shortDescription;
    @BindView(R.id.next_step_button)
    Button nextButton;
    @BindView(R.id.previous_step_button)
    Button previousButton;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_detail, container, false);
        ButterKnife.bind(this, rootView);
        twoPane = getResources().getBoolean(R.bool.istablet);
        if(savedInstanceState!=null){
            playerPosition = savedInstanceState.getLong(KeyConstants.PLAYER_POSITION, playerPosition);
        }

        return rootView;
    }

    private void setUpUi() {
        if (isNullOrEmpty(getCurrentStep().getDescribtion())) {
            getCurrentStep().setDescribtion("");
        }
        if (isNullOrEmpty(getCurrentStep().getShortDescribtion())) {
            getCurrentStep().setShortDescribtion("");
        }

        int orientation = getResources().getConfiguration().orientation;
        initializePlayer();
        setUpToolBar();

        description.setText(getCurrentStep().getDescribtion());
        shortDescription.setText(getCurrentStep().getShortDescribtion());

        if (isNullOrEmpty(getCurrentStep().getVideoUrl())) {
            playerView.setVisibility(View.GONE);
            linearLayout.setPaddingRelative(2, 200, 2, 2);
        } else if (orientation == ORIENTATION_LANDSCAPE && !twoPane) {
            linearLayout.setPaddingRelative(0, 0, 0, 0);
            playerView.getLayoutParams().height = MATCH_PARENT;
        } else {
            linearLayout.setPaddingRelative(0, 40, 0, 0);
            playerView.getLayoutParams().height = 0;
        }

    }

    private void setUpToolBar() {
        int orientation = getResources().getConfiguration().orientation;
        if (twoPane || orientation == ORIENTATION_LANDSCAPE) {
            appBarLayout.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
        } else {
            appBarLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(Recipe.getRecipeDetails().getName());
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(v -> {
                try {
                    getActivity().onBackPressed();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            });
        }
    }

    public boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private void setUpButtonClickListener() {
        int maxStep = Recipe.getRecipeDetails().getStepDetailsList().size() - 1;
        nextButton.setOnClickListener(v -> {
            Recipe.selectedStep++;
            previousButton.setEnabled(true);
            if ( Recipe.selectedStep > maxStep) {
                Recipe.selectedStep = maxStep;
            }
            if ( Recipe.selectedStep == maxStep) {
                nextButton.setEnabled(false);
            }
            setUpUi();
        });

        previousButton.setOnClickListener(v -> {
            Recipe.selectedStep--;
            nextButton.setEnabled(true);
            if ( Recipe.selectedStep < 0) {
                Recipe.selectedStep = 0;
            }
            if ( Recipe.selectedStep == 0) {
                previousButton.setEnabled(false);
            }
            setUpUi();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpUi();
        setUpButtonClickListener();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstance) {
        savedInstance.putLong(KeyConstants.PLAYER_POSITION, playerPosition);
        super.onSaveInstanceState(savedInstance);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        if (savedInstance != null) {
            playerPosition = savedInstance.getLong(KeyConstants.PLAYER_POSITION, playerPosition);
        }
    }

    @Override
    public void onPause() {
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            destroyPlayer();
        }
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void destroyPlayer() {
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        destroyPlayer();
        super.onDestroy();
    }

    public StepDetails getCurrentStep() {
        return Recipe.getStepDetails();
    }

    private void initializePlayer() {
        String url = getCurrentStep().getVideoUrl();
        if (url == null || url.isEmpty()) {
            playerView.setVisibility(View.INVISIBLE);
        } else {
            playerView.setVisibility(View.VISIBLE);
            Uri mediaUri = Uri.parse(url);
            if (exoPlayer == null) {
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
                playerView.setPlayer(exoPlayer);
                exoPlayer.setPlayWhenReady(true);
                exoPlayer.seekTo(playerPosition);
            } else {
                exoPlayer.seekTo(1L);
            }
            MediaSource videoSource = new ExtractorMediaSource
                    .Factory(new DefaultHttpDataSourceFactory("ua"))
                    .createMediaSource(mediaUri);
            exoPlayer.prepare(videoSource, false, false);

        }
    }


}

