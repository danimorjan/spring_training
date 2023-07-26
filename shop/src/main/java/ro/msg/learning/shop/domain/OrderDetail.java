package ro.msg.learning.shop.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "orderdetail", schema = "shop")
@IdClass(OrderDetailId.class)

public class OrderDetail {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "order_id")
    private Order orderId;
    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "product_id")
    private Product productId;

    @JoinColumn(name = "shipped_from")
    @ManyToOne
    @JdbcTypeCode(SqlTypes.JSON)
    private Location shippedFrom;

    @Column(name = "quantity")
    private Integer quantity;
}