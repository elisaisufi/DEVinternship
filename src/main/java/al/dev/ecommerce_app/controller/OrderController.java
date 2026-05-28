package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.OrderResponse;
import al.dev.ecommerce_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // userId no longer comes from the path — it comes from the authenticated user
    @PostMapping("/checkout")
    public OrderResponse checkout(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return orderService.checkout(userDetails.getUsername());
    }

    // Users can only see their own orders
    @GetMapping
    public List<OrderResponse> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return orderService.getUserOrders(userDetails.getUsername());
    }

    @PutMapping("/pay/{orderId}")
    public OrderResponse payOrder(
            @PathVariable Long orderId,
            @RequestParam boolean success,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return orderService.payOrder(orderId, success, userDetails.getUsername());
    }
}