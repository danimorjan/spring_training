package ro.msg.learning.shop.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Table(name = "stock", schema = "shop")
@IdClass(StockId.class)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Stock {
    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @JoinColumn(name = "product")
    @ManyToOne
    private Product product;

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @JoinColumn(name = "location")
    @ManyToOne
    private Location location;

    @Column(name = "quantity")
    private Integer quantity;
}