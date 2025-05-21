package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<MemberDto> list = memberService.findMembers().stream()
                .map(m -> new MemberDto(m.getName()))
                .toList();
        return new Result(list);

    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        memberService.join(member);
        return new CreateMemberResponse(member.getId());

    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), request.getName());
    }

    @Data
    public static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }

        public CreateMemberResponse() {
        }
    }
    @Data
    public static class CreateMemberRequest {
        private String name;
    }

    @Data
    public static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
