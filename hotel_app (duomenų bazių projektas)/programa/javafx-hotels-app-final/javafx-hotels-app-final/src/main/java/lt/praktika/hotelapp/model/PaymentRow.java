package lt.praktika.hotelapp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PaymentRow {
    private final SimpleIntegerProperty paymentId;
    private final SimpleIntegerProperty bookingId;
    private final SimpleStringProperty amount;
    private final SimpleStringProperty paymentStatus;

    public PaymentRow(int paymentId, int bookingId, String amount, String paymentStatus) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.amount = new SimpleStringProperty(amount == null ? "0.00" : amount);
        this.paymentStatus = new SimpleStringProperty(paymentStatus == null ? "-" : paymentStatus);
    }

    public int getPaymentId() { return paymentId.get(); }
    public int getBookingId() { return bookingId.get(); }
    public String getAmount() { return amount.get(); }
    public String getPaymentStatus() { return paymentStatus.get(); }
}
