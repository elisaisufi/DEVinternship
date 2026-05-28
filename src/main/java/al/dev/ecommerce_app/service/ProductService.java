package al.dev.ecommerce_app.service;

import al.dev.ecommerce_app.dto.ProductDto;
import al.dev.ecommerce_app.dto.ProductResponse;
import al.dev.ecommerce_app.entity.Category;
import al.dev.ecommerce_app.entity.Product;
import al.dev.ecommerce_app.exception.CustomException;
import al.dev.ecommerce_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductResponse create(ProductDto dto) {

        Category category = categoryService.getEntityById(dto.getCategoryId());

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(category)
                .build();

        return ProductResponse.from(
                productRepository.save(product)
        );
    }

    public List<ProductResponse> getAll() {

        return productRepository.findByIsActiveTrue()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse getById(Long id) {

        return ProductResponse.from(getEntityById(id));
    }

    public Product getEntityById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException("Product not found")
                );

        if (!product.isActive()) {
            throw new CustomException("Product not found");
        }

        return product;
    }

    public List<ProductResponse> search(String name) {

        return productRepository
                .findByNameContainingIgnoreCaseAndIsActiveTrue(name)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse update(Long id, ProductDto dto) {

        Product product = getEntityById(id);

        Category category = categoryService.getEntityById(dto.getCategoryId());

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(category);

        return ProductResponse.from(
                productRepository.save(product)
        );
    }

    public void delete(Long id) {

        Product product = getEntityById(id);

        product.setActive(false);

        productRepository.save(product);
    }
}