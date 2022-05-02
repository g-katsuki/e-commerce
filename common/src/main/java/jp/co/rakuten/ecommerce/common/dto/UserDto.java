package jp.co.rakuten.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String address;
    private String phone;
    private boolean enabled;
    private Date createdAt;
    private Integer point;
}