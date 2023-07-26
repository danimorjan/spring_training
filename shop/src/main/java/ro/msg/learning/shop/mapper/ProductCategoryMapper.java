package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductCategoryDto;

@Component
public class ProductCategoryMapper {

    public ProductCategory toEntity(ProductCategoryDto productCategoryCreateDto){
        return ProductCategory.builder().name(productCategoryCreateDto.getName()).description(productCategoryCreateDto.getDescription()).build();
    }

    public ProductCategoryDto toDto(ProductCategory productCategory){
        return ProductCategoryDto.builder().id(productCategory.getId()).name(productCategory.getName()).description(productCategory.getDescription()).build();
    }
}
