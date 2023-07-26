package ro.msg.learning.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;
import ro.msg.learning.shop.dto.StockIDsDto;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {
    public static final String ID_INVALID = "ID invalid.";
    public static final String PRODUCT_OR_CATEGORY_DOESN_T_EXISTS = "Product or category doesn't exists";

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockMapper stockMapper;


    public Stock createStock(StockIDsDto stock) {

        Optional<Product> productFound = productService.findById(stock.getProductId());
        Optional<Location> locationFound = locationService.findById(stock.getLocationId());

        if (productFound.isEmpty() || locationFound.isEmpty()) {
            throw new EntityNotFoundException(PRODUCT_OR_CATEGORY_DOESN_T_EXISTS);
        }

        return stockRepository.save(stockMapper.toStockEntity(productFound.get(), locationFound.get(), stock.getQuantity()));
    }

    public List<StockIDsDto> getAllStockCategories() {
        return stockRepository.findAll().stream().map(stock -> stockMapper.toDto(stock)).toList();
    }

    public Boolean existById(StockId stockId) {
        return stockRepository.existsById(stockId);
    }

    public void deleteById(StockId stockId) {
        if (Boolean.FALSE.equals(stockRepository.existsById(stockId))) {
            throw new EntityNotFoundException(ID_INVALID);
        }
        stockRepository.deleteById(stockId);
    }

    public Optional<Stock> findById(UUID locationId, UUID productId) {
        return stockRepository.findById(StockId.builder().location(locationId).product(productId).build());
    }

    public List<UUID> findLocationIdsByProductIdsAndStock(List<ProductIdAndQuantity> productIdAndQuantityList, List<Product> products) {
        return stockRepository.findLocationIdsByProductIdsAndStock(productIdAndQuantityList, products);
    }

    public Location findLocationWithHighestStock(Product product) {
        return stockRepository.findLocationWithHighestStock(product);
    }

    public List<Location> findLocationIdsByProducts(List<Product> products, Integer productsCount) {
        return stockRepository.findLocationIdsByProducts(products, productsCount);
    }

    public Stock updateStock(UUID productId, UUID locationId, Integer quantity) {
        Optional<Stock> existingStock = stockRepository.findById(StockId.builder().location(locationId).product(productId).build());

        if (existingStock.isEmpty()) {
            throw new EntityNotFoundException(ID_INVALID);
        }

        existingStock.get().setQuantity(quantity);

        return stockRepository.save(existingStock.get());
    }


}
