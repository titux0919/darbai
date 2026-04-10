package lt.viko.eif.tsaviscevas.first.spring.model;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * Represents a restaurant customer.
 * A customer has a name and a table number in the restaurant.
 */
public class Customer {

    private String name;
    private int tableNumber;

    /**
     * Default constructor required for JAXB.
     */
    public Customer() {
    }

    /**
     * Creates a customer with a name and table number.
     *
     * @param name customer name
     * @param tableNumber table number assigned to the customer
     */
    public Customer(String name, int tableNumber) {
        this.name = name;
        this.tableNumber = tableNumber;
    }

    /**
     * Gets customer name.
     *
     * @return customer name
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Sets customer name.
     *
     * @param name customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets table number.
     *
     * @return table number
     */
    @XmlElement
    public int getTableNumber() {
        return tableNumber;
    }

    /**
     * Sets table number.
     *
     * @param tableNumber table number assigned to the customer
     */
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}