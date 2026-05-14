package lt.praktika.hotelapp.model;

import java.math.BigDecimal;

public record CustomerItem(int id, String email, String fullName, BigDecimal balance) {
    @Override public String toString() { return fullName + " (" + email + ") - " + balance + " €"; }
}
