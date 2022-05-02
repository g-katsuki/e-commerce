package jp.co.rakuten.ecommerce.application.session;

import jp.co.rakuten.ecommerce.common.dto.OrderDto;
import lombok.Data;

@Data
public class CartItem {

    private Integer itemId;
    private String name;
    private String description;
    private Double price;
    private String photoAddress;
    private Integer quantity;
    private Integer point;

    public OrderDto.OrderItem toOrderItem() {
        return OrderDto.OrderItem.builder()
                .itemId(this.itemId)
                .price(this.price)
                .quantity(this.quantity)
                .build();
    }
}