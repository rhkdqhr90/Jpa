package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;         // Member.name
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private OrderStatus status;
    private Address address;

    public OrderSimpleQueryDto(Long id, String name, LocalDateTime orderDate,OrderStatus status, Address address) {
        this.orderId = id;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = status;
        this.status = status;
        this.address = address;
    }
}
