package jp.co.rakuten.ecommerce.application.form;

import jp.co.rakuten.ecommerce.common.dto.UserDto;
import lombok.Data;

@Data
public class UserRegisterForm {

    private Integer id;
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String zipCode;
    private String address;
    private String phone;
    private Integer point;

    public UserDto toUserDto() {
        return UserDto.builder().id(this.id)
                .email(this.email)
                .password(this.password)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .zipCode(this.zipCode)
                .address(this.address)
                .phone(this.phone)
                .point(this.point)
                .build();
    }
}