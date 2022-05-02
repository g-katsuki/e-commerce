package jp.co.rakuten.ecommerce.api.controller;

import jp.co.rakuten.ecommerce.api.service.OrderService;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public IdDto saveOrder(@RequestBody OrderDto orderDto) {
        return orderService.processNewOrder(orderDto);
    }

    @GetMapping("/orders")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrdersForAdmin();
    }

    /**
     * Get list of orders for userId or customer id
     * @param userId userId
     */
    @GetMapping("/orders/{userId}")
    public List<OrderDto> getOrdersByUserId(@PathVariable Integer userId) {
        return orderService.getOrdersForUser(userId);
    }
}