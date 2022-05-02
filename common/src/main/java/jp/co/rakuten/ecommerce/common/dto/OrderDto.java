package jp.co.rakuten.ecommerce.common.dto;

import jp.co.rakuten.ecommerce.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Integer orderId;
    private Integer customerId;
    private OrderStatus status;
    private Date createdAt;
    private Integer point;
    public String sdf;

    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        Integer itemId;
        Integer quantity;
        Double price;
    }
}