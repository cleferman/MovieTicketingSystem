package app.ticketing.td.movieticketingsystem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Movie;
import app.ticketing.td.movieticketingsystem.models.MovieDate;
import app.ticketing.td.movieticketingsystem.models.MovieTime;

public class SeatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra(MoviesActivity.SELECTED_MOVIE);
        final MovieDate selectedDate = intent.getParcelableExtra(MoviesActivity.SELECTED_DATE);
        final MovieTime selectedTime = intent.getParcelableExtra(MoviesActivity.SELECTED_TIME);

        // MERGE BAA
    }
}
