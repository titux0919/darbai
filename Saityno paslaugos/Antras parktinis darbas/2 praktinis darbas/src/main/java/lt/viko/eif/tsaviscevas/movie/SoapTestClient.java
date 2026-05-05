package lt.viko.eif.tsaviscevas.movie;

import lt.viko.eif.tsaviscevas.movie.client.MovieServiceImplService;
import lt.viko.eif.tsaviscevas.movie.server.MovieService;
import lt.viko.eif.tsaviscevas.movie.model.Movie;

import java.util.List;

public class SoapTestClient {
    public static void main(String[] args) {

        MovieServiceImplService service =
                new MovieServiceImplService();

        MovieService port = service.getMovieServiceImplPort();

        List<Movie> movies = port.getMovies();

        for (Movie m : movies) {
            System.out.println(m);
            System.out.println(); // tarpas tarp filmų
        }
    }
}