package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductGetDto;
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

    public Product createProduct(ProductGetDto product) {

        ProductCategory productCategoryFound = productCategoryService.findByName(product.getCategoryName()).orElse(null);

        if (productCategoryFound == null) {
            ProductCategory productCategoryInserted = productCategoryService.createProductCategory(productMapper.toProductCategoryEntity(product));
            return productRepository.save(productMapper.toProductEntity(product, productCategoryInserted));
        }

        return productRepository.save(productMapper.toProductEntity(product, productCategoryFound));
    }

    public List<Product> getAllProductCategories() {
        return productRepository.findAll();
    }

    public Boolean existById(UUID productId) {
        return productRepository.existsById(productId);
    }

    public void deleteById(UUID productId) {
        if (Boolean.FALSE.equals(productRepository.existsById(productId))) {
            throw new IllegalArgumentException(ID_INVALID);
        }
        productRepository.deleteById(productId);
    }

    public Optional<Product> findById(UUID productId) {
        return productRepository.findById(productId);
    }

    public Product updateProduct(UUID productId, ProductGetDto updatedProduct) {
        Product existingProduct = productRepository.findById(productId).orElse(null);

        if (existingProduct == null) {
            throw new IllegalArgumentException(ID_INVALID);
        }

        existingProduct.setName(updatedProduct.getProductName());
        existingProduct.setDescription(updatedProduct.getProductDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setSupplier(updatedProduct.getSupplier());
        existingProduct.setWeight(updatedProduct.getWeight());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        ProductCategory existingCategory = productCategoryService.findByName(updatedProduct.getCategoryName()).orElse(null);
        if(existingCategory == null){
            existingCategory = productCategoryService.createProductCategory(productMapper.toProductCategoryEntity(updatedProduct));
        }
        existingCategory.setDescription(updatedProduct.getCategoryDescription());
        existingProduct.setProductCategory(existingCategory);

        return productRepository.save(existingProduct);
    }
}
