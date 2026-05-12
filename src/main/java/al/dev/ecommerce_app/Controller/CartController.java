package al.dev.ecommerce_app.Controller;

import al.dev.ecommerce_app.Dto.CartDto;
import al.dev.ecommerce_app.Entity.CartItem;
import al.dev.ecommerce_app.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartItem addToCart(@Valid @RequestBody CartDto dto) {
        return cartService.addToCart(dto);
    }

    @GetMapping("/{userId}")
    public List<CartItem> getUserCart(@PathVariable Long userId) {
        return cartService.getUserCart(userId);
    }

    @PutMapping("/{cartItemId}")
    public CartItem updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ) {
        return cartService.updateQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/{cartItemId}")
    public void removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
    }
}