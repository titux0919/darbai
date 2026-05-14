package lt.praktika.hotelapp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class HotelRow {
    private final SimpleIntegerProperty hotelId;
    private final SimpleStringProperty name;
    private final SimpleStringProperty city;
    private final SimpleStringProperty address;
    private final SimpleIntegerProperty starRating;
    private final SimpleStringProperty facilities;

    public HotelRow(int hotelId, String name, String city, String address, int starRating, String facilities) {
        this.hotelId = new SimpleIntegerProperty(hotelId);
        this.name = new SimpleStringProperty(name);
        this.city = new SimpleStringProperty(city);
        this.address = new SimpleStringProperty(address == null ? "-" : address);
        this.starRating = new SimpleIntegerProperty(starRating);
        this.facilities = new SimpleStringProperty(facilities == null ? "-" : facilities);
    }

    public int getHotelId() { return hotelId.get(); }
    public String getName() { return name.get(); }
    public String getCity() { return city.get(); }
    public String getAddress() { return address.get(); }
    public int getStarRating() { return starRating.get(); }
    public String getFacilities() { return facilities.get(); }
}
