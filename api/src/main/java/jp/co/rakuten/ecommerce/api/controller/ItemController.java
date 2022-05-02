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
public class ItemController {

    ItemService itemService;

    @GetMapping("/items")
    public ItemListDto getItems() {
        return itemService.getItems();
    }

    @GetMapping("/items/{id}")
    public ItemDto getItem(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    @PostMapping("/items")
    public IdDto createItem(@RequestBody ItemDto itemDto) {
        // TODO: validate itemDto
        return itemService.createItem(itemDto);
    }

    @PutMapping("/items")
    public MessageDto updateItem(@RequestBody ItemDto itemDto) {
        // TODO: validate itemDto
        System.out.println(itemDto.getPoint());
        return itemService.updateItem(itemDto);
    }
}
