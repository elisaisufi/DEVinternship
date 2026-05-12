package al.dev.ecommerce_app.Controller;

import al.dev.ecommerce_app.Dto.CategoryDto;
import al.dev.ecommerce_app.Entity.Category;
import al.dev.ecommerce_app.Service.CategoryService;
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
}
