package lt.viko.eif.tsaviscevas.first.spring.repository;

import lt.viko.eif.tsaviscevas.first.spring.model.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for RestaurantEntity.
 * Provides CRUD operations for the database.
 */
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}