package lt.viko.eif.tsaviscevas.first.spring.service;

import lt.viko.eif.tsaviscevas.first.spring.model.Restaurant;
import lt.viko.eif.tsaviscevas.first.spring.model.RestaurantEntity;
import lt.viko.eif.tsaviscevas.first.spring.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Command line runner that provides a console menu
 * for starting the XML server, receiving XML files,
 * and transforming XML to POJO objects and back.
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    /**
     * Repository for saving data to the database.
     */
    @Autowired
    private RestaurantRepository repository;

    /**
     * Default constructor.
     */
    public CommandLineRunnerImpl() {
    }

    /**
     * Runs the console menu when the application starts.
     *
     * @param args command line arguments
     * @throws Exception if execution fails
     */
    @Override
    public void run(String... args) throws Exception {

        XMLServer server = new XMLServer();
        XMLClient client = new XMLClient();
        XMLTransformationService transformer = new XMLTransformationService();

        Scanner scanner = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n===== MENU =====");
            System.out.println("1 - Start XML Server");
            System.out.println("2 - Receive XML Client");
            System.out.println("3 - XML -> POJO");
            System.out.println("4 - POJO -> XML");
            System.out.println("0 - Exit");

            choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    new Thread(server::sendXML).start();
                    break;

                case 2:
                    client.receiveXML();
                    break;

                case 3:
                    Restaurant restaurant = transformer.transformToPOJO("received_restaurant.xml");

                    if (restaurant != null) {
                        System.out.println("Restaurant loaded:");
                        System.out.println(restaurant.getName());
                        System.out.println("Menu items: " + restaurant.getMenuItems().size());

                        // save to database
                        RestaurantEntity entity = new RestaurantEntity(1L, restaurant.getName());
                        repository.save(entity);

                        System.out.println("Saved to database!");
                    }
                    break;

                case 4:
                    Restaurant r = transformer.transformToPOJO("received_restaurant.xml");

                    if (r != null) {
                        transformer.transformToXML(r, "generated_restaurant.xml");
                    }
                    break;

                case 0:
                    System.out.println("Program finished.");
                    break;

                default:
                    System.out.println("Wrong option.");
            }

        } while (choice != 0);
    }
}