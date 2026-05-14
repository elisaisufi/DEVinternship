package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.entity.CartItem;
import al.dev.ecommerce_app.entity.Order;
import al.dev.ecommerce_app.entity.OrderItem;
import al.dev.ecommerce_app.entity.Product;
import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.enums.OrderStatus;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.OrderRepository;
import al.dev.ecommerce_app.repository.ProductRepository;
import al.dev.ecommerce_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public Order checkout(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new CustomException("User not found")
                );

        List<CartItem> cartItems = cartService.getUserCart(userId);

        if(cartItems.isEmpty()) {
            throw new CustomException("Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if(product.getStock() < cartItem.getQuantity()) {
                throw new CustomException(
                        product.getName() + " is out of stock"
                );
            }

            product.setStock(
                    product.getStock() - cartItem.getQuantity()
            );

            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

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

        cartService.clearCart(userId);

        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId);
    }

    public Order payOrder(Long orderId, boolean paymentSuccessful) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException("Order not found")
                );

        if(paymentSuccessful) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }

        return orderRepository.save(order);
    }
}