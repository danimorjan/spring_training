package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.msg.learning.shop.domain.Location;
import ro.msg.learning.shop.domain.Product;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StockGetDto {
    private Product product;
    private Location location;
    private Integer quantity;
}
