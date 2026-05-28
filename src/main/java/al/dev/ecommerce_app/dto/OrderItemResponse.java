package al.dev.ecommerce_app.dto;

import al.dev.ecommerce_app.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public static OrderItemResponse from(OrderItem orderItem) {

        OrderItemResponse response = new OrderItemResponse();

        response.setId(orderItem.getId());
        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setQuantity(orderItem.getQuantity());
        response.setPrice(orderItem.getPrice());
        response.setSubtotal(
                orderItem.getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
        );

        return response;
    }
}