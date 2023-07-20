package ro.msg.learning.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.StockCreateDto;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {
    public static final String ID_INVALID = "ID invalid.";

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private StockMapper stockMapper;


    public Stock createStock(StockCreateDto stock) {

        Product productFound = productService.findById(stock.getProductId()).orElse(null);
        Location locationFound = locationService.findById(stock.getLocationId()).orElse(null);

        if (productFound == null || locationFound == null) {
            throw new IllegalArgumentException("Product or category doesn't exists");
        }

        return stockRepository.save(stockMapper.toStockEntity(productFound, locationFound, stock.getQuantity()));
    }

    public List<Stock> getAllStockCategories() {
        return stockRepository.findAll();
    }

    public Boolean existById(StockId stockId) {
        return stockRepository.existsById(stockId);
    }

    public void deleteById(StockId stockId) {
        if (Boolean.FALSE.equals(stockRepository.existsById(stockId))) {
            throw new IllegalArgumentException(ID_INVALID);
        }
        stockRepository.deleteById(stockId);
    }

    public Optional<Stock> findById(StockId stockId) {
        return stockRepository.findById(stockId);
    }

    public Stock updateStock(UUID productId, UUID locationId, Integer quantity) {
        Stock existingStock = stockRepository.findById(StockId.builder().location(locationId).product(productId).build()).orElse(null);

        if (existingStock == null) {
            throw new IllegalArgumentException(ID_INVALID);
        }

        existingStock.setQuantity(quantity);

        return stockRepository.save(existingStock);
    }
}
