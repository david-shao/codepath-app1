package com.david.flickster.activities;

import android.os.Bundle;

import com.david.flickster.R;
import com.david.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 3/12/2017.
 */

public class MoviePlayerActivity extends YouTubeBaseActivity {
    Movie movie;

    @BindView(R.id.player) YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_movie_player);
        ButterKnife.bind(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");

        youTubePlayerView.initialize(getResources().getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        if (!b) {
                            youTubePlayer.setFullscreen(true);
                            youTubePlayer.loadVideo(movie.getVideoKey());
                        }
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
