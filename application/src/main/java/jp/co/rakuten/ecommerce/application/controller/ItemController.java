package jp.co.rakuten.ecommerce.application.controller;

import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.ItemListDto;
import jp.co.rakuten.ecommerce.common.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.text.SimpleDateFormat;

import static jp.co.rakuten.ecommerce.application.constant.SessionAttributes.LOGGED_IN_USER;

@Controller
public class ItemController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index() {
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(ItemController.class, "list");
    }

    @GetMapping("/item/list")
    public ModelAndView list(HttpServletRequest request) {
        Date date = new Date();
        ItemListDto response = restTemplate.getForObject("/items", ItemListDto.class);
        ModelAndView model = new ModelAndView("item/list");
        model.addObject("user", (UserDto)request.getSession().getAttribute(LOGGED_IN_USER));
        model.addObject("items", response.getItems());
        return model;
    }

    @GetMapping("/item/detail/{itemId}")
    public ModelAndView detail(@PathVariable Integer itemId, HttpServletRequest request) {
        // Calling api to get the item
        ItemDto item = restTemplate.getForObject("/items/" + itemId, ItemDto.class);
        ModelAndView model = new ModelAndView("item/detail");
        model.addObject("user", (UserDto)request.getSession().getAttribute(LOGGED_IN_USER));
        model.addObject("item", item);
        return model;
    }
}
