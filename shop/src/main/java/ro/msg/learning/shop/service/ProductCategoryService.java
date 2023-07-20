package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.mapper.ProductCategoryMapper;
import ro.msg.learning.shop.repository.ProductCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductCategoryService {

    public static final String THE_VALUE_IS_ALREADY_IN_THE_LIST = "The value is already in the list.";
    public static final String ID_INVALID = "ID invalid.";
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    public ProductCategory createProductCategory(ProductCategory productCategory) {
        ProductCategory productCategoryFound = productCategoryRepository.findByName(productCategory.getName()).orElse(null);
        if (productCategoryFound != null) {
            throw new IllegalArgumentException(THE_VALUE_IS_ALREADY_IN_THE_LIST);
        }
        return productCategoryRepository.save(productCategory);
    }

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    public Boolean existById(UUID categoryId) {
        return productCategoryRepository.existsById(categoryId);
    }

    public void deleteById(UUID categoryId) {
        if (Boolean.FALSE.equals(productCategoryRepository.existsById(categoryId))) {
            throw new IllegalArgumentException(ID_INVALID);
        }
        productCategoryRepository.deleteById(categoryId);
    }

    public Optional<ProductCategory> findById(UUID categoryID) {
        return productCategoryRepository.findById(categoryID);
    }

    public ProductCategory updateProductCategory(UUID categoryID, ProductCategory updatedProductCategory) {
        ProductCategory existingProductCategory = productCategoryRepository.findById(categoryID).orElse(null);

        if (existingProductCategory == null) {

            throw new IllegalArgumentException(ID_INVALID);
        }

        existingProductCategory.setName(updatedProductCategory.getName());

        existingProductCategory.setDescription(updatedProductCategory.getDescription());

        return productCategoryRepository.save(existingProductCategory);
    }

    public Optional<ProductCategory> findByName(String name) {
        return productCategoryRepository.findByName(name);
    }

}
