package ro.msg.learning.shop.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {

            ProductCategory productCategory = productCategoryService.createProductCategory(productCategoryMapper.toEntity(productCategoryDto));
            return new ResponseEntity<>(productCategoryMapper.toDto(productCategory), HttpStatus.CREATED);

        } catch (EntityExistsException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping()
    public ResponseEntity<List<ProductCategoryDto>> getAllProductCategories() {
        List<ProductCategoryDto> productCategories = productCategoryService.getAllProductCategories();
        return new ResponseEntity<>(productCategories, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteProductCategory(@PathVariable UUID categoryId) {
        try {
            productCategoryService.deleteById(categoryId);
            return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + categoryId + HAS_BEEN_DELETED);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }

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
        try {

            ProductCategory productCategory = productCategoryService.updateProductCategory(categoryId, productCategoryMapper.toEntity(updatedProductCategory));
            return ResponseEntity.ok(productCategoryMapper.toDto(productCategory));

        } catch (EntityNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.notFound().build();
        }

    }
}
