package lt.viko.eif.tsaviscevas.first.spring.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Restaurant model.
 * Verifies that restaurant properties work correctly.
 */
public class RestaurantTest {

    /**
     * Default constructor.
     */
    public RestaurantTest() {
    }

    @Test
    void testRestaurantName() {

        Restaurant restaurant = new Restaurant();
        restaurant.setName("City Restaurant");

        assertEquals("City Restaurant", restaurant.getName());

    }
}