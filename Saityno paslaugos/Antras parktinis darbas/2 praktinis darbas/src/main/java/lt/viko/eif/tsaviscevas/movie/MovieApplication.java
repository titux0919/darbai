package lt.viko.eif.tsaviscevas.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieApplication.class, args);

		try {
		MovieConsoleMenu.main(args);
	} catch (Exception e) {
			e.printStackTrace();
		}
}}