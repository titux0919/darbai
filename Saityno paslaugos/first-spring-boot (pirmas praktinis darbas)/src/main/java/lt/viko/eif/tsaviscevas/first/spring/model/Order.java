package lt.viko.eif.tsaviscevas.first.spring.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.List;

/**
 * Represents a restaurant order containing menu items.
 * Each order has an ID and a list of ordered menu items.
 */
public class Order {

    private int orderId;
    private List<MenuItem> items;

    /**
     * Default constructor required for JAXB.
     */
    public Order() {
    }

    /**
     * Creates an order with ID and menu items.
     *
     * @param orderId unique order identifier
     * @param items list of ordered menu items
     */
    public Order(int orderId, List<MenuItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    /**
     * Gets order ID.
     *
     * @return order identifier
     */
    @XmlElement
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets order ID.
     *
     * @param orderId order identifier
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets list of menu items in the order.
     *
     * @return list of ordered menu items
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "menuItem")
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * Sets menu items in the order.
     *
     * @param items list of menu items
     */
    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}