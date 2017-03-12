package com.david.flickster.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.david.flickster.R;
import com.david.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

/**
 * Created by David on 3/12/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    int position;
    Movie movie;
    ImageView ivImage;
    TextView tvTitle;
    TextView tvRelease;
    TextView tvOverview;
    RatingBar rbRating;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_movie_details);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        position = getIntent().getIntExtra("pos", 0);

//        ivImage = (ImageView) findViewById(R.id.ivBackdrop);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvRelease = (TextView) findViewById(R.id.tvReleaseDate);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbRating = (RatingBar) findViewById(R.id.rbRating);

//        String imageUrl;
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            imageUrl = movie.getPosterPath();
//        } else {
//            imageUrl = movie.getBackdropPath();
//        }
//        Picasso.with(this).load(imageUrl).fit().centerCrop()
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.not_available)
//                .into(ivImage);

        tvTitle.setText(movie.getOriginalTitle());
        tvRelease.setText(String.format(getResources().getString(R.string.release_date), movie.getReleaseDate()));
        tvOverview.setText(movie.getOverview());
        rbRating.setRating((float) movie.getRating() / 10 * 5); //convert rating out of 10 to out of 5 stars

        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubeFragment);
        youtubeFragment.initialize(getResources().getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(movie.getVideoKey());
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
