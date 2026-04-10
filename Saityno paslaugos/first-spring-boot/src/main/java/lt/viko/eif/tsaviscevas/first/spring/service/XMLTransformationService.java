package lt.viko.eif.tsaviscevas.first.spring.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lt.viko.eif.tsaviscevas.first.spring.model.Restaurant;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.File;

/**
 * Service responsible for transforming Restaurant objects
 * to XML files and reading XML files back into Java objects.
 * Also performs XML validation using an XSD schema.
 */
public class XMLTransformationService {

    /**
     * Default constructor.
     */
    public XMLTransformationService() {
    }

    /**
     * Converts a Restaurant object into an XML file.
     *
     * @param restaurant restaurant object to be converted
     * @param filePath path where the XML file will be saved
     */
    public void transformToXML(Restaurant restaurant, String filePath) {
        try {

            JAXBContext context = JAXBContext.newInstance(Restaurant.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(filePath);

            marshaller.marshal(restaurant, file);
            marshaller.marshal(restaurant, System.out);

            System.out.println("XML created: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads an XML file and converts it into a Restaurant object.
     * The XML is validated against the restaurant.xsd schema.
     *
     * @param filePath path to the XML file
     * @return Restaurant object if conversion succeeds, otherwise null
     */
    public Restaurant transformToPOJO(String filePath) {

        try {

            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("XML file not found: " + file.getAbsolutePath());
                return null;
            }

            JAXBContext context = JAXBContext.newInstance(Restaurant.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // XSD validation
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("src/main/resources/restaurant.xsd"));
            unmarshaller.setSchema(schema);

            return (Restaurant) unmarshaller.unmarshal(file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}