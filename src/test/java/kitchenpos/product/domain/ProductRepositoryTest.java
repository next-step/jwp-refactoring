package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("상품 리파지토리 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        Product product = new Product("양념치킨", BigDecimal.valueOf(19000.00));
        Product save = productRepository.save(product);
        assertThat(save.getId()).isNotNull();
    }
}
