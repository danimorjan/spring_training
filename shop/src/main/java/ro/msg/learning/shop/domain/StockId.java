package ro.msg.learning.shop.domain;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockId implements Serializable {
    private UUID product;
    private UUID location;
}