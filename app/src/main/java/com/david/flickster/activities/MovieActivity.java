package com.david.flickster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.david.flickster.R;
import com.david.flickster.adapters.MovieArrayAdapter;
import com.david.flickster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieActivity extends AppCompatActivity {

    List<Movie> movies;
    MovieArrayAdapter movieAdapter;
    @BindView(R.id.lvMovies) ListView lvItems;

    public static OkHttpClient httpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);

//                FragmentManager fm = getSupportFragmentManager();
//                MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(position, movie);
//                movieDetailsFragment.show(fm, "fragment_movie");

                if (movie.getRating() > 5) {
                    Intent i = new Intent(MovieActivity.this, MoviePlayerActivity.class);
                    i.putExtra("movie", movie);
                    i.putExtra("force_fullscreen", true);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MovieActivity.this, MovieDetailsActivity.class);
                    i.putExtra("movie", movie);
                    i.putExtra("pos", position);
                    startActivity(i);
                }
            }
        });

//        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(url, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                JSONArray movieJsonResults = null;
//                try {
//                    movieJsonResults = response.getJSONArray("results");
//                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
//                    movieAdapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        });

        String url = "https://api.themoviedb.org/3/movie/now_playing";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed");
        url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Response unsuccessful: " + response);
                }

                final String responseData = response.body().string();

                MovieActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray movieJsonResults = null;
                        try {
                            JSONObject json = new JSONObject(responseData);
                            movieJsonResults = json.getJSONArray("results");
                            movies.addAll(Movie.fromJSONArray(movieJsonResults));
                            movieAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
