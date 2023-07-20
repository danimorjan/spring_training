package ro.msg.learning.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OrderDto {

    private UUID customerId;

    private LocalDateTime createDate;

    private String addressCountry;

    private String addressCity;

    private String addressStreetAddress;

    private List<ProductIdAndQuantity> productIdAndQuantityList;
}
