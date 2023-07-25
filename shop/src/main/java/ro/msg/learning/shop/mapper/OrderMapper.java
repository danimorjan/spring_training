package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderDto;

@Component
public class OrderMapper {
    public Order toEntity(OrderDto orderDto, Customer customer) {
        return Order.builder()
                .addressCity(orderDto.getAddressCity())
                .addressCountry(orderDto.getAddressCountry())
                .addressStreetAddress(orderDto.getAddressStreetAddress())
                .createDate(orderDto.getCreateDate())
                .customer(customer)
                .build();
    }
}
