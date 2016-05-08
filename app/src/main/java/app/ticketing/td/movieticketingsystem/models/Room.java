package app.ticketing.td.movieticketingsystem.models;

public class Room {
    private int ID;
    private int CinemaID;

    public int getCinemaID() {
        return CinemaID;
    }

    public void setCinemaID(int cinemaID) {
        CinemaID = cinemaID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
