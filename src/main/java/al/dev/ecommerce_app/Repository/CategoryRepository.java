package al.dev.ecommerce_app.Repository;

import al.dev.ecommerce_app.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByIsActiveTrue();
}