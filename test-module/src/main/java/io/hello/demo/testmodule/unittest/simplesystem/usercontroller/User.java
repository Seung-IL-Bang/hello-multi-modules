package io.hello.demo.testmodule.unittest.simplesystem.usercontroller;

public class User {
    private Long userId;
    private String email;
    private String password;

    public User(Long userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
