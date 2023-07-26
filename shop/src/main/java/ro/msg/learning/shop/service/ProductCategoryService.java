package ro.msg.learning.shop.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.ProductCategory;
import ro.msg.learning.shop.dto.ProductCategoryDto;
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
        Optional<ProductCategory> productCategoryFound = productCategoryRepository.findByName(productCategory.getName());
        if (productCategoryFound.isPresent()) {
            throw new EntityExistsException(THE_VALUE_IS_ALREADY_IN_THE_LIST);
        }
        return productCategoryRepository.save(productCategory);
    }

    public List<ProductCategoryDto> getAllProductCategories() {
        return productCategoryRepository.findAll().stream().map(productCategory -> productCategoryMapper.toDto(productCategory)).toList();
    }

    public Boolean existById(UUID categoryId) {
        return productCategoryRepository.existsById(categoryId);
    }

    public void deleteById(UUID categoryId) {
        if (Boolean.FALSE.equals(productCategoryRepository.existsById(categoryId))) {
            throw new EntityNotFoundException(ID_INVALID);
        }
        productCategoryRepository.deleteById(categoryId);
    }

    public Optional<ProductCategory> findById(UUID categoryID) {
        return productCategoryRepository.findById(categoryID);
    }

    public ProductCategory updateProductCategory(UUID categoryID, ProductCategory updatedProductCategory) {
        Optional<ProductCategory> existingProductCategory = productCategoryRepository.findById(categoryID);

        if (existingProductCategory.isEmpty()) {

            throw new EntityNotFoundException(ID_INVALID);
        }

        existingProductCategory.get().setName(updatedProductCategory.getName());

        existingProductCategory.get().setDescription(updatedProductCategory.getDescription());

        return productCategoryRepository.save(existingProductCategory.get());
    }

    public Optional<ProductCategory> findByName(String name) {
        return productCategoryRepository.findByName(name);
    }

}
