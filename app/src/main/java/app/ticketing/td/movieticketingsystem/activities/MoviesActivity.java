package app.ticketing.td.movieticketingsystem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.ticketing.td.movieticketingsystem.ConnectionClass;
import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Cinema;
import app.ticketing.td.movieticketingsystem.models.Movie;
import app.ticketing.td.movieticketingsystem.models.MovieDate;
import app.ticketing.td.movieticketingsystem.models.MovieTime;
import app.ticketing.td.movieticketingsystem.utility.NothingSelectedSpinnerAdapter;

public class MoviesActivity extends AppCompatActivity {

    //region KEYS
    public final static String SELECTED_MOVIE = "app.ticketing.td.movieticketingsystem.SELECTED_MOVIE";
    public final static String SELECTED_DATE = "app.ticketing.td.movieticketingsystem.SELECTED_DATE";
    public final static String SELECTED_TIME = "app.ticketing.td.movieticketingsystem.SELECTED_TIME";
    //endregion

    private TextView TextView_AvailableMovies;
    private Spinner DropDown_Movies;
    private Spinner DropDown_SelectDate;
    private Spinner DropDown_SelectTime;
    private Button Button_Back;
    private Button Button_Next;

    private Movie selectedMovie;
    private MovieDate selectedDate;
    private MovieTime selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Intent intent = getIntent();
        final Cinema selectedCinema = intent.getParcelableExtra(MainActivity.SELECTED_CINEMA);

