package com.jpabook.jpashop.repository.order.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {
    private Long id;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long id, String itemName, int orderPrice, int count) {
        this.id = id;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
