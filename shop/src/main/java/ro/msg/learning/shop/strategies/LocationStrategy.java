package ro.msg.learning.shop.strategies;

import ro.msg.learning.shop.domain.Stock;
import ro.msg.learning.shop.dto.OrderDto;

import java.util.List;

public interface LocationStrategy {

    /**
     * obtaining a list of objects with the following structure:
     * location, product, quantity (= how many items of the given product are taken from the given location).
     *
     * @param order The OrderDto object containing information about the products to be taken from locations.
     * @return A list of Stock objects representing the quantity of each product taken from each location.
     */
    List<Stock> processOrder(OrderDto order);
}
