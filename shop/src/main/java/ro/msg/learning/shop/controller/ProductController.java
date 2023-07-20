package ro.msg.learning.shop.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.dto.ProductGetDto;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.mapper.ProductCategoryMapper;
import ro.msg.learning.shop.service.ProductCategoryService;
import ro.msg.learning.shop.service.ProductService;

import java.util.List;
import java.util.UUID;

@RequestMapping("/product")
@RestController
@Validated
public class ProductController {

    public static final String PRODUCT_CATEGORY_WITH_ID = "Product with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping()
    public ResponseEntity<ProductGetDto> createProduct(@RequestBody @NonNull ProductGetDto body) {
        Product product = productService.createProduct(body);
        return new ResponseEntity<>(productMapper.toGetDto(product), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ProductGetDto>> getAllProducts() {
        List<Product> productList = productService.getAllProductCategories();
        return new ResponseEntity<>(productList.stream().map(product -> productMapper.toGetDto(product)).toList(), HttpStatus.OK);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId) {

        try {
            productService.deleteById(productId);
            return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + productId + HAS_BEEN_DELETED);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductGetDto> getProductById(@PathVariable UUID productId) {
        Product product = productService.findById(productId).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toGetDto(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductGetDto> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductGetDto updatedProduct
    ) {
        try {

            Product product = productService.updateProduct(productId, updatedProduct);
            return ResponseEntity.ok(productMapper.toGetDto(product));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
