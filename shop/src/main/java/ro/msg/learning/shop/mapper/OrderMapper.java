package ro.msg.learning.shop.mapper;

import org.springframework.stereotype.Component;
import ro.msg.learning.shop.domain.Customer;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderInputDto;
import ro.msg.learning.shop.dto.OrderDto;

@Component
public class OrderMapper {

    public OrderInputDto toDto(Order order){
        return OrderInputDto.builder()
                .id(order.getId())
                .addressCity(order.getAddressCity())
                .addressCountry(order.getAddressCountry())
                .addressStreetAddress(order.getAddressStreetAddress())
                .createDate(order.getCreateDate())
                .customer(order.getCustomer())
                .build();
    }
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
