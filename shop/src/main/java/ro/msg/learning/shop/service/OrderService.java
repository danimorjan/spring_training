package ro.msg.learning.shop.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.dto.StockIDsDto;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.repository.OrderRepository;
import ro.msg.learning.shop.strategies.LocationStrategy;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    public static final String NO_SUCH_CUSTOMER = "No such customer";
    public static final String QUANTITY_MUST_BE_POSITIVE = "quantity must be positive";

    @Autowired
    private StockService stockService;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private LocationStrategy locationStrategy;
    @Autowired
    private OrderMapper orderMapper;

    public void deleteAll() {
        orderRepository.deleteAll();
    }

    public List<StockIDsDto> createOrder(OrderDto order) {

        Optional<Customer> customer = customerService.findById(order.getCustomerId());

        if (customer.isEmpty()) {
            throw new EntityNotFoundException(NO_SUCH_CUSTOMER);
        }
        List<Stock> stocks = locationStrategy.processOrder(order);
        orderRepository.save(orderMapper.toEntity(order, customer.get()));
        return stocks.stream().map(stock -> stockMapper.toDto(stock)).toList();
    }

}
