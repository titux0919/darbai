package lt.viko.eif.tsaviscevas.movie.server;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.tsaviscevas.movie.model.Actor;
import lt.viko.eif.tsaviscevas.movie.model.Movie;

@WebService(endpointInterface = "lt.viko.eif.tsaviscevas.movie.server.MovieService")
public class MovieServiceImpl implements MovieService {

    @Override
    public List<Movie> getMovies() {

        List<Movie> movies = new ArrayList<>();

        // 1
        List<Actor> actors1 = new ArrayList<>();
        actors1.add(new Actor("John Darkwood", 40));
        actors1.add(new Actor("Emma Stonefield", 35));
        movies.add(new Movie("Inception", 2010, 8.8f, true, 'A', actors1));

        // 2
        List<Actor> actors2 = new ArrayList<>();
        actors2.add(new Actor("Anna Night", 30));
        actors2.add(new Actor("Chris Black", 42));
        movies.add(new Movie("Shadow City", 2015, 7.5f, false, 'B', actors2));

        // 3
        List<Actor> actors3 = new ArrayList<>();
        actors3.add(new Actor("Mike Storm", 38));
        actors3.add(new Actor("Lisa Ray", 29));
        movies.add(new Movie("Future War", 2022, 9.1f, true, 'A', actors3));

        return movies;
    }
}