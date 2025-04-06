package io.hello.demo.testmodule.unittest.simplesystem.paymentstatus;

import org.springframework.stereotype.Service;

@Service
public interface PointService {
    void addPoints(String userId, Long points);
}
