package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class MovieDate implements Parcelable {

    private int MovieID;
    private Date MovieDate;

    protected MovieDate(Parcel in) {
    }

    public MovieDate() {

    }

    public static final Creator<MovieDate> CREATOR = new Creator<MovieDate>() {
        @Override
        public MovieDate createFromParcel(Parcel in) {
            MovieDate movieDate = new MovieDate(in);
            movieDate.MovieDate = new Date(in.readLong());
            return movieDate;
        }

        @Override
        public MovieDate[] newArray(int size) {
            return new MovieDate[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(MovieID);
        parcel.writeSerializable(MovieDate);
    }

    public int getMovieID() {
        return MovieID;
    }

    public void setMovieID(int movieID) {
        MovieID = movieID;
    }

    public Date getMovieDate() {
        return MovieDate;
    }

    public void setMovieDate(Date movieDate) {
        MovieDate = movieDate;
    }

    @Override
    public String toString() {
        return MovieDate.toString();
    }
}
