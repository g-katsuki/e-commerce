package jp.co.rakuten.ecommerce.application.controller;

import jp.co.rakuten.ecommerce.application.form.UserLoginForm;
import jp.co.rakuten.ecommerce.application.form.UserRegisterForm;
import jp.co.rakuten.ecommerce.common.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jp.co.rakuten.ecommerce.application.constant.SessionAttributes.LOGGED_IN_USER;

@Controller
@RequestMapping("/user")
@Slf4j
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;  // apiのコントローラに情報を渡す型

    @GetMapping("/login")
    public ModelAndView loginForm() {
        ModelAndView model = new ModelAndView("user/login");
        model.addObject("login", new UserLoginForm());
        return model;
    }

    @PostMapping("/login")  // user/login内でsubmitボタンが押されるとuser/loginが発動してその時はFormの引数を持ってるからこのメソッドが呼ばれる
    public String postLogin(@ModelAttribute UserLoginForm loginForm, HttpServletRequest request, Model model) {
        model.addAttribute("login", loginForm);
        UserDto userDto = new UserDto();
        try {
            userDto = restTemplate.getForObject("/users/" + loginForm.getEmail(), UserDto.class); // api側のエンドポイントにrestTemplateを使って送る
            if(userDto==null) {
                model.addAttribute("message", "メールアドレスかパスワードが違います");
                return "user/login";
            }
        } catch (Exception exception) {
            model.addAttribute("message", "入力されていません");
            System.out.println(exception);
            return "user/login";
        }
        request.getSession().setAttribute(LOGGED_IN_USER, userDto);  // 28行目でインクルードしてる
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(ItemController.class,
                "list", request).build().toUri().toString();  // listにはメソッドで移動するから引数にsessionつける
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(ItemController.class,
                "list", request).build().toUri().toString();
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {

        UserDto userDto = (UserDto) request.getSession().getAttribute(LOGGED_IN_USER);
        // userIdをキーとしてDBから一致するordersを持ってくる
        ResponseEntity<List<OrderDto>> responseEntity = restTemplate.exchange("/orders/" + userDto.getId(),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderDto>>() {
                });
        List<OrderDto> orderDtoList = responseEntity.getBody();  // まとまり毎のオーダー

        if (CollectionUtils.isEmpty(orderDtoList)) {
            model.addAttribute("isEmptyOrder", true);
        } else {
            // Hashを使った検索 画像の取得の為で、全オーダーの画像だから同じアイテムの画像は無駄になるから被らないように省く
            Map<Integer, ItemDto> itemDtoMap = new HashMap<>();
            for (OrderDto orderDto : orderDtoList) {
                for (OrderDto.OrderItem orderItem : orderDto.getItems()) {
                    Integer itemId = orderItem.getItemId();
                    if (!itemDtoMap.containsKey(itemId)) {
                        itemDtoMap.put(itemId, restTemplate.getForObject("/items/" + itemId, ItemDto.class));
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'E'曜日'k'時'mm'分'ss'秒'");
                orderDto.setSdf(sdf.format(orderDto.getCreatedAt()));
            }
            model.addAttribute("itemMap", itemDtoMap);  // photoAdressはorderListには含まれない為
        }
        model.addAttribute("user", userDto);
        // まとまり毎のオーダー
        model.addAttribute("orderList", orderDtoList);  // quantityはitemDtoMapには含まれない為
        return "user/profile";
    }

    @GetMapping("/register")
    public ModelAndView registrationForm() {
        ModelAndView model = new ModelAndView("user/register");
        model.addObject("userForm", new UserRegisterForm());
        return model;
    }

    @PostMapping("/register")  // submitタイプのボタンが押されたときの引数の数で実行されるメソッド
    public String postRegistration(@ModelAttribute UserRegisterForm userForm, HttpServletRequest request, Model model) {
        userForm.setPoint(0);
        model.addAttribute("userForm", userForm);
        try {
            // メールアドレスの重複チェック
            // apiとの橋渡しをするためにDtoを使う。restTemplateでapiと通信。
            UserDto userDto = restTemplate.getForObject("/users/" + userForm.getEmail(), UserDto.class);
            if (userDto != null) {
                model.addAttribute("message", "このメールアドレスはすでに登録されています");
                return "user/register";
            }
        } catch (Exception ex) {
            // if user not exist, api throw 404 exception
            log.error("Exception", ex);
        }

        // save the new user
        IdDto idDto = restTemplate.postForObject("/users", userForm.toUserDto(), IdDto.class);
        if (idDto.getId() > 0) {
            // get the newly created user
            UserDto userDto = restTemplate.getForObject("/users/" + userForm.getEmail(), UserDto.class);

            // set in the session
            request.getSession().setAttribute(LOGGED_IN_USER, userDto);
        }

        return "redirect:" + MvcUriComponentsBuilder.fromMethodName(ItemController.class,
                "list", request).build().toUri().toString();
    }

}