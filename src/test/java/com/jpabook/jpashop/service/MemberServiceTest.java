package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;


import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("Lee");
        //when
        Long savedId = memberService.join(member);
        //then
        em.flush();
        Assertions.assertThat(savedId).isEqualTo(memberRepository.findOne(savedId).getId());

    }

   @Test
   public void 중복회원제외() throws Exception{
       //given
       Member member = new Member();
       member.setName("Lee");

       Member member2 = new Member();
       member2.setName("Lee");


       //when
        memberService.join(member);
       IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
           memberService.join(member2);
       });
       assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}