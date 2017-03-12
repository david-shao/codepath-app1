package com.david.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.flickster.R;
import com.david.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.david.flickster.R.id.tvOverview;
import static com.david.flickster.R.id.tvTitle;

/**
 * Created by David on 3/11/2017.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private final int NUM_VIEW_TYPES = 2;
    private final int TYPE_POPULAR = 1;
    private final int TYPE_DEFAULT = 0;

    // View lookup cache
    private static class ViewHolderType0 {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
    }

    private static class ViewHolderType1 {
        ImageView ivImage;
        ImageView ivPlay;
        TextView tvTitle;
        TextView tvOverview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = getItem(position);
        if (movie.getRating() > 5) {
            return TYPE_POPULAR;
        }
        return TYPE_DEFAULT;
    }

    @Override
    public int getViewTypeCount() {
        return NUM_VIEW_TYPES;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);
        int viewType = getItemViewType(position);

        String imageUrl;

        switch (viewType) {
            case TYPE_POPULAR:
                ViewHolderType1 viewHolder1;
                if (convertView == null) {
//                    Log.d("DEBUG", "creating new viewHolder1");
                    viewHolder1 = new ViewHolderType1();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_movie_pop, parent, false);

                    viewHolder1.ivImage = (ImageView) convertView.findViewById(R.id.ivBackdrop);
                    viewHolder1.ivPlay = (ImageView) convertView.findViewById(R.id.ivPlay);
                    viewHolder1.tvTitle = (TextView) convertView.findViewById(tvTitle);
                    viewHolder1.tvOverview = (TextView) convertView.findViewById(tvOverview);

                    convertView.setTag(viewHolder1);
                } else {
                    viewHolder1 = (ViewHolderType1) convertView.getTag();
                }

                viewHolder1.ivImage.setImageResource(0);
                if (viewHolder1.tvTitle != null) {
                    viewHolder1.tvTitle.setText(movie.getOriginalTitle());
                }
                if (viewHolder1.tvOverview != null) {
                    viewHolder1.tvOverview.setText(movie.getOverview());
                }

                imageUrl = movie.getBackdropPath();
                Picasso.with(getContext()).load(imageUrl).fit().centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.not_available)
                        .into(viewHolder1.ivImage);
                break;
            case TYPE_DEFAULT:
            default:
                ViewHolderType0 viewHolder;
                if (convertView == null) {
//                    Log.d("DEBUG", "creating new viewHolder0");
                    viewHolder = new ViewHolderType0();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.item_movie, parent, false);

                    viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivPoster);
                    viewHolder.tvTitle = (TextView) convertView.findViewById(tvTitle);
                    viewHolder.tvOverview = (TextView) convertView.findViewById(tvOverview);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolderType0) convertView.getTag();
                }

                viewHolder.ivImage.setImageResource(0);
                viewHolder.tvTitle.setText(movie.getOriginalTitle());
                viewHolder.tvOverview.setText(movie.getOverview());

                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    imageUrl = movie.getBackdropPath();
                } else {
                    imageUrl = movie.getPosterPath();
                }
                Picasso.with(getContext()).load(imageUrl).fit().centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.not_available)
                        .into(viewHolder.ivImage);
                break;
        }

        return convertView;
    }

}
