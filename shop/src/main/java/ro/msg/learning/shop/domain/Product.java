package ro.msg.learning.shop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Table(name = "product", schema = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Product extends BaseEntity {

    @Column(name = "name", length = 20, unique = false)
    private String name;

    @Column(name = "description", length = 128, unique = false)
    private String description;

    @Column(name = "price", unique = false)
    private BigDecimal price;

    @Column(name = "weight", unique = false)
    private Double weight;

    @JoinColumn (name = "category_id")
    @ManyToOne
    private ProductCategory productCategory;

    @Column(name = "supplier", length = 20, unique = false)
    private String supplier;

    @Column(name = "image_url", length = 255, unique = false)
    private String imageUrl;
}
