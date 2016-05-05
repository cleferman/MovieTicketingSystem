package app.ticketing.td.movieticketingsystem.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Cinema implements Parcelable {
    private int ID;
    private String Name;

    public Cinema() {

    }

    protected Cinema(Parcel in) {
        ID = in.readInt();
        Name = in.readString();
    }

    public static final Creator<Cinema> CREATOR = new Creator<Cinema>() {
        @Override
        public Cinema createFromParcel(Parcel in) {
            return new Cinema(in);
        }

        @Override
        public Cinema[] newArray(int size) {
            return new Cinema[size];
        }
    };

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
    }
}
