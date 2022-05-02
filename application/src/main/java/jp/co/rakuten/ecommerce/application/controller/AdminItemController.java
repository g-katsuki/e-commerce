package jp.co.rakuten.ecommerce.application.controller;

import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.ItemListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@RequestMapping("/admin/item")
public class AdminItemController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminItemController.class);
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/list")
    public ModelAndView list() {
        // Calling api to get list of items
        ItemListDto response = restTemplate.getForObject("/items", ItemListDto.class);

        ModelAndView model = new ModelAndView("admin/item/list");

        model.addObject("items", response.getItems());

        return model;
    }

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable Integer id) {
        // Calling api to get the item
        ItemDto item = restTemplate.getForObject("/items/" + id, ItemDto.class);

        ModelAndView model = new ModelAndView("admin/item/detail");

        model.addObject("item", item);

        return model;
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ItemDto item) {

        Integer itemId = item.getItemId();
        Double d_point = item.getPrice()/100;
        Integer point = d_point.intValue();
        item.setPoint(point);

        if (itemId != null) {
            //update request
            restTemplate.put("/items", item);
            return "redirect:" + MvcUriComponentsBuilder.fromMethodName(AdminItemController.class, "refresh", itemId).build().toUri();
        } else {
            // create new
            IdDto idDto = restTemplate.postForObject("/items", item, IdDto.class); // itemに追加
            itemId = idDto.getId(); // 新しく作るからitemじゃなくてidDto.javaから呼んでid上書き(既存のid+1だからitemのidとは関係ない)
            LOG.info("qqqqqqqqqqqqqqqqqqqqq");
            return "redirect:" + MvcUriComponentsBuilder.fromMethodName(AdminItemController.class, "list").build().toUri();
        }

    }

    @GetMapping("/new")
    private ModelAndView add() {
        // TODO: implement
        ItemDto new_item = new ItemDto();
        ModelAndView model = new ModelAndView("admin/item/new");
        model.addObject("item", new_item);
        return model;
    }

    @GetMapping("/detail/{id}/refresh")
    private ModelAndView refresh(@PathVariable Integer id) {
        ModelAndView model = detail(id);
        model.addObject("message", "Successfully Saved!");
        return model;
    }
}
