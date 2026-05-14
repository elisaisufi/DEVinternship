package al.dev.ecommerce_app.repository;

import al.dev.ecommerce_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    List<Product> findByIsActiveTrue();
}
