package lt.praktika.hotelapp.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PredictionRow {
    private final SimpleIntegerProperty hotelId;
    private final SimpleStringProperty hotelName;
    private final SimpleStringProperty basePrice;
    private final SimpleStringProperty competitorPrice;
    private final SimpleStringProperty recommendedPrice;

    public PredictionRow(int hotelId, String hotelName, String basePrice, String competitorPrice, String recommendedPrice) {
        this.hotelId = new SimpleIntegerProperty(hotelId);
        this.hotelName = new SimpleStringProperty(hotelName == null ? "-" : hotelName);
        this.basePrice = new SimpleStringProperty(basePrice == null ? "0.00" : basePrice);
        this.competitorPrice = new SimpleStringProperty(competitorPrice == null ? "0.00" : competitorPrice);
        this.recommendedPrice = new SimpleStringProperty(recommendedPrice == null ? "0.00" : recommendedPrice);
    }

    public int getHotelId() { return hotelId.get(); }
    public String getHotelName() { return hotelName.get(); }
    public String getBasePrice() { return basePrice.get(); }
    public String getCompetitorPrice() { return competitorPrice.get(); }
    public String getRecommendedPrice() { return recommendedPrice.get(); }
}
