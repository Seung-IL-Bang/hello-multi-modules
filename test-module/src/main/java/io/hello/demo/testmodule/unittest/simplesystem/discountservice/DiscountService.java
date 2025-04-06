package io.hello.demo.testmodule.unittest.simplesystem.discountservice;

import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    private final MemberService memberService;

    public DiscountService(MemberService memberService) {
        this.memberService = memberService;
    }

    public int applyDiscount(Long memberId, int price) {
        Member member = memberService.findById(memberId);
        if (member.getGrade() == Grade.VIP) {
            return (int) (price * 0.8); // 20% 할인
        }
        return price; // 할인 없음
    }
}
