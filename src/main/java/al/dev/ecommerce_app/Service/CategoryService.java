package al.dev.ecommerce_app.Service;

import al.dev.ecommerce_app.Dto.CategoryDto;
import al.dev.ecommerce_app.Entity.Category;
import al.dev.ecommerce_app.Exception.CustomException;
import al.dev.ecommerce_app.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(CategoryDto dto) {

        Category category = Category.builder()
                .name(dto.getName())
                .build();

        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findByIsActiveTrue();
    }

    public Category getById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException("Category not found")
                );

        if(!category.isActive()) {
            throw new CustomException("Category not found");
        }

        return category;
    }

    public Category update(Long id, CategoryDto dto) {

        Category category = getById(id);

        category.setName(dto.getName());

        return categoryRepository.save(category);
    }

    public void delete(Long id) {

        Category category = getById(id);

        category.setActive(false);

        categoryRepository.save(category);
    }
}