package com.david.flickster.models;

import com.david.flickster.activities.MovieActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

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
    int movieId;
    List<String> videoKeys;
    static Random rand = new Random();

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        originalTitle = jsonObject.getString("original_title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        releaseDate = jsonObject.getString("release_date");
        movieId = jsonObject.getInt("id");
        videoKeys = new ArrayList<>();

//        String videoUrl = "https://api.themoviedb.org/3/movie/%s/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

//        Log.d("DEBUG", "doing async call to get videos for movie " + movieId);
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(String.format(videoUrl, movieId), new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                JSONArray movieJsonResults = null;
//                try {
//                    movieJsonResults = response.getJSONArray("results");
//                    for (int i = 0; i < movieJsonResults.length(); i++) {
//                        JSONObject video = movieJsonResults.getJSONObject(i);
//                        if (video.getString("type").equals("Trailer")) {
//                            videoKeys.add(video.getString("key"));
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Log.d("DEBUG", "Failed getting videos: " + responseString);
//            }
//        });

        String url = "https://api.themoviedb.org/3/movie/%s/videos";
        url = String.format(url, movieId);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");
        url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        MovieActivity.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Movie response unsuccessful for id: " + movieId + " response: " + response);
                }

                JSONArray movieJsonResults = null;
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    movieJsonResults = json.getJSONArray("results");
                    for (int i = 0; i < movieJsonResults.length(); i++) {
                        JSONObject video = movieJsonResults.getJSONObject(i);
                        if (video.getString("type").equals("Trailer")) {
                            videoKeys.add(video.getString("key"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public String getVideoKey() {
        if (videoKeys.size() > 0) {
            int i = rand.nextInt(videoKeys.size());
            if (i < videoKeys.size()) {
                return videoKeys.get(i);
            }
        }
        return "";
    }
}
