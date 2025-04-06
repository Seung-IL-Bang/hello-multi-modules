package io.hello.demo.testmodule.unittest.simplesystem.discountservice;

public class Member {

    private Long memberId;
    private Grade grade;

    public Member(Long memberId, Grade grade) {
        this.memberId = memberId;
        this.grade = grade;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Grade getGrade() {
        return grade;
    }
}
