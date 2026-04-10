package lt.viko.eif.tsaviscevas.first.spring.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Represents a restaurant with menu items, customers and orders.
 * This class is used as the root element for XML transformation.
 */
@XmlRootElement(name = "restaurant")
public class Restaurant {

    private String name;
    private List<MenuItem> menuItems;
    private List<Order> orders;
    private List<Customer> customers;

    /**
     * Default constructor required for JAXB.
     */
    public Restaurant() {
    }

    /**
     * Constructs a restaurant with menu items, orders and customers.
     *
     * @param name restaurant name
     * @param menuItems list of menu items
     * @param orders list of restaurant orders
     * @param customers list of restaurant customers
     */
    public Restaurant(String name, List<MenuItem> menuItems, List<Order> orders, List<Customer> customers) {
        this.name = name;
        this.menuItems = menuItems;
        this.orders = orders;
        this.customers = customers;
    }

    /**
     * Gets restaurant name.
     *
     * @return restaurant name
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Sets restaurant name.
     *
     * @param name restaurant name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets restaurant menu items.
     *
     * @return list of menu items
     */
    @XmlElementWrapper(name = "menuItems")
    @XmlElement(name = "menuItem")
    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * Sets restaurant menu items.
     *
     * @param menuItems list of menu items
     */
    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    /**
     * Gets restaurant orders.
     *
     * @return list of orders
     */
    @XmlElementWrapper(name = "orders")
    @XmlElement(name = "order")
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Sets restaurant orders.
     *
     * @param orders list of orders
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Gets restaurant customers.
     *
     * @return list of customers
     */
    @XmlElementWrapper(name = "customers")
    @XmlElement(name = "customer")
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * Sets restaurant customers.
     *
     * @param customers list of customers
     */
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}