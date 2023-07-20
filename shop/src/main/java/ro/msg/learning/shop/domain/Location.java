package ro.msg.learning.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Table(name = "location", schema = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Location extends BaseEntity {

    @Column(name = "name",columnDefinition = "VARCHAR(30)", length = 30, unique = false)
    private String name;

    @Column(name = "address_country", columnDefinition = "VARCHAR(30)", length = 30, unique = false)
    private String addressCountry;

    @Column(name = "address_city",columnDefinition = "VARCHAR(30)", length = 30, unique = false)
    private String addressCity;

    @Column(name = "address_streetaddress",columnDefinition = "VARCHAR(30)", length = 30, unique = false)
    private String addressStreetAddress;
}
