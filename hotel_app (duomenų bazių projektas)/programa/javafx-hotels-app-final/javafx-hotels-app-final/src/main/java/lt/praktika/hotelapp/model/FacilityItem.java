package lt.praktika.hotelapp.model;

public record FacilityItem(int id, String name) {
    @Override public String toString() { return name; }
}
