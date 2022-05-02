package jp.co.rakuten.ecommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data transfer object for Item class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Integer itemId;
    private String name;
    private String description;
    private String photoAddress;
    private Double price;
    private Integer inventoryCount;
    private Integer quantity;
    private boolean enabled;
    private Date createdAt;
    private Integer point;
}
