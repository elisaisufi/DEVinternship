package al.dev.ecommerce_app.Service;

import al.dev.ecommerce_app.Dto.CartDto;
import al.dev.ecommerce_app.Entity.CartItem;
import al.dev.ecommerce_app.Entity.Product;
import al.dev.ecommerce_app.Entity.User;
import al.dev.ecommerce_app.Exception.CustomException;
import al.dev.ecommerce_app.Repository.CartItemRepository;
import al.dev.ecommerce_app.Repository.ProductRepository;
import al.dev.ecommerce_app.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItem addToCart(CartDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new CustomException("User not found")
                );

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() ->
                        new CustomException("Product not found")
                );

        if(product.getStock() < dto.getQuantity()) {
            throw new CustomException("Not enough stock");
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(dto.getQuantity())
                .build();

        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getUserCart(Long userId) {

        return cartItemRepository.findByUserId(userId);
    }

    public CartItem updateQuantity(Long cartItemId, int quantity) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new CustomException("Cart item not found")
                );

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public void removeItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new CustomException("Cart item not found")
                );

        cartItemRepository.delete(cartItem);
    }

    public void clearCart(Long userId) {

        cartItemRepository.deleteByUserId(userId);
    }
}
