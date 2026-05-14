package lt.praktika.hotelapp.model;

public record HotelItem(int id, String name) {
    @Override public String toString() { return name; }
}
