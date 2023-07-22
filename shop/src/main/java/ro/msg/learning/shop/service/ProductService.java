package ro.msg.learning.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductDto;
import ro.msg.learning.shop.mapper.ProductCategoryMapper;
import ro.msg.learning.shop.mapper.ProductMapper;
import ro.msg.learning.shop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    public static final String ID_INVALID = "ID invalid.";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductMapper productMapper;

    public Product createProduct(ProductDto product) {

        ProductCategory productCategoryFound = productCategoryService.findByName(product.getCategoryName()).orElse(null);

        if (productCategoryFound == null) {
            ProductCategory productCategoryInserted = productCategoryService.createProductCategory(productMapper.toProductCategoryEntity(product));
            return productRepository.save(productMapper.toProductEntity(product, productCategoryInserted));
        }

        return productRepository.save(productMapper.toProductEntity(product, productCategoryFound));
    }

    public List<ProductDto> getAllProductCategories() {
        return productRepository.findAll().stream().map(product -> productMapper.toDto(product)).toList();
    }

    public Boolean existById(UUID productId) {
        return productRepository.existsById(productId);
    }

    public void deleteById(UUID productId) {
        if (Boolean.FALSE.equals(productRepository.existsById(productId))) {
            throw new EntityNotFoundException(ID_INVALID);
        }
        productRepository.deleteById(productId);
    }

    public Optional<Product> findById(UUID productId) {
        return productRepository.findById(productId);
    }

    public Product updateProduct(UUID productId, ProductDto updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(productId);

        if (existingProduct.isEmpty()) {
            throw new EntityNotFoundException(ID_INVALID);
        }

        existingProduct.get().setName(updatedProduct.getProductName());
        existingProduct.get().setDescription(updatedProduct.getProductDescription());
        existingProduct.get().setPrice(updatedProduct.getPrice());
        existingProduct.get().setSupplier(updatedProduct.getSupplier());
        existingProduct.get().setWeight(updatedProduct.getWeight());
        existingProduct.get().setImageUrl(updatedProduct.getImageUrl());

        ProductCategory existingCategory = productCategoryService.findByName(updatedProduct.getCategoryName()).orElse(null);
        if (existingCategory == null) {
            existingCategory = productCategoryService.createProductCategory(productMapper.toProductCategoryEntity(updatedProduct));
        }
        existingCategory.setDescription(updatedProduct.getCategoryDescription());
        existingProduct.get().setProductCategory(existingCategory);

        return productRepository.save(existingProduct.get());
    }
}
