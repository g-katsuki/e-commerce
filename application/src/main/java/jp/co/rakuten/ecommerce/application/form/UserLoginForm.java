package jp.co.rakuten.ecommerce.application.form;

import lombok.Data;

@Data
public class UserLoginForm {
    private String email;
    private String password;
}