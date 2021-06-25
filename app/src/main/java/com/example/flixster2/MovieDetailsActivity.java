package com.example.flixster2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster2.databinding.ActivityMovieDetailsBinding;
import com.example.flixster2.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    String TAG = "MovieDetailsActivity";

    TextView title;
    TextView overview;
    ImageView image;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        Log.d(TAG, "Details of movie: " + movie.getTitle());

        title = binding.tvDetailsTitle;
        overview = binding.tvDetailsOverview;
        image = binding.ivDetails;
        ratingBar = binding.rbDetails;

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());

        String imageUrl = movie.getBackdropPath();
        int placeholder= R.drawable.flicks_backdrop_placeholder;

        Glide.with(this)
                .load(imageUrl)
                .placeholder(placeholder)
                .into(image);
        
        ratingBar.setRating( movie.getVoteAverage().floatValue() / 2.0f);



        //Link image with YT player:
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Hi from listener");
                if (!movie.getYtLink().isEmpty()){
                    Log.d(TAG, "Hi from condition");
                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie.getYtLink())); //Parser to enable passing an entire object
                    MovieDetailsActivity.this.startActivity(intent);
                }

            }
        });





    }
}