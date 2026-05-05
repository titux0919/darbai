package lt.viko.eif.tsaviscevas.movie.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "movie")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "year", "rating", "popular", "category", "actors"})
public class Movie {

    private String title;
    private int year;
    private float rating;
    private boolean popular;
    private char category;

    @XmlElementWrapper(name = "actors")
    @XmlElement(name = "actor")
    private List<Actor> actors;

    public Movie() {}

    public Movie(String title, int year, float rating, boolean popular, char category, List<Actor> actors) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.popular = popular;
        this.category = category;
        this.actors = actors;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }

    public char getCategory() { return category; }
    public void setCategory(char category) { this.category = category; }

    public List<Actor> getActors() { return actors; }
    public void setActors(List<Actor> actors) { this.actors = actors; }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("====================================\n");
        sb.append("MOVIE\n");
        sb.append("====================================\n");

        sb.append("Title    : ").append(title).append("\n");
        sb.append("Year     : ").append(year).append("\n");
        sb.append("Rating   : ").append(rating).append("\n");
        sb.append("Popular  : ").append(popular).append("\n");
        sb.append("Category : ").append(category).append("\n");

        sb.append("Actors:\n");

        if (actors != null && !actors.isEmpty()) {
            for (Actor a : actors) {
                sb.append("   - ")
                        .append(a.getName())
                        .append(" (")
                        .append(a.getAge())
                        .append(")\n");
            }
        } else {
            sb.append("   (no actors)\n");
        }

        sb.append("\n"); // 🔥 tarpas tarp filmų

        return sb.toString();
    }
}