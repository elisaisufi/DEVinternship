package al.dev.ecommerce_app.Service;

import al.dev.ecommerce_app.Entity.*;
import al.dev.ecommerce_app.Enum.OrderStatus;
import al.dev.ecommerce_app.Exception.CustomException;
import al.dev.ecommerce_app.Repository.CartItemRepository;
import al.dev.ecommerce_app.Repository.OrderRepository;
import al.dev.ecommerce_app.Repository.ProductRepository;
import al.dev.ecommerce_app.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Order checkout(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new CustomException("User not found")
                );

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

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

        cartItemRepository.deleteByUserId(userId);

        return savedOrder;
    }

    public List<Order> getUserOrders(Long userId) {

        return orderRepository.findByUserId(userId);
    }

    public Order payOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new CustomException("Order not found")
                );

        order.setStatus(OrderStatus.PAID);

        return orderRepository.save(order);
    }
}
