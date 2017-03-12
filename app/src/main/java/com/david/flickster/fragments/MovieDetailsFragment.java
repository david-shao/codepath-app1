package com.david.flickster.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.david.flickster.R;
import com.david.flickster.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by David on 3/11/2017.
 */

public class MovieDetailsFragment extends DialogFragment {

    int position;
    Movie movie;
    ImageView ivImage;
    TextView tvTitle;
    TextView tvRelease;
    TextView tvOverview;
    RatingBar rbRating;

    public MovieDetailsFragment() {
        //Needs to be empty
    }

    public static MovieDetailsFragment newInstance(int position, Movie movie) {
        MovieDetailsFragment frag = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putSerializable("movie", movie);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movie = (Movie) getArguments().getSerializable("movie");
        position = getArguments().getInt("pos");

        ivImage = (ImageView) view.findViewById(R.id.ivBackdrop);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvRelease = (TextView) view.findViewById(R.id.tvReleaseDate);
        tvOverview = (TextView) view.findViewById(R.id.tvOverview);
        rbRating = (RatingBar) view.findViewById(R.id.rbRating);

        String imageUrl;
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = movie.getPosterPath();
        } else {
            imageUrl = movie.getBackdropPath();
        }
        Picasso.with(getContext()).load(imageUrl).fit().centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.not_available)
                .into(ivImage);

        tvTitle.setText(movie.getOriginalTitle());
        tvRelease.setText(String.format(getResources().getString(R.string.release_date), movie.getReleaseDate()));
        tvOverview.setText(movie.getOverview());
        rbRating.setRating((float) movie.getRating() / 10 * 5); //convert rating out of 10 to out of 5 stars
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MovieDetailsDialog);
    }

}
