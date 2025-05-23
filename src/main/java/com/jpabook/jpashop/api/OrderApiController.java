package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;

import com.jpabook.jpashop.repository.order.query.OrderFlatDto;
import lombok.Getter;
import com.jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import com.jpabook.jpashop.repository.order.query.OrderQueryDto;
import com.jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public   List<Order>  orderV1(){
        List<Order> all = orderRepository.findAll(new OrderSearch());
        for(Order order : all){
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem ->
                orderItem.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream().map(OrderDto::new).toList();

    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> allWithItem = orderRepository.findAllWithItem();
        return allWithItem.stream().map(OrderDto::new).toList();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value="offset" ,defaultValue = "1")int offset,
                                       @RequestParam(value = "limit",defaultValue = "100")int limit) {

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);
        return orders.stream().map(OrderDto::new).toList();
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto>  ordersV4(){
        return orderQueryRepository.findOrderDtos();

    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto>  ordersV5(){
        return orderQueryRepository.findAllByDto_optimiztion();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto>  ordersV6(){
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();


        Map<OrderQueryDto, List<OrderItemQueryDto>> grouped = flats.stream()
                .collect(Collectors.groupingBy(
                        flat -> new OrderQueryDto(
                                flat.getId(),
                                flat.getName(),
                                flat.getOrderDate(),
                                flat.getOrderStatus(),
                                flat.getAddress()
                        ),
                        LinkedHashMap::new,
                        Collectors.mapping(
                                flat -> new OrderItemQueryDto(
                                        flat.getId(),
                                        flat.getItemName(),
                                        flat.getOrderPrice(),
                                        flat.getCount()
                                ),
                                Collectors.toList()
                        )
                ));

        return grouped.entrySet().stream()
                .map(entry -> {
                    OrderQueryDto order = entry.getKey();
                    order.setOrderItems(entry.getValue());
                    return order;
                })
                .toList();
    }







    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems; //엔티티가 의존하면 안되서 DTO로 감쌓서 해야한다

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).toList();
        }
    }
    @Data
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
