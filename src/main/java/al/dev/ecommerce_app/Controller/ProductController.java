package al.dev.ecommerce_app.Controller;

import al.dev.ecommerce_app.Dto.ProductDto;
import al.dev.ecommerce_app.Entity.Product;
import al.dev.ecommerce_app.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product create(@Valid @RequestBody ProductDto dto) {
        return productService.create(dto);
    }

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String name) {
        return productService.search(name);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto dto
    ) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}