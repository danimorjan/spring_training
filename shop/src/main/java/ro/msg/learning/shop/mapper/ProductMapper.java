package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductDto;

@Component
public class ProductMapper {
    public ProductCategory toProductCategoryEntity(ProductDto productCreateDto) {
        return ProductCategory.builder().name(productCreateDto.getCategoryName()).description(productCreateDto.getCategoryDescription()).build();
    }

    public Product toProductEntity(ProductDto productCreateDto, ProductCategory productCategory) {
        return Product.builder()
                .name(productCreateDto.getProductName())
                .description(productCreateDto.getProductDescription())
                .price(productCreateDto.getPrice())
                .imageUrl(productCreateDto.getImageUrl())
                .weight(productCreateDto.getWeight())
                .supplier(productCreateDto.getSupplier())
                .productCategory(productCategory)
                .build();
    }

    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .weight(product.getWeight())
                .supplier(product.getSupplier())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getProductCategory().getName())
                .categoryDescription(product.getProductCategory().getDescription())
                .categoryId(product.getProductCategory().getId())
                .productDescription(product.getDescription())
                .id(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .build();
    }
}
