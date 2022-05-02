package jp.co.rakuten.ecommerce.application.controller;

import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/list")
    public ModelAndView list() {
        ModelAndView model = new ModelAndView("admin/order/list");
        // Calling api to get list of items
        ResponseEntity<List<OrderDto>> responseEntity = restTemplate.exchange("/orders",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<OrderDto>>() {
                });

        List<OrderDto> orderDtoList = responseEntity.getBody();

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
        model.addObject("itemMap", itemDtoMap);

        model.addObject("orderList", orderDtoList);

        return model;
    }
}