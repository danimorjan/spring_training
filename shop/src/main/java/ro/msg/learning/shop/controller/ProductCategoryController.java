package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductCategoryGetDto;
import ro.msg.learning.shop.mapper.ProductCategoryMapper;
import ro.msg.learning.shop.service.ProductCategoryService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/productCategory")
@RestController
@Validated
public class ProductCategoryController {

    public static final String PRODUCT_CATEGORY_WITH_ID = "Product category with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @PostMapping()
    public ResponseEntity<ProductCategoryGetDto> createProductCategory(@RequestBody @NonNull ProductCategoryGetDto body) {
        try {

            ProductCategory productCategory = productCategoryService.createProductCategory(productCategoryMapper.toEntity(body));
            return new ResponseEntity<>(productCategoryMapper.toGetDto(productCategory), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping()
    public ResponseEntity<List<ProductCategoryGetDto>> getAllProductCategories() {
        List<ProductCategory> productCategories = productCategoryService.getAllProductCategories();
        return new ResponseEntity<>(productCategories.stream().map(productCategory -> productCategoryMapper.toGetDto(productCategory)).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteProductCategory(@PathVariable UUID categoryId) {
        try {
            productCategoryService.deleteById(categoryId);
            return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + categoryId + HAS_BEEN_DELETED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryGetDto> getProductCategoryById(@PathVariable UUID categoryId) {
        ProductCategory productCategory = productCategoryService.findById(categoryId).orElse(null);

        if (productCategory == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productCategoryMapper.toGetDto(productCategory));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ProductCategoryGetDto> updateProductCategory(
            @PathVariable UUID categoryId,
            @RequestBody ProductCategoryGetDto updatedProductCategory
    ) {
        try {

            ProductCategory productCategory = productCategoryService.updateProductCategory(categoryId, productCategoryMapper.toEntity(updatedProductCategory));
            return ResponseEntity.ok(productCategoryMapper.toGetDto(productCategory));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
