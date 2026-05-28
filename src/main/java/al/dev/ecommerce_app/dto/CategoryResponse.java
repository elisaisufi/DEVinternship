package al.dev.ecommerce_app.dto;

import al.dev.ecommerce_app.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {

    private Long id;
    private String name;

    public static CategoryResponse from(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;
    }
}
