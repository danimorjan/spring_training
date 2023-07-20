package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.StockGetDto;

@Component
public class StockMapper {

    public Stock toStockEntity(Product product, Location location, Integer quantity) {
        return Stock.builder()
                .location(location)
                .product(product)
                .quantity(quantity)
                .build();
    }

    public StockGetDto toGetDto(Stock stock) {
        return StockGetDto.builder()
                .product(stock.getProduct())
                .location(stock.getLocation())
                .quantity(stock.getQuantity())
                .build();
    }
}
