package al.dev.ecommerce_app.dto;

import al.dev.ecommerce_app.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String categoryName;

    public static ProductResponse from(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategoryName(
                product.getCategory() != null
                        ? product.getCategory().getName()
                        : null
        );

        return response;
    }
}