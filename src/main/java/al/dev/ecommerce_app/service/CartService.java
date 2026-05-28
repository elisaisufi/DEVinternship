package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.dto.CartDto;
import al.dev.ecommerce_app.dto.CartItemResponse;
import al.dev.ecommerce_app.entity.CartItem;
import al.dev.ecommerce_app.entity.Product;
import al.dev.ecommerce_app.entity.User;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;

    public CartItemResponse addToCart(CartDto dto, String username) {

        User user = userService.getByUsername(username);

        Product product = productService.getEntityById(dto.getProductId());

        if (product.getStock() < dto.getQuantity()) {
            throw new CustomException("Not enough stock");
        }

        CartItem existingItem =
                cartItemRepository
                        .findByUserIdAndProductId(
                                user.getId(),
                                product.getId()
                        )
                        .orElse(null);

        if (existingItem != null) {

            int newQuantity =
                    existingItem.getQuantity() + dto.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new CustomException("Not enough stock");
            }

            existingItem.setQuantity(newQuantity);

            return CartItemResponse.from(
                    cartItemRepository.save(existingItem)
            );
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(dto.getQuantity())
                .build();

        return CartItemResponse.from(
                cartItemRepository.save(cartItem)
        );
    }

    public List<CartItemResponse> getUserCart(String username) {

        User user = userService.getByUsername(username);

        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(CartItemResponse::from)
                .toList();
    }

    public CartItemResponse updateQuantity(
            Long cartItemId,
            int quantity,
            String username
    ) {

        CartItem cartItem =
                getCartItemAndVerifyOwner(cartItemId, username);

        if (quantity <= 0) {
            throw new CustomException(
                    "Quantity must be at least 1"
            );
        }

        if (quantity > cartItem.getProduct().getStock()) {
            throw new CustomException("Not enough stock");
        }

        cartItem.setQuantity(quantity);

        return CartItemResponse.from(
                cartItemRepository.save(cartItem)
        );
    }

    public void removeItem(Long cartItemId, String username) {

        CartItem cartItem =
                getCartItemAndVerifyOwner(cartItemId, username);

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart(Long userId) {

        cartItemRepository.deleteByUserId(userId);
    }

    public List<CartItem> getCartItemsByUserId(Long userId) {

        return cartItemRepository.findByUserId(userId);
    }

    private CartItem getCartItemAndVerifyOwner(
            Long cartItemId,
            String username
    ) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new CustomException("Cart item not found")
                );

        if (!cartItem.getUser()
                .getUsername()
                .equals(username)) {

            throw new CustomException("Access denied");
        }

        return cartItem;
    }
}