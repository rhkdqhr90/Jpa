package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.domain.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.jpabook.jpashop.domain.QMember.member;
import static com.jpabook.jpashop.domain.QOrder.order;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    /**
     * jpa criteria 크리테리아
     * @param orderSearch
     * @return
     */
    public List<Order> findAll1(OrderSearch orderSearch){
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

    public List<Order> findAll(OrderSearch orderSearch) {

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), getLike(orderSearch, member))
                .limit(100)
                .fetch();
    }

    private static BooleanExpression getLike(OrderSearch orderSearch, QMember member) {
        if(!StringUtils.hasText(orderSearch.getMemberName())){
            return null;
        }
        return member.name.like(orderSearch.getMemberName());
    }

    private BooleanExpression statusEq(OrderStatus statusCond){
        if (statusCond == null) {
            return null;
        }
        return order.status.eq(statusCond);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o join fetch o.member m join fetch o.delivery d", Order.class)
                .getResultList()
                ;
    }

    public List<OrderSimpleQueryDto> findOrderDtos(){
       return  em.createQuery("select new com.jpabook.jpashop.repository.OrderSimpleQueryDto(o.id,m.name,o.orderDate,o.status,d.address) " +
               "from Order o join o.member m join o.delivery d", OrderSimpleQueryDto.class).getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o " +
                "join fetch o.orderItems oi " +
                "join fetch o.member m " +
                "join fetch o.delivery d " +
                "join fetch oi.item i ", Order.class).getResultList(); //잘 작동 하지만 페이징이 안된다
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o join fetch o.member m join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList()
                ;
    }

}
