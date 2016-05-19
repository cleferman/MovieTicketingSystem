package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.sql.Time;

public class Seat implements Parcelable{
    private int Id;
    private int RoomID;
    private int SeatRow;
    private int SeatColumn;
    private Date MovieDate;
    private Time MovieTime;
    private boolean IsTaken;

    public Seat() {

    }

    protected Seat(Parcel in) {
        Id = in.readInt();
        RoomID = in.readInt();
        SeatRow = in.readInt();
        SeatColumn = in.readInt();
        MovieDate = new Date(in.readLong());
        MovieTime = new Time(in.readLong());
        IsTaken = in.readByte() != 0;
    }

    public static final Creator<Seat> CREATOR = new Creator<Seat>() {
        @Override
        public Seat createFromParcel(Parcel in) {
            return new Seat(in);
        }

        @Override
        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isTaken() {
        return IsTaken;
    }

    public void setTaken(boolean taken) {
        IsTaken = taken;
    }

    public int getSeatRow() {
        return SeatRow;
    }

    public void setSeatRow(int seatRow) {
        SeatRow = seatRow;
    }

    public int getSeatColumn() {
        return SeatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        SeatColumn = seatColumn;
    }

    public Date getMovieDate() {
        return MovieDate;
    }

    public void setMovieDate(Date movieDate) {
        MovieDate = movieDate;
    }

    public Time getMovieTime() {
        return MovieTime;
    }

    public void setMovieTime(Time movieTime) {
        MovieTime = movieTime;
    }

    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int roomID) {
        RoomID = roomID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeInt(RoomID);
        parcel.writeInt(SeatRow);
        parcel.writeInt(SeatColumn);
        parcel.writeLong(MovieDate.getTime());
        parcel.writeLong(MovieTime.getTime());
        parcel.writeByte((byte) (IsTaken ? 1 : 0));
    }
}
