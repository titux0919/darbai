package lt.praktika.hotelapp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookingRow {
    private final SimpleIntegerProperty bookingId;
    private final SimpleStringProperty customerEmail;
    private final SimpleStringProperty hotelName;
    private final SimpleStringProperty status;

    public BookingRow(int bookingId, String customerEmail, String hotelName, String status) {
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.customerEmail = new SimpleStringProperty(customerEmail == null ? "-" : customerEmail);
        this.hotelName = new SimpleStringProperty(hotelName == null ? "-" : hotelName);
        this.status = new SimpleStringProperty(status == null ? "-" : status);
    }

    public int getBookingId() { return bookingId.get(); }
    public String getCustomerEmail() { return customerEmail.get(); }
    public String getHotelName() { return hotelName.get(); }
    public String getStatus() { return status.get(); }
}
