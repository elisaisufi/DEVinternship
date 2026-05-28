package al.dev.ecommerce_app.dto;

import al.dev.ecommerce_app.entity.CartItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal subtotal;

    public static CartItemResponse from(CartItem cartItem) {

        CartItemResponse response = new CartItemResponse();

        response.setId(cartItem.getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setProductPrice(cartItem.getProduct().getPrice());
        response.setQuantity(cartItem.getQuantity());
        response.setSubtotal(
                cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );

        return response;
    }
}
