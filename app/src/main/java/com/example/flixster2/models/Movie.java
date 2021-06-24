package com.example.flixster2.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    String title;
    String overview;
    String posterPath;
    String backdropPath;

    public Movie(JSONObject jsonObject) throws JSONException {
            title = jsonObject.getString("title");
            //posterPath = jsonObject.getString("poster_path");
            overview = jsonObject.getString("overview");
            backdropPath = jsonObject.getString("backdrop_path");
    }

    public static List<Movie> toMovieArray(JSONArray jsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i< jsonArray.length(); i++){
            movies.add(new Movie(jsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }
}
