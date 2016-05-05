package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;

public class Movie implements Parcelable {

    private int ID;
    private String Name;
    private int IDCinema;
    private static Time Ora;
    public static Date Data;
    private int Room;

    protected Movie(Parcel in) {
        ID = in.readInt();
        Name = in.readString();
        IDCinema = in.readInt();
        Room = in.readInt();
    }

    public Movie() {

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            Movie movie = new Movie(in);
            movie.setData(new Date(in.readLong()));
            movie.setOra(new Time(in.readLong()));
            return movie;
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setIDCinema(int IDCinema) {
        this.IDCinema = IDCinema;
    }

    public int getIDCinema() {
        return this.IDCinema;
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

    public void setData(Date Data) {
        this.Data = Data;
    }

    public Date getData() {
        return this.Data;
    }

    public void setOra(Time Ora) {
        this.Ora = Ora;
    }

    public Time getOra() {
        return this.Ora;
    }

    @Override
    public String toString() {
        return Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(Name);
        parcel.writeInt(IDCinema);
        parcel.writeInt(Room);
        parcel.writeSerializable(Data.getTime());
        parcel.writeSerializable(Ora.getTime());
    }
}
