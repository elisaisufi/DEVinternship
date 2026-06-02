package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.dto.CategoryDto;
import al.dev.ecommerce_app.dto.CategoryResponse;
import al.dev.ecommerce_app.entity.Category;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryDto dto) {

        Category category = Category.builder()
                .name(dto.getName())
                .isActive(true)
                .build();

        return CategoryResponse.from(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAll() {

        return categoryRepository.findByIsActiveTrue()
                .stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    // Used internally by ProductService — returns the raw entity
    public Category getEntityById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException("Category not found")
                );

        if (!category.isActive()) {
            throw new CustomException("Category not found");
        }

        return category;
    }

    public CategoryResponse update(Long id, CategoryDto dto) {

        Category category = getEntityById(id);

        category.setName(dto.getName());

        return CategoryResponse.from(categoryRepository.save(category));
    }

    public void delete(Long id) {

        Category category = getEntityById(id);

        category.setActive(false);

        categoryRepository.save(category);
    }
}