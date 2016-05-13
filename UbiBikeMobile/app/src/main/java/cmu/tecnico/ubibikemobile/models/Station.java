package cmu.tecnico.ubibikemobile.models;

import com.google.android.gms.maps.model.LatLng;

public class Station {

    private final String name;
    private final LatLng coordinates;
    private int totalBikes;
    private int bookedBikes;

    public Station(String name, int totalBikes, LatLng coordinates) {
        this.name = name;
        this.totalBikes = totalBikes;
        this.coordinates = coordinates;
        this.bookedBikes = 0;
    }

    public String getName() { return name; }
    public LatLng getCoordinates() { return coordinates; }
    public int getBookedBikes() { return bookedBikes; }
    public int getTotalBikes() { return totalBikes; }

    public boolean bookBike() {
        //TODO comunicar alterações com o server
        if(bookedBikes == totalBikes)
            return false;

        bookedBikes++;
        return true;
    }

    public boolean unbookBike() {
        //TODO comunicar alterações com o server
        if(bookedBikes == 0)
            return false;

        bookedBikes--;
        return true;
    }

    public boolean pickupBike(boolean wasBooked) {
        //TODO comunicar alterações com o server
        if(totalBikes == 0 || totalBikes == bookedBikes)
            return false;

        if(wasBooked)
            bookedBikes--;

        totalBikes--;
        return true;
    }

    public void dropoffBike() {
        //TODO comunicar alterações com o server
        totalBikes++;
    }

}
