package ro.msg.learning.shop;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ro.msg.learning.shop.service.OrderService;

@TestConfiguration
@Profile("test")
public class TestCreateOrderConfig {

//    @Bean
//    public OrderService customerService() {
//        return new OrderService();
//    }

}