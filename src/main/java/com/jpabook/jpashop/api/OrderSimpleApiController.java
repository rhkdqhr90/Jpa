package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * Order -> member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public Result ordersV1(){
        List<Order> list = orderRepository.findAll(new OrderSearch());
        return new Result(list);
    }

    @GetMapping("/api/v2/simple-orders")
    public Result ordersV2(){
        List<SimpleOrderDto> list = orderRepository.findAll(new OrderSearch()).stream()
                .map(SimpleOrderDto::new)
                .toList();
        return new Result(list);
    }
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;         // Member.name
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private OrderStatus status;
        private Address address;


        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); // 강제 초기화
            this.address = order.getDelivery().getAddress();
            this.orderStatus = order.getStatus();
            this.orderDate = order.getOrderDate();
            this.status = order.getStatus();

        }
    }
    @Data
    static class Result<T>{
        private T memberDto;

        public Result(T memberDto) {
            this.memberDto = memberDto;
        }
        public Result() {
        }
    }
}
