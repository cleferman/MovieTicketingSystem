package app.ticketing.td.movieticketingsystem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.*;

import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Cinema;
import app.ticketing.td.movieticketingsystem.models.Movie;
import app.ticketing.td.movieticketingsystem.models.MovieDate;
import app.ticketing.td.movieticketingsystem.models.MovieTime;
import app.ticketing.td.movieticketingsystem.models.Seat;

public class ConfirmationActivity extends AppCompatActivity {

    private static final int ticketCost = 15;
    private Button Button_Confirm;
    private TextView TextView_Confirmation;
    private EditText EditText_Email;

    private String selectedSeatsString = "(Row ; Column)\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();
        ArrayList<Seat> selectedSeats = intent.getParcelableArrayListExtra(SeatsActivity.SELECTED_SEATS);
        Cinema selectedCinema = intent.getParcelableExtra(MainActivity.SELECTED_CINEMA);
        Movie selectedMovie = intent.getParcelableExtra(MoviesActivity.SELECTED_MOVIE);
        MovieDate selectedDate = intent.getParcelableExtra(MoviesActivity.SELECTED_DATE);
        MovieTime selectedTime = intent.getParcelableExtra(MoviesActivity.SELECTED_TIME);

        Button_Confirm = (Button) findViewById(R.id.Button_Confirm);
        TextView_Confirmation = (TextView) findViewById(R.id.TextView_Confirmation);
        EditText_Email = (EditText) findViewById(R.id.EditText_Email);

        seatsToString(selectedSeats);

        TextView_Confirmation.setText(TextView_Confirmation.getText() +
                "\n\n" + "Cinema: "+ selectedCinema +
                "\n" + "Movie: " + selectedMovie +
                "\n" + "Date: " + selectedDate +
                "\n" + "At: " + selectedTime +
                "\n" + "Number of seats: " + selectedSeats.size() +
                "\n" + "Selected Seat(s): " + selectedSeatsString +
                "\n\n" + "Total Cost: " + selectedSeats.size()*ticketCost + " RON");

        Button_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailValidation())
                {
                    // Update the taken seats;
                    // Send confirmation e-mail;
                }
                else
                {
                    Toast.makeText(ConfirmationActivity.this, "The e-mail you entered is not valid.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    private boolean emailValidation()
    {
        // Simple expression to find whether or not the input is a valid e-mail address
        Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}");
        Matcher matcher = pattern.matcher(EditText_Email.getText().toString().toUpperCase());
        return matcher.matches();
    }
    private void seatsToString(ArrayList<Seat> selectedSeats)
    {
        for(Seat seat : selectedSeats)
        {
            selectedSeatsString = selectedSeatsString + "(" + seat.getSeatRow() + " ; " + seat.getSeatColumn() + ") ";
        }
    }


}
