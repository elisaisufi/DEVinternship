package al.dev.ecommerce_app.Service;

import al.dev.ecommerce_app.Dto.ProductDto;
import al.dev.ecommerce_app.Entity.Category;
import al.dev.ecommerce_app.Entity.Product;
import al.dev.ecommerce_app.Exception.CustomException;
import al.dev.ecommerce_app.Repository.CategoryRepository;
import al.dev.ecommerce_app.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product create(ProductDto dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new CustomException("Category not found")
                );

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(category)
                .build();

        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findByIsActiveTrue();
    }

    public Product getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException("Product not found")
                );

        if(!product.isActive()) {
            throw new CustomException("Product not found");
        }

        return product;
    }

    public List<Product> search(String name) {

        return productRepository
                .findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    public Product update(Long id, ProductDto dto) {

        Product product = getById(id);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() ->
                        new CustomException("Category not found")
                );

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void delete(Long id) {

        Product product = getById(id);

        product.setActive(false);

        productRepository.save(product);
    }
}