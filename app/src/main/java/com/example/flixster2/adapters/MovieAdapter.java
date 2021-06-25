package com.example.flixster2.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster2.MovieDetailsActivity;
import com.example.flixster2.R;
import com.example.flixster2.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

//Adapter that will pass the List<Movie> to a RecyclerView
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull

    //Inflates (creates) a layout from XML and returns a ViewHolder. Creates as many ViewHolders as they fit in the screen
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieItemView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieItemView);

    }

    //Adds data to each ViewHolder (created in the function above), As you scroll, it changes the already existing ViewHolders with new information
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get the movie to fill the View with
        Movie movie = movies.get(position);

        //Bind data to holder (the View HOlder)
        holder.bind(movie);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    //Each row of the RecyclerView, contains the item_movie layout
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        //Creation of the ViewHolder
        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivDetails);
            itemView.setOnClickListener(this);
        }

        //Adds or updates information to a given ViewHolder
        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int placeholder;


            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                placeholder= R.drawable.flicks_backdrop_placeholder;
            } else{
                imageUrl = movie.getPosterPath();
                placeholder = R.drawable.flicks_movie_placeholder;
            }

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(placeholder)
                    .transform(new RoundedCornersTransformation(30,0))
                    .into(ivPoster);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            Movie movie = movies.get(index);

            String linkAPI = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=38658f4199dd7fb175701cfd65ac917d&language=en-US",movie.getId());

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(linkAPI, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Headers headers, JSON json) {
                    JSONObject jsonObject =  json.jsonObject;
                    try {
                        JSONArray results = jsonObject.getJSONArray("results");
                        String ytlink = results.getJSONObject(0).getString("key");
                        Log.d("MovieAdapter", "yt id is: " + ytlink);
                        movie.setYtLink(ytlink);
                        changeScreen();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                    changeScreen();
                }

                public void changeScreen(){
                    //Create new intent with destination screen
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    //Add data to pass to the new Activity
                    intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie)); //Parser to enable passing an entire object
                    //Display the new Screen
                    context.startActivity(intent);

                }
            });



        }
    }
}
