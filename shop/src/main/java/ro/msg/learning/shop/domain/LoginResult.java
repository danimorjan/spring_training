package ro.msg.learning.shop.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    private String jwt;
}