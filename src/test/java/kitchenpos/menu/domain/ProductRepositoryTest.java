package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @DisplayName("상품 등록")
    @Test
    void create() {
        Product product = productRepository.save(new Product("후라이드치킨", BigDecimal.valueOf(160000)));
        assertThat(product).isNotNull();
    }

    @DisplayName("상품 등록 예외 - 가격이 0미만인 경우")
    @Test
    void validLessThanZero() {
        assertThatThrownBy(() -> {
            productRepository.save(new Product("후라이드치킨", BigDecimal.valueOf(-1)));
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
