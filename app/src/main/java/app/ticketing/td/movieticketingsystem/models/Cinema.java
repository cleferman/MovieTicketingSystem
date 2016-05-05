package app.ticketing.td.movieticketingsystem.models;

public class Cinema {
    private int ID;
    private String Name;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString(){
        return Name;
    }
}
