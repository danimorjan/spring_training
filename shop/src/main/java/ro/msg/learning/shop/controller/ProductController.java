package ro.msg.learning.shop.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequestMapping("/product")
@RestController
public class ProductController {

    public static final String PRODUCT_CATEGORY_WITH_ID = "Product with ID ";
    public static final String HAS_BEEN_DELETED = " has been deleted.";
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@RequestBody @NonNull ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(productMapper.toDto(product), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> productList = productService.getAllProductCategories();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID productId) {

        try {
            productService.deleteById(productId);
            return ResponseEntity.ok(PRODUCT_CATEGORY_WITH_ID + productId + HAS_BEEN_DELETED);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID productId) {
        Optional<Product> product = productService.findById(productId);

        return product.map(value -> ResponseEntity.ok(productMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductDto updatedProduct
    ) {
        try {

            Product product = productService.updateProduct(productId, updatedProduct);
            return ResponseEntity.ok(productMapper.toDto(product));

        } catch (EntityNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.notFound().build();
        }

    }
}
