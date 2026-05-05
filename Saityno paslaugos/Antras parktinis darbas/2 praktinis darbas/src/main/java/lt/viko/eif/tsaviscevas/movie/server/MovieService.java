package lt.viko.eif.tsaviscevas.movie.server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;
import lt.viko.eif.tsaviscevas.movie.model.Movie;

@WebService
public interface MovieService {

    @WebMethod
    List<Movie> getMovies();
}