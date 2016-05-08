package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;

/**
 * Created by cosmi on 5/7/2016.
 */
public class MovieTime implements Parcelable{

    private int MovieID;
    private Time MovieTime;

    protected MovieTime(Parcel in) {
        MovieID = in.readInt();
    }
    public MovieTime() {

    }

    public static final Creator<MovieTime> CREATOR = new Creator<MovieTime>() {
        @Override
        public MovieTime createFromParcel(Parcel in) {
            MovieTime movieTime = new MovieTime(in);
            movieTime.setMovieTime(new Time(in.readLong()));
            return movieTime;
        }

        @Override
        public MovieTime[] newArray(int size) {
            return new MovieTime[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(MovieID);
        parcel.writeSerializable(MovieTime.getTime());
    }

    @Override
    public String toString() {
        return MovieTime.toString();
    }
}
