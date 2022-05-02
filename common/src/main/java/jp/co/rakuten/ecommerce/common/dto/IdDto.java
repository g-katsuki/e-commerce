package jp.co.rakuten.ecommerce.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdDto {
    private Integer id;
    public IdDto() {}
    public IdDto(Integer id) {
        this.id = id;
    }
}
