package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.ProductDto;
import al.dev.ecommerce_app.dto.ProductResponse;
import al.dev.ecommerce_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse create(@Valid @RequestBody ProductDto dto) {

        return productService.create(dto);
    }

    @GetMapping
    public List<ProductResponse> getAll() {

        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) {

        return productService.getById(id);
    }

    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String name) {

        return productService.search(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto dto
    ) {

        return productService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        productService.delete(id);
    }
}