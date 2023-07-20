package ro.msg.learning.shop.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Table(name = "productcategory", schema="shop")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class ProductCategory extends BaseEntity {

    @Column(name="name", length=255, unique=false)
    private String name;

    @Column(name="description", length=255, unique=false)
    private String description;
}
