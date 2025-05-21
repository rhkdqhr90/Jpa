package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.*;
import com.jpabook.jpashop.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserA
 * Jpa1,Jpa2
 * UserB
 * Spring1,Spring2
 */
@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit();
        initService.dbInit2();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public void dbInit(){
            Member member = createMember("member1", new Address("서울", "용산", "123"));
            em.persist(member);
            Book book = getBook("jpa", 10000, 13);
            em.persist(book);

            Book book2 = getBook("spring", 20000, 22);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 3);
            OrderItem orderItem1 = OrderItem.createOrderItem(book2, 20000, 5);
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);
            em.persist(order);

        }

        public void dbInit2(){
            Member member = createMember("member2", new Address("서울", "광진구", "456"));
            em.persist(member);

            Book book1 = getBook("spring1", 10000, 10);
            Book book2 = getBook("spring2", 20000, 5);
            em.persist(book1);
            em.persist(book2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            OrderItem orderItem = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem1 = OrderItem.createOrderItem(book2, 20000, 2);

            Order order = Order.createOrder(member, delivery, orderItem, orderItem1);

            em.persist(order);
        }

        private static Book getBook(String name, int prive, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(prive);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Member createMember(String name, Address address) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(address);          
            return member;
        }

    }
}


