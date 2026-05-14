package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.entity.Order;
import al.dev.ecommerce_app.service.OrderService;
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
    public Order payOrder(
            @PathVariable Long orderId,
            @RequestParam boolean success
    ) {
        return orderService.payOrder(orderId, success);
    }
}