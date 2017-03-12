package com.david.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/11/2017.
 */

public class Movie implements Serializable {

    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    double rating;
    String releaseDate;

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        originalTitle = jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        releaseDate = jsonObject.getString("release_date");
    }

    public static List<Movie> fromJSONArray(JSONArray array) {
        List<Movie> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", backdropPath);
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
