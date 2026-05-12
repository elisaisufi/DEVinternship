package al.dev.ecommerce_app.Controller;

import al.dev.ecommerce_app.Entity.Order;
import al.dev.ecommerce_app.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public Order checkout(@PathVariable Long userId) {
        return orderService.checkout(userId);
    }

    @GetMapping("/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @PutMapping("/pay/{orderId}")
    public Order payOrder(@PathVariable Long orderId) {
        return orderService.payOrder(orderId);
    }
}