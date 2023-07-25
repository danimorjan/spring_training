package ro.msg.learning.shop.strategies;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;
import ro.msg.learning.shop.service.ProductService;
import ro.msg.learning.shop.service.StockService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ro.msg.learning.shop.service.OrderService.QUANTITY_MUST_BE_POSITIVE;

@Component
public class MostAbundantStrategy implements LocationStrategy {
    public static final String NO_SUCH_LOCATION = "No such location";
    public static final String NO_SUCH_PRODUCT = "No such product";
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductService productService;

    /**
     * obtaining a list of objects with the following structure:
     * location, product, quantity (= how many items of the given product are taken from the given location).
     *
     * @param order The OrderDto object containing information about the products to be taken from locations.
     * @return A list of Stock objects representing the quantity of each product taken from each location.
     */
    @Override
    public List<Stock> processOrder(OrderDto order) {

        validateOrder(order);

        List<Stock> stocks = new ArrayList<>();

        for (ProductIdAndQuantity productIdAndQuantity : order.getProductIdAndQuantityList()) {
            Optional<Product> product = productService.findById(productIdAndQuantity.getProductId());
            if (product.isPresent()) {
                Location location = stockService.findLocationWithHighestStock(product.get());
                Optional<Stock> stock = stockService.findById(location.getId(), (product.get().getId()));
                if (stock.isEmpty()) {
                    throw new EntityNotFoundException(NO_SUCH_LOCATION);
                }
                if (stock.get().getQuantity() - productIdAndQuantity.getQuantity() < 0) {
                    throw new EntityNotFoundException(NO_SUCH_LOCATION);
                } else {
                    stockService.updateStock(stock.get().getProduct().getId(), stock.get().getLocation().getId(), stock.get().getQuantity() - productIdAndQuantity.getQuantity());
                    stocks.add(Stock.builder().location(stock.get().getLocation()).product(stock.get().getProduct()).quantity(productIdAndQuantity.getQuantity()).build());
                }
            } else {
                throw new EntityNotFoundException(NO_SUCH_PRODUCT);
            }
        }

        return stocks;
    }

    private static void validateOrder(OrderDto order) {
        order.getProductIdAndQuantityList().forEach(productIdAndQuantity -> {
            if (productIdAndQuantity.getQuantity() <= 0) {
                throw new IllegalArgumentException(QUANTITY_MUST_BE_POSITIVE);
            }
        });
    }
}
