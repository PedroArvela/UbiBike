package cmu.tecnico.ubibikeserver.domain;

public class Station {
	public String name;
	public String coordinates;
	public int freeBikes;
	public int reservedBikes;

	public Station(String name, String coordinates) {
		this.name = name;
		this.coordinates = coordinates;
		this.freeBikes = 10;
		this.reservedBikes = 0;
	}
	
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
