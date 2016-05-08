package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Movie implements Parcelable {

    private int ID;
    private String Name;
    private int CinemaID;
    private int Room;
    private String Description;

    public Movie() {

    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setCinemaID(int CinemaID) {
        this.CinemaID = CinemaID;
    }

    public int getCinemaID() {
        return this.CinemaID;
    }

    public void setRoom(int Room) {
        this.Room = Room;
    }

    public int getRoom() {
        return this.Room;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return this.Name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    @Override
    public String toString() {
        return Name;
    }

    protected Movie(Parcel in) {
        ID = in.readInt();
        Name = in.readString();
        CinemaID = in.readInt();
        Room = in.readInt();
        Description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeInt(CinemaID);
        dest.writeInt(Room);
        dest.writeString(Description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}