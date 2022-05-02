package jp.co.rakuten.ecommerce.api.controller;

import jp.co.rakuten.ecommerce.api.exception.NotFoundException;
import jp.co.rakuten.ecommerce.api.service.UserService;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.OrderDto;
import jp.co.rakuten.ecommerce.common.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class CustomerController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{email}")
    // メールアドレスからユーザー情報をとってくる
    public UserDto getUser(@PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return userDto;
    }

    @PostMapping("/users")
    // 新しくユーザーを作る
    public IdDto createUser(@RequestBody UserDto userDto) {
        userDto.setEnabled(true);
        userDto.setCreatedAt(new Date());
        return userService.createUser(userDto);
    }

    @PostMapping("/users/update")
    public void updateUser(@RequestBody UserDto userDto) {
        userService.updateUser(userDto);
        return;
    }
}