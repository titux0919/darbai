package lt.viko.eif.tsaviscevas.first.spring.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MenuItem model.
 * Verifies that MenuItem attributes are set and retrieved correctly.
 */
public class MenuItemTest {

    /**
     * Default constructor.
     */
    public MenuItemTest() {
    }

    @Test
    void testMenuItem() {

        MenuItem item = new MenuItem();
        item.setName("Pizza");
        item.setPrice(9.99);
        item.setAvailable(true);

        assertEquals("Pizza", item.getName());
        assertEquals(9.99, item.getPrice());
        assertTrue(item.isAvailable());
    }
}