package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryNew extends JpaRepository<Member, Long> {

     List<Member> findByName(String name);
     Optional<Member> findById(Long id);
}
