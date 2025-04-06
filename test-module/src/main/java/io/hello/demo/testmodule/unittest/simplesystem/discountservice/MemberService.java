package io.hello.demo.testmodule.unittest.simplesystem.discountservice;

import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    Member findById(Long memberId);
}
