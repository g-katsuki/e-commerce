package jp.co.rakuten.ecommerce.application.controller;

import jp.co.rakuten.ecommerce.application.form.AddCartItemForm;
import jp.co.rakuten.ecommerce.application.session.Cart;
import jp.co.rakuten.ecommerce.application.session.CartItem;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.ItemListDto;
import jp.co.rakuten.ecommerce.common.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static jp.co.rakuten.ecommerce.application.constant.SessionAttributes.LOGGED_IN_USER;

@Controller
@RequestMapping("/cart")

public class CartController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    Cart cart;
    ItemListDto items = new ItemListDto();

    @PostMapping("/add")  // おそらく呼ばれた時のエンドポイントとメソッドの名前が同じだからメソッドが発動する？
    public String add(@ModelAttribute CartItem item, HttpServletRequest request) {
        ItemDto temp_item = new ItemDto();
        temp_item.setItemId(item.getItemId());
        temp_item.setName(item.getName());
        temp_item.setDescription(item.getDescription());
        temp_item.setPrice(item.getPrice());
        temp_item.setPhotoAddress(item.getPhotoAddress());
        temp_item.setQuantity(item.getQuantity());
        temp_item.setPoint(item.getPoint());
        cart.add(temp_item, item.getQuantity());
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(CartController.class, "list", request).build().toUri();
    }

    @GetMapping("/list")
    private ModelAndView list(HttpServletRequest request) {
        System.out.println(cart.getItems().get(0).getPoint());
        Integer totalPoint = 0, doubledPoint = 0;
        for(CartItem cartItem: cart.getItems()){
            totalPoint += cartItem.getPoint() * cartItem.getQuantity();
        }
        cart.setTotalPoint(totalPoint);
        ModelAndView model = new ModelAndView("/cart/list"); // このパスにあるHTMLをmodelに指定
        model.addObject("user", (UserDto)request.getSession().getAttribute(LOGGED_IN_USER));
        model.addObject("items", cart.getItems());  // modelにオブジェクト情報を追加
        model.addObject("totalPrice", cart.getTotalPrice());  // modelに,cartクラス内でGetter付きで宣言されたtotalPriceを追加
        model.addObject("totalPoint", totalPoint);
        if(cart.getTotalPrice() > 9999){
            doubledPoint = cart.getTotalPoint() * 2;
            model.addObject("doubledPoint", doubledPoint);
        }
        return model;
    }

    @GetMapping("/update")
    public String up(@RequestParam("itemId") Integer itemId, @RequestParam("unit") Integer unit, HttpServletRequest request) {
        cart.update(itemId, unit);
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(CartController.class, "list", request).build().toUri();
    }

    @GetMapping("/clear")  // 未完成
    public String clear() {
        System.out.println("AA");
        cart.clear();
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(CartController.class, "list").build().toUri();
    }
}