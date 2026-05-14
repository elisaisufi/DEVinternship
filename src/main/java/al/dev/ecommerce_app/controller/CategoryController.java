package al.dev.ecommerce_app.controller;

import al.dev.ecommerce_app.dto.CategoryDto;
import al.dev.ecommerce_app.entity.Category;
import al.dev.ecommerce_app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Category create(@Valid @RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @PutMapping("/{id}")
    public Category update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDto dto
    ) {
        return categoryService.update(id, dto);
    }
}
