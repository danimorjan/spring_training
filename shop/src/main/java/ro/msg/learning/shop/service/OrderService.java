package ro.msg.learning.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.*;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.ProductIdAndQuantity;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    public static final String ID_INVALID = "ID invalid.";
    public static final String NO_SUCH_LOCATION = "No such location";
    public static final String NO_SUCH_CUSTOMER = "No such customer";

    @Autowired
    private StockService stockService;

    @Autowired
    private LocationService locationService;

    @Autowired
    CustomerService customerService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;


    private UUID findLocationWithRequiredProducts(OrderDto order) {
        List<Location> allLocations = locationService.getAllLocations();
        for (Location location : allLocations) {
            boolean hasAllProducts = true;
            for (ProductIdAndQuantity requiredProduct : order.getProductIdAndQuantityList()) {
                Stock stock = stockService.findById(StockId.builder().location(location.getId()).product(requiredProduct.getProductId()).build()).orElse(null);
                if (stock == null || stock.getQuantity() < requiredProduct.getQuantity()) {
                    hasAllProducts = false;
                    break;
                }
            }
            if (hasAllProducts) {
                return location.getId();
            }
        }
        return null;
    }

    private void updateStocks(UUID locationId, List<ProductIdAndQuantity> requiredProducts) {
        for (ProductIdAndQuantity requiredProduct : requiredProducts) {
            stockService
                    .findById(StockId.builder().location(locationId).product(requiredProduct.getProductId()).build())
                    .ifPresent(stock -> stockService.updateStock(requiredProduct.getProductId(), locationId, stock.getQuantity() - requiredProduct.getQuantity()));

        }
    }

    public Order createOrder(OrderDto order) {

        UUID foundLocation = findLocationWithRequiredProducts(order);
        if (foundLocation == null) {
            throw new EntityNotFoundException(NO_SUCH_LOCATION);
        }
        Customer customer = customerService.findById(order.getCustomerId()).orElse(null);

        if (customer == null) {
            throw new EntityNotFoundException(NO_SUCH_CUSTOMER);
        }
        updateStocks(foundLocation, order.getProductIdAndQuantityList());


        return orderRepository.save(orderMapper.toEntity(order, customer));
    }

}
