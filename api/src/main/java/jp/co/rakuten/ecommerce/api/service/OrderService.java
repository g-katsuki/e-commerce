package jp.co.rakuten.ecommerce.api.service;

import jp.co.rakuten.ecommerce.api.entity.Order;
import jp.co.rakuten.ecommerce.api.entity.OrderDetail;
import jp.co.rakuten.ecommerce.api.repository.OrderDetailRepository;
import jp.co.rakuten.ecommerce.api.repository.OrderRepository;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.OrderDto;
import jp.co.rakuten.ecommerce.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;

    private OrderDetailRepository orderDetailRepository;

    public List<OrderDto> getOrdersForUser(Integer userId) {
        List<Order> orders = orderRepository.findByCustomerId(userId);
        if (CollectionUtils.isEmpty(orders)) return Collections.EMPTY_LIST;
        return getOrderList(orders);
    }

    public List<OrderDto> getAllOrdersForAdmin() {
        Iterable<Order> orders = orderRepository.findAll();
        return getOrderList(orders);
    }

    // DBから持ってきたordersをDtoに移し替える
    private List<OrderDto> getOrderList(Iterable<Order> orders) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : orders) {
            OrderDto orderDto = makeOrderDto(order);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
            orderDetails.forEach(orderDetail -> orderDto.addItem(makeOrderItem(orderDetail)));
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    @Transactional
    // 新しいオーダーが入った時にDBに追加
    public IdDto processNewOrder(OrderDto orderDto) {
        Order order = makeOrderEntity(orderDto);  // DBのordersに入る型の実体を生成
        // DBに注文があったことを書き込む
        order = orderRepository.save(order);  // orderのidを指定しないで書き込むから順番にidが振られる
        final Integer orderId = order.getId();
        // DBのorder_detailsに入る型の実体を生成して書き込む
        orderDto.getItems().forEach(orderItem -> orderDetailRepository.save(makeOrderDetailEntity(orderItem, orderId)));
        return IdDto.builder().id(orderId).build();
    }

    // DBのordersに入る型の実体を生成(idを指定しないで書き込むから順番にidが振られる)
    private Order makeOrderEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setCustomerId(orderDto.getCustomerId());
        order.setStatus(orderDto.getStatus().name());
        order.setCreatedAt(new Date());
        return order;
    }

    // DBのorder_detailsに入る型の実体を生成
    private OrderDetail makeOrderDetailEntity(OrderDto.OrderItem orderItem, Integer orderId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setItemId(orderItem.getItemId());
        orderDetail.setQuantity(orderItem.getQuantity());
        orderDetail.setPrice(orderItem.getPrice());
        return orderDetail;
    }

    private OrderDto makeOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(order.getCustomerId());
        orderDto.setOrderId(order.getId());
        orderDto.setStatus(OrderStatus.valueOf(order.getStatus()));
        orderDto.setCreatedAt(order.getCreatedAt());
        return orderDto;
    }

    private OrderDto.OrderItem makeOrderItem(OrderDetail orderDetail) {
        OrderDto.OrderItem orderItem = new OrderDto.OrderItem();
        orderItem.setItemId(orderDetail.getItemId());
        orderItem.setPrice(orderDetail.getPrice());
        orderItem.setQuantity(orderDetail.getQuantity());
        return orderItem;
    }
}