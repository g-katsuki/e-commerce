package jp.co.rakuten.ecommerce.api.controller;

import jp.co.rakuten.ecommerce.api.service.ItemService;
import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.ItemListDto;
import jp.co.rakuten.ecommerce.common.dto.MessageDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CartController {

    ItemService itemService;

    @GetMapping("/add")
    public ItemListDto getItems() {
        return itemService.getItems();
    }
}
