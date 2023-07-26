package ro.msg.learning.shop.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.repository.CustomerRepository;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;
import ro.msg.learning.shop.service.OrderService;

@Service
public class TestService {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private OrderService orderService;

    public void populate() {
        customerRepository.save(Customer.builder().build());
        Product product = productRepository.save(Product.builder().build());
        Location location = locationRepository.save(Location.builder().build());
        stockRepository.save(Stock.builder().product(product).location(location).quantity(100).build());
    }

    public void clear() {
        orderService.deleteAll();
        customerRepository.deleteAll();
        stockRepository.deleteAll();
        productRepository.deleteAll();
        locationRepository.deleteAll();
    }
}
