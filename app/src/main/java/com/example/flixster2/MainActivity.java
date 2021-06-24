package com.example.flixster2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster2.adapters.MovieAdapter;
import com.example.flixster2.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=38658f4199dd7fb175701cfd65ac917d";
    public static final String TAG = "MainActivity";

    List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the Recycle View up with its adapter
        RecyclerView rmMovies = findViewById(R.id.rvMovies);
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rmMovies.setAdapter(movieAdapter);
        rmMovies.setLayoutManager(new LinearLayoutManager(this));



        //Create new http client
        AsyncHttpClient client = new AsyncHttpClient();
        //Make a request to the API
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            //If the request succeded:
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                //Try to get the array of Results
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: "+ results.toString());
                    //Add array of movies to movies, then notify this to the adapter
                    movies.addAll(Movie.toMovieArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "No results in JSON");
                }


            }

            //If request failed
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure" + s, throwable);
            }
        });
    }
}
