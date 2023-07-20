package ro.msg.learning.shop.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderCreateDto;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.service.OrderService;

@RequestMapping("/order")
@RestController
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping()
    public ResponseEntity<OrderCreateDto> createOrder(@RequestBody @NonNull OrderDto body) {
        try {
            Order order = orderService.createOrder(body);
            return new ResponseEntity<>(orderMapper.toGetDto(order), HttpStatus.CREATED);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
