package lt.viko.eif.tsaviscevas;

import java.util.List;

public class CityService {
    public static List<City> getCities() {
        var cities = List.of(
                new City(1L, "Bratislava", 432000),
                new City(2L, "Budapest", 432000),
                new City(3L, "Vilnius", 432000),
                new City(4L, "Warsaw", 432000),
                new City(5L, "Riga", 432000),
                new City(6L, "Moscow", 432000),
                new City(7L, "London", 432000),
                new City(8L, "Rome", 432000));

        return cities;
    }
}
