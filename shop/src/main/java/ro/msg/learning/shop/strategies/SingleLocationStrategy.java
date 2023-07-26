package ro.msg.learning.shop.strategies;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.service.CustomerService;
import ro.msg.learning.shop.service.LocationService;
import ro.msg.learning.shop.service.ProductService;
import ro.msg.learning.shop.service.StockService;

import java.util.*;

@Component
public class SingleLocationStrategy implements LocationStrategy {
    public static final String NO_SUCH_LOCATION = "No such location";
    public static final String QUANTITY_MUST_BE_POSITIVE = "quantity must be positive";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    @Autowired
    private StockService stockService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    private List<Stock> updateStocks(UUID locationId, List<ProductIdAndQuantity> requiredProducts) {
        List<Stock> stocks = new ArrayList<>();
        for (ProductIdAndQuantity requiredProduct : requiredProducts) {
            stockService
                    .findById((locationId), requiredProduct.getProductId())
                    .ifPresent(stock -> {
                        stockService.updateStock(requiredProduct.getProductId(), locationId, stock.getQuantity() - requiredProduct.getQuantity());
                        stocks.add(Stock.builder().product(stock.getProduct()).location(stock.getLocation()).quantity(requiredProduct.getQuantity()).build());
                    });

        }
        return stocks;
    }

    public static Map<UUID, Integer> countOccurrencesOfIds(List<UUID> list) {
        Map<UUID, Integer> occurrences = new HashMap<>();

        for (UUID element : list) {
            occurrences.put(element, occurrences.getOrDefault(element, 0) + 1);
        }

        return occurrences;
    }

    /**
     * obtaining a list of objects with the following structure:
     * location, product, quantity (= how many items of the given product are taken from the given location).
     *
     * @param order The OrderDto object containing information about the products to be taken from locations.
     * @return A list of Stock objects representing the quantity of each product taken from each location.
     */
    @Override
    public List<Stock> processOrder(OrderDto order) {

        List<Product> products = getProductsForOrder(order);

        List<UUID> locations = stockService.findLocationIdsByProductIdsAndStock(order.getProductIdAndQuantityList(), products);

        Map<UUID, Integer> occurrences = countOccurrencesOfIds(locations);

        UUID foundLocation = occurrences.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == products.size())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(NO_SUCH_LOCATION));

        return updateStocks(foundLocation, order.getProductIdAndQuantityList());
    }

    private List<Product> getProductsForOrder(OrderDto order) {
        return order.getProductIdAndQuantityList()
                .stream()
                .peek(productIdAndQuantity -> {
                    if (productIdAndQuantity.getQuantity() <= 0) {
                        throw new IllegalArgumentException(QUANTITY_MUST_BE_POSITIVE);
                    }
                })
                .map(productIdAndQuantity -> productService.findById(productIdAndQuantity.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND)))
                .toList();
    }
}
