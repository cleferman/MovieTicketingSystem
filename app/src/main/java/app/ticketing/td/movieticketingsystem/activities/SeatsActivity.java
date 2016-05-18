package app.ticketing.td.movieticketingsystem.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import app.ticketing.td.movieticketingsystem.ConnectionClass;
import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Movie;
import app.ticketing.td.movieticketingsystem.models.MovieDate;
import app.ticketing.td.movieticketingsystem.models.MovieTime;
import app.ticketing.td.movieticketingsystem.models.Seat;

public class SeatsActivity extends AppCompatActivity {

    private List<Seat> selectedSeats = new ArrayList<Seat>();
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


        Button_Back = (Button) findViewById(R.id.Button_Back);
        Button_Next = (Button) findViewById(R.id.Button_Next);

        Button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeatsActivity.this, MoviesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSeats != null) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            updateSeats(selectedSeats);
                        }
                    });
                    //Intent intent = new Intent(SeatsActivity.this, MoviesActivity.class);
                    //startActivity(intent);
                }
            }
        });

        List<Seat> seats = getSeats(selectedMovie.getRoom(), selectedDate.getMovieDate(), selectedTime.getMovieTime());

        TableLayout layout = (TableLayout) findViewById(R.id.tableLayout);

        TableRow tableRow = null;
        int oldSeatRow = 0;
        for (final Seat seat : seats) {
            if (seat.getSeatColumn() == 5)
                layout.addView(tableRow);
            if (oldSeatRow != seat.getSeatRow()) {
                tableRow = new TableRow(this);
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            }
            if (seat.getSeatColumn() <= 5) {
                final boolean isSeatTaken = seat.isTaken();
                final ImageButton btnSeat = new ImageButton(this);
                if (isSeatTaken)
                    btnSeat.setImageResource(R.drawable.common_ic_googleplayservices);
                else
                    btnSeat.setImageResource(R.mipmap.seat_front);

                btnSeat.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                btnSeat.setTag("seat_" + seat.getSeatRow() + seat.getSeatColumn());
                btnSeat.setId(seat.getSeatRow() * 10 + seat.getSeatColumn());

                btnSeat.setOnClickListener(new View.OnClickListener() {
                    private boolean isTaken = isSeatTaken;
                    private boolean isTemporarySelected = false;

                    @Override
                    public void onClick(View view) {
                        btnSeat.getId();
                        if (isTaken) {
                            return;
                        }
                        if (!isTemporarySelected) {
                            seat.setTaken(true);
                            selectedSeats.add(seat);
                            btnSeat.setImageResource(R.drawable.common_ic_googleplayservices);
                            isTemporarySelected = true;
                        } else {
                            seat.setTaken(false);
                            btnSeat.setImageResource(R.mipmap.seat_front);
                            isTemporarySelected = false;
                        }
                    }
                });
                tableRow.addView(btnSeat);
            }
            oldSeatRow = seat.getSeatRow();
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
                seat.setRoomID(seatsResultSet.getInt(1));
                seat.setSeatRow(seatsResultSet.getInt(2));
                seat.setSeatColumn(seatsResultSet.getInt(3));
                seat.setTaken(seatsResultSet.getBoolean(4));
                seat.setMovieDate(seatsResultSet.getDate(5));
                seat.setMovieTime(seatsResultSet.getTime(6));
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

    private void updateSeats(List<Seat> seats) {
        Statement statement = ConnectionClass.GetStatement();
        if (statement == null)
            return;
        for(Seat seat : seats) {
            int binaryValue = (seat.isTaken()) ? 1 : 0;
            String query = "UPDATE Seats SET IsTaken = " + binaryValue + " WHERE RoomID = 11"; //SeatID to be added
            try {
                statement.executeUpdate(query);

                statement.getConnection().close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
}
