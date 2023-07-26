package ro.msg.learning.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Table(name = "customer", schema = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Customer extends BaseEntity {

    @Column(name = "firstname", length = 30, unique = false)
    private String firstname;

    @Column(name = "lastname", length = 30, unique = false)
    private String lastname;

    @Column(name = "username", length = 30, unique = false)
    private String username;

    @Column(name = "password", length = 30, unique = false)
    private String password;

    @Column(name = "emailaddress", length = 30, unique = false)
    private String emailAddress;
}
