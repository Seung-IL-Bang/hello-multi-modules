package io.hello.demo.testmodule.unittest.simplesystem.paymentstatus;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private Long amount;
    private String status;

    protected Payment() {}
    public Payment(String userId, Long amount, String status) {
        this.userId = userId;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public Long getAmount() { return amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
