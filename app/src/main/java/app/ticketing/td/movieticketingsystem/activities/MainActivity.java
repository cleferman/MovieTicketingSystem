package app.ticketing.td.movieticketingsystem.activities;

import app.ticketing.td.movieticketingsystem.ConnectionClass;
import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Cinema;
import app.ticketing.td.movieticketingsystem.models.Movie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.URISyntaxException;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //region KEYS
    public final static String SELECTED_CINEMA = "app.ticketing.td.movieticketingsystem.SELECTED_CINEMA";
    //endregion

    private Spinner DropDown_Cinema;
    private Button Button_Next;
    private Cinema cinema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Cinema> cinemas = getCinemasFromDatabase();

        DropDown_Cinema = (Spinner) findViewById(R.id.DropDown_Cinemas);
        ArrayAdapter<Cinema> adapter = new ArrayAdapter<Cinema>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, cinemas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DropDown_Cinema.setAdapter(adapter);
        DropDown_Cinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cinema = (Cinema) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cinema = (Cinema) adapterView.getItemAtPosition(0);
            }
        });
        Button_Next = (Button) findViewById(R.id.Button_Next);
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm())
                    return;

                Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
                intent.putExtra(SELECTED_CINEMA, cinema);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region PRIVATE MEMBERS
    private boolean validateForm() {
        boolean isValid = false;

        View moviesSelectedView = DropDown_Cinema.getSelectedView();
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

        return isValid;
    }

    private List<Cinema> getCinemasFromDatabase() {
        List<Cinema> cinemas = new ArrayList<Cinema>();

        //query example
        Statement statement = ConnectionClass.GetStatement();
        if (statement == null)
            return null;
        try {
            String query = "SELECT * FROM Cinemas";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Cinema cinemaEntry = new Cinema();
                cinemaEntry.setID(resultSet.getInt(1));
                cinemaEntry.setName(resultSet.getString(2));

                cinemas.add(cinemaEntry);
            }
            resultSet.close();
            statement.getConnection().close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cinemas;
    }
    //endregion
}