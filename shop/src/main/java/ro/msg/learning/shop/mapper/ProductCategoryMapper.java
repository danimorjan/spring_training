package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductCategoryGetDto;

@Component
public class ProductCategoryMapper {

    public ProductCategory toEntity(ProductCategoryGetDto productCategoryCreateDto){
        return ProductCategory.builder().name(productCategoryCreateDto.getName()).description(productCategoryCreateDto.getDescription()).build();
    }

    public ProductCategoryGetDto toGetDto(ProductCategory productCategory){
        return ProductCategoryGetDto.builder().id(productCategory.getId()).name(productCategory.getName()).description(productCategory.getDescription()).build();
    }
}
