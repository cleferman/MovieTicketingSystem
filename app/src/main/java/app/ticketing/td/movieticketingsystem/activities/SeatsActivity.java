package app.ticketing.td.movieticketingsystem.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import app.ticketing.td.movieticketingsystem.ConnectionClass;
import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Cinema;
import app.ticketing.td.movieticketingsystem.models.Movie;
import app.ticketing.td.movieticketingsystem.models.MovieDate;
import app.ticketing.td.movieticketingsystem.models.MovieTime;
import app.ticketing.td.movieticketingsystem.models.Seat;

import static app.ticketing.td.movieticketingsystem.R.mipmap.seat_front;

public class SeatsActivity extends AppCompatActivity {
    public final static String SELECTED_SEATS = "app.ticketing.td.movieticketingsystem.SELECTED_SEATS";

    private ArrayList<Seat> selectedSeats = new ArrayList<Seat>();
    private boolean roomBusy = true;
    private Button Button_Back;
    private Button Button_Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        Intent intent = getIntent();
        final Movie selectedMovie = intent.getParcelableExtra(MoviesActivity.SELECTED_MOVIE);
        final MovieDate selectedDate = intent.getParcelableExtra(MoviesActivity.SELECTED_DATE);
        final MovieTime selectedTime = intent.getParcelableExtra(MoviesActivity.SELECTED_TIME);
        final Cinema selectedCinema = intent.getParcelableExtra(MainActivity.SELECTED_CINEMA);

        Button_Back = (Button) findViewById(R.id.Button_Back);
        Button_Next = (Button) findViewById(R.id.Button_Next);

        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeatsActivity.this, MoviesActivity.class);
                intent.putExtra(MainActivity.SELECTED_CINEMA, selectedCinema);
                startActivity(intent);
                finish();
            }
        });
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roomBusy)
                    return;
                if (selectedSeats.size() > 0) {
                    Intent intent = new Intent(SeatsActivity.this, ConfirmationActivity.class);
                    intent.putParcelableArrayListExtra(SELECTED_SEATS, selectedSeats);
                    intent.putExtra(MainActivity.SELECTED_CINEMA, selectedCinema);
                    intent.putExtra(MoviesActivity.SELECTED_MOVIE, selectedMovie);
                    intent.putExtra(MoviesActivity.SELECTED_DATE, selectedDate);
                    intent.putExtra(MoviesActivity.SELECTED_TIME, selectedTime);
                    startActivity(intent);
                }
                else
                    Toast.makeText(SeatsActivity.this, "Please select at least a seat.", Toast.LENGTH_LONG).show();
            }
        });

        List<Seat> seats = getSeats(selectedMovie.getRoom(), selectedDate.getMovieDate(), selectedTime.getMovieTime());

        TableLayout layout = (TableLayout) findViewById(R.id.tableLayout);
        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);

        TableRow tableRow = null;
        int oldSeatRow = 0;
        for (final Seat seat : seats) {
            if(!seat.isTaken()) {
                roomBusy = false;
            }
            if (oldSeatRow != seat.getSeatRow()) {
                tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            }
            if (seat.getSeatColumn() <= 5) {
                final boolean isSeatTaken = seat.isTaken();
                final ImageButton btnSeat = new ImageButton(this);
                if (isSeatTaken)
                    btnSeat.setImageResource(R.mipmap.seat_front_taken);
                else
                    btnSeat.setImageResource(seat_front);

               // btnSeat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                btnSeat.setTag("seat_" + seat.getSeatRow() + seat.getSeatColumn());
                btnSeat.setId(seat.getSeatRow() * 10 + seat.getSeatColumn());

                btnSeat.setOnClickListener(new View.OnClickListener() {
                    private boolean isTaken = isSeatTaken;
                    private boolean isTemporarySelected = false;

                    @Override
                    public void onClick(View view) {
                        if (isTaken) {
                            return;
                        }
                        if (!isTemporarySelected) {
                            seat.setTaken(true);
                            if (selectedSeats.size() == 0) {
                                    selectedSeats.add(seat);
                            }
                            boolean isDifferentSeat = false;
                            for (int i = 0; i < selectedSeats.size(); i++) {
                                    isDifferentSeat = selectedSeats.get(i).getId() != seat.getId();
                            }
                            if(isDifferentSeat)
                                selectedSeats.add(seat);

                            btnSeat.setImageResource(R.mipmap.seat_front_selected);
                            isTemporarySelected = true;
                        } else {
                            seat.setTaken(false);
                            btnSeat.setImageResource(seat_front);
                            selectedSeats.remove(seat);
                            isTemporarySelected = false;
                        }
                    }
                });
                tableRow.addView(btnSeat,cellLp);
            }
            if (seat.getSeatColumn() == 5)
                layout.addView(tableRow,rowLp);
            oldSeatRow = seat.getSeatRow();
        }
        if(roomBusy){
            Toast.makeText(SeatsActivity.this, "Room is full, please choose another one.", Toast.LENGTH_LONG).show();
        }
    }

    //region PRIVATE MEMBERS
    private List<Seat> getSeats(int roomId, Date movieDate, Time movieTime) {
        Statement statement = ConnectionClass.GetStatement();
        if (statement == null)
            return null;

        List<Seat> seats = new ArrayList<>();
        String query = "SELECT * FROM SEATS WHERE RoomID = " + roomId + " AND MovieDate = '" + movieDate.toString() + "' AND MovieTime = '" + movieTime.toString() + "'";
        ResultSet seatsResultSet = null;
        try {
            seatsResultSet = statement.executeQuery(query);
            while (seatsResultSet.next()) {
                Seat seat = new Seat();
                seat.setId(seatsResultSet.getInt(1));
                seat.setRoomID(seatsResultSet.getInt(2));
                seat.setSeatRow(seatsResultSet.getInt(3));
                seat.setSeatColumn(seatsResultSet.getInt(4));
                seat.setTaken(seatsResultSet.getBoolean(5));
                seat.setMovieDate(seatsResultSet.getDate(6));
                seat.setMovieTime(seatsResultSet.getTime(7));
                seats.add(seat);
            }
            seatsResultSet.close();
            statement.getConnection().close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }
    //endregion
}
