package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.dto.OrderResponse;
import al.dev.ecommerce_app.entity.*;
import al.dev.ecommerce_app.enums.OrderStatus;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.OrderRepository;
import al.dev.ecommerce_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartService cartService;

    @Transactional
    public OrderResponse checkout(String username) {

        User user = userService.getByUsername(username);

        List<CartItem> cartItems =
                cartService.getCartItemsByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new CustomException("Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (!product.isActive()) {
                throw new CustomException(
                        product.getName() + " is unavailable"
                );
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new CustomException(
                        product.getName() + " is out of stock"
                );
            }

            product.setStock(
                    product.getStock() - cartItem.getQuantity()
            );

            productRepository.save(product);

            BigDecimal itemTotal =
                    product.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    );

            total = total.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user.getId());

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> getUserOrders(String username) {

        User user = userService.getByUsername(username);

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    public OrderResponse payOrder(
            Long orderId,
            boolean paymentSuccessful,
            String username
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException("Order not found")
                );

        if (!order.getUser()
                .getUsername()
                .equals(username)) {

            throw new CustomException("Access denied");
        }

        order.setStatus(
                paymentSuccessful
                        ? OrderStatus.PAID
                        : OrderStatus.FAILED
        );

        return OrderResponse.from(
                orderRepository.save(order)
        );
    }
}