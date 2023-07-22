package ro.msg.learning.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.StockId;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.*;

@Service
public class OrderService {
    public static final String ID_INVALID = "ID invalid.";
    public static final String NO_SUCH_LOCATION = "No such location";
    public static final String NO_SUCH_CUSTOMER = "No such customer";
    public static final String QUANTITY_MUST_BE_POSITIVE = "quantity must be positive";

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


    private void updateStocks(UUID locationId, List<ProductIdAndQuantity> requiredProducts) {
        for (ProductIdAndQuantity requiredProduct : requiredProducts) {
            stockService
                    .findById(StockId.builder().location(locationId).product(requiredProduct.getProductId()).build())
                    .ifPresent(stock -> stockService.updateStock(requiredProduct.getProductId(), locationId, stock.getQuantity() - requiredProduct.getQuantity()));

        }
    }

    public static Map<UUID, Integer> countOccurrencesOfIds(List<UUID> list) {
        Map<UUID, Integer> occurrences = new HashMap<>();

        for (UUID element : list) {
            occurrences.put(element, occurrences.getOrDefault(element, 0) + 1);
        }

        return occurrences;
    }

    public Order createOrder(OrderDto order) {
        List<Product> products = new ArrayList<>();

        order.getProductIdAndQuantityList().forEach(productIdAndQuantity -> {
            if (productIdAndQuantity.getQuantity() <= 0) {
                throw new IllegalArgumentException(QUANTITY_MUST_BE_POSITIVE);
            } else {
                productService.findById(productIdAndQuantity.getProductId()).ifPresent(products::add);

            }
        });

        List<UUID> locations = stockService.findLocationIdsByProductIdsAndStock(order.getProductIdAndQuantityList(), products);

        Map<UUID, Integer> occurrences = countOccurrencesOfIds(locations);

        UUID foundLocation = null;

        for (Map.Entry<UUID, Integer> entry : occurrences.entrySet()) {
            if (entry.getValue() == products.size()) {
                foundLocation = entry.getKey();
                break;
            }
        }

        if (foundLocation == null) {
            throw new EntityNotFoundException(NO_SUCH_LOCATION);
        }
        Optional<Customer> customer = customerService.findById(order.getCustomerId());

        if (customer.isEmpty()) {
            throw new EntityNotFoundException(NO_SUCH_CUSTOMER);
        }
        updateStocks(foundLocation, order.getProductIdAndQuantityList());

        return orderRepository.save(orderMapper.toEntity(order, customer.get()));
    }

}
