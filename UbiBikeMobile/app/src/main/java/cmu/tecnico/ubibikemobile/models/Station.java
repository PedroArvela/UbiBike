package cmu.tecnico.ubibikemobile.models;

import com.google.android.gms.maps.model.LatLng;

public class Station {

    private final String name;
    private final LatLng coordinates;
    private int freeBikes;
    private int reservedBikes;

    public Station(String name, int freeBikes, LatLng coordinates) {
        this.name = name;
        this.freeBikes = freeBikes;
        this.coordinates = coordinates;
        this.reservedBikes = 0;
    }

    public Station(String name, int freeBikes, LatLng coordinates, int reservedBikes) {
        this(name, freeBikes, coordinates);
        this.reservedBikes = reservedBikes;
    }

    public String getName() { return name; }
    public LatLng getCoordinates() { return coordinates; }
    public int getReservedBikesBikes() { return reservedBikes; }
    public int getFreeBikes() { return freeBikes; }

    public boolean reserveBike() {
        if(freeBikes == 0)
            return false;

        reservedBikes++;
        freeBikes--;
        return true;
    }

    public boolean cancelBikeReservation() {
        if(reservedBikes == 0)
            return false;

        reservedBikes--;
        freeBikes++;
        return true;
    }

    public boolean pickupBike() {
        if(freeBikes == 0)
            return false;

        reservedBikes--;
        freeBikes--;
        return true;
    }

    public void dropoffBike() {
        freeBikes++;
    }

}
