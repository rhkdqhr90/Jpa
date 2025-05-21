package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        if (orderSearch.getOrderStatus() != null) {
            jpql += isFirstCondition ? " where" : " and";
            jpql += " o.status = :status";
            isFirstCondition = false;
        }

        if (orderSearch.getMemberName() != null && !orderSearch.getMemberName().isBlank()) {
            jpql += isFirstCondition ? " where" : " and";
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (orderSearch.getMemberName() != null && !orderSearch.getMemberName().isBlank()) {
            query.setParameter("name", "%" + orderSearch.getMemberName() + "%");
        }

        return query.getResultList();
    }

}
