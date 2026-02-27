package lt.viko.eif.tsaviscevas.first.spring.db;


import lt.viko.eif.tsaviscevas.first.spring.model.Account;
import lt.viko.eif.tsaviscevas.first.spring.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}