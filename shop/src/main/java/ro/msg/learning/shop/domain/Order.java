package ro.msg.learning.shop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Table(name = "orderr", schema = "shop")
@Data
@Entity
@Builder
public class Order extends BaseEntity {

    @JoinColumn(name = "customer")
    @ManyToOne
    @JdbcTypeCode(SqlTypes.JSON)
    private Customer customer;

    @Column(name = "createdate")
    private LocalDateTime createDate;

    @Column(name = "address_country", length = 30, unique = false)
    private String addressCountry;

    @Column(name = "address_city", length = 30, unique = false)
    private String addressCity;

    @Column(name = "address_streetaddress", length = 30, unique = false)
    private String addressStreetAddress;
}
