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
import com.google.android.material.appbar.AppBarLayout;
import com.mani.baking.R;
import com.mani.baking.datastruct.Recipe;
import com.mani.baking.datastruct.StepDetails;
import com.mani.baking.utils.KeyConstants;

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
    private static Long playerPosition = 1L;
    public static int currentSelection = -1;
    private static int instanceSavedForStep = -1;
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
        if(savedInstanceState!=null){
            playerPosition = savedInstanceState.getLong(KeyConstants.PLAYER_POSITION,1L);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_item_detail, container, false);
        ButterKnife.bind(this, rootView);
        twoPane = getResources().getBoolean(R.bool.istablet);
        return rootView;
    }

    private void setUpUi() {
        if (isNullOrEmpty(getCurrentStep().getDescription())) {
            getCurrentStep().setDescription("");
        }
        if (isNullOrEmpty(getCurrentStep().getShortDescription())) {
            getCurrentStep().setShortDescription("");
        }

        int orientation = getResources().getConfiguration().orientation;
        setUpPlayer();
        setUpToolBar();
        setUpButton();

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
        if(Recipe.selectedStep == 0){
            previousButton.setEnabled(false);
        }
        if(Recipe.selectedStep == Recipe.getRecipeDetails().getStepDetailsList().size()-1){
            nextButton.setEnabled(false);
        }
        if(twoPane){
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
            Recipe.selectedStep++;
            previousButton.setEnabled(true);
            if (Recipe.selectedStep > maxStep) {
                Recipe.selectedStep = maxStep;
            }
            if (Recipe.selectedStep == maxStep) {
                nextButton.setEnabled(false);
            }
            playerPosition =1L;
            setUpUi();
        });

        previousButton.setOnClickListener(v -> {
            Recipe.selectedStep--;
            nextButton.setEnabled(true);
            if (Recipe.selectedStep < 0) {
                Recipe.selectedStep = 0;
            }
            if (Recipe.selectedStep == 0) {
                previousButton.setEnabled(false);
            }
            playerPosition =1L;
            setUpUi();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpUi();
        if(!twoPane) {
            setUpButtonClickListener();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstance) {
        if(exoPlayer!=null){
            instanceSavedForStep = Recipe.selectedStep;
            savedInstance.putLong(KeyConstants.PLAYER_POSITION, exoPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(savedInstance);
    }

    @Override
    public void onPause() {
        if (exoPlayer != null && currentSelection == instanceSavedForStep) {
            playerPosition = exoPlayer.getCurrentPosition();
        } else {
            playerPosition = 1L;
        }
        super.onPause();
    }

    private void destroyPlayer() {
        if (exoPlayer != null) {
            if (currentSelection == instanceSavedForStep) {
                playerPosition = exoPlayer.getCurrentPosition();
            } else {
                playerPosition = 1L;
            }
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
            } else {
                playerPosition =1L ;
            }
            exoPlayer.seekTo(playerPosition);
            MediaSource videoSource = new ExtractorMediaSource
                    .Factory(new DefaultHttpDataSourceFactory("load_uri")).setMinLoadableRetryCount(3)
                    .createMediaSource(mediaUri);
            exoPlayer.prepare(videoSource, false, false);
        }
    }
}

