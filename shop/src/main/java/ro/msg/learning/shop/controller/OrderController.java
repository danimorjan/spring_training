package ro.msg.learning.shop.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.domain.Order;
import ro.msg.learning.shop.dto.OrderInputDto;
import ro.msg.learning.shop.dto.OrderDto;
import ro.msg.learning.shop.mapper.OrderMapper;
import ro.msg.learning.shop.service.OrderService;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping()
    public ResponseEntity<OrderInputDto> createOrder(@RequestBody @NonNull OrderDto orderDto) {
        try {
            Order order = orderService.createOrder(orderDto);
            return new ResponseEntity<>(orderMapper.toDto(order), HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
