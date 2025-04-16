package io.hello.demo.testmodule.unittest.simplesystem.productrepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // DB 설정이 필요함 -> H2 testImplementation 의존성을 추가하여 DB를 메모리에 올림 & 엔티티 ID 생성 전략 설정 필요.
class ProductRepositoryTest {

    /**
     * @DataJpaTest 어노테이션은 기본적으로 실제 데이터소스 대신 임베디드 데이터베이스를 사용하려고 시도합니다. 하지만 적절한 임베디드 데이터베이스 의존성이 클래스패스에 없어서 문제가 발생할 수 있습니다.
     * @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) -> 실제 데이터베이스를 사용하게 됩니다. 이 방법은 application.properties 또는 application.yml에 이미 설정된 데이터소스를 사용합니다.
     */

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품_저장_및_조회_테스트() {
        // given
        Product product = new Product(null, "Laptop", 1500);
        Product savedProduct = productRepository.save(product);

        // when
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // then
        assertTrue(foundProduct.isPresent());
        assertEquals(savedProduct.getId(), foundProduct.get().getId());
        assertEquals(savedProduct.getName(), foundProduct.get().getName());
        assertEquals(savedProduct.getPrice(), foundProduct.get().getPrice());
    }
}