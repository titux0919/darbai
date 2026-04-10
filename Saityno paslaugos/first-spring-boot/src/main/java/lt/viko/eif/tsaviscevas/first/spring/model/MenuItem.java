package lt.viko.eif.tsaviscevas.first.spring.model;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * Represents a menu item in the restaurant.
 * Contains item name, price and availability status.
 */
public class MenuItem {

    private String name;
    private double price;
    private boolean available;

    /**
     * Default constructor required for JAXB.
     */
    public MenuItem() {
    }

    /**
     * Constructs a menu item with all attributes.
     *
     * @param name menu item name
     * @param price price of the item
     * @param available indicates if the item is available
     */
    public MenuItem(String name, double price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    /**
     * Gets menu item name.
     *
     * @return item name
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Sets menu item name.
     *
     * @param name item name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets menu item price.
     *
     * @return price
     */
    @XmlElement
    public double getPrice() {
        return price;
    }

    /**
     * Sets menu item price.
     *
     * @param price item price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Checks if the menu item is available.
     *
     * @return true if available
     */
    @XmlElement
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets menu item availability.
     *
     * @param available availability status
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}