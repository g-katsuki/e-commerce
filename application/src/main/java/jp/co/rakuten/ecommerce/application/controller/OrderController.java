package jp.co.rakuten.ecommerce.application.controller;

import jp.co.rakuten.ecommerce.application.session.Cart;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.OrderDto;
import jp.co.rakuten.ecommerce.common.dto.UserDto;
import jp.co.rakuten.ecommerce.common.enums.OrderStatus;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;

import static jp.co.rakuten.ecommerce.application.constant.SessionAttributes.LOGGED_IN_USER;

@Controller
@RequestMapping("/order")

public class OrderController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    Cart cart;

    @GetMapping("/confirm")
    public ModelAndView confirm(HttpServletRequest request) {
        Integer usePoint = 0, doubledPoint = 0;
        ModelAndView model = new ModelAndView("order/confirm");
        model.addObject("user", (UserDto)request.getSession().getAttribute(LOGGED_IN_USER));
        model.addObject("items", cart.getItems());
        model.addObject("totalPrice", cart.getTotalPrice());
        model.addObject("totalPoint", cart.getTotalPoint());
        if(cart.getTotalPrice() > 9999){
            doubledPoint = cart.getTotalPoint() * 2;
            model.addObject("doubledPoint", doubledPoint);
        }
        model.addObject("usePoint", usePoint);
        return model;
    }
////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/complete")
    public String complete(Integer usePoint, HttpServletRequest request) {
        System.out.println(usePoint);
        UserDto userDto = (UserDto) request.getSession().getAttribute(LOGGED_IN_USER);
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(userDto.getId());
        orderDto.setStatus(OrderStatus.NEW);
        // カート内のすべてのアイテムをorderDtoに入れる
        cart.getItems().forEach(cartItem -> orderDto.addItem(cartItem.toOrderItem()));
        // point加算
        if(cart.getTotalPrice() > 9999){
            Integer doubledPoint = cart.getTotalPoint() * 2;
            cart.setTotalPoint(doubledPoint);
        }
        Integer newPoint = userDto.getPoint() + cart.getTotalPoint() - usePoint;
        // ポイント情報を移す
        orderDto.setPoint(cart.getTotalPoint());
        userDto.setPoint(newPoint);
        // orderDtoの中のcustomerIdとList<OrderItem>をDBに書き込んで、web上に表示するための注文idを返す
        // ここで一緒にユーザーポイントの更新をしてもいいのでは?
        IdDto idDto = restTemplate.postForObject("/orders", orderDto, IdDto.class);
        restTemplate.postForObject("/users/update", userDto, UserDto.class);
        Double finalPrice = cart.getTotalPrice() - usePoint;
        cart.clear();
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(OrderController.class,
                "completed", idDto.getId(), finalPrice).build().toUri().toString();
    }
/////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/completed")
    public ModelAndView completed(@RequestParam(required = false) Integer orderId, Double finalPrice) {
        ModelAndView model = new ModelAndView("order/complete");
        model.addObject("orderId", orderId);
        model.addObject("finalPrice", finalPrice);
        return model;
    }
}
