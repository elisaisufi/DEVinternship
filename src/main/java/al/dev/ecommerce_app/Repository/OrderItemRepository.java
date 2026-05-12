package al.dev.ecommerce_app.Repository;

import al.dev.ecommerce_app.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}