package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private UUID id;
    private UUID categoryId;
    private String productName;
    private String productDescription;
    private BigDecimal price;
    private Double weight;
    private String categoryName;
    private String categoryDescription;
    private String supplier;
    private String imageUrl;
}
