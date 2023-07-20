package ro.msg.learning.shop.domain;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue
    protected UUID id;

}

