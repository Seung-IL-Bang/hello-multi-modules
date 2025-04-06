package io.hello.demo.testmodule.unittest.simplesystem.discountservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private DiscountService discountService;


    @Test
    void VIP회원_할인적용_테스트() {
        // given
        Long memberId = 1L;
        int price = 10000;
        new Member(memberId, Grade.VIP);

        when(memberService.findById(anyLong())).thenReturn(new Member(memberId, Grade.VIP));

        // when
        int discountedPrice = discountService.applyDiscount(memberId, price);

        // then
        assertEquals(8000, discountedPrice); // 20% 할인 적용
    }

    @Test
    void 일반회원_할인없음_테스트() {
        // given
        Long memberId = 2L;
        int price = 10000;

        when(memberService.findById(anyLong())).thenReturn(new Member(memberId, Grade.NORMAL));

        // when
        int discountedPrice = discountService.applyDiscount(memberId, price);

        // then
        assertEquals(10000, discountedPrice); // 할인 없음
    }


}