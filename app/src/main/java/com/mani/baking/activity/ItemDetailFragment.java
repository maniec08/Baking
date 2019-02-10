package com.mani.baking.activity;

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
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.mani.baking.R;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.StepDetails;
import com.mani.baking.utils.KeyConstants;
import com.mani.baking.utils.SelectionSesionVar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.mani.baking.utils.KeyConstants.AUTOPLAY;
import static com.mani.baking.utils.KeyConstants.PLAYER_POSITION;
import static com.mani.baking.utils.KeyConstants.STEP_POSITION;
import static com.mani.baking.utils.KeyConstants.WINDOW_POSITION;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    public ItemDetailFragment() {
    }

    private static final String TAG = ItemDetailFragment.class.getSimpleName();
    private boolean twoPane = false;
    private ExoPlayer exoPlayer;
    private Long playerPosition = 1L;
    private int currentWindow;
    private boolean autoPlay = false;
    public static int currentSelection = -1;
    private static int instanceSavedForStep = -1;

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
        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION, 1L);
            currentWindow = savedInstanceState.getInt(WINDOW_POSITION, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
            instanceSavedForStep = savedInstanceState.getInt(STEP_POSITION, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_detail, container, false);
        ButterKnife.bind(this, rootView);
        twoPane = getResources().getBoolean(R.bool.istablet);
        setUpButtonClickListener();
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstance) {
        super.onSaveInstanceState(savedInstance);
        instanceSavedForStep = SelectionSesionVar.step;
        savedInstance.putInt(STEP_POSITION, instanceSavedForStep);
        if (exoPlayer == null) {
            savedInstance.putLong(PLAYER_POSITION, playerPosition);
            savedInstance.putInt(KeyConstants.WINDOW_POSITION, currentWindow);
            savedInstance.putBoolean(AUTOPLAY, autoPlay);
        }

/*        if (exoPlayer != null) {

            savedInstance.putLong(KeyConstants.PLAYER_POSITION, exoPlayer.getCurrentPosition());
        }*/

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            setUpPlayer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        setUpUi(false);
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            setUpPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playerPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private StepDetails getCurrentStep() {
        return Recipe.getStepDetails();
    }

    private String getVideoUrl() {
        String url = getCurrentStep().getVideoUrl();
        if (isNullOrEmpty(url) && !isNullOrEmpty(getCurrentStep().getThumbnailUrl())) {
            return getCurrentStep().getThumbnailUrl();
        }
        return url;
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void setUpUi(boolean isSetUpPlayer) {
        if (isNullOrEmpty(getCurrentStep().getDescription())) {
            getCurrentStep().setDescription("");
        }
        if (isNullOrEmpty(getCurrentStep().getShortDescription())) {
            getCurrentStep().setShortDescription("");
        }

        int orientation = getResources().getConfiguration().orientation;
        setUpToolBar();
        setUpButton();
        if (isSetUpPlayer) {
            setUpPlayer();
        }
        description.setText(getCurrentStep().getDescription());
        shortDescription.setText(getCurrentStep().getShortDescription());

        if (isNullOrEmpty(getVideoUrl())) {
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

    private void setUpButton() {
        if (SelectionSesionVar.step == 0) {
            previousButton.setEnabled(false);
        }
        if (SelectionSesionVar.step == Recipe.getRecipeDetails().getStepDetailsList().size() - 1) {
            nextButton.setEnabled(false);
        }
        if (twoPane) {
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);
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

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private void setUpButtonClickListener() {
        int maxStep = Recipe.getRecipeDetails().getStepDetailsList().size() - 1;
        nextButton.setOnClickListener(v -> {
            SelectionSesionVar.step++;
            previousButton.setEnabled(true);
            if (SelectionSesionVar.step > maxStep) {
                SelectionSesionVar.step = maxStep;
            }
            if (SelectionSesionVar.step == maxStep) {
                nextButton.setEnabled(false);
            }
            playerPosition = 1L;
            setUpUi(true);
        });

        previousButton.setOnClickListener(v -> {
            SelectionSesionVar.step--;
            nextButton.setEnabled(true);
            if (SelectionSesionVar.step < 0) {
                SelectionSesionVar.step = 0;
            }
            if (SelectionSesionVar.step == 0) {
                previousButton.setEnabled(false);
            }
            playerPosition = 1L;
            setUpUi(true);
        });
    }


    private void setUpPlayer() {
        String url = getVideoUrl();
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
            }
            exoPlayer.seekTo(currentWindow, playerPosition);
            MediaSource videoSource = new ExtractorMediaSource
                    .Factory(new DefaultHttpDataSourceFactory("load_uri")).setMinLoadableRetryCount(3)
                    .createMediaSource(mediaUri);
            exoPlayer.prepare(videoSource, false, false);
        }
    }

}

