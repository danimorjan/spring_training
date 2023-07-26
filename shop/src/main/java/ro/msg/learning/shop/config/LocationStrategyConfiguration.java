package ro.msg.learning.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.strategies.LocationStrategy;
import ro.msg.learning.shop.strategies.MostAbundantStrategy;
import ro.msg.learning.shop.strategies.SingleLocationStrategy;

@Configuration
public class LocationStrategyConfiguration {

    @Value("${location.strategy}")
    private String locationStrategy;

    @Autowired
    private SingleLocationStrategy singleLocationStrategy;

    @Autowired
    private MostAbundantStrategy mostAbundantStrategy;

    @Bean
    public LocationStrategy locationStrategy() {
        if ("custom".equals(locationStrategy)) {
            return mostAbundantStrategy;
        } else {
            return singleLocationStrategy;
        }
    }
}