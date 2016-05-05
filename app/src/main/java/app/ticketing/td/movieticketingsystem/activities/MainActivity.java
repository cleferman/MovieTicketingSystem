package app.ticketing.td.movieticketingsystem.activities;

import app.ticketing.td.movieticketingsystem.ConnectionClass;
import app.ticketing.td.movieticketingsystem.R;
import app.ticketing.td.movieticketingsystem.models.Cinema;

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

    public final static String SELECTED_CINEMA_NAME = "app.ticketing.td.movieticketingsystem.SELECTED_CINEMA_NAME";
    public final static String SELECTED_CINEMA_ID = "app.ticketing.td.movieticketingsystem.SELECTED_CINEMA_ID";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Spinner DropDown_Cinema;
    private Button Button_Next;
    private int CinemaID;
    private String CinemaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Cinema> cinemas = getCinemasFromDatabase();

        DropDown_Cinema = (Spinner)findViewById(R.id.DropDown_Cinemas);
        ArrayAdapter<Cinema> adapter = new ArrayAdapter<Cinema>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, cinemas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DropDown_Cinema.setAdapter(adapter);
        DropDown_Cinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cinema cinema = (Cinema)adapterView.getItemAtPosition(i);
                CinemaID = cinema.getID();
                CinemaName = cinema.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Cinema cinema = (Cinema)adapterView.getItemAtPosition(0);
                CinemaID = cinema.getID();
                CinemaName = cinema.getName();
            }
        });

        Button_Next = (Button)findViewById(R.id.Button_Next);
        Button_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoviesActivity.class);
                intent.putExtra(SELECTED_CINEMA_NAME, CinemaName);
                intent.putExtra(SELECTED_CINEMA_ID, CinemaID);
                startActivity(intent);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private List<Cinema> getCinemasFromDatabase() {
        ArrayList<Cinema> cinemas = new ArrayList<Cinema>();

        //query example
        Statement statement = ConnectionClass.GetStatement();
        if (statement == null)
            return null;
        try {
            String query = "SELECT * FROM Cinemas";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Cinema cinema = new Cinema();
                cinema.setID(resultSet.getInt(1));
                cinema.setName(resultSet.getString(2));

                cinemas.add(cinema);
            }
            resultSet.close();
            statement.getConnection().close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cinemas;
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

    private static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = "postgres://dpgdlvzhdlgcvc:5fPpVCeSZwmyTHbgLOIrjRYbla@ec2-54-228-183-183.eu-west-1.compute.amazonaws.com:5432/d788vu4ld3efd9";
        return DriverManager.getConnection(dbUrl);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.ticketing.td.movieticketingsystem/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://app.ticketing.td.movieticketingsystem/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}