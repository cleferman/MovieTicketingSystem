package app.ticketing.td.movieticketingsystem.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import app.ticketing.td.movieticketingsystem.ConnectionClass;
import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Cinema;
import app.ticketing.td.movieticketingsystem.models.Movie;
import app.ticketing.td.movieticketingsystem.models.MovieDate;
import app.ticketing.td.movieticketingsystem.models.MovieTime;
import app.ticketing.td.movieticketingsystem.models.Seat;
import app.ticketing.td.movieticketingsystem.utility.Mail;

public class ConfirmationActivity extends AppCompatActivity {

    //region KEYS
    public final static String SELECTED_MOVIE = "app.ticketing.td.movieticketingsystem.SELECTED_MOVIE";
    public final static String SELECTED_DATE = "app.ticketing.td.movieticketingsystem.SELECTED_DATE";
    public final static String SELECTED_TIME = "app.ticketing.td.movieticketingsystem.SELECTED_TIME";
    private final static String EMAIL_ADDRESS = "proiecttransmisiadatelor@gmail.com";
    //endregion

    private static final int ticketCost = 15;
    private Button Button_Confirm;
    private Button Button_Back;
    private Button Button_Exit;
    private TextView TextView_Confirmation;
    private EditText EditText_Email;

    private String selectedSeatsString = "(Row ; Column)\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();

        final ArrayList<Seat> selectedSeats = intent.getParcelableArrayListExtra(SeatsActivity.SELECTED_SEATS);
        final Cinema selectedCinema = intent.getParcelableExtra(MainActivity.SELECTED_CINEMA);
        final Movie selectedMovie = intent.getParcelableExtra(MoviesActivity.SELECTED_MOVIE);
        final MovieDate selectedDate = intent.getParcelableExtra(MoviesActivity.SELECTED_DATE);
        final MovieTime selectedTime = intent.getParcelableExtra(MoviesActivity.SELECTED_TIME);

        Button_Confirm = (Button) findViewById(R.id.Button_Confirm);
        Button_Exit = (Button) findViewById(R.id.Button_Exit);
        Button_Back = (Button) findViewById(R.id.Button_Back);
        TextView_Confirmation = (TextView) findViewById(R.id.TextView_Confirmation);
        EditText_Email = (EditText) findViewById(R.id.EditText_Email);

        seatsToString(selectedSeats);

        TextView_Confirmation.setText(TextView_Confirmation.getText() + getMessageBody(selectedSeats, selectedCinema, selectedMovie, selectedDate, selectedTime));

        Button_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        Button_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailValidation()) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            updateSeats(selectedSeats);
                        }
                    });

                    Mail m = new Mail("proiecttransmisiadatelor@gmail.com", "transmisiadatelor");

                    String[] toArr = {EditText_Email.getText().toString()};
                    m.setTo(toArr);
                    m.setFrom(EMAIL_ADDRESS);
                    m.setSubject("Thank you for purchasing!");
                    m.setBody(getMessageBody(selectedSeats, selectedCinema, selectedMovie, selectedDate, selectedTime));

                    try {
                        if (m.send()) {
                            Toast.makeText(ConfirmationActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                            Button_Back.setVisibility(View.INVISIBLE);
                            Button_Exit.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ConfirmationActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("MailApp", "Could not send email", e);
                    }


                } else {
                    Toast.makeText(ConfirmationActivity.this, "The e-mail you entered is not valid.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ConfirmationActivity.this, SeatsActivity.class);
                intent.putExtra(SELECTED_MOVIE, selectedMovie);
                intent.putExtra(SELECTED_DATE, selectedDate);
                intent.putExtra(SELECTED_TIME, selectedTime);
                intent.putExtra(MainActivity.SELECTED_CINEMA, selectedCinema);
                startActivity(intent);
                finish();
            }
        });
    }

    //region PRIVATE MEMBERS
    @NonNull
    private String getMessageBody(ArrayList<Seat> selectedSeats, Cinema selectedCinema, Movie selectedMovie, MovieDate selectedDate, MovieTime selectedTime) {
        return "\n\n" + "Cinema: " + selectedCinema +
                "\n" + "Movie: " + selectedMovie +
                "\n" + "Date: " + selectedDate +
                "\n" + "At: " + selectedTime +
                "\n" + "Number of seats: " + selectedSeats.size() +
                "\n" + "Selected Seat(s): " + selectedSeatsString +
                "\n\n" + "Total Cost: " + selectedSeats.size() * ticketCost + " RON";
    }

    private boolean emailValidation() {
        // Simple expression to find whether or not the input is a valid e-mail address
        Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}");
        Matcher matcher = pattern.matcher(EditText_Email.getText().toString().toUpperCase());
        return matcher.matches();
    }

    private void seatsToString(ArrayList<Seat> selectedSeats) {
        for (Seat seat : selectedSeats) {
            selectedSeatsString = selectedSeatsString + "(" + seat.getSeatRow() + " ; " + seat.getSeatColumn() + ") ";
        }
    }

    private void updateSeats(List<Seat> seats) {
        Statement statement = ConnectionClass.GetStatement();
        if (statement == null)
            return;
        for (Seat seat : seats) {
            int binaryValue = (seat.isTaken()) ? 1 : 0;
            String query = "UPDATE Seats SET IsTaken = " + binaryValue + " WHERE Id = " + seat.getId(); //SeatID to be added
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            statement.getConnection().close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
