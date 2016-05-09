package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class MovieDate implements Parcelable {

    private int MovieID;
    private Date MovieDate;

    public MovieDate() {

    }

    protected MovieDate(Parcel in) {
        MovieID = in.readInt();
        MovieDate = new Date(in.readLong());
    }

    public static final Creator<MovieDate> CREATOR = new Creator<MovieDate>() {
        @Override
        public MovieDate createFromParcel(Parcel in) {
            return new MovieDate(in);
        }

        @Override
        public MovieDate[] newArray(int size) {
            return new MovieDate[size];
        }
    };

    //region GETTERS/SETTERS
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
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(MovieID);
        parcel.writeLong(MovieDate.getTime());
    }

    @Override
    public String toString() {
        return MovieDate.toString();
    }
}
