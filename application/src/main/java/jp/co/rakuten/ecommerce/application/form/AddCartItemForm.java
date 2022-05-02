package jp.co.rakuten.ecommerce.application.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemForm {

    Integer itemId;
    String name;
    String description;
    Double price;
    String photoAddress;
    Integer quantity;
}