package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductCategoryDto;
import ro.msg.learning.shop.mapper.ProductCategoryMapper;
import ro.msg.learning.shop.service.ProductCategoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/productCategory")
@RestController
public class ProductCategoryController {

    public static final String PRODUCT_CATEGORY_WITH_ID = "Product category with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @PostMapping()
    public ResponseEntity<ProductCategoryDto> createProductCategory(@RequestBody @NonNull ProductCategoryDto productCategoryDto) {
        ProductCategory productCategory = productCategoryService.createProductCategory(productCategoryMapper.toEntity(productCategoryDto));
        return new ResponseEntity<>(productCategoryMapper.toDto(productCategory), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ProductCategoryDto>> getAllProductCategories() {
        List<ProductCategoryDto> productCategories = productCategoryService.getAllProductCategories();
        return new ResponseEntity<>(productCategories, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteProductCategory(@PathVariable UUID categoryId) {
        productCategoryService.deleteById(categoryId);
        return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + categoryId + HAS_BEEN_DELETED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryDto> getProductCategoryById(@PathVariable UUID categoryId) {
        Optional<ProductCategory> productCategory = productCategoryService.findById(categoryId);

        return productCategory.map(category -> ResponseEntity.ok(productCategoryMapper.toDto(category))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryDto> updateProductCategory(
            @PathVariable UUID categoryId,
            @RequestBody ProductCategoryDto updatedProductCategory
    ) {
        ProductCategory productCategory = productCategoryService.updateProductCategory(categoryId, productCategoryMapper.toEntity(updatedProductCategory));
        return ResponseEntity.ok(productCategoryMapper.toDto(productCategory));

    }
}
