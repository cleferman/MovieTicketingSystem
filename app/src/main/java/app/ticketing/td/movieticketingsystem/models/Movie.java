package app.ticketing.td.movieticketingsystem.models;

import java.sql.Date;
import java.sql.Time;

public class Movie {

    private int ID;
    private String Name;
    private int IDCinema;
    private Time Ora;
    private Date Data;
    private int Room;

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
}