        //section for finding controls
        TextView_AvailableMovies = (TextView) findViewById(R.id.TextView_AvailableMovies);
        DropDown_Movies = (Spinner) findViewById(R.id.DropDown_Movies);
        DropDown_SelectDate = (Spinner) findViewById(R.id.DropDown_SelectDate);
        DropDown_SelectTime = (Spinner) findViewById(R.id.DropDown_SelectTime);
        Button_Back = (Button) findViewById(R.id.Button_Back);
        Button_Next = (Button) findViewById(R.id.Button_Next);

        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoviesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm())
                    return;

                Intent intent = new Intent(MoviesActivity.this, SeatsActivity.class);
                intent.putExtra(SELECTED_MOVIE, selectedMovie);
                intent.putExtra(SELECTED_DATE, selectedDate);
                intent.putExtra(SELECTED_TIME, selectedTime);
                startActivity(intent);
            }
        });

        TextView_AvailableMovies.setText(TextView_AvailableMovies.getText() + " " + selectedCinema);
        List<Movie> movies = getMoviesFromDatabase(selectedCinema.getID());
        if (movies.size() > 0) {
            ArrayAdapter<Movie> adapter = new ArrayAdapter<Movie>(MoviesActivity.this, android.R.layout.simple_spinner_dropdown_item, movies);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            DropDown_Movies.setPrompt("Select a movie");
            DropDown_Movies.setAdapter(new NothingSelectedSpinnerAdapter(
                    adapter,
                    R.layout.contact_spinner_row_nothing_selected,
                    MoviesActivity.this));
            DropDown_Movies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int iCurrentSelection = DropDown_Movies.getSelectedItemPosition();

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(iCurrentSelection !=i) {
                        DropDown_SelectDate.setVisibility(View.INVISIBLE);
                        DropDown_SelectTime.setVisibility(View.INVISIBLE);
                        selectedMovie = (Movie) adapterView.getItemAtPosition(i);
                        if(selectedMovie != null) {
                            dateSpinner(selectedMovie);
                        }
                    }
                    iCurrentSelection = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedMovie = (Movie) adapterView.getItemAtPosition(0);
                }
            });
        } else {
            TextView_AvailableMovies.setText("No available movies for cinema " + selectedCinema.getName());
            DropDown_Movies.setVisibility(View.INVISIBLE);
        }
    }

    //region PRIVATE MEMBERS
    private boolean validateForm() {
        boolean isValid = false;

        View moviesSelectedView = DropDown_Movies.getSelectedView();
        if (moviesSelectedView != null && moviesSelectedView instanceof TextView) {
            TextView selectedTextView = (TextView) moviesSelectedView;
            if (selectedTextView.getText().equals("Please select...") || selectedTextView.getVisibility() == View.INVISIBLE) {
                String errorString = getResources().getString(R.string.nothingSelectedError);
                selectedTextView.setError(errorString);
                isValid = false;
            }
            else {
                selectedTextView.setError(null);
                isValid = true;
            }
        }

        View dateSelectedView = DropDown_SelectDate.getSelectedView();
        if (dateSelectedView != null && dateSelectedView instanceof TextView) {
            TextView selectedTextView = (TextView) dateSelectedView;
            if (selectedTextView.getText().equals("Please select...") || selectedTextView.getVisibility() == View.INVISIBLE) {
                String errorString = getResources().getString(R.string.nothingSelectedError);
                selectedTextView.setError(errorString);
                isValid = false;
            }
            else {
                selectedTextView.setError(null);
                isValid = true;
            }
        }

        View timeSelectedView = DropDown_SelectTime.getSelectedView();
        if (timeSelectedView != null && timeSelectedView instanceof TextView) {
            TextView selectedTextView = (TextView) timeSelectedView;
            if (selectedTextView.getText().equals("Please select...") || selectedTextView.getVisibility() == View.INVISIBLE) {
                String errorString = getResources().getString(R.string.nothingSelectedError);
                selectedTextView.setError(errorString);
                isValid = false;
            }
            else {
                selectedTextView.setError(null);
                isValid = true;
            }
        }
        return isValid;
    }

    private void dateSpinner(Movie movie) {
        final Movie localMovie = movie;
        ArrayAdapter<MovieDate> dateAdapter = new ArrayAdapter<MovieDate>(MoviesActivity.this, android.R.layout.simple_spinner_dropdown_item, getMovieDates(localMovie.getID()));
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DropDown_Movies.setPrompt("Select a date");
        DropDown_SelectDate.setAdapter(new NothingSelectedSpinnerAdapter(
                dateAdapter,
                R.layout.contact_spinner_row_nothing_selected,
                getBaseContext()
        ));
        DropDown_SelectDate.setVisibility(View.VISIBLE);
        DropDown_SelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DropDown_SelectTime.setVisibility(View.INVISIBLE);
                selectedDate = (MovieDate) adapterView.getItemAtPosition(i);
                if(selectedDate != null) {
                    timeSpinner(localMovie);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedDate = (MovieDate) adapterView.getItemAtPosition(0);
            }
        });
    }

    private void timeSpinner(Movie movie) {
        Movie localMovie = movie;
        ArrayAdapter<MovieTime> timeAdapter = new ArrayAdapter<MovieTime>(MoviesActivity.this, android.R.layout.simple_spinner_dropdown_item, getMovieTimes(localMovie.getID()));
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DropDown_SelectTime.setPrompt("Select a time");
        DropDown_SelectTime.setAdapter(new NothingSelectedSpinnerAdapter(
                timeAdapter,
                R.layout.contact_spinner_row_nothing_selected,
                getBaseContext()
        ));
        DropDown_SelectTime.setVisibility(View.VISIBLE);
        DropDown_SelectTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTime = (MovieTime) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedTime = (MovieTime) adapterView.getItemAtPosition(0);
            }
        });
    }

    private List<Movie> getMoviesFromDatabase(int id) {
        List<Movie> movies = new ArrayList<Movie>();
        //query example
        Statement statement = ConnectionClass.GetStatement();
        if (statement == null)
            return null;
        try {
            String query = "SELECT * FROM Movies WHERE CinemaID = " + id;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Movie movie = new Movie();

                movie.setID(resultSet.getInt(1));
                movie.setName(resultSet.getString(2));
                movie.setCinemaID(resultSet.getInt(3));
                movie.setRoom(resultSet.getInt(4));
                movie.setDescription(resultSet.getString(5));

                movies.add(movie);
            }
            resultSet.close();
            statement.getConnection().close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private List<MovieTime> getMovieTimes(int id){
        Statement statement = ConnectionClass.GetStatement();
        if(statement == null)
            return null;

        List<MovieTime> movieTimes = new ArrayList<>();
        String query = "SELECT * FROM MoviesTimes WHERE MovieID = " + id;
        ResultSet timesResultSet = null;
        try {
            timesResultSet = statement.executeQuery(query);
            while(timesResultSet.next()) {
                MovieTime movieTime = new MovieTime();
                movieTime.setMovieID(timesResultSet.getInt(1));
                movieTime.setMovieTime(timesResultSet.getTime(2));
                movieTimes.add(movieTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieTimes;
    }

    private List<MovieDate> getMovieDates(int id) {
        Statement statement = ConnectionClass.GetStatement();
        if(statement == null)
            return null;

        List<MovieDate> movieDates = new ArrayList<>();
        String query = "SELECT * FROM MoviesDates WHERE MovieID = " + id;
        ResultSet datesResultSet = null;
        try {
            datesResultSet = statement.executeQuery(query);
            while(datesResultSet.next()) {
                MovieDate movieDate = new MovieDate();
                movieDate.setMovieID(datesResultSet.getInt(1));
                movieDate.setMovieDate(datesResultSet.getDate(2));
                movieDates.add(movieDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieDates;
    }
    //endregion
}
