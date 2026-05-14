package lt.praktika.hotelapp.model;

public record CountryItem(int id, String name) {
    @Override public String toString() { return name; }
}
