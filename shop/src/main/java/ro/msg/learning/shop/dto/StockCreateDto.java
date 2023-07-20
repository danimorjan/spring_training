package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class StockCreateDto {
    private UUID productId;
    private UUID locationId;
    private Integer quantity;
}
