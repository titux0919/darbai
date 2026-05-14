package lt.praktika.hotelapp;

import javafx.application.Application;

/**
 * Pagrindinis aplikacijos paleidimo taškas.
 */
public final class Main {
    private Main() {
        // Utility class.
    }

    public static void main(String[] args) {
        Application.launch(lt.praktika.hotelapp.app.HotelApplication.class, args);
    }
}
