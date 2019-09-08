package com.chandu.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chandu.bakingapp.cooking.Recipe;
import com.chandu.bakingapp.cooking.RecipeSteps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class RecipeStepsFragment extends Fragment {
    private SimpleExoPlayer mPlayer;
    @BindView(R.id.video_exoplayer_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.recipe_steps_image)
    ImageView recipeImage;
    @BindView(R.id.recipe_steps_desc)
    TextView recipeDesc;
    @BindView(R.id.prev)
    Button prevStep;
    @BindView(R.id.next)
    Button nextStep;
    private Unbinder unbinder;
    RecipeSteps recipeSteps = null;
    Recipe recipeMain = null;
    private navigateRecipeStep navigationStep;
    final String recipeObject = "RecipeObject";
    final String recipeStepsObject = "RecipeStepsObject";
    final String recipeStepsNumber = "RecipeStepNumber";
    final String recipeStepsCount = "RecipeSteps";
    private static final String TAG = RecipeStepsFragment.class.getSimpleName();
    private static final String EXO_PLAYER_POSITION = "exo_player_position";
    private static final String NEXT_STEP_POSITION = "NEXT_STEP_POSITION";
    private static final String PREV_STEP_POSITION = "PREV_STEP_POSITION";
    private static final String CURRENT_STEP_POSITION = "CURRENT_STEP_POSITION";
    private String videoURL;
    private long position;
    public int prevStepSeq = 0;
    public int nextStepSeq = 1;
    public int seq = 0;
    public int seqMax = 0;
    private boolean onStop = false;
    private TrackSelector trackSelector;
    private PlaybackStateCompat.Builder mStateBuilder;
    private MediaSessionCompat mMediaSession;

    public RecipeStepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_steps_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        Intent i = getActivity().getIntent();
        //if (recipeSteps == null) {

            seq = i.getIntExtra(recipeStepsNumber, 0);
            seqMax = i.getIntExtra(recipeStepsCount, 0);
            if (i.hasExtra(recipeStepsObject)) {
                recipeSteps = (RecipeSteps) i.getSerializableExtra(recipeStepsObject);
            } else if (i.hasExtra(recipeObject)) {
                //int seq = i.getIntExtra(recipeStepsNumber, 0);
                recipeMain = (Recipe) i.getSerializableExtra(recipeObject);
                recipeSteps = recipeMain.RecipeSeq[seq];
            }
        //}

        if (savedInstanceState != null) {
            nextStepSeq = savedInstanceState.getInt(NEXT_STEP_POSITION);
            prevStepSeq = savedInstanceState.getInt(PREV_STEP_POSITION);
            seq = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }

        determineSteps(seq);
        recipeSteps = recipeMain.RecipeSeq[seq];

        i.putExtra(recipeStepsNumber, seq);
        i.putExtra(recipeStepsCount, seqMax);

        getActivity().setTitle(recipeSteps.getShortDesc());
        videoURL = recipeSteps.getVideoUrl();

        if (!TextUtils.isEmpty(recipeSteps.getVideoUrl())) {
            initializePlayer(recipeSteps.getVideoUrl());
            recipeImage.setVisibility(View.INVISIBLE);
        } else {
            mPlayer = null;
            mPlayerView.setVisibility(View.GONE);
            if (TextUtils.isEmpty(recipeSteps.getThumbUrl())) {
                recipeImage.setImageResource(R.drawable.novideo);
            } else {
                recipeImage.setImageResource(R.drawable.novideo);
                Picasso.get().load(recipeSteps.getThumbUrl()).into(recipeImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        recipeImage.setImageResource(R.drawable.novideo);
                    }
                });
            }
        }

        recipeDesc.setText(recipeSteps.getDesc());

        return view;
    }

    private void determineSteps(final int steps) {
        if (steps != 0) {
            prevStepSeq = steps - 1;
            prevStep.setVisibility(View.VISIBLE);
        } else {
            prevStep.setVisibility(View.INVISIBLE);
        }
        if (steps != (seqMax -1)) {
            nextStepSeq = steps + 1;
            nextStep.setVisibility(View.VISIBLE);
        } else {
            nextStep.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer(String videoURL) {
        if (mPlayer == null) {
            trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            // Create the player with previously created TrackSelector
            mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity().getApplicationContext(), trackSelector, loadControl);

            // Load the SimpleExoPlayerView with the created player
            mPlayerView.setPlayer(mPlayer);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ViewGroup.LayoutParams params = (LinearLayout.LayoutParams)
                        mPlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                mPlayerView.setLayoutParams(params);
            }

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    getContext(),
                    Util.getUserAgent(getActivity().getApplicationContext(), "BakingApp"));

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(
                    Uri.parse(videoURL),
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                    null);

            mPlayer.prepare(videoSource);
            mPlayer.setPlayWhenReady(true);
            mPlayer.seekTo(position);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        position = mPlayer != null? mPlayer.getCurrentPosition() : 0;
        destroyExo();
    }

    private void destroyExo() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            trackSelector = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        //destroyExo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(EXO_PLAYER_POSITION);
            nextStepSeq = savedInstanceState.getInt(NEXT_STEP_POSITION);
            prevStepSeq = savedInstanceState.getInt(PREV_STEP_POSITION);
            seq = savedInstanceState.getInt(CURRENT_STEP_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXO_PLAYER_POSITION, position);
        outState.putInt(NEXT_STEP_POSITION, nextStepSeq);
        outState.putInt(PREV_STEP_POSITION, prevStepSeq);
        outState.putInt(CURRENT_STEP_POSITION, seq);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*
    * Referred this link:
    * https://stackoverflow.com/questions/9095910/navigate-among-fragments-within-one-activity
    */
    public interface navigateRecipeStep {
        void previousStep();

        void nextStep();
    }

    @Optional
    @OnClick(R.id.prev)
    public void previousStep() {
        navigationStep.previousStep();
    }

    @Optional
    @OnClick(R.id.next)
    public void nextStep() {
        navigationStep.nextStep();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            navigationStep = (navigateRecipeStep) context;
        } catch (Exception e) {
            System.out.println("Interface navigateRecipeStep not implemented by "
                    +context.toString());
        }
    }
}
