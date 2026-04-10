package lt.viko.eif.tsaviscevas.first.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class.
 * This class starts the Spring Boot application.
 */
@SpringBootApplication
public class FirstSpringBootApplication {

	/**
	 * Default constructor.
	 */
	public FirstSpringBootApplication() {
	}

	/**
	 * Application entry point.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FirstSpringBootApplication.class, args);
	}

}