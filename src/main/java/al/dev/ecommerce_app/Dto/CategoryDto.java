package al.dev.ecommerce_app.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {

    @NotBlank(message = "Category name is required")
    private String name;
}