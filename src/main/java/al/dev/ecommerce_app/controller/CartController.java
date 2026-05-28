package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.CartDto;
import al.dev.ecommerce_app.dto.CartItemResponse;
import al.dev.ecommerce_app.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // userId is now taken from the logged-in user, not from the request body
    @PostMapping
    public CartItemResponse addToCart(
            @Valid @RequestBody CartDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cartService.addToCart(dto, userDetails.getUsername());
    }

    // No userId in path — cart always belongs to the authenticated user
    @GetMapping
    public List<CartItemResponse> getMyCart(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cartService.getUserCart(userDetails.getUsername());
    }

    @PutMapping("/{cartItemId}")
    public CartItemResponse updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return cartService.updateQuantity(
                cartItemId, quantity, userDetails.getUsername()
        );
    }

    @DeleteMapping("/{cartItemId}")
    public void removeItem(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        cartService.removeItem(cartItemId, userDetails.getUsername());
    }
}