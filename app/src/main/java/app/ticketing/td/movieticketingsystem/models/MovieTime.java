package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

public class MovieTime implements Parcelable{

    private int MovieID;
    private Time MovieTime;

    public MovieTime() {

    }

    protected MovieTime(Parcel in) {
        MovieID = in.readInt();
        MovieTime = new Time(in.readLong());
    }

    public static final Creator<MovieTime> CREATOR = new Creator<MovieTime>() {
        @Override
        public MovieTime createFromParcel(Parcel in) {
            return new MovieTime(in);
        }

        @Override
        public MovieTime[] newArray(int size) {
            return new MovieTime[size];
        }
    };

    //region GETTERS/SETTERS
    public Time getMovieTime() {
        return MovieTime;
    }

    public void setMovieTime(Time movieTime) {
        MovieTime = movieTime;
    }

    public int getMovieID() {
        return MovieID;
    }

    public void setMovieID(int movieID) {
        MovieID = movieID;
    }
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(MovieID);
        parcel.writeLong(MovieTime.getTime());
    }

    @Override
    public String toString() {
        return MovieTime.toString();
    }
}
