package lt.praktika.hotelapp.model;

public record RoomTypeItem(int id, String name) {
    @Override public String toString() { return name; }
}
