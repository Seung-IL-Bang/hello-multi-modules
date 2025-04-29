package io.hello.demo.hellomultimodules;

import lombok.Getter;

@Getter
public class LombokTests {
    private String name;

    public LombokTests(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        LombokTests test = new LombokTests("Hello, Lombok!");
        System.out.println(test.getName());
    }
}
