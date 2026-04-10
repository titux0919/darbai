package lt.viko.eif.tsaviscevas.first.spring.service;

import lt.viko.eif.tsaviscevas.first.spring.model.Restaurant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for XMLTransformationService.
 * Verifies that XML files are correctly transformed into Restaurant objects.
 */
public class XMLTransformationServiceTest {

    /**
     * Default constructor.
     */
    public XMLTransformationServiceTest() {
    }

    /**
     * Tests conversion from XML file to Restaurant object.
     */
    @Test
    void testXMLtoPOJO() {

        XMLTransformationService service = new XMLTransformationService();

        Restaurant restaurant = service.transformToPOJO("src/main/resources/restaurant.xml");

        assertNotNull(restaurant);
        assertEquals("City Restaurant", restaurant.getName());

    }
}