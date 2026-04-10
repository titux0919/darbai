package lt.viko.eif.tsaviscevas.first.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Entity class representing a restaurant in the database.
 */
@Entity
public class RestaurantEntity {

    @Id
    private Long id;
    private String name;

    /**
     * Default constructor.
     */
    public RestaurantEntity() {
    }

    /**
     * Constructor with parameters.
     */
    public RestaurantEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}