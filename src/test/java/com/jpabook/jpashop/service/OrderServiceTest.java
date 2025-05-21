package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import com.jpabook.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문(){
        //given
        Member member = createMember();
        Item item = createItem("jpa",10000,10);
        //when
        int orderCount =2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
        Assertions.assertThat(getOrder.getTotalPrice()).isEqualTo(20000);
        Assertions.assertThat(item.getStockQuantity()).isEqualTo(8);

     }

    
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createItem("JPA",1000,10);
        //when
        int orderCount =11;
        //then
        Assertions.assertThatThrownBy(() -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        }).isInstanceOf(NotEnoughStockException.class)
                .hasMessage("수량이 더 필요 합니다");

    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Item item = createItem("JPA",1000,10);
        int orderCount =2;
        Long order = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(order);
        //then
        Order getOrder = orderRepository.findOne(order);
        Assertions.assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        Assertions.assertThat(item.getStockQuantity()).isEqualTo(10);
    }



    private Item createItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        em.persist(item);
        return item;
    }
    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","용산구","123-123"));
        em.persist(member);
        return member;
    }




}